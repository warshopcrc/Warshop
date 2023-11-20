package com.example.warshop1.data

data class User (
    val firstname: String,
    val lastName: String,
    val email: String,
    val password: String,
    var imagepath: String = ""
) {
    constructor() : this("", "", "", "")
}
