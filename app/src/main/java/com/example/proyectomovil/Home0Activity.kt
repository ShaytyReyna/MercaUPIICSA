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
        val url = "http://192.168.100.129:8080/Movil/consultaHome.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                if (response.length() > 0) {
                    for (i in 0 until response.length()) {
                        val producto = response.getJSONObject(i)
                        val registro = LayoutInflater.from(this).inflate(R.layout.table_row_vendedor, null, false)
                        val colNombre = registro.findViewById<TextView>(R.id.colNombre)
                        val colPrecio = registro.findViewById<TextView>(R.id.colPrecio)
                        val colIMG = registro.findViewById<ImageView>(R.id.colIMG)
                        val colPV = registro.findViewById<Button>(R.id.btnPV)
                        val idVendedor = producto.getInt("idVendedor")

                        colNombre.text = producto.getString("NOMBRE")
                        colPrecio.text = "$"+producto.getString("PRECIO")

                        val imageUrl = producto.getString("IMG")
                        Glide.with(this).load(imageUrl).into(colIMG)

                        colPV.tag = idVendedor
                        colPV.setOnClickListener { view ->
                            val vendedorId = view.tag as Int
                            val intent = Intent(this, PerfilVU::class.java)
                            intent.putExtra("VendedorId", vendedorId)
                            startActivity(intent)
                        }

                        tbProductos?.addView(registro)
                    }
                } else {
                    Toast.makeText(this, "No hay registros", Toast.LENGTH_LONG).show()
                }
            }, { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                Log.e("Home0Activity", error.toString())
            }
        )

        queue.add(jsonArrayRequest)
        binding.butoncito.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
