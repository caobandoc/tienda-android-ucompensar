import org.jetbrains.kotlin.org.jline.utils.InputStreamReader
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("kotlin-kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.compensarshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.compensarshop"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"${getLocalProperty("GOOGLE_WEB_CLIENT_ID")}\"")
        manifestPlaceholders["MAPS_API_KEY"] = getLocalProperty("MAPS_API_KEY")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.glide)

    //new credentials
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    //Maps
    implementation(libs.play.services.maps)

    //DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.proto)

    kapt(libs.glide)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}

fun getLocalProperty(key: String): String {
    val properties = Properties()
    val localProperties = File(rootDir, "local.properties")
    if (localProperties.isFile){
        properties.load(InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8))
    }
    return properties.getProperty(key) ?: ""
}