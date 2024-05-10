package com.example.proyectomovil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout

class registro_usuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        val name = findViewById<TextInputLayout>(R.id.name)
        val boleta = findViewById<TextInputLayout>(R.id.textinputboleta)
        val lunes = findViewById<TextInputLayout>(R.id.textinputlunes)
        val martes = findViewById<TextInputLayout>(R.id.textinputmartes)
        val miercoles = findViewById<TextInputLayout>(R.id.textinputmiercoles)
        val jueves = findViewById<TextInputLayout>(R.id.textinputjueves)
        val viernes = findViewById<TextInputLayout>(R.id.textinputviernes)
        val contraseña = findViewById<TextInputLayout>(R.id.textinputcontraseña)
        val confirmacionContraseña = findViewById<TextInputLayout>(R.id.textinputcontraseña2)
        val celular = findViewById<TextInputLayout>(R.id.textinputcelular)
        val facebook = findViewById<TextInputLayout>(R.id.textinputfacebook)
        val instagram = findViewById<TextInputLayout>(R.id.textinputinstagram)
    }
}