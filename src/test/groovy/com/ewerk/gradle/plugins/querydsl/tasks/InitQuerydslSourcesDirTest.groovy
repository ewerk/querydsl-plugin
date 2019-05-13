package com.ewerk.gradle.plugins.querydsl.tasks

import com.ewerk.gradle.plugins.querydsl.QuerydslPlugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.assertj.core.api.Assertions.*

/**
 * @author holgerstolzenberg
 * @since 1.0.0
 */
class InitQuerydslSourcesDirTest {

  private Project project
  private InitQuerydslSourcesDir createTask

  @Before
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
