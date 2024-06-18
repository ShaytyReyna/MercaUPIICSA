package com.example.proyectomovil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.proyectomovil.databinding.ActivityPerfilVendedorBinding

class PerfilVendedor : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilVendedorBinding
    var nombre: EditText? = null
    var telefono: EditText? = null
    var face: EditText? = null
    var insta: EditText? = null
    var lunes: EditText? = null
    var martes: EditText? = null
    var miercoles: EditText? = null
    var jueves: EditText? = null
    var viernes: EditText? = null
    var sabado: EditText? = null
    var domingo: EditText? = null
    var tbProductos: TableLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        // Obtén el valor de "boletaI" del Intent
        val boleta = intent.getStringExtra("boletaI") ?: ""

        val queue = Volley.newRequestQueue(this)

        val url = "http://192.168.100.129:8080/Movil/consulta.php?idBoleta=${boleta}"
        //val url = "http://10.109.75.143:8080/Movil/consulta.php?idBoleta=${boleta}"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                if (response.length() > 0) {
                    val jsonObject = response.getJSONObject(0)
                    nombre?.setText(jsonObject.getString("Nombre"))
                    telefono?.setText(jsonObject.getString("Telefono"))
                    face?.setText(jsonObject.getString("Facebook"))
                    insta?.setText(jsonObject.getString("Instagram"))
                    lunes?.setText(jsonObject.getString("lunes"))
                    martes?.setText(jsonObject.getString("martes"))
                    miercoles?.setText(jsonObject.getString("miercoles"))
                    jueves?.setText(jsonObject.getString("jueves"))
                    viernes?.setText(jsonObject.getString("viernes"))
                    sabado?.setText(jsonObject.getString("sabado"))
                    domingo?.setText(jsonObject.getString("domingo"))

                    // Iterar a través de los productos y añadir filas a la tabla
                    for (i in 0 until response.length()) {
                        val producto = response.getJSONObject(i)
                        val registro = LayoutInflater.from(this).inflate(R.layout.table_row_producto, null, false)
                        val colNombre = registro.findViewById<TextView>(R.id.colNombre)
                        val colPrecio = registro.findViewById<TextView>(R.id.colPrecio)
                        val colIMG = registro.findViewById<ImageView>(R.id.colIMG)
                        val colEditar = registro.findViewById<View>(R.id.colEditar)
                        val colBorrar = registro.findViewById<View>(R.id.colBorrar)
                        var IdProducto = 0

                        colNombre.text = producto.getString("ProductoNombre")
                        colPrecio.text = producto.getString("ProductoPrecio")
                        IdProducto = producto.getInt("ProductoId")



                        // Cargar imagen usando Glide
                        val imageUrl = producto.getString("IMG")
                        Glide.with(this).load(imageUrl).into(colIMG)

                        colEditar.id = IdProducto
                        colBorrar.id = IdProducto
                        tbProductos?.addView(registro)
                    }
                } else {
                    Toast.makeText(this, "No hay registros", Toast.LENGTH_LONG).show()
                }
            }, { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                Log.e("PerfilVendedor", error.toString())
            }
        )
        queue.add(jsonArrayRequest)
    }

    fun clickTablaEditar(view: View) {
        val boleta = intent.getStringExtra("boletaI") ?: ""
        val productoId = view.id.toString()

        val intent = Intent(this@PerfilVendedor, EditarProducto::class.java)
        intent.putExtra("BoletaPV", boleta)
        intent.putExtra("ProductoIdPV", productoId)
        startActivity(intent)
    }


    fun clickTablaBorrar(view: View) {
        val boleta = intent.getStringExtra("boletaI") ?: ""
        Toast.makeText(this, view.id.toString(), Toast.LENGTH_LONG).show()
        Toast.makeText(this, boleta, Toast.LENGTH_LONG).show()

        val producto = intent.getStringExtra(view.id.toString())
    }

    fun clickAgregarP(view: View){
        // Redirigir a Agregar producto
        val boleta = intent.getStringExtra("boletaI") ?: ""
        val intent = Intent(this@PerfilVendedor, AgregarProducto ::class.java).apply { putExtra("boleta", boleta)}
        startActivity(intent)
    }
    fun clickRegresar(view: View){
        // Redirigir a Home
        val intent = Intent(this@PerfilVendedor, Home0Activity ::class.java)
        startActivity(intent)
    }
}
