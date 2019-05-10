package com.ewerk.gradle.plugins.querydsl;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * This plugin can be used to easily create Querydsl classes and attach them to the project
 * classpath.<br/><br/>
 *
 * The plugin registers the extension 'querydsl' so that plugin specific configuration can
 * be overwritten within the build sScript. Please see the readme doc on Github for details on that.
 * <br/><br/>
 *
 * The plugin will generate an additional source directory into where the querydsl
 * classes will be compiled, so that they can be ignored from SCM commits. Per default, this will
 * be {@link QuerydslPluginExtension#DEFAULT_QUERYDSL_SOURCES_DIR}.
 * <br/><br/>
 *
 * @author holgerstolzenberg
 * @since 5.0.0
 */
public class QuerydslPlugin implements Plugin<Project> {

  @Override
  public void apply(final Project project) {
    // TODO implement
  }
}
