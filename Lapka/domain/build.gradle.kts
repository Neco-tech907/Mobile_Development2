// Модуль domain — чистая Java-библиотека: ни одной зависимости от Android.
// Здесь бизнес-модели, интерфейсы репозиториев и use case'ы.
plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
