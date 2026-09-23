plugins {
    alias(libs.plugins.android.application)
    // читает google-services.json и подключает проект Firebase
    alias(libs.plugins.google.services)
}

android {
    namespace = "ru.mirea.ivanovrr.lapka"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "ru.mirea.ivanovrr.lapka"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    // presentation видит domain (use case'ы, модели) и data (только чтобы собрать зависимости в di)
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // MVVM: ViewModel и LiveData (MediatorLiveData лежит в lifecycle-livedata)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // список приютов
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}