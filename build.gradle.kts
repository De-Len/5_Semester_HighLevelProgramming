plugins {
    kotlin("jvm") version "2.0.10"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.sonarqube") version "7.0.0.6105"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sonar {
    properties {
        property("sonar.projectKey", "De-Len_5_Semester_HighLevelProgramming")
        property("sonar.organization", "de-len")
    }
}

application {
    mainClass.set("MainKt")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.h2database:h2:2.2.224")


}

tasks.test {
    useJUnitPlatform()
}