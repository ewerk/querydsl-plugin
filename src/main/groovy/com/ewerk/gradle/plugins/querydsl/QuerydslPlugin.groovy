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

import com.ewerk.gradle.plugins.querydsl.tasks.CleanQuerydslSourcesDir
import com.ewerk.gradle.plugins.querydsl.tasks.QuerydslCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.JavaPlugin

/**
 * This plugin can be used to easily create Querydsl Q-classes and attach them to the project
 * classpath.
 *
 * The plugin registers the extension 'querydsl' so that plugin specific configuration can
 * be overwritten within the build sScript. Please see the readme doc on Github for details on that.
 *
 * The plugin will generate an additional source directory into where the querydsl
 * classes will be compiled, so that they can be ignored from SCM commits. Per default, this will
 * be {@link QuerydslPluginExtension#DEFAULT_QUERYDSL_SOURCES_DIR}.
 *
 * @author holgerstolzenberg , iboyko
 * @since 1.0.0
 */
class QuerydslPlugin implements Plugin<Project> {

  public static final String TASK_GROUP = "Querydsl"

  private static final Logger LOG = Logging.getLogger(QuerydslPlugin.class)

  @Override
  void apply(final Project project) {
    LOG.info("Applying Querydsl plugin")

    // do nothing if plugin is already applied
    if (project.plugins.hasPlugin(QuerydslPlugin.class)) {
      return
    }

    LOG.info("Applying querydsl plugin")

    // apply core 'java' plugin if not present to make 'sourceSets' available
    if (!project.plugins.hasPlugin(JavaPlugin.class)) {
      project.plugins.apply(JavaPlugin.class)
    }

    // add 'Querydsl' DSL extension
    project.extensions.create(QuerydslPluginExtension.NAME, QuerydslPluginExtension)

    File querydslSourcesDir = querydslSourcesDir(project)

    // add new tasks for creating/cleaning the auto-value sources dir
    project.tasks.register("cleanQuerydslSourcesDir", CleanQuerydslSourcesDir) {
      sourcesDir = querydslSourcesDir
    }

    // make 'clean' depend clean ing querydsl sources
    project.tasks.clean.dependsOn project.tasks.cleanQuerydslSourcesDir

    project.tasks.register("compileQuerydsl", QuerydslCompile) {
      sourcesDir = querydslSourcesDir
    }

    project.tasks.compileJava.dependsOn project.tasks.compileQuerydsl

    project.afterEvaluate {
      addLibrary(project)
      configureSourceSet(project, querydslSourcesDir)
      registerSourceAtCompileJava(project, querydslSourcesDir)
      applyCompilerOptions(project)
    }
  }

  private static void applyCompilerOptions(Project project) {
    project.tasks.compileQuerydsl.options.compilerArgs += [
        "-proc:only",
        "-processor", project.querydsl.processors()
    ]

    if (project.querydsl.aptOptions.size() > 0) {
      for (aptOption in project.querydsl.aptOptions) {
        project.tasks.compileQuerydsl.options.compilerArgs << "-A" + aptOption
      }
    }
  }

  private void registerSourceAtCompileJava(Project project, File querydslSourcesDir) {
    project.compileJava {
      source querydslSourcesDir
    }
  }

  private void addLibrary(Project project) {
    def library = project.extensions.querydsl.library
    LOG.info("Querydsl library: {}", library)
    project.dependencies {
      implementation library
    }
  }

  private void configureSourceSet(Project project, File sourcesDir) {
    LOG.info("Create source set 'querydsl'.")
    project.sourceSets.main.java {
      srcDirs += sourcesDir
    }
  }

  private static File querydslSourcesDir(Project project) {
    File querydslSourcesDir = project.extensions.querydsl.querydslSourcesDir != null ?
      project.file(project.extensions.querydsl.querydslSourcesDir) :
      project.file("${project.buildDir}/generated/querydsl")
    LOG.info("Querydsl sources dir: {}", querydslSourcesDir.absolutePath)
    return querydslSourcesDir
  }
}
