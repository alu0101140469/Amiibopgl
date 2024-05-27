package com.example.amiibopgl.shared

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.launch

class LoginModel: ViewModel(){
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    val conexion = FirebaseFirestore.getInstance()

    fun singIn( email: String, pswd: String, home: () -> Unit)
            = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, pswd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d("Felicidades", "Has entrado correctamente : ${task.result.toString()}")
                        home()
                    }else{
                        Log.d("Error", "Error catch : ${task.result.toString()}")
                    }
                }
        }catch (ex:Exception){
            Log.d("Error", "Error catch : ${ex.message}")

        }
    }

    fun register(email: String, pswd:String, home: () -> Unit){
        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email,pswd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        home()
                    else{
                        Log.d("Error", "Error al iniciar sesión : ${task.result.toString()}")
                    }
                    _loading.value = false
                }

        }

    }

}