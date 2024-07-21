
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.d308app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.testingWGU.d308app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnit4"
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
}

//dependencies {
//
//    implementation(libs.appcompat)
//    implementation(libs.material)
//    implementation(libs.activity)
//    implementation(libs.constraintlayout)
//    implementation(libs.room.common)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.ext.junit)
//    androidTestImplementation(libs.espresso.core)
//
//
//    // Room components
//    var room_version = "2.6.1"
//    implementation ("androidx.room:room-runtime:$room_version")
//    annotationProcessor ("androidx.room:room-compiler:$room_version")
//    testImplementation ("androidx.room:room-testing:$room_version")
//
//    // Add the following dependencies for unit testing
//    testImplementation ("androidx.test:core:1.4.0")
//    testImplementation ("androidx.arch.core:core-testing:2.1.0")
//    testImplementation ("org.mockito:mockito-core:3.11.2")
//}

dependencies {
    implementation (libs.appcompat)
    implementation (libs.material)
    implementation (libs.activity)
    implementation (libs.constraintlayout)
    implementation (libs.room.common)
    testImplementation (libs.junit)
    androidTestImplementation (libs.ext.junit)
    androidTestImplementation (libs.espresso.core)

    // Room components
    var room_version = "2.6.1"
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")
    testImplementation ("androidx.room:room-testing:$room_version")

    // Add the following dependencies for unit testing
    var jUnitVersion = "1.1.4"
    testImplementation ("androidx.test:core:1.4.0")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.robolectric:robolectric:4.8.1")
    testImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    testImplementation ("org.mockito:mockito-core:3.11.2")
    testImplementation ("junit:junit:$jUnitVersion")
}

