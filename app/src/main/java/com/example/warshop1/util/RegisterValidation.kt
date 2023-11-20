package com.example.warshop1.util

sealed class RegisterValidation(){
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}

data class RegisterFieldState(
    val email : RegisterValidation,
    val password : RegisterValidation,
    val confpassword : RegisterValidation
)

