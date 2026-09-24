// Модуль data — Android-библиотека. Реализует интерфейсы из domain
// и знает про источники данных: SharedPreferences, Room, NetworkApi, Firebase.
plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "ru.mirea.ivanovrr.lapka.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.annotation)

    // Firebase Authentication — версии всех firebase-библиотек задаёт BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    // Retrofit + Gson — загрузка пород, приютов и питомцев из сети
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Room — локальная БД для альбома встреч и отзывов
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
