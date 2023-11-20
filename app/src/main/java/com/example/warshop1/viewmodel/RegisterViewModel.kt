package com.example.warshop1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warshop1.data.User
import com.example.warshop1.util.RegisterFieldState
import com.example.warshop1.util.RegisterValidation
import com.example.warshop1.util.Resource
import com.example.warshop1.util.validateConfpassword
import com.example.warshop1.util.validateEmail
import com.example.warshop1.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
):ViewModel(){
    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register: Flow<Resource<FirebaseUser>> = _register

    private val _validation = Channel<RegisterFieldState>()
     val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User,password: String, confpassword: String){
        if(checkValidation(user, password, confpassword)){
            viewModelScope.launch {
                _register.emit(Resource.Loading())

                try {
                    val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()
                    _register.value = Resource.Success(authResult.user!!)
                } catch (exception: Exception) {
                    val errorMessage = when (exception) {
                        is FirebaseAuthWeakPasswordException -> "Password is too weak."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email format."
                        is FirebaseAuthUserCollisionException -> "Email is already in use."
                        else -> exception.message ?: "Unknown error occurred."
                    }
                    _register.value = Resource.Error(errorMessage)
                }
            }
        }else{
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email), validatePassword(password)
                , validateConfpassword(password,confpassword)
            )
            viewModelScope.launch {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun checkValidation(user: User, password: String, confpassword: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val confpasswordValidation = validateConfpassword(password, confpassword)
        val shouldRegister = emailValidation is RegisterValidation.Success
                && passwordValidation is RegisterValidation.Success
                && confpasswordValidation is RegisterValidation.Success
        return shouldRegister
    }
}