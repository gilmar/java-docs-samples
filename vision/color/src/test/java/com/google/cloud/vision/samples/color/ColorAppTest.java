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

import static com.google.common.truth.Truth.assertThat;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Integration (system) tests for {@link ColorApp}.
 */
@RunWith(JUnit4.class)
public class ColorAppTest {

	private ColorApp appUnderTest;

	@Before
	public void setUp() throws Exception {
		appUnderTest = new ColorApp(ColorApp.getVisionService());
	}

	@Test
	public void goodMangoShoudReturnRedOrYellow() throws Exception {
		String predominantColor = appUnderTest.getDominantColor(Paths.get("data/good_mango.jpg"));
		assertThat(predominantColor).isAnyOf("red", "yellow");
	}
	
	@Test
	public void immatureMangoShoudReturnGreen() throws Exception {
		String predominantColor = appUnderTest.getDominantColor(Paths.get("data/green_mango.jpg"));
		assertThat(predominantColor).isEqualTo("green");
	}
}
