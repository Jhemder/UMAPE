plugins {
    // Aplica los plugins necesarios para este módulo de aplicación.
    // Estos plugins ya fueron declarados (con 'apply false') en el build.gradle.kts raíz.
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)    // Aplicando el plugin kapt
    alias(libs.plugins.kotlin.compose) // Si usas Jetpack Compose
}

android {
    namespace = "com.example.umape" // Reemplaza con tu namespace real
    compileSdk = 36 // O la versión más reciente que quieras usar

    defaultConfig {
        applicationId = "com.example.umape" // Reemplaza con tu applicationId real
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true // Habilita Compose si lo estás usando
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeBom.get() // Asumiendo que tienes composeBom en tu libs.versions.toml
        // Si no tienes composeBom y tienes una versión específica para kotlinCompilerExtensionVersion
        // podrías tener algo como: kotlinCompilerExtensionVersion = "1.5.3" (debe coincidir con tu versión de Compose y Kotlin)
        // O referenciarla desde libs.versions.toml si tienes una versión específica allí para el compilador de compose.
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // BOM de Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences.core)
    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-core:1.1.1")
// Animaciones avanzadas
    implementation("androidx.compose.animation:animation:1.5.4")
    implementation("androidx.compose.animation:animation-core:1.5.4")

// Lottie para animaciones (opcional)
    implementation("com.airbnb.android:lottie-compose:6.1.0")

// Coil para imágenes
    implementation("io.coil-kt:coil-compose:2.5.0")

// Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.ktx)
    implementation("androidx.compose.material:material-icons-extended:<version>")

    // Ejemplo de dependencia que podría usar kapt (como Room o Hilt)
    // implementation("androidx.room:room-runtime:2.6.1")
    // kapt("androidx.room:room-compiler:2.6.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
