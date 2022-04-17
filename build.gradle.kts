plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.12"
}

group = "com.github.loadingbg"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.fxyz3d:fxyz3d:0.5.4")
}

javafx {
    version = "17.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("loadingbg.stereometry.Main")
}

tasks.register("runReference") {
    application {
        mainClass.set("loadingbg.stereometry.reference.Main")
    }
    dependsOn("run")
}