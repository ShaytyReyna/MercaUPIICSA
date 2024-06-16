package com.example.proyectomovil

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyectomovil.data.model.User
import com.example.proyectomovil.databinding.ActivityMainBinding
import com.example.proyectomovil.databinding.ActivityRegistroUsuarioBinding

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
//para validaciones
import java.util.regex.Pattern

class registro_usuario : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroUsuarioBinding
    lateinit var user: User

    var name : EditText?=null
    var boleta : EditText?=null

    var contraseña : EditText?=null
    var confirmacionContraseña : EditText?=null
    var celular : EditText?=null
    var facebook : EditText?=null
    var instagram : EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el objeto User
        user = User()

        name = findViewById(R.id.textinputname)
        boleta = findViewById(R.id.textinputboleta)

        contraseña = findViewById(R.id.textinputcontraseña)
        confirmacionContraseña = findViewById(R.id.textinputcontraseña2)
        celular = findViewById(R.id.textinputcelular)
        facebook = findViewById(R.id.textinputfacebook)
        instagram = findViewById(R.id.textinputinstagram)


    }
    fun clickBtnRegistro(view: View){
        //val url = "http://192.168.100.129:8080/movil/nuevoVendedor.php"
        val url = "http://192.168.0.8:8080/movil/nuevoVendedor.php"
        val queue = Volley.newRequestQueue(this)

        // Validaciones
        var isValid = true

        if (!validarCampoNoVacio(name?.text.toString(), "Nombre")) {
            isValid = false
        } else if (!validarNombre(name?.text.toString())) {
            Toast.makeText(this, "El nombre debe tener hasta 50 caracteres.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!validarCampoNoVacio(boleta?.text.toString(), "Boleta")) {
            isValid = false
        } else if (!validarBoleta(boleta?.text.toString())) {
            Toast.makeText(this, "La boleta debe tener exactamente 10 dígitos y solo números.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!validarCampoNoVacio(celular?.text.toString(), "Celular")) {
            isValid = false
        } else if (!validarTelefono(celular?.text.toString())) {
            Toast.makeText(this, "El número de teléfono debe tener entre 10 y 15 dígitos y solo números.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!validarCampoNoVacio(facebook?.text.toString(), "Facebook")) {
            isValid = false
        } else if (!validarFacebook(facebook?.text.toString())) {
            Toast.makeText(this, "El nombre de usuario de Facebook debe tener hasta 50 caracteres.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!validarCampoNoVacio(instagram?.text.toString(), "Instagram")) {
            isValid = false
        } else if (!validarInstagram(instagram?.text.toString())) {
            Toast.makeText(this, "El nombre de usuario de Instagram debe tener hasta 30 caracteres.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!validarCampoNoVacio(contraseña?.text.toString(), "Contraseña")) {
            isValid = false
        } else if (!validarContrasena(contraseña?.text.toString())) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres, incluyendo al menos un número, una letra minúscula y una letra mayúscula.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!validarCampoNoVacio(confirmacionContraseña?.text.toString(), "Confirmar Contraseña")) {
            isValid = false
        } else if (!confirmarContrasena(contraseña?.text.toString(), confirmacionContraseña?.text.toString())) {
            Toast.makeText(this, "Las contraseñas deben coincidir.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (isValid) {
            // Asigna los datos al objeto User, lo pasamos al modelo
            user.nombre = name?.text.toString()
            user.boleta = boleta?.text.toString()
            user.celular = celular?.text.toString()
            user.facebook = facebook?.text.toString()
            user.instagram = instagram?.text.toString()
            user.contrasena = contraseña?.text.toString()
            user.confirmacionContra = confirmacionContraseña?.text.toString()


            //Toast.makeText(this, " ${user.boleta} :0", Toast.LENGTH_SHORT).show()

            var resultadoPOST = object : StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    Toast.makeText(this, "a $response", Toast.LENGTH_LONG).show()
                    Log.d("ResultadoPOST", "a $response")
                }, Response.ErrorListener { error ->
                    Toast.makeText(this, "$error :c", Toast.LENGTH_LONG).show()
                    Log.d("ResultadoPOST", "$error :c")
                }) {
                override fun getParams(): MutableMap<String, String>? {
                    val parametros = HashMap<String, String>()
                    parametros.put("idBoleta", boleta?.text.toString())
                    parametros.put("Nombre", name?.text.toString())
                    parametros.put("Telefono", celular?.text.toString())
                    parametros.put("Facebook", facebook?.text.toString())
                    parametros.put("Instagram", instagram?.text.toString())
                    parametros.put("Contra", contraseña?.text.toString())
                    return parametros
                }

            }
            queue.add(resultadoPOST)
            val intent = Intent(this,registroHorario::class.java).apply { putExtra("boleta", boleta?.text.toString()) }
            startActivity(intent)

        }
    }

    // Funciones de validación
    private fun validarCampoNoVacio(campo: String, nombreCampo: String): Boolean {
        return if (campo.isEmpty()) {
            Toast.makeText(this, "El campo $nombreCampo no puede estar vacío.", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun validarNombre(nombre: String): Boolean {
        return nombre.length <= 50
    }

    private fun validarTelefono(tel: String): Boolean {
        return tel.matches("\\d{10,15}".toRegex())
    }

    private fun validarBoleta(boleta: String): Boolean {
        return boleta.length == 10 && boleta.matches("\\d+".toRegex())
    }

    private fun validarFacebook(facebook: String): Boolean {
        return facebook.length <= 50
    }

    private fun validarInstagram(instagram: String): Boolean {
        return instagram.length <= 30
    }

    private fun validarContrasena(contraseña: String): Boolean {
        val regex = """^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)[A-Za-z\d]{8,}$""".toRegex()
        return contraseña.matches(regex)
    }

    fun confirmarContrasena(contraseña: String, confirmacion: String): Boolean {
        return contraseña == confirmacion
    }


}