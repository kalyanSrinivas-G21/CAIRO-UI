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
    implementation("org.teavm:teavm-classlib:0.9.2")
    implementation("org.teavm:teavm-jso-apis:0.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

teavm {
    // REVERTED: Strictly targeting WebAssembly per architecture constraints
    wasm {
        mainClass = "com.uiframework.practice.DrawTest"
        targetFileName = "app.wasm"
        outputDir = file("build/wasm")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// Updated to output back to the build/wasm directory
val prepareWebDir by tasks.registering(Copy::class) {
    dependsOn("generateWasm")
    from("src/main/webapp")
    into("build/wasm")
}

tasks.named("build") {
    dependsOn(prepareWebDir)
}