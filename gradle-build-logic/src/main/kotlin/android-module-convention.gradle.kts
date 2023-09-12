import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    compileSdkVersion(33)
    namespace = "ru.tetraquark.ton.explorer.app.android"

    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
