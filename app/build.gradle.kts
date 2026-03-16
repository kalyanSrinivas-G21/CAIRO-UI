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
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

teavm {
    // PIVOT: Compiling to JavaScript for robust DOM and Canvas interaction
    js {
        mainClass = "com.uiframework.practice.DrawTest"
        targetFileName = "app.js"
        outputDir = file("build/js")
        // Disabling obfuscation makes debugging in the browser console much easier
        obfuscated = false
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// Updated to depend on the JS compilation task and output to the new directory
val prepareWebDir by tasks.registering(Copy::class) {
    dependsOn("generateJavaScript")
    from("src/main/webapp")
    into("build/js")
}

tasks.named("build") {
    dependsOn(prepareWebDir)
}