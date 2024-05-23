package com.example.proyectomovil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.proyectomovil.data.model.User
import com.example.proyectomovil.DatabaseHelper

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class registro_usuario : AppCompatActivity() {
    private lateinit var user: User
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        // Inicializa el objeto User
        user = User()

        // Inicializa DatabaseHelper
        dbHelper = DatabaseHelper(this)

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View){
                // Aquí obtienes los datos del formulario
                val name = findViewById<TextInputEditText>(R.id.name).text.toString()
                val boleta = findViewById<TextInputEditText>(R.id.textinputboleta).text.toString()
                val lunes = findViewById<TextInputEditText>(R.id.textinputlunes).text.toString()
                val martes = findViewById<TextInputEditText>(R.id.textinputmartes).text.toString()
                val miercoles = findViewById<TextInputEditText>(R.id.textinputmiercoles).text.toString()
                val jueves = findViewById<TextInputEditText>(R.id.textinputjueves).text.toString()
                val viernes = findViewById<TextInputEditText>(R.id.textinputviernes).text.toString()
                val contraseña = findViewById<TextInputEditText>(R.id.textinputcontraseña).text.toString()
                val confirmacionContraseña = findViewById<TextInputEditText>(R.id.textinputcontraseña2).text.toString()
                val celular = findViewById<TextInputEditText>(R.id.textinputcelular).text.toString()
                val facebook = findViewById<TextInputEditText>(R.id.textinputfacebook).text.toString()
                val instagram = findViewById<TextInputEditText>(R.id.textinputinstagram).text.toString()

                // Asigna los datos al objeto User
                user.nombre = name
                user.boleta = boleta
                user.celular = celular
                user.facebook = facebook
                user.instagram = instagram
                user.contrasena = contraseña
                user.confirmacionContra = confirmacionContraseña
                user.lunes = lunes
                user.martes = martes
                user.miercoles = miercoles
                user.jueves = jueves
                user.viernes = viernes

                // Inserta el usuario en la base de datos
                val idInsertado = dbHelper.insertarUsuario(user)
                if (idInsertado!= -1L) {
                    // Mostrar mensaje de éxito
                    Toast.makeText(this@registro_usuario, "Usuario registrado exitosamente", Toast.LENGTH_LONG).show()
                } else {
                    // Manejar error
                    Toast.makeText(this@registro_usuario, "Error al registrar el usuario", Toast.LENGTH_LONG).show()
                }
            }
        })
/*
        val name = findViewById<TextInputEditText>(R.id.name).text.toString()
        val boleta = findViewById<TextInputEditText>(R.id.textinputboleta).text.toString()
        val lunes = findViewById<TextInputEditText>(R.id.textinputlunes).text.toString()
        val martes = findViewById<TextInputEditText>(R.id.textinputmartes).text.toString()
        val miercoles = findViewById<TextInputEditText>(R.id.textinputmiercoles).text.toString()
        val jueves = findViewById<TextInputEditText>(R.id.textinputjueves).text.toString()
        val viernes = findViewById<TextInputEditText>(R.id.textinputviernes).text.toString()
        val contraseña = findViewById<TextInputEditText>(R.id.textinputcontraseña).text.toString()
        val confirmacionContraseña = findViewById<TextInputEditText>(R.id.textinputcontraseña2).text.toString()
        val celular = findViewById<TextInputEditText>(R.id.textinputcelular).text.toString()
        val facebook = findViewById<TextInputEditText>(R.id.textinputfacebook).text.toString()
        val instagram = findViewById<TextInputEditText>(R.id.textinputinstagram).text.toString()

        user.nombre = name
        user.boleta = boleta
        user.celular = celular
        user.facebook = facebook
        user.instagram = instagram

        user.contrasena =contraseña
        user.confirmacionContra = confirmacionContraseña

        user.lunes = lunes
        user.martes =martes
        user.miercoles = miercoles
        user.jueves = jueves
        user.viernes = viernes
*/

    }
}