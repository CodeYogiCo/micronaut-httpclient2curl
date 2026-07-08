import org.jetbrains.kotlin.gradle.dsl.JvmTarget

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}


plugins {
    kotlin("jvm") version "2.4.0"
    `maven-publish`
    jacoco
    id("org.jetbrains.dokka") version "1.9.20" apply false
}
val targetJvm = 25

kotlin {
    jvmToolchain(targetJvm)
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(targetJvm.toString()))
    }
}

group = "com.codeyogico"
version = (findProperty("publishVersion") as String?) ?: "0.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}


dependencies {

    implementation(platform(libs.micronautBom))
    implementation(platform(libs.jacksonBom))
    implementation(libs.kotlinReflect)
    implementation(libs.kotlinStdlib)
    implementation(libs.bundles.micronaut)

    testImplementation(platform(libs.micronautBom))

    testImplementation(libs.kotlinStdlib)
    testImplementation(libs.kotlinReflect)
    testImplementation(libs.bundles.micronaut)

    testImplementation(libs.junitJupiterApi)
    testImplementation(libs.junitJupiterParams)
    testRuntimeOnly(libs.junitJupiterEngine)

    testImplementation(libs.bundles.micronautTest)

    testImplementation(libs.jacksonModuleKotlin)
}

tasks {
    test {
        useJUnitPlatform()
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/codeyogico/micronaut-httpclient2curl")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

