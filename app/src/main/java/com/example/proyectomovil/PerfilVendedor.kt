package com.example.proyectomovil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.proyectomovil.databinding.ActivityLoginBinding
import com.example.proyectomovil.databinding.ActivityPerfilVendedorBinding

class PerfilVendedor : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilVendedorBinding
    var nombre : EditText?=null
    var telefono : EditText?=null
    var face : EditText?=null
    var insta : EditText?=null
    var lunes : EditText?=null
    var martes : EditText?=null
    var miercoles : EditText?=null
    var jueves : EditText?=null
    var viernes : EditText?=null
    var sabado : EditText?=null
    var domingo : EditText?=null
    var tbProductos: TableLayout?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_vendedor)


        nombre = findViewById(R.id.textinputNombre)
        telefono = findViewById(R.id.textinputTelefono)
        face = findViewById(R.id.textinputfacebook)
        insta = findViewById(R.id.textinputinstagram)
        lunes = findViewById(R.id.textinputlunes)
        martes = findViewById(R.id.textinputmartes)
        miercoles = findViewById(R.id.textinputmiercoles)
        jueves = findViewById(R.id.textinputjueves)
        viernes = findViewById(R.id.textinputviernes)
        sabado = findViewById(R.id.textinputsabado)
        domingo = findViewById(R.id.textinputdomingo)

        tbProductos = findViewById(R.id.tbProductos)
        tbProductos?.removeAllViews()

        for(i in 0 until 5){
            val registro = LayoutInflater.from(this).inflate(R.layout.table_row_producto, null, false)
            val colNombre = registro.findViewById<View>(R.id.colNombre)
        }
        val queue = Volley.newRequestQueue(this)
        val boleta = intent.getStringExtra("boletaI")!!
        val url = "http://192.168.1.70//movil/Consulta.php?idBoleta=${boleta}"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            {response ->
                nombre?.setText(response.getString("Nombre"))
                telefono?.setText(response.getString("Telefono"))
                face?.setText(response.getString("Facebook"))
                insta?.setText(response.getString("Instagram"))
                lunes?.setText(response.getString("lunes"))
                martes?.setText(response.getString("martes"))
                miercoles?.setText(response.getString("miercoles"))
                jueves?.setText(response.getString("jueves"))
                viernes?.setText(response.getString("viernes"))
                sabado?.setText(response.getString("sabado"))
                domingo?.setText(response.getString("domingo"))

            },{error->
                Toast.makeText(this,error.toString(), Toast.LENGTH_LONG).show()
            }

        )
        queue.add(jsonObjectRequest)

    }
}