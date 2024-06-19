package com.example.proyectomovil

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.io.IOException
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.android.volley.toolbox.JsonArrayRequest
import com.bumptech.glide.Glide
import com.example.proyectomovil.data.model.User
import com.example.proyectomovil.ui.login.LoginActivity
import java.util.*
class EditarProducto : AppCompatActivity() {
    lateinit var user: User

    private lateinit var btnBuscar: Button
    private lateinit var btnSubir: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var iv: ImageView
    private lateinit var et: TextInputEditText
    private lateinit var et1: TextInputEditText

    private lateinit var checkBoxAccesorios: CheckBox
    private lateinit var checkBoxComida: CheckBox
    private lateinit var checkBoxElectronica: CheckBox
    private lateinit var checkBoxJoyeria: CheckBox

    private var bitmap: Bitmap? = null
    //private val uploadUrl = "http://10.109.75.143:8080/movil/nuevoProducto.php"


    private val keyImage = "foto"
    private val keyNombre = "nombre"
    private val keyPrecio = "precio"
    private val keyIdVendedor = "idVendedor"
    private val keyIdProducto = "idProducto"

    private val keyAccesorios = "Accesorios"
    private val keyComida = "Comida"
    private val keyElectronica = "Electronica"
    private val keyJoyeria = "Joyeria"

    private var boleta: String? = null
    private var productoid: String? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        user = User()


        btnBuscar = findViewById(R.id.btnBuscar)
        progressBar = findViewById(R.id.progressBar)
        et = findViewById(R.id.textinputnameP)
        et1 = findViewById(R.id.textinputPrecioP)
        iv = findViewById(R.id.imageView)

        checkBoxAccesorios = findViewById(R.id.checkBoxAccesorios)
        checkBoxComida = findViewById(R.id.checkBoxComida)
        checkBoxElectronica = findViewById(R.id.checkBoxElectronica)
        checkBoxJoyeria = findViewById(R.id.checkBoxJoyeria)

        boleta = intent.getStringExtra("BoletaPV")
        productoid = intent.getStringExtra("ProductoIdPV")

        val queue = Volley.newRequestQueue(this)

        if (boleta == null || productoid == null) {
            Toast.makeText(this, "Datos insuficientes para editar el producto", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        Toast.makeText(this, "Boleta: $boleta, ProductoID: $productoid", Toast.LENGTH_LONG).show()
        val url = "http://192.168.100.129:8080/movil/EditarP.php?idVendedor=${boleta}&idProducto=${productoid}"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                if (response.length() > 0) {
                    val jsonObject = response.getJSONObject(0)

                    et.setText(jsonObject.getString("ProductoNombre"))
                    et1.setText(jsonObject.getString("ProductoPrecio"))
                    val a = jsonObject.getInt("Accesorios")
                    val c = jsonObject.getInt("Comida")
                    val e = jsonObject.getInt("Electronica")
                    val j = jsonObject.getInt("Joyeria")

                    if (e == 1)
                        checkBoxElectronica!!.isChecked = true
                    if(a == 1)
                        checkBoxAccesorios!!.isChecked = true
                    if(c == 1)
                        checkBoxComida!!.isChecked = true
                    if (j == 1)
                        checkBoxJoyeria!!.isChecked= true



                } else {
                    Toast.makeText(this, "No hay registros", Toast.LENGTH_LONG).show()
                }
            }, { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                Log.e("Editar Producto", error.toString())
            }
        )
        queue.add(jsonArrayRequest)

        btnBuscar.setOnClickListener { showFileChooser() }

        btnSubir = findViewById(R.id.buttonagregar)
        btnSubir.setOnClickListener {
            uploadImage()
            val intent = Intent(this@EditarProducto, PerfilVendedor::class.java).apply { putExtra("boletaI", boleta) }
            startActivity(intent)
        }

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null && result.data!!.data != null) {
                val filePath = result.data!!.data
                try {
                    bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(this.contentResolver, filePath!!)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                    }
                    iv.setImageBitmap(bitmap!!)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imagePickerLauncher.launch(Intent.createChooser(intent, "Selecciona imagen"))
    }

    private fun uploadImage() {
        //****//
        val uploadUrl = "http://192.168.100.129:8080/movil/ActualizarP.php"
        bitmap?.let {
            progressBar.visibility = ProgressBar.VISIBLE
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.POST, uploadUrl,
                Response.Listener<String?> { response ->
                    progressBar.visibility = ProgressBar.GONE
                    Snackbar.make(findViewById(android.R.id.content), response ?: "Error", Snackbar.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    progressBar.visibility = ProgressBar.GONE
                    Snackbar.make(findViewById(android.R.id.content), error.message ?: "Error", Snackbar.LENGTH_LONG).show()
                    Log.e("NewProducto", "Error en la solicitud HTTP: ${error.message}")
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val imagen: String = getStringImagen(it)
                    val nombre = et?.text?.toString()?.trim() ?: ""
                    val precio = et1?.text?.toString()?.trim() ?: ""

                    if (boleta.isNullOrBlank() || productoid.isNullOrBlank()) {
                        Log.e("NewProducto", "El campo Boleta o ProductoID está vacío.")
                        Snackbar.make(findViewById(android.R.id.content), "Los campos Boleta y ProductoID son requeridos.", Snackbar.LENGTH_LONG).show()
                        return emptyMap()
                    }

                    val params: MutableMap<String, String> = Hashtable()
                    params[keyIdProducto] = productoid!!
                    params[keyImage] = imagen
                    params[keyNombre] = nombre
                    params[keyPrecio] = precio
                    params[keyIdVendedor] = boleta!!

                    params[keyAccesorios] = if (checkBoxAccesorios.isChecked) "1" else "0"
                    params[keyComida] = if (checkBoxComida.isChecked) "1" else "0"
                    params[keyElectronica] = if (checkBoxElectronica.isChecked) "1" else "0"
                    params[keyJoyeria] = if (checkBoxJoyeria.isChecked) "1" else "0"

                    return params

                }
            }

            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        } ?: run {
            Snackbar.make(findViewById(android.R.id.content), "Selecciona una imagen primero", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getStringImagen(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}
