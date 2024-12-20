package com.example.loja.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    fun getatual(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun login(email: String, senha: String): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, senha).await()
            authResult.user
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun registar(email: String, senha: String): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, senha).await()
            authResult.user
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao registar usuário", e)
            null
        }
    }


    fun logout() {
        firebaseAuth.signOut()
    }

}