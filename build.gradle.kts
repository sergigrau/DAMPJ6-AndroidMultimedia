// Top-level build file where you can add configuration options common to all sub-projects/modules.
// build.gradle.kts (Raíz del proyecto)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
}