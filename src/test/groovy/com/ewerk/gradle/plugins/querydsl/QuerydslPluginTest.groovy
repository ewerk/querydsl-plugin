/*
 * Copyright 2015 the original author or authors.
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

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.WarPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat

/**
 * Unit test for the {@link QuerydslPlugin} class.
 *
 * @author holger.stolzenberg* @since 1.0.0
 */
class QuerydslPluginTest {

  private Project project

  @BeforeEach
  void setup() {
    project = ProjectBuilder.builder()
      .build()
    project.plugins.apply(QuerydslPlugin.class)
    project.plugins.apply(WarPlugin.class)

    project.extensions.querydsl.jpa = true
    project.extensions.querydsl.jdo = true
    project.extensions.querydsl.roo = true
    project.extensions.querydsl.hibernate = true
    project.extensions.querydsl.morphia = true
    project.extensions.querydsl.springDataMongo = true
    project.extensions.querydsl.querydslDefault = true

    project.extensions.querydsl.aptOptions = ['querydsl.entityAccessors=true', 'querydsl.useFields=false']
  }

  @Test
  void testPluginAppliesItself() {
    assertThat(project.plugins.hasPlugin(QuerydslPlugin.class)).isTrue()
  }

  @Test
  void testReApplyDoesNotFail() {
    project.plugins.apply(QuerydslPlugin.class)
  }

  @Test
  void testPluginAppliesJavaPlugin() {
    assertThat(project.plugins.hasPlugin(JavaPlugin.class)).isTrue()
  }

  @Test
  void testPluginRegistersQuerydslExtensions() {
    QuerydslPluginExtension querydsl = project.extensions.querydsl
    assertThat(querydsl).isNotNull()
  }

  @Test
  void testPluginProcessorsFromQuerydslExtensions() {
    assertThat(project.extensions.querydsl.processors()).isNotNull()
  }

//  @Test
//  void testCleanTaskAreAvailable() {
//    Task cleanTask = project.tasks.cleanQuerydslSourcesDir
//    assertThat(cleanTask).isNotNull()
//  }

//  @Test
//  void testTaskTypes() {
//    Task initTask = project.tasks.cleanQuerydslSourcesDir
//    assertThat(initTask).isInstanceOf(CleanQuerydslSourcesDir.class)
//  }
//
  @Test
  void testAfterEvaluate() {
    project.evaluate()

    println("get")
    DefaultExternalModuleDependency lib = project.configurations.annotationProcessor.dependencies[0] as DefaultExternalModuleDependency
    String id = lib.group + ":" + lib.name + ":" + lib.version

    assertThat(id).isEqualTo(QuerydslPluginExtension.DEFAULT_LIBRARY)
  }
}
