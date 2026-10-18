# CurtinEAT

Android food-ordering app for COMP2008.

## App description

CurtinEAT is for Curtin students and staff who buy food on campus, and for permanent and temporary bazaar vendors who sell it. At the moment, people must walk to each stall to find out what is on the menu, because vendors are scattered around campus. The core task is making food discovery and ordering easier: customers browse menus, search products and place orders, while vendors manage products and update order status. Vendor and Product are related data, since each product belongs to one vendor. Customer, Vendor and Order are also related, because each order links one customer to one vendor and contains order products, with notifications linked to orders. Online payment and wallet top-up, and food delivery, are out of scope.

1. **Users and problem.** CurtinEAT is for Curtin students and staff who buy food on campus, and for permanent and temporary bazaar vendors who sell it. Vendors are scattered around campus, so people must walk to each stall to find out what is on the menu.
2. **Core task.** Making food discovery and ordering easier: customers browse menus, search products and place orders, while vendors manage products and update order status.
3. **Related data.** Each Product belongs to one Vendor. Each Order links one Customer to one Vendor and contains order products, and Notifications are linked to orders.
4. **Out of scope.** Online payment, wallet top-up and food delivery.

## Requirements

| Item | Version |
| --- | --- |
| Android Studio | Quail 4 (2026.1.4) |
| JDK (for Gradle) | 17 |
| Android Gradle Plugin | 9.4.1 |
| Gradle | 9.6.0 (via the Gradle Wrapper, no separate install) |
| Kotlin | 2.2.10 |
| Android SDK Platform | API 37 |
| Minimum Android version | Android 7.0 (API 24) |
| Application ID | `com.example.curtineat` |

Other requirements:

- **Internet access** for Gradle dependencies, Firebase and the image API.
- **Firebase config**: `./app/google-services.json` must be present for `com.example.curtineat` (Email/Password sign-in and Cloud Firestore enabled). It contains project identifiers only, not a private key.
- **Image API**: a hosted service whose URL is set in the source. Nothing to install, but it must be online for image upload and retrieval.
- **Gradle JDK**: set to 17 under Settings > Build, Execution, Deployment > Build Tools > Gradle.
- **SDK**: install Android SDK Platform 37 from the SDK Manager.

## Build steps

1. Extract the ZIP and open the project root in Android Studio.
2. Set the Gradle JDK to 17 (see above).
3. Check that `./app/google-services.json` is present.
4. Wait for Gradle sync, then choose Build > Make Project.

Command line, from the project root:

```bash
chmod +x ./gradlew
./gradlew assembleDebug
```

The APK is created at `./app/build/outputs/apk/debug/app-debug.apk`.

## Run steps

1. Start an emulator (API 24 or higher) or connect a phone with USB debugging on.
2. Select the `app` configuration and press Run.
3. The app opens on the startup screen, then the main screen. You can browse vendors and products as a guest.
4. To sign in, register as a Customer or a Vendor. There are no test accounts, so register a new one.
5. On Android 13 or higher the app asks for notification permission on first launch. Allow it to see order notifications.

## Demonstrating network features

1. **Cloud data (Firebase).** Open the main screen and press Refresh. A full-screen spinner
   shows while vendors and products are fetched from Firestore.
2. **Image API upload.** Sign in as a vendor, open Add Product, choose an image and save.
   The image is sent to the image API, which returns an image ID stored with the product.
3. **Image API retrieval and caching.** Open the main screen as a customer. The product image
   loads through the API once, then from the local cache on later visits.
4. **Real-time orders.** Use two devices or emulators. As a customer, add a product to the
   cart and check out. The vendor's order list updates without refreshing. Change the order
   status as the vendor and watch the customer's order update.
5. **Offline behaviour.** Turn on airplane mode and reopen the app. Cached vendors, products
   and images still show. Press Refresh to see the error message. Checkout needs a connection.

## Demonstrating device features
The app uses Android's system image picker (`ActivityResultContracts.GetContent`). The app also requests the notification permission (Android 13+). It does not use the camera, GPS or contacts, and needs no storage permission.

1. As a vendor, open Add Product and tap Choose Product Image (or change your profile picture
   on the profile page).
2. Pick a photo from the system picker. The selected image appears in the app.
3. Cancel the picker once to check that the app stays usable.

## Contents of this ZIP

Source code, `./gradlew`, `./gradlew.bat`, `./gradle/wrapper/`, the Gradle build files and version catalog, and `./app/google-services.json`. Generated `build/` directories and APKs are excluded. The group report (PDF) and contribution statement are included at the top level.