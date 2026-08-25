import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy
import java.io.File
import java.util.Base64

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

// Ensure debug.keystore exists before build or task execution
fun ensureDebugKeystoreFile(): File {
  val userHomeDir = System.getProperty("user.home") ?: ""
  val userHomeKeystore = file("$userHomeDir/.android/debug.keystore")
  val rootKeystore = rootProject.file("debug.keystore")
  val base64Keystore = rootProject.file("debug.keystore.base64")

  if (rootKeystore.exists() && rootKeystore.length() > 0) {
    return rootKeystore
  }

  if (userHomeKeystore.exists() && userHomeKeystore.length() > 0) {
    return userHomeKeystore
  }

  if (base64Keystore.exists() && base64Keystore.length() > 0) {
    try {
      val base64Text = base64Keystore.readText().trim()
      val decodedBytes = Base64.getDecoder().decode(base64Text)
      rootKeystore.writeBytes(decodedBytes)
      try {
        userHomeKeystore.parentFile?.mkdirs()
        userHomeKeystore.writeBytes(decodedBytes)
      } catch (_: Exception) {}
      return rootKeystore
    } catch (_: Exception) {}
  }

  // Generate temporary debug keystore using keytool if not found
  try {
    val targetKeystore = if (userHomeKeystore.parentFile?.exists() == true || userHomeKeystore.parentFile?.mkdirs() == true) userHomeKeystore else rootKeystore
    val process = ProcessBuilder(
      "keytool", "-genkeypair",
      "-alias", "androiddebugkey",
      "-keypass", "android",
      "-keystore", targetKeystore.absolutePath,
      "-storepass", "android",
      "-dname", "CN=Android Debug,O=Android,C=US",
      "-keyalg", "RSA",
      "-keysize", "2048",
      "-validity", "10000"
    ).start()
    process.waitFor()
    if (targetKeystore.exists()) {
      return targetKeystore
    }
  } catch (_: Exception) {}

  return userHomeKeystore
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.hyperbolicexplorer.hecmos"
    minSdk = 24
    targetSdk = 36
    versionCode = 5
    versionName = "1.3.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    getByName("debug") {
      val keystore = ensureDebugKeystoreFile()
      storeFile = keystore
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug { 
      signingConfig = signingConfigs.getByName("debug")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }
  
  testOptions { 
    unitTests { isIncludeAndroidResources = true } 
  }
  
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

// Sync system environment GEMINI_API_KEY to .env file if available
val envGeminiKey: String? = System.getenv("GEMINI_API_KEY") ?: (project.findProperty("GEMINI_API_KEY") as? String)
if (!envGeminiKey.isNullOrBlank()) {
  val envFile = rootProject.file(".env")
  val existingContent = if (envFile.exists()) envFile.readText() else ""
  if (!existingContent.contains("GEMINI_API_KEY=") || existingContent.contains("GEMINI_API_KEY=MY_GEMINI_API_KEY") || existingContent.contains("GEMINI_API_KEY=\n")) {
    val updatedContent = if (existingContent.contains("GEMINI_API_KEY=")) {
      existingContent.replace(Regex("GEMINI_API_KEY=.*"), "GEMINI_API_KEY=$envGeminiKey")
    } else {
      "$existingContent\nGEMINI_API_KEY=$envGeminiKey\n".trim() + "\n"
    }
    envFile.writeText(updatedContent)
  }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.converter.moshi)
  implementation(libs.firebase.ai)
  implementation(libs.firebase.appcheck.recaptcha)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  implementation(libs.retrofit)
  
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}

// Task to automatically generate or restore debug.keystore using keytool if missing
abstract class GenerateDebugKeystoreTask : DefaultTask() {
  @get:OutputFile
  abstract val keystoreFile: RegularFileProperty

  @TaskAction
  fun generate() {
    val target = keystoreFile.get().asFile
    val userHomeDir = System.getProperty("user.home") ?: ""
    val userHomeKeystore = File("$userHomeDir/.android/debug.keystore")

    if ((target.exists() && target.length() > 0) || (userHomeKeystore.exists() && userHomeKeystore.length() > 0)) {
      return
    }

    val base64File = File(target.parentFile, "debug.keystore.base64")
    if (base64File.exists() && base64File.length() > 0) {
      try {
        val base64Text = base64File.readText().trim()
        val decodedBytes = Base64.getDecoder().decode(base64Text)
        target.writeBytes(decodedBytes)
        userHomeKeystore.parentFile?.mkdirs()
        userHomeKeystore.writeBytes(decodedBytes)
        return
      } catch (_: Exception) {}
    }

    try {
      target.parentFile?.mkdirs()
      ProcessBuilder(
        "keytool", "-genkeypair",
        "-alias", "androiddebugkey",
        "-keypass", "android",
        "-keystore", target.absolutePath,
        "-storepass", "android",
        "-dname", "CN=Android Debug,O=Android,C=US",
        "-keyalg", "RSA",
        "-keysize", "2048",
        "-validity", "10000"
      ).start().waitFor()

      if (target.exists() && target.length() > 0) {
        userHomeKeystore.parentFile?.mkdirs()
        target.copyTo(userHomeKeystore, overwrite = true)
      }
    } catch (_: Exception) {}
  }
}

val generateDebugKeystore = tasks.register<GenerateDebugKeystoreTask>("generateDebugKeystore") {
  description = "Automatically generates a debug.keystore file using keytool if missing before validateSigningDebug"
  keystoreFile.set(rootProject.file("debug.keystore"))
}

tasks.configureEach {
  if (name == "validateSigningDebug" || name == "preDebugBuild" || name.contains("packageDebug", ignoreCase = true)) {
    dependsOn(generateDebugKeystore)
  }
}


