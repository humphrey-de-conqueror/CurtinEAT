# CurtinEAT: Build Setup

Android food-ordering app (COMP2008). This README lists what is needed to build the project.

## Required versions

| Item | Version |
| --- | --- |
| Android Studio | Quail 4 (2026.1.4) |
| JDK (for Gradle) | 17 |
| Android Gradle Plugin | 9.4.1 |
| Gradle | 9.6.0 (included through the Gradle Wrapper, no separate install) |
| Kotlin | 2.2.10 |
| Android SDK Platform | API 37 |
| Minimum Android version | Android 7.0 (API 24) |
| Application ID | `com.example.curtineat` |

## Other setup requirements

1. **Internet access.** Needed for Gradle to download dependencies and for the app's Firebase and image API features.
2. **Firebase configuration.** The file `./app/google-services.json` must be present and must belong to the Firebase project for `com.example.curtineat`. The project needs Email/Password sign-in and Cloud Firestore enabled. It contains project identifiers only, not a private key.
3. **Image API.** The app uses a hosted image service, with its URL set in the source. Nothing needs to be installed, but the service must be online for image upload and retrieval.
4. **Gradle JDK setting.** In Android Studio, set the Gradle JDK to 17 under Settings > Build, Execution, Deployment > Build Tools > Gradle.
5. **SDK.** Install Android SDK Platform 37 from SDK Manager.

## Build

Open the project root in Android Studio, wait for Gradle sync, then choose Build > Make Project.

Or from the command line, in the project root:

```bash
chmod +x ./gradlew
./gradlew assembleDebug
```

The debug APK is created at `./app/build/outputs/apk/debug/app-debug.apk`.

## Contents of this ZIP

The ZIP includes the source code, `./gradlew`, `./gradlew.bat`, the `./gradle/wrapper/` directory, the Gradle build files and version catalog, and `./app/google-services.json`. Generated `build/` directories and APKs are excluded.