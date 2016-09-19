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
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;
// [END import_libraries]

/**
 * A sample application that uses the Vision API to verify the predominant color of a picture 
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

  public String getPredominantColor(Path imagePath) {
	  return null;
  }
  
  /**
   * Prints the  received from the Vision API.
   */
  public static void printProperties(PrintStream out, Path imagePath, List<EntityAnnotation> properties) {
	  //TODO
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
   * Gets up to {@code maxResults} properties for an image stored at {@code path}.
   */
  public List<EntityAnnotation> getImageProperties(Path path) throws IOException {
    // [START construct_request]
	//TODO
    // [END construct_request]

    // [START parse_response]
	//TODO
    // [END parse_response]
    return null;
  }
}
