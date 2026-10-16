## CurtinEAT

CurtinEAT is for Curtin students and staff who buy food on campus, and for permanent and temporary bazaar vendors who sell it. At the moment, people must walk to each stall to find out what is on the menu, because vendors are scattered around campus. The core task is making food discovery and ordering easier: customers browse menus, search products and place orders, while vendors manage products and update order status. Vendor and Product are related data, since each product belongs to one vendor. Customer, Vendor and Order are also related, because each order links one customer to one vendor and contains order products, with notifications linked to orders. Online payment and wallet top-up, and food delivery, are out of scope.


## Requirements

- Android Studio Quail 4 (2026.1.4) with JDK 17
- Android SDK Platform 37, minimum Android 7.0 (API 24)
- Internet access (Gradle downloads, Firebase, image API)
- Firebase config at ./app/google-services.json for the app ID `com.example.curtineat`
  (Email/Password sign-in and Cloud Firestore enabled in the Firebase project)

## Build steps

1. Extract the ZIP (or clone the repo) and open the project root in Android Studio.
2. Set the Gradle JDK to 17 under Settings > Build, Execution, Deployment > Build Tools > Gradle.
3. Check that ./app/google-services.json is present.
4. Wait for Gradle sync, then choose Build > Make Project.

Command line:

```bash
chmod +x ./gradlew
./gradlew assembleDebug
```

The APK is at ./app/build/outputs/apk/debug/app-debug.apk.

## Run steps

1. Start an emulator (API 24 or higher) or connect a phone with USB debugging on.
2. Select the `app` configuration and press Run.
3. The app opens on the startup screen, then the main screen with a loading spinner.
   You can browse vendors and products as a guest.
4. Sign in or register as a Customer or a Vendor. No test account, please register on the spot.

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

The app uses Android's system image picker (`ActivityResultContracts.GetContent`).

1. As a vendor, open Add Product and tap Choose Product Image (or change your profile picture
   on the profile page).
2. Pick a photo from the system picker. The selected image appears in the app.
3. Cancel the picker once to check that the app stays usable.

The app does not use the camera, GPS or contacts, and needs no storage permission.
