
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.6"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-html-builder-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")

    implementation("at.quickme.kotlinmailer:kotlinmailer-core:0.3.0")
    implementation("at.quickme.kotlinmailer:kotlinmailer-html:0.3.0")

    implementation("org.quartz-scheduler:quartz:2.3.2")

    //implementation("javax.mail:javax.mail-api:1.6.2")
    //implementation("com.sun.mail:javax.mail:1.6.2")
}
