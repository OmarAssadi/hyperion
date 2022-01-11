plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("application")
    id("java")
}

group = "org.hyperion"
version = "1.0.1"

repositories {
    mavenCentral()
}

val graalVersion = "21.3.0"
val junitVersion = "5.8.2"

dependencies {
    implementation("org.apache.mina:mina-core:2.0.16") // 2.0.17 and beyond seem to have compatibility issues, need to look into
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("org.slf4j:slf4j-jdk14:1.7.32")
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("org.bouncycastle:bcprov-jdk15on:1.69")
    implementation("org.graalvm.sdk:graal-sdk:$graalVersion")
    implementation("org.graalvm.js:js:$graalVersion")
    implementation("org.graalvm.js:js-scriptengine:$graalVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}

val appMainClass = "org.hyperion.Server"

application {
    mainClass.set(appMainClass)
    applicationDefaultJvmArgs = listOf("-Dpolyglot.js.nashorn-compat=true")
}

tasks.withType<Jar>  {
    manifest {
        attributes["Main-Class"] = appMainClass
    }
}

task("generateRsaKeys", JavaExec::class) {
    dependsOn("classes")
    main = "org.hyperion.util.RsaKeyGenerator"
    classpath = sourceSets.main.get().runtimeClasspath
}
