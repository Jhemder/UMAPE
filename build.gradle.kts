// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Declara los plugins que estarán disponibles para los módulos,
    // usando los alias de tu archivo libs.versions.toml.
    // 'apply false' significa que la versión del plugin se define aquí,
    // pero el plugin se APLICA en el build.gradle.kts del módulo correspondiente.

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.kapt) apply false // Asegúrate de que este alias exista en tu libs.versions.toml
}
