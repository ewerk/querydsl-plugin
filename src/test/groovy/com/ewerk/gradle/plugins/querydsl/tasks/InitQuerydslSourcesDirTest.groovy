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
package com.ewerk.gradle.plugins.querydsl.tasks

import com.ewerk.gradle.plugins.querydsl.QuerydslPlugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.*

/**
 * @author holgerstolzenberg
 * @since 1.0.0
 */
class InitQuerydslSourcesDirTest {

  private Project project
  private InitQuerydslSourcesDir createTask

  @BeforeEach
  void setup() {
    project = ProjectBuilder.builder().build()
    project.plugins.apply(QuerydslPlugin.class)
    project.evaluate()

    createTask = project.tasks.initQuerydslSourcesDir as InitQuerydslSourcesDir
  }

  @Test
  void testCreateSourceFolders() {
    createTask.createSourceFolders()
    assertThat(project.sourceSets.querydsl as SourceSet).isNotNull()

    File javaDir = project.sourceSets.querydsl.java.srcDirs.first() as File
    assertThat(javaDir.name).isEqualTo("java")
  }

  @Test
  void testGroup() {
    assertThat(createTask.group).isEqualTo(QuerydslPlugin.TASK_GROUP)
  }

  @Test
  void testDescription() {
    assertThat(createTask.description).isEqualTo(InitQuerydslSourcesDir.DESCRIPTION)
  }
}
