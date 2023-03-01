import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    compileSdkVersion(33)

    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
