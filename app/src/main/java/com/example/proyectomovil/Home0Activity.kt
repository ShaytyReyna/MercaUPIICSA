package com.example.proyectomovil

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.proyectomovil.databinding.ActivityHome0Binding
import com.example.proyectomovil.ui.login.LoginActivity
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class Home0Activity : AppCompatActivity() {
    private lateinit var binding: ActivityHome0Binding
    var tbProductos: TableLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome0Binding.inflate(layoutInflater)
        setContentView(binding.root)

        tbProductos = findViewById(R.id.tbProductos)
        tbProductos?.removeAllViews()
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.100.129:8080/Movil/consulta.php?idBoleta=2021607687"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                if (response.length() > 0) {
                    // Iterar a través de los productos y añadir filas a la tabla
                    for (i in 0 until response.length()) {
                        val producto = response.getJSONObject(i)
                        val registro = LayoutInflater.from(this).inflate(R.layout.table_row_vendedor, null, false)
                        val colNombre = registro.findViewById<TextView>(R.id.colNombre)
                        val colPrecio = registro.findViewById<TextView>(R.id.colPrecio)
                        val colIMG = registro.findViewById<ImageView>(R.id.colIMG)
                        val colPV = registro.findViewById<View>(R.id.btnPV)
                        var IdProducto = 0

                        colNombre.text = producto.getString("ProductoNombre")
                        colPrecio.text = producto.getString("ProductoPrecio")
                        IdProducto = producto.getInt("ProductoId")
                        // Cargar imagen usando Glide
                        val imageUrl = producto.getString("IMG")
                        Glide.with(this).load(imageUrl).into(colIMG)

                        colPV.id = IdProducto
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
        binding.butoncito.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            //val intent = Intent(this, NewProducto::class.java)
            startActivity(intent)
            Toast.makeText(this, "Butoncito funciona", Toast.LENGTH_LONG).show()
        }
    }
}