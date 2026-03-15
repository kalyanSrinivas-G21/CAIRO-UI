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
    // TeaVM standard library and JavaScript Object (JSO) APIs
    implementation("org.teavm:teavm-classlib:0.9.2")
    implementation("org.teavm:teavm-jso-apis:0.9.2")
}

teavm {
    wasm {
        mainClass = "com.uiframework.cairo.Main"
        targetFileName = "app.wasm"
        outputDir = file("build/wasm")    }
}

// Convenience task to copy index.html into the Wasm build directory
val prepareWebDir by tasks.registering(Copy::class) {
    dependsOn("generateWasm")
    from("src/main/webapp")
    into("build/wasm")
}

tasks.named("build") {
    dependsOn(prepareWebDir)
}