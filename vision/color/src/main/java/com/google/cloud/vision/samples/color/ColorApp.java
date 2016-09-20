/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.vision.samples.color;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

// [START import_libraries]
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Color;
import com.google.api.services.vision.v1.model.ColorInfo;
import com.google.api.services.vision.v1.model.DominantColorsAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageProperties;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
// [END import_libraries]

/**
 * A sample application that uses the Vision API to verify the dominant color of a picture 
 */
public class ColorApp {
  /**
   * Be sure to specify the name of your application. If the application name is {@code null} or
   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "Google-VisionLabelSample/1.0";

  // [START run_application]
  /**
   * Annotates an image using the Vision API.
   */
  public static void main(String[] args) throws IOException, GeneralSecurityException {
    if (args.length != 1) {
      System.err.println("Missing imagePath argument.");
      System.err.println("Usage:");
      System.err.printf("\tjava %s imagePath\n", ColorApp.class.getCanonicalName());
      System.exit(1);
    }
    Path imagePath = Paths.get(args[0]);

    ColorApp app = new ColorApp(getVisionService());
    printProperties(System.out, imagePath, app.getImageProperties(imagePath));
  }

	private float rgbDistance(Color c1, Color c2) {
		return (float) Math.sqrt((Math.abs(Math.pow(c1.getRed() - c2.getRed(), 2)) 
				+ Math.abs(Math.pow(c1.getGreen() - c2.getGreen(), 2))
				+ Math.abs(Math.pow(c1.getBlue() - c2.getBlue(), 2))));
	}
  
  private String closestColorName(Color c) {
	  Color red = new Color();
	  red.setRed(255.0f);
	  red.setGreen(0.0f);
	  red.setBlue(0.0f);
	  red.set("name", "red");
	  Color yellow = new Color();
	  yellow.setRed(255.0f);
	  yellow.setGreen(255.0f);
	  yellow.setBlue(0.0f);
	  yellow.set("name", "yellow");
	  Color green = new Color();
	  green.setRed(0.0f);
	  green.setGreen(255.0f);
	  green.setBlue(0.0f);
	  green.set("name", "green");
	  java.util.List<Color> referenceColors = Lists.newArrayList(red, yellow, green);
	  float minDistance = rgbDistance(red, c);
	  String closestColorName = (String) red.get("name");
	  for (Color ref : referenceColors) {
		  float distance = rgbDistance(ref, c);
		  if (distance <= minDistance) {
			  minDistance = distance;
			  closestColorName = (String) ref.get("name");
		  }
	  }
	  return closestColorName;
  }
  
	public String getDominantColor(Path imagePath) throws IOException, GeneralSecurityException {
		ImageProperties properties = getImageProperties(imagePath);
		DominantColorsAnnotation dominantColorsAnnotation = properties.getDominantColors();
		ColorInfo colorInfo = dominantColorsAnnotation.getColors().get(0);
		return this.closestColorName(colorInfo.getColor());
	}
  
	/**
	 * Prints the properties received from the Vision API.
	 * 
	 * @throws IOException
	 */
	public static void printProperties(PrintStream out, Path imagePath, ImageProperties properties)
			throws IOException {
		out.printf("Properties for image %s:\n %s", imagePath, properties.toPrettyString());
		if (properties.isEmpty()) {
			out.println("\tNo properties found.");
		}
	}
  // [END run_application]

  // [START authenticate]
  /**
   * Connects to the Vision API using Application Default Credentials.
   */
  public static Vision getVisionService() throws IOException, GeneralSecurityException {
    GoogleCredential credential =
        GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
  }
  // [END authenticate]

  private final Vision vision;

  /**
   * Constructs a {@link ColorApp} which connects to the Vision API.
   */
  public ColorApp(Vision vision) {
    this.vision = vision;
  }

  /**
   * Gets properties for an image stored at {@code path}.
   */
  public ImageProperties getImageProperties(Path path) throws IOException {
		byte[] data = Files.readAllBytes(path);

		AnnotateImageRequest request = new AnnotateImageRequest()
				.setImage(new Image().encodeContent(data))
				.setFeatures(ImmutableList.of(new Feature().setType("IMAGE_PROPERTIES")));
		Vision.Images.Annotate annotate = vision.images()
				.annotate(new BatchAnnotateImagesRequest()
				.setRequests(ImmutableList.of(request)));

		// [START parse_response]
		BatchAnnotateImagesResponse batchResponse = annotate.execute();
		assert batchResponse.getResponses().size() == 1;
		AnnotateImageResponse response = batchResponse.getResponses().get(0);
		if (response.getImagePropertiesAnnotation() == null) {
			throw new IOException(response.getError() != null ? response.getError().getMessage()
					: "Unknown error getting image annotations");
		}
		return response.getImagePropertiesAnnotation();
		// [END parse_response]
  }
}
