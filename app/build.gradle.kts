plugins {
    id("java")
    id("org.teavm") version "0.9.2"
}

group = "com.uiframework.cairo"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Standard library and JSO APIs (works natively with the JS target)
    implementation("org.teavm:teavm-classlib:0.9.2")
    implementation("org.teavm:teavm-jso-apis:0.9.2")

    // JUnit 5 Test Implementation
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    // CRITICAL HOTFIX: Required by modern Gradle to execute JUnit 5 tests
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

teavm {
    js {
        // Change from "com.uiframework.practice.DrawTest" to this:
        mainClass = "com.uiframework.cairo.demo.ToastDemo"
        targetFileName = "app.js"
        outputDir = file("build/js") // Ensuring we output to the nested JS folder
        obfuscated = false
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

val prepareWebDir by tasks.registering(Copy::class) {
    dependsOn("generateJavaScript")
    from("src/main/webapp")
    into("build/js")
}

tasks.named("build") {
    dependsOn(prepareWebDir)
}