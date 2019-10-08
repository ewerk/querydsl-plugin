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

import com.ewerk.gradle.plugins.querydsl.tasks.QuerydslCompile
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.compile.JavaCompile

/**
 * This plugin can be used to easily create Querydsl Q-classes and attach them to the project
 * classpath.
 *
 * The plugin registers the extension 'querydsl' so that plugin specific configuration can
 * be overwritten within the build script. Please see the readme doc on Github for details on that.
 *
 * The plugin will generate an additional source directory into where the querydsl
 * classes will be compiled, so that they can be ignored from SCM commits. Per default, this will
 * be $buildDir/generated/querydsl.
 *
 * @author holgerstolzenberg , iboyko
 * @since 1.0.0
 */
@CompileStatic
class  QuerydslPlugin implements Plugin<Project> {

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
    def querydslPluginExtension = project.extensions.create(QuerydslPluginExtension.NAME, QuerydslPluginExtension) as QuerydslPluginExtension

    def javaPlugin = project.convention.plugins["java"] as JavaPluginConvention
    javaPlugin.sourceSets.configureEach { SourceSet it ->
        configureSourceSet(it, project, querydslPluginExtension)
    }
  }

  private void configureSourceSet(SourceSet sourceSet, Project project, QuerydslPluginExtension extension) {
    def configurations = project.configurations

    addLibrary(sourceSet, project, extension)
    configurations.getByName(sourceSet.annotationProcessorConfigurationName) { Configuration it ->
      it.extendsFrom(configurations.getByName(sourceSet.implementationConfigurationName))
    }
    configureTasks(sourceSet, project, extension)
  }

  private static void configureTasks(SourceSet sourceSet, Project project, QuerydslPluginExtension querydslPlugin) {
    def compileTask = project.tasks.getByName(sourceSet.compileJavaTaskName) as JavaCompile
    def querydslCompileTask = project.tasks.register(sourceSet.getTaskName("compile", "Querydsl"), QuerydslCompile)

    def destinationDir = compileTask.options.annotationProcessorGeneratedSourcesDirectory

    sourceSet.java.srcDir(destinationDir)

    querydslCompileTask.configure { JavaCompile task ->
      task.classpath = sourceSet.compileClasspath
      task.options.compilerArgs += [
        "-proc:only",
        "-processor", querydslPlugin.processors()
      ]
      task.options.annotationProcessorGeneratedSourcesDirectory = destinationDir
      task.options.annotationProcessorPath = sourceSet.annotationProcessorPath

      if (querydslPlugin.aptOptions.size() > 0) {
        for (aptOption in querydslPlugin.aptOptions) {
          task.options.compilerArgs << "-A" + aptOption
        }
      }

      task.source(sourceSet.java)
      task.destinationDir = sourceSet.java.outputDir
    }

    compileTask.configure { JavaCompile task ->
      task.dependsOn += querydslCompileTask
    }
  }

  private void addLibrary(SourceSet sourceSet, Project project, QuerydslPluginExtension querydslPlugin) {
    LOG.info("Querydsl library: {}", querydslPlugin.library)
    project.dependencies.add(sourceSet.annotationProcessorConfigurationName, querydslPlugin.library)
  }
}
