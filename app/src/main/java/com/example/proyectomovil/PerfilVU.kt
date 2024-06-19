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
import com.example.proyectomovil.databinding.ActivityPerfilVuBinding
class PerfilVU : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilVuBinding
    var nombre: TextView? = null
    var telefono: TextView? = null
    var face: TextView? = null
    var insta: TextView? = null
    var lunes: TextView?= null
    var martes:TextView? = null
    var miercoles: TextView? = null
    var jueves: TextView?= null
    var viernes: TextView? = null
    var sabado: TextView? = null
    var domingo: TextView?= null
    var tbProductos: TableLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_vu)

        nombre = findViewById(R.id.user)
        telefono = findViewById(R.id.telvendedor)
        face = findViewById(R.id.facevendedor)
        insta = findViewById(R.id.Insvendedor)
        lunes = findViewById(R.id.luneshi)
        martes = findViewById(R.id.marteshi)
        miercoles = findViewById(R.id.miercoleshi)
        jueves = findViewById(R.id.jueveshi)
        viernes = findViewById(R.id.vierneshi)
        sabado = findViewById(R.id.sabadohi)
        domingo = findViewById(R.id.domingohi)

        tbProductos = findViewById(R.id.tbProductos)
        tbProductos?.removeAllViews()

        val idVendedor = intent.getIntExtra("VendedorId", 0)
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.100.129:8080/Movil/consulta.php?idBoleta=${idVendedor}"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                if (response.length() > 0) {
                    val jsonObject = response.getJSONObject(0)
                    nombre?.text = jsonObject.getString("Nombre")
                    telefono?.text = jsonObject.getString("Telefono")
                    face?.text = jsonObject.getString("Facebook")
                    insta?.text = jsonObject.getString("Instagram")
                    lunes?.text =jsonObject.getString("lunes")
                    martes?.text =jsonObject.getString("martes")
                    miercoles?.text =jsonObject.getString("miercoles")
                    jueves?.text =jsonObject.getString("jueves")
                    viernes?.text =jsonObject.getString("viernes")
                    sabado?.text =jsonObject.getString("sabado")
                    domingo?.text =jsonObject.getString("domingo")

                    for (i in 0 until response.length()) {
                        val producto = response.getJSONObject(i)
                        val registro = LayoutInflater.from(this).inflate(R.layout.table_row_producto2, null, false)
                        val colNombre = registro.findViewById<TextView>(R.id.colNombre)
                        val colPrecio = registro.findViewById<TextView>(R.id.colPrecio)
                        val colIMG = registro.findViewById<ImageView>(R.id.colIMG)

                        colNombre.text = producto.getString("ProductoNombre")
                        colPrecio.text = "$ "+producto.getString("ProductoPrecio")

                        val imageUrl = producto.getString("IMG")
                        Glide.with(this).load(imageUrl).into(colIMG)

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

    fun clickRegresar(view: View){
        val intent = Intent(this@PerfilVU, Home0Activity::class.java)
        startActivity(intent)
    }
}
