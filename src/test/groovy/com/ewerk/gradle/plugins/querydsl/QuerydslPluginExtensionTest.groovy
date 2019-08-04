/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ewerk.gradle.plugins.querydsl

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat


/**
 * @author holgerstolzenberg
 * @since 1.0.0
 */
class QuerydslPluginExtensionTest {

  private QuerydslPluginExtension extension

  @BeforeEach
  void setup() {
    extension = new QuerydslPluginExtension()
  }

  @Test
  void testDefaultGeneratedSourcesDirIsSet() {
    String defaultDir = QuerydslPluginExtension.DEFAULT_QUERYDSL_SOURCES_DIR
    assertThat(extension.querydslSourcesDir as File).isEqualTo(new File(defaultDir))
  }

  @Test
  void testDefaultLibraryIsSet() {
    def defaultLibrary = QuerydslPluginExtension.DEFAULT_LIBRARY
    assertThat(extension.library).isEqualTo(defaultLibrary)
  }
}
