package com.example.warshop1.util

import android.util.Patterns
import java.util.regex.Pattern

fun validateEmail(email: String): RegisterValidation{
    if (email.isEmpty())
        return RegisterValidation.Failed("Email Tidak Boleh Kosong")
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Format Email Salah")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation{
    if (password.isEmpty())
        return RegisterValidation.Failed("Password Tidak Boleh Kosong")
    if (password.length < 6 )
        return RegisterValidation.Failed("Password Tidak Boleh Kurang Dari 6 Karakter")

    return RegisterValidation.Success
}
fun validateConfpassword(password: String, confpassword: String): RegisterValidation{
    if (confpassword.isEmpty())
        return RegisterValidation.Failed("Konfirmasi Password Harus Diisi!!")
    if (password != confpassword)
        return RegisterValidation.Failed("Password Tidak Sama")

    return RegisterValidation.Success
}