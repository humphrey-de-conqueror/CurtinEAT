package com.example.curtineat.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseProvider {

    val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()

    val auth: FirebaseAuth =
        FirebaseAuth.getInstance()
}