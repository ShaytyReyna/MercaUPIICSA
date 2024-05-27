package com.example.proyectomovil

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
//para validaciones
import java.util.regex.Pattern

class registro_usuario : AppCompatActivity() {
    private lateinit var user: User

    var name : EditText?=null
    var boleta : EditText?=null
    var lunes : EditText?=null
    var martes : EditText?=null
    var miercoles : EditText?=null
    var jueves : EditText?=null
    var viernes : EditText?=null
    var contraseña : EditText?=null
    var confirmacionContraseña : EditText?=null
    var celular : EditText?=null
    var facebook : EditText?=null
    var instagram : EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        name = findViewById(R.id.textinputname)
        boleta = findViewById(R.id.textinputboleta)
        lunes = findViewById(R.id.textinputlunes)
        martes = findViewById(R.id.textinputmartes)
        miercoles = findViewById(R.id.textinputmiercoles)
        jueves = findViewById(R.id.textinputjueves)
        viernes = findViewById(R.id.textinputviernes)
        contraseña = findViewById(R.id.textinputcontraseña)
        confirmacionContraseña = findViewById(R.id.textinputcontraseña2)
        celular = findViewById(R.id.textinputcelular)
        facebook = findViewById(R.id.textinputfacebook)
        instagram = findViewById(R.id.textinputinstagram)

    }
    fun clickBtnRegistro(view: View){
        val url = "http://192.168.1.70/movil/NuevoVendedor.php"
        val queue = Volley.newRequestQueue(this)
        var resultadoPOST = object : StringRequest(Request.Method.POST,url,
            Response.Listener<String>{response ->
                Toast.makeText(this, "a $response", Toast.LENGTH_LONG).show()
            },Response.ErrorListener { error ->   Toast.makeText(this, "$error :c", Toast.LENGTH_LONG).show()}){
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                parametros.put("idBoleta",boleta?.text.toString())
                parametros.put("Nombre",name?.text.toString())
                parametros.put("Telefono",celular?.text.toString())
                parametros.put("Facebook",facebook?.text.toString())
                parametros.put("Instagram",instagram?.text.toString())
                parametros.put("Contra",contraseña?.text.toString())
                return parametros
            }

        }
        queue.add(resultadoPOST)
    }

}