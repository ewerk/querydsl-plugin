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

/**
 * DSL extension for the Querydsl plugin. Provides some convenient configuration options.
 *
 * The processor configuration and processor classes are defined in this location.
 *
 * @author holgerstolzenberg , griffio
 * @since 1.0.0
 */
class QuerydslPluginExtension {

  static String HIBERNATE_PROC = "com.querydsl.apt.hibernate.HibernateAnnotationProcessor"
  static String JDO_PROC = "com.querydsl.apt.jdo.JDOAnnotationProcessor"
  static String JPA_PROC = "com.querydsl.apt.jpa.JPAAnnotationProcessor"
  static String MORPHIA_PROC = "com.querydsl.apt.morphia.MorphiaAnnotationProcessor"
  static String QUERYDSL_PROC = "com.querydsl.apt.QuerydslAnnotationProcessor"
  static String ROO_PROC = "com.querydsl.apt.roo.RooAnnotationProcessor"
  static String SPRING_DATA_MONGO_PROC = "org.springframework.data.mongodb.repository.support.MongoAnnotationProcessor"
  static String LOMBOK_PROC = 'lombok.launch.AnnotationProcessorHider$AnnotationProcessor'

  static final String NAME = "querydsl"
  static final String DEFAULT_LIBRARY = "com.querydsl:querydsl-apt:4.1.4"

  String library = DEFAULT_LIBRARY

  boolean jpa = false
  boolean jdo = false
  boolean hibernate = false
  boolean morphia = false
  boolean roo = false
  boolean springDataMongo = false
  boolean querydslDefault = false
  boolean lombok = false

  List aptOptions = []

  String processors() {

    List processors = []

    if (hibernate) {
      processors << HIBERNATE_PROC
    }

    if (jdo) {
      processors << JDO_PROC
    }

    if (jpa) {
      processors << JPA_PROC
    }

    if (morphia) {
      processors << MORPHIA_PROC
    }

    if (roo) {
      processors << ROO_PROC
    }

    if (querydslDefault) {
      processors << QUERYDSL_PROC
    }

    if (springDataMongo) {
      processors << SPRING_DATA_MONGO_PROC
    }

    if (lombok) {
      processors << LOMBOK_PROC
    }

    return processors.join(",")
  }
}
