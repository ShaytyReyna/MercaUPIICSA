package com.example.proyectomovil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyectomovil.data.model.User
import com.example.proyectomovil.databinding.ActivityRegistroHorarioBinding

class registroHorario : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroHorarioBinding
    lateinit var user: User


    var lunes : EditText?=null
    var martes : EditText?=null
    var miercoles : EditText?=null
    var jueves : EditText?=null
    var viernes : EditText?=null
    var sabado : EditText?=null
    var domingo : EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroHorarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el objeto User
        user = User()

        lunes = findViewById(R.id.textinputlunes)
        martes = findViewById(R.id.textinputmartes)
        miercoles = findViewById(R.id.textinputmiercoles)
        jueves = findViewById(R.id.textinputjueves)
        viernes = findViewById(R.id.textinputviernes)
        sabado = findViewById(R.id.textinputsabado)
        domingo = findViewById(R.id.textinputdomingo)
    }
    fun clickBtnRegistroH(view: View){
        //val url = "http://192.168.100.129:8080/movil/nuevoHorario.php"
        val url = "http://192.168.0.8:8080/movil/nuevoHorario.php"
        val queue = Volley.newRequestQueue(this)

        // Validaciones
        var isValid = true

        if (!validarHorario(lunes?.text.toString()) ||!validarHorario(martes?.text.toString()) ||!validarHorario(miercoles?.text.toString()) ||!validarHorario(jueves?.text.toString()) ||!validarHorario(viernes?.text.toString()) ||!validarHorario(sabado?.text.toString())||!validarHorario(domingo?.text.toString())) {
            Toast.makeText(this, "Los horarios deben tener hasta 15 caracteres.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (isValid){
            // Asigna los datos al objeto User, lo pasamos al modelo
            user.lunes = lunes?.text.toString()
            user.martes = martes?.text.toString()
            user.miercoles = miercoles?.text.toString()
            user.jueves = jueves?.text.toString()
            user.viernes = viernes?.text.toString()
            user.sabado = sabado?.text.toString()
            user.domingo = domingo?.text.toString()

            val boleta = intent.getStringExtra("boleta")!!

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
                    parametros.put("idVendedor", boleta)
                    parametros.put("lunes", lunes?.text.toString())
                    parametros.put("martes", martes?.text.toString())
                    parametros.put("miercoles", miercoles?.text.toString())
                    parametros.put("jueves", jueves?.text.toString())
                    parametros.put("viernes", viernes?.text.toString())
                    parametros.put("sabado", sabado?.text.toString())
                    parametros.put("domingo", domingo?.text.toString())
                    return parametros
                }

            }
            queue.add(resultadoPOST)
            val intent = Intent(this, NewProducto::class.java).apply { putExtra("boleta", boleta)}
            startActivity(intent)
        }
    }
    private fun validarHorario(horario: String): Boolean {
        return horario.length <= 15
    }
}