import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-parcelize")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val newsApiKey: String = localProperties.getProperty("NEWS_API_KEY") ?: ""

android {
    namespace = "com.company.newsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.company.newsapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "NEWS_API_KEY", "\"${newsApiKey}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    flavorDimensions += "newsSource"
    productFlavors {
        create("bbc") {
            dimension = "newsSource"
            applicationIdSuffix = ".bbc"
            versionNameSuffix = "-bbc"
            buildConfigField("String", "NEWS_SOURCE_ID", "\"bbc-news\"")
            buildConfigField("String", "NEWS_SOURCE_NAME", "\"BBC News\"")
        }
        
        create("cnn") {
            dimension = "newsSource" 
            applicationIdSuffix = ".cnn"
            versionNameSuffix = "-cnn"
            buildConfigField("String", "NEWS_SOURCE_ID", "\"cnn\"")
            buildConfigField("String", "NEWS_SOURCE_NAME", "\"CNN\"")
        }
        
        create("techcrunch") {
            dimension = "newsSource"
            applicationIdSuffix = ".tech" 
            versionNameSuffix = "-tech"
            buildConfigField("String", "NEWS_SOURCE_ID", "\"techcrunch\"")
            buildConfigField("String", "NEWS_SOURCE_NAME", "\"TechCrunch\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") 
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("androidx.compose.material:material:1.7.6")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.13")
    
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}