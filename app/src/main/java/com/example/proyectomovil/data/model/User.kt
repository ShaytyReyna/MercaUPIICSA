package com.example.proyectomovil.data.model

data class User(
    var boleta: String = "",
    var nombre: String = "",
    var celular: String = "",
    var facebook: String = "",
    var instagram: String = "",

    var contrasena: String = "",
    var confirmacionContra: String = "",

    var lunes: String = "",
    var martes: String = "",
    var miercoles: String = "",
    var jueves: String = "",
    var viernes: String = "",
    var sabado: String = "",
    var domingo: String = "",

    var disponible: Int = 0
)
