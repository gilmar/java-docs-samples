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

package com.google.cloud.vision.samples.facesentimentdetect;

import static com.google.common.truth.Truth.assertThat;

import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.cloud.vision.samples.facesentimentdetect.FaceSentimentDetectApp;
import com.google.common.collect.Lists;

/**
 * Integration (system) tests for {@link FaceSentimentDetectApp}.
 */
@RunWith(JUnit4.class)
@SuppressWarnings("checkstyle:abbreviationaswordinname")
public class FaceSentimentDetectTest {
	private static final int MAX_RESULTS = 6;

	private FaceSentimentDetectApp appUnderTest;

	@Before
	public void setUp() throws Exception {
		appUnderTest = new FaceSentimentDetectApp(FaceSentimentDetectApp.getVisionService());
	}

	@Test
	public void shouldReturnAtLeastOneFace() throws Exception {
		List<FaceAnnotation> faces = appUnderTest.detectFaces(Paths.get("data/meeting.jpg"), MAX_RESULTS);

		assertThat(faces).named("meeting.jpg faces").isNotEmpty();
		assertThat(faces.get(0).getFdBoundingPoly().getVertices()).named("face.jpg face #0 FdBoundingPoly Vertices")
				.isNotEmpty();
	}

	@SuppressWarnings("static-access")
	@Test
	public void shouldGetSentimentsRight() throws Exception {
		List<String> sentiments = Lists.newArrayList();
		List<FaceAnnotation> faces = appUnderTest.detectFaces(Paths.get("data/meeting.jpg"), MAX_RESULTS);
		for (FaceAnnotation face : faces) {
			sentiments.add(appUnderTest.getSentiment(face));
		}
		assertThat(sentiments).containsExactly("neutral", "neutral", "positive", "neutral", 
				"positive", "positive");
		System.out.println(sentiments);
	}

}
