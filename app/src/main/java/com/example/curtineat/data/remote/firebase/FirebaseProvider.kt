package com.example.curtineat.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseProvider {
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
}