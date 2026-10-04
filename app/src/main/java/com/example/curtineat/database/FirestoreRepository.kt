package com.example.curtineat.database

import com.example.daodao.Vendor
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.curtineat.R
import java.io.ByteArrayOutputStream


class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()


    // =========================
    // TEST CONNECTION
    // =========================

    suspend fun testConnection(): String? {

        val document = db
            .collection("testData")
            .document("test1")
            .get()
            .await()

        return document.getString("message")
    }


    // =========================
    // SEED FIRESTORE
    // =========================

    suspend fun seedFirestoreData(
        context: Context
    ) {

        val batch = db.batch()


        // VENDORS
        val vendors = listOf(

            Vendor(
                vendorId = 1,
                vendorName = "Mama's Kitchen",
                rating = 4.5,
                category = "Local Food",
                distance = 0.3,
                vendorPassword = "a",
                vendorEmail = "abc"
            ),

            Vendor(
                vendorId = 2,
                vendorName = "Burger Bros",
                rating = 4.2,
                category = "Western",
                distance = 0.8,
                vendorPassword = "b",
                vendorEmail = "burgerbros@gmail.com"
            ),

            Vendor(
                vendorId = 3,
                vendorName = "Sushi Zen",
                rating = 4.8,
                category = "Japanese",
                distance = 1.2,
                vendorPassword = "c",
                vendorEmail = "sushizen@gmail.com"
            ),

            Vendor(
                vendorId = 4,
                vendorName = "Taco Fiesta",
                rating = 3.9,
                category = "Mexican",
                distance = 2.0,
                vendorPassword = "d",
                vendorEmail = "tacofiesta@gmail.com"
            ),

            Vendor(
                vendorId = 5,
                vendorName = "Pizza Palace",
                rating = 4.1,
                category = "Western",
                distance = 1.5,
                vendorPassword = "e",
                vendorEmail = "pizzapalace@gmail.com"
            )
        )

        vendors.forEach { vendor ->

            val ref = db
                .collection("vendors")
                .document(vendor.vendorId.toString())

            batch.set(ref, vendor)
        }


        // CUSTOMER
        val customer = Customer(
            customerId = 1,
            customerName = "Customer satu",
            customerEmail = "b",
            customerPassword = "c",
            moneyBalance = 100.00
        )

        batch.set(
            db.collection("customers")
                .document(customer.customerId.toString()),
            customer
        )


        // PRODUCTS
        val products = listOf(

            Product(
                productId = 1,
                vendorID = 1,
                productName = "Nasi Lemak",
                productPrice = 5.50,
                productImage = drawableToBase64(
                    context,
                    R.drawable.nasi_lemak
                )
            ),

            Product(
                productId = 2,
                vendorID = 1,
                productName = "Mee Goreng",
                productPrice = 6.00,
                productImage = drawableToBase64(
                    context,
                    R.drawable.mee_goreng
                )
            ),

            Product(
                productId = 3,
                vendorID = 2,
                productName = "Cheeseburger",
                productPrice = 12.90,
                productImage = drawableToBase64(
                    context,
                    R.drawable.cheeseburger
                )
            ),

            Product(
                productId = 4,
                vendorID = 2,
                productName = "Chicken Wings",
                productPrice = 9.90,
                productImage = drawableToBase64(
                    context,
                    R.drawable.chicken_wings
                )
            ),

            Product(
                productId = 5,
                vendorID = 3,
                productName = "Salmon Sushi",
                productPrice = 18.00,
                productImage = drawableToBase64(
                    context,
                    R.drawable.salmon_sushi
                )
            ),

            Product(
                productId = 6,
                vendorID = 3,
                productName = "Miso Soup",
                productPrice = 4.50,
                productImage = drawableToBase64(
                    context,
                    R.drawable.miso_soup
                )
            ),

            Product(
                productId = 7,
                vendorID = 4,
                productName = "Beef Taco",
                productPrice = 8.90,
                productImage = drawableToBase64(
                    context,
                    R.drawable.beef_taco
                )
            ),

            Product(
                productId = 8,
                vendorID = 5,
                productName = "Margherita Pizza",
                productPrice = 22.00,
                productImage = drawableToBase64(
                    context,
                    R.drawable.margherita
                )
            ),

            Product(
                productId = 9,
                vendorID = 5,
                productName = "Garlic Bread",
                productPrice = 5.00,
                productImage = drawableToBase64(
                    context,
                    R.drawable.garlic_bread
                )
            )
        )

        products.forEach { product ->

            val ref = db
                .collection("products")
                .document(product.productId.toString())

            batch.set(ref, product)
        }


        // NOTIFICATIONS
        val notifications = listOf(

            Notification(
                notificationId = 1,
                message = "order submitted, pending for vendor verification",
                time = "12 p.m."
            ),

            Notification(
                notificationId = 2,
                message = "vendor accept your order, please wait",
                time = "11 p.m."
            ),

            Notification(
                notificationId = 3,
                message = "food prepared, please pick up or i buang your food",
                time = "10 p.m."
            ),

            Notification(
                notificationId = 4,
                message = "vendor blocklist you",
                time = "14 p.m."
            )
        )

        notifications.forEach { notification ->

            val ref = db
                .collection("notifications")
                .document(notification.notificationId.toString())

            batch.set(ref, notification)
        }


        batch.commit().await()
    }


    // =========================
    // GET PRODUCTS
    // =========================

    suspend fun getProducts(): List<Product> {

        val result = db
            .collection("products")
            .get()
            .await()

        return result.documents.mapNotNull { document ->

            val productId =
                document.getLong("productId")?.toInt()

            val vendorID =
                document.getLong("vendorID")?.toInt()

            val productName =
                document.getString("productName")

            val productPrice =
                document.getDouble("productPrice")

            val productImage =
                document.getString("productImage")


            if (
                productId == null ||
                vendorID == null ||
                productName.isNullOrBlank() ||
                productPrice == null ||
                productImage.isNullOrBlank()
            ) {

                null

            } else {

                Product(
                    productId = productId,
                    vendorID = vendorID,
                    productName = productName,
                    productPrice = productPrice,
                    productImage = productImage
                )
            }
        }
    }


    // =========================
    // ADD PRODUCT
    // =========================

    suspend fun addProduct(product: Product) {

        db.collection("products")
            .document(product.productId.toString())
            .set(product)
            .await()
    }


    // =========================
    // UPDATE PRODUCT
    // =========================

    suspend fun updateProduct(product: Product) {

        db.collection("products")
            .document(product.productId.toString())
            .set(product)
            .await()
    }


    // =========================
    // DELETE PRODUCT
    // =========================

    suspend fun deleteProduct(productId: Int) {

        db.collection("products")
            .document(productId.toString())
            .delete()
            .await()
    }

    private fun drawableToBase64(
        context: Context,
        drawableId: Int
    ): String {

        val originalBitmap =
            BitmapFactory.decodeResource(
                context.resources,
                drawableId
            )

        // Make image smaller so Firestore document is not huge
        val resizedBitmap =
            Bitmap.createScaledBitmap(
                originalBitmap,
                400,
                400,
                true
            )

        val outputStream =
            ByteArrayOutputStream()

        resizedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            60,
            outputStream
        )

        val imageBytes =
            outputStream.toByteArray()

        return "base64:" + Base64.encodeToString(
            imageBytes,
            Base64.NO_WRAP
        )
    }
}