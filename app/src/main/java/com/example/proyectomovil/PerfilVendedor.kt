package com.example.proyectomovil


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.proyectomovil.databinding.ActivityPerfilVendedorBinding
import java.util.*

class PerfilVendedor : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilVendedorBinding
    private lateinit var nombre: EditText
    private lateinit var telefono: EditText
    private lateinit var face: EditText
    private lateinit var insta: EditText
    private lateinit var lunes: EditText
    private lateinit var martes: EditText
    private lateinit var miercoles: EditText
    private lateinit var jueves: EditText
    private lateinit var viernes: EditText
    private lateinit var sabado: EditText
    private lateinit var domingo: EditText
    private lateinit var tbProductos: TableLayout
    private lateinit var boleta: String

    companion object {
        const val AGREGAR_PRODUCTO_REQUEST_CODE = 1
        const val EDITAR_PRODUCTO_REQUEST_CODE = 2
    }

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
        tbProductos.removeAllViews()

        // Obtén el valor de "boletaI" del Intent
        boleta = intent.getStringExtra("boletaI") ?: ""

        cargarDatos()
    }

    fun clickTablaEditar(view: View) {
        val productoId = view.id.toString()

        val intent = Intent(this@PerfilVendedor, EditarProducto::class.java).apply {
            putExtra("BoletaPV", boleta)
            putExtra("ProductoIdPV", productoId)
        }
        startActivityForResult(intent, EDITAR_PRODUCTO_REQUEST_CODE)
    }

    fun clickTablaBorrar(view: View) {
        val urlBorrar = "http://192.168.100.129:8080/movil/borrarProducto.php?idBoleta=${boleta}&idProducto=${view.id.toString()}"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, urlBorrar, null,
            { response ->
                if (response.length() > 0) {
                    cargarDatos()
                } else {
                    Toast.makeText(this, "Ocurrió un error al borrar el producto", Toast.LENGTH_LONG).show()
                }
            }, { error ->
                Toast.makeText(this, "Error en la solicitud: ${error.message}", Toast.LENGTH_LONG).show()
                Log.e("PerfilVendedor", "Error en la solicitud: ${error.message}")
            }
        )

        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }

    fun clickAgregarP(view: View) {
        val intent = Intent(this@PerfilVendedor, AgregarProducto::class.java).apply {
            putExtra("boleta", boleta)
        }
        startActivityForResult(intent, AGREGAR_PRODUCTO_REQUEST_CODE)
    }

    fun clickRegresar(view: View) {
        val intent = Intent(this@PerfilVendedor, Home0Activity::class.java)
        startActivity(intent)
    }

    fun cargarDatos() {
        val url = "http://192.168.100.129:8080/Movil/consulta.php?idBoleta=${boleta}"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                if (response.length() > 0) {
                    val jsonObject = response.getJSONObject(0)
                    nombre.setText(jsonObject.getString("Nombre"))
                    telefono.setText(jsonObject.getString("Telefono"))
                    face.setText(jsonObject.getString("Facebook"))
                    insta.setText(jsonObject.getString("Instagram"))
                    lunes.setText(jsonObject.getString("lunes"))
                    martes.setText(jsonObject.getString("martes"))
                    miercoles.setText(jsonObject.getString("miercoles"))
                    jueves.setText(jsonObject.getString("jueves"))
                    viernes.setText(jsonObject.getString("viernes"))
                    sabado.setText(jsonObject.getString("sabado"))
                    domingo.setText(jsonObject.getString("domingo"))

                    tbProductos.removeAllViews()

                    for (i in 0 until response.length()) {
                        val producto = response.getJSONObject(i)
                        val registro = LayoutInflater.from(this).inflate(R.layout.table_row_producto, null, false)
                        val colNombre = registro.findViewById<TextView>(R.id.colNombre)
                        val colPrecio = registro.findViewById<TextView>(R.id.colPrecio)
                        val colIMG = registro.findViewById<ImageView>(R.id.colIMG)
                        val colEditar = registro.findViewById<View>(R.id.colEditar)
                        val colBorrar = registro.findViewById<View>(R.id.colBorrar)
                        val idProducto = producto.getInt("ProductoId")

                        colNombre.text = producto.getString("ProductoNombre")
                        colPrecio.text = producto.getString("ProductoPrecio")

                        // Cargar imagen usando Glide
                        val imageUrl = producto.getString("IMG")
                        Glide.with(this).load(imageUrl).into(colIMG)

                        colEditar.id = idProducto
                        colEditar.setOnClickListener { clickTablaEditar(it) }

                        colBorrar.id = idProducto
                        colBorrar.setOnClickListener { clickTablaBorrar(it) }

                        tbProductos.addView(registro)
                    }
                } else {
                    Toast.makeText(this, "No hay registros", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error en la solicitud: ${error.message}", Toast.LENGTH_LONG).show()
                Log.e("PerfilVendedor", "Error en la solicitud: ${error.message}")
            }
        )

        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AGREGAR_PRODUCTO_REQUEST_CODE || requestCode == EDITAR_PRODUCTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                cargarDatos()
            }
        }
    }
}
