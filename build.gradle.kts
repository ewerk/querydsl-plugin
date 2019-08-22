import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  wrapper
  groovy
  `java-gradle-plugin`
  `maven-publish`
  id("com.fizzpod.sweeney") version ("4.0.0")
  id("com.github.ksoichiro.build.info") version "0.2.0"
  id("com.gradle.plugin-publish") version "0.10.1"
}

sweeney {
  enforce(mapOf("type" to "jdk", "expect" to "[1.8,)"))
  enforce("gradle:[5.0,)")
}

repositories {
  jcenter()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.5.1"))

  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testImplementation("org.assertj:assertj-core:3.12.2")
}

tasks {
  wrapper {
    gradleVersion = "5.0"
  }

  named<Test>("test") {
    useJUnitPlatform()

    minHeapSize = "128m"
    testLogging.events = setOf(TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.PASSED)
  }

  register<Jar>("sourceJar") {
    from(sourceSets["main"].allSource)
    classifier = "sources"
  }

  withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
  }
}

publishing {
  publications {
    register<MavenPublication>("thePlugin") {
      from(components["java"])
      artifact(tasks["sourceJar"])
      pom {
        name.set("EWERK Querydsl Plugin")
        url.set("https://github.com/ewerk/querydsl-plugin")
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
      }
    }
  }
}

gradlePlugin {
  plugins {
    register("querydslPlugin") {
      id = "com.ewerk.gradle.plugins.querydsl"
      implementationClass = "com.ewerk.gradle.plugins.querydsl.QuerydslPlugin"
    }
  }
}

pluginBundle {
  website = "https://github.com/ewerk/querydsl-plugin"
  vcsUrl = "https://github.com/ewerk/querydsl-plugin"
  description = "Plugin for generating QueryDSL Q classes."
  tags = setOf("querydsl", "ewerk")

  plugins {
    named("querydslPlugin") {
      displayName = "Gradle Querydsl plugin"
      description = "A plugin for generating Querydsl Q classes."
    }
  }
}
