# CurtinEAT

**COMP2008 — Mobile Application Development**

CurtinEAT is an Android food-ordering application designed for Curtin University students and campus food vendors. It provides a central platform for discovering food menus, managing products, and handling customer orders, including food offered by temporary vendors at campus bazaars.

Repository: [humphrey-de-conqueror/CurtinEAT](https://github.com/humphrey-de-conqueror/CurtinEAT)

## 1. Application Description

### Users

CurtinEAT serves two main user groups:

* **Curtin students and customers:** Students commonly need to visit food vendors physically to discover menus and place orders. Food vendors may be scattered across campus, making it difficult to discover available food options.
* **Campus food vendors:** Permanent and temporary vendors, including vendors participating in campus bazaars, need a central platform to advertise their food menus and manage products and orders.

### Core Task

The core task is to make food discovery and ordering more convenient by providing a shared platform where customers can browse vendor menus and vendors can manage their products and customer orders.

### Data

CurtinEAT uses structured local data, cloud data, and remote image storage.

| Data entity  | Purpose                                                                                  | Relationship                                                |
| ------------ | ---------------------------------------------------------------------------------------- | ----------------------------------------------------------- |
| Vendor       | Stores vendor information, including name, email, category, rating, and image identifier | A vendor can offer multiple products                        |
| Product      | Stores product name, price, availability, vendor identifier, and image identifier        | Each product is associated with a vendor through `vendorId` |
| Customer     | Stores customer account and profile information                                          | Associated with customer authentication and ordering        |
| Order        | Represents a customer's order                                                            | Related to customer and order-product information           |
| OrderProduct | Represents product information associated with an order                                  | Connects orders with ordered products                       |
| Notification | Stores notification information                                                          | Associated with relevant users and application events       |
| CachedImage  | Stores cached image data                                                                 | Uses an image identifier to retrieve cached images          |

Room provides local persistence for application data and image caching. Firebase Authentication and Cloud Firestore provide authentication and cloud-backed functionality. The image API stores uploaded images remotely and returns image identifiers used by the application.

Theme preferences are persisted separately using Android Preferences DataStore.

### Out of Scope

The following features are deliberately excluded from the project:

1. **Online payment and wallet management:** The application does not implement online payment processing, wallet top-ups, or payment-gateway integration.
2. **Food delivery:** The application does not provide delivery services, driver assignment, or delivery tracking.

## 2. Features

* Customer and vendor authentication.
* Restaurant and food-product browsing.
* Product search.
* Shopping cart and checkout workflow.
* Vendor profile and product management.
* Order information and order-status management.
* Customer and vendor profile management.
* Notifications.
* Image selection, upload, retrieval, and local image caching.
* Multiple application themes: System, Dark, White, Warm, and Fresh.
* Local persistence using Room and DataStore.

The available functions depend on the user's role and the configuration of the remote services.

## 3. Technology Stack

| Component            | Technology                                 |
| -------------------- | ------------------------------------------ |
| Language             | Kotlin                                     |
| UI                   | Jetpack Compose                            |
| Design system        | Material 3                                 |
| Architecture         | MVVM                                       |
| State management     | ViewModel and Kotlin Flow / StateFlow      |
| Navigation           | Navigation Compose                         |
| Cloud authentication | Firebase Authentication                    |
| Cloud database       | Cloud Firestore                            |
| Local database       | Room and SQLite                            |
| User preferences     | Preferences DataStore                      |
| Image API            | Retrofit, OkHttp, and Kotlin serialization |
| Image display        | Coil                                       |
| Build system         | Gradle Wrapper                             |

The application separates UI screens, ViewModels, repositories, and data sources. Composables display observable state and send user actions to the application layer. Repositories coordinate local storage and remote services.

## 4. System Requirements

The following environment is recommended for building the submitted project.

| Requirement             | Version                                               |
| ----------------------- | ----------------------------------------------------- |
| Android Studio          | Quail 4 (2026.1.4)                                    |
| JDK                     | 17                                                    |
| Android Gradle Plugin   | 9.4.1                                                 |
| Gradle                  | 9.6.0 through the Gradle Wrapper                      |
| Android SDK Platform    | API 37                                                |
| Minimum Android version | Android 7.0 (API 24)                                  |
| Internet access         | Required for dependency downloads and online features |

Android Studio Quail 4 supports Android Gradle Plugin 9.4.1. The project uses Kotlin 2.2.10 and Java 11 source and target compatibility. The Gradle build itself requires JDK 17.

Official compatibility references:

* [Android Gradle Plugin compatibility](https://developer.android.com/build/releases/about-agp)
* [Android Studio releases](https://developer.android.com/studio/releases)

## 5. Project Setup

### 5.1 Obtain the source code

Download the repository as a ZIP from GitHub and extract it, or clone it using Git:

```bash
git clone https://github.com/humphrey-de-conqueror/CurtinEAT.git
cd CurtinEAT
```

Open the project root in Android Studio. The root directory should contain the Gradle build files and wrapper scripts.

### 5.2 Configure the development environment

1. Install Android Studio Quail 4 or another compatible release.
2. Install JDK 17.
3. Open the extracted project in Android Studio.
4. Set the Gradle JDK to JDK 17 under **Settings → Build, Execution, Deployment → Build Tools → Gradle**.
5. Install Android SDK Platform 37 using SDK Manager.
6. Allow Gradle to download dependencies and complete project synchronization.

A separate system-wide Gradle installation is not required when the included Gradle Wrapper is present.

### 5.3 Firebase configuration

The project uses Firebase Authentication and Cloud Firestore. The Android Firebase configuration file is expected at:

```text
app/google-services.json
```

The submission package should include the required configuration file if permitted by the assessment and repository policies. Confirm that it belongs to the intended Firebase project.

This file contains project configuration and identifiers; it is not equivalent to a service-account private key. Nevertheless, configuration should be reviewed before publication, and private credentials must never be committed.

If the Firebase configuration is missing or points to an inaccessible project, obtain the correct configuration and register the application with the application ID:

```text
com.example.curtineat
```

### 5.4 Image API

CurtinEAT uses a remote image service for uploading and retrieving images. The API provides the following operations:

* `POST /images` — uploads an image and returns an image identifier.
* `GET /images/{imageId}` — retrieves an image using its identifier.

The application stores image identifiers in its data records and caches image bytes locally. A working internet connection and an available image service are required for remote image operations.

## 6. Build Instructions

### Option A: Build using Android Studio

1. Open the project root in Android Studio.
2. Wait for Gradle synchronization to finish.
3. Select **Build → Make Project**.
4. Review the Build Output window and confirm that the build succeeds.

To generate a debug APK, select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.

### Option B: Build using the Gradle Wrapper

Run the following commands from the project root.

**Linux or macOS:**

```bash
chmod +x gradlew
./gradlew clean
./gradlew assembleDebug
```

**Windows Command Prompt:**

```bat
gradlew.bat clean
gradlew.bat assembleDebug
```

To run the project's full Gradle build task:

```bash
./gradlew build
```

On Windows:

```bat
gradlew.bat build
```

The generated debug APK is normally located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Build outputs are generated during compilation and should not be included in the source ZIP.

## 7. Run Instructions

### Using an Android emulator

1. Open Android Studio's Device Manager.
2. Create or select an Android Virtual Device running Android API 24 or later.
3. Start the emulator.
4. Select the `app` run configuration.
5. Click **Run**.
6. Register or sign in with an appropriate test account.

### Using a physical Android device

1. Enable Developer Options and USB debugging.
2. Connect the phone to the development computer.
3. Approve the debugging authorization prompt.
4. Select the connected device in Android Studio.
5. Click **Run** to install and launch CurtinEAT.

An internet connection is needed for Firebase-backed and image-service operations. Use test accounts and data prepared for the demonstration.

## 8. Demonstration Guide

The following scenarios are intended to demonstrate the application's main functionality and its network and device integrations.

### 8.1 Customer workflow

1. Register or sign in with a customer account.
2. Browse the available vendors and food products.
3. Search for a product.
4. Add a product to the shopping cart.
5. Proceed through the checkout workflow.
6. Inspect the available order information.
7. Open the customer profile and notifications.

### 8.2 Vendor workflow

1. Sign in with a vendor account.
2. Open the vendor dashboard and profile.
3. Browse the vendor's products.
4. Add a product and select an image.
5. Save the product and verify that it appears in the relevant menu.
6. Open the order-management screen.
7. Demonstrate an available order-status update.

Use prepared test accounts and existing data where necessary. Do not rely on an unconfigured account or unavailable backend data during the demonstration.

### 8.3 Network demonstration

The image API provides a concrete network interaction that can be demonstrated as follows:

1. Connect the emulator or physical device to the internet.
2. Open a product or vendor profile that supports image selection.
3. Choose an image through the Android system photo picker.
4. Observe the upload progress indicator.
5. Verify that the upload returns an image identifier and that the resulting image is displayed.
6. Reopen the relevant screen or image to test retrieval and caching.

The application also uses Firebase Authentication and Cloud Firestore for account and cloud-data operations. Demonstrate a supported read or update and verify the resulting application state.

For the assessment, prepare and test loading, success, empty, error, retry, and offline scenarios where supported by the implemented request flows. Do not claim that a scenario is handled until it has been tested in the submitted build.

### 8.4 Device demonstration

CurtinEAT uses Android's system image-selection interface through `ActivityResultContracts.GetContent`.

To demonstrate this feature:

1. Open the vendor profile editor or Add Product screen.
2. Select **Change Profile Picture** or **Choose Product Image**.
3. Select an image from the system-provided image interface.
4. Verify that the image upload begins and that the resulting image appears in the application.
5. Cancel image selection and verify that the application remains usable.

The image picker is an interaction with an Android-provided interface. Camera capture, GPS, and delivery tracking are not claimed as supported features.

## 9. Architecture and Source Structure

The project uses separate packages for UI, application state, and data access.

```text
app/src/main/java/com/example/curtineat/
├── data/
│   ├── local/room/
│   │   ├── dao/
│   │   ├── entity/
│   │   ├── mapper/
│   │   ├── AppDatabase.kt
│   │   └── RoomProvider.kt
│   ├── remote/
│   │   ├── api/
│   │   └── firebase/
│   └── repository/
├── model/
├── ui/
│   ├── components/
│   └── theme/
├── view/
├── viewmodel/
├── CurtinEATApplication.kt
└── MainActivity.kt
```

* **View:** Compose screens display application state and forward user actions.
* **ViewModel:** Coordinates screen state and application actions.
* **Repositories:** Coordinate data operations and isolate screens from data-source implementation details.
* **Local data sources:** Room DAOs provide access to cached and structured local data.
* **Remote data sources:** Firebase sources and the image API provide cloud and network operations.
* **Theme preferences:** DataStore persists the selected application theme.

## 10. Submission Checklist

Before submitting the project ZIP:

* Include all application source code and required resources.
* Include `gradlew`, `gradlew.bat`, and the complete `gradle/wrapper/` directory.
* Include all Gradle build files and the version catalog.
* Include the required Firebase configuration if permitted.
* Exclude generated `build/` directories, compiled APKs, local caches, and private credentials.
* Extract the ZIP into a clean directory and build it using the documented commands.
* Verify that the app launches on the selected emulator or physical device.
* Test the customer and vendor workflows.
* Verify the network and image-selection demonstrations.
* Confirm that the documented failure and offline scenarios match the actual implementation.

## Repository

https://github.com/humphrey-de-conqueror/CurtinEAT
