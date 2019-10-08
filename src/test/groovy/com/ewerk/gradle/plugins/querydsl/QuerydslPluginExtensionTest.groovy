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

import groovy.transform.CompileStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat


/**
 * @author holgerstolzenberg
 * @since 1.0.0
 */
@CompileStatic
class QuerydslPluginExtensionTest {

  private QuerydslPluginExtension extension

  @BeforeEach
  void setup() {
    extension = new QuerydslPluginExtension()
  }

  @Test
  void testDefaultLibraryIsSet() {
    def defaultLibrary = QuerydslPluginExtension.DEFAULT_LIBRARY
    assertThat(extension.library).isEqualTo(defaultLibrary)
  }

  @Test
  void testProcessors() {
    extension.jpa = true
    extension.jdo = true
    extension.hibernate = true
    extension.morphia = true
    extension.roo = true
    extension.springDataMongo = true
    extension.querydslDefault = true
    extension.lombok = true

    def processors = getProcessors()
    assertThat(processors)
      .containsExactlyInAnyOrder(QuerydslPluginExtension.HIBERNATE_PROC,
        QuerydslPluginExtension.JDO_PROC,
        QuerydslPluginExtension.JPA_PROC,
        QuerydslPluginExtension.MORPHIA_PROC,
        QuerydslPluginExtension.QUERYDSL_PROC,
        QuerydslPluginExtension.ROO_PROC,
        QuerydslPluginExtension.SPRING_DATA_MONGO_PROC,
        QuerydslPluginExtension.LOMBOK_PROC)
  }

  @Test
  void testProcessorsDefaults() {
    def processors = getProcessors()
    assertThat(processors).isEmpty()
  }

  private String[] getProcessors() {
    if (extension.processors().isBlank()) {
      return new String[0]
    }
    return extension.processors().split(",")
  }
}
