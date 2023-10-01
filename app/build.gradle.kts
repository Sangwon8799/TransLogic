plugins {
    id("com.android.application")
}

android {
    namespace = "com.yeungjin.translogic"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.yeungjin.translogic"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // HTTP를 통해 이미지를 불러오는 라이브러리
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // HTTP 통신을 간결하게 해주는 라이브러리
    implementation("com.android.volley:volley:1.2.1")

    // 채팅에 사용하는 Socket 통신 라이브러리
    implementation("io.socket:socket.io-client:2.0.0")

    // JSON 파일 변환을 쉽게 도와주는 라이브러리
    implementation("com.google.code.gson:gson:2.8.9")

    // 당겨서 새로고침 애니메이션을 지원하는 라이브러리
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}