package com.example.proyectomovil

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
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
import java.util.*

class AgregarProducto : AppCompatActivity() {

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
    private val uploadUrl = "http://192.168.100.129:8080/movil/nuevoProducto.php"
    private val keyImage = "foto"
    private val keyNombre = "nombre"
    private val keyPrecio = "precio"
    private val keyIdVendedor = "idVendedor"
    private val keyAccesorios = "Accesorios"
    private val keyComida = "Comida"
    private val keyElectronica = "Electronica"
    private val keyJoyeria = "Joyeria"
    private var boleta: String? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)

        btnBuscar = findViewById(R.id.btnBuscar)
        progressBar = findViewById(R.id.progressBar)
        et = findViewById(R.id.textinputnameP)
        et1 = findViewById(R.id.textinputPrecioP)
        iv = findViewById(R.id.imageView)

        checkBoxAccesorios = findViewById(R.id.checkBoxAccesorios)
        checkBoxComida = findViewById(R.id.checkBoxComida)
        checkBoxElectronica = findViewById(R.id.checkBoxElectronica)
        checkBoxJoyeria = findViewById(R.id.checkBoxJoyeria)

        boleta = intent.getStringExtra("boleta")

        btnBuscar.setOnClickListener { showFileChooser() }

        btnSubir = findViewById(R.id.buttonagregar)
        btnSubir.setOnClickListener {
            uploadImage()
        }

        // Inicialización del ActivityResultLauncher para el selector de imágenes
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                try {
                    bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    } else {
                        val source = imageUri?.let { ImageDecoder.createSource(contentResolver, it) }
                        source?.let { ImageDecoder.decodeBitmap(it) }
                    }
                    iv.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        imagePickerLauncher.launch(Intent.createChooser(intent, "Selecciona una imagen"))
    }

    private fun uploadImage() {
        progressBar.visibility = ProgressBar.VISIBLE

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, uploadUrl,
            Response.Listener<String?> { response ->
                progressBar.visibility = ProgressBar.GONE
                Snackbar.make(findViewById(android.R.id.content), response ?: "Error", Snackbar.LENGTH_LONG).show()
                setResult(Activity.RESULT_OK)
                finish()
            },
            Response.ErrorListener { error ->
                progressBar.visibility = ProgressBar.GONE
                Snackbar.make(findViewById(android.R.id.content), error.message ?: "Error", Snackbar.LENGTH_LONG).show()
                Log.e("AgregarProducto", "Error en la solicitud HTTP: ${error.message}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params[keyImage] = bitmapToString(bitmap)
                params[keyNombre] = et.text.toString().trim()
                params[keyPrecio] = et1.text.toString().trim()
                params[keyIdVendedor] = boleta ?: ""
                params[keyAccesorios] = if (checkBoxAccesorios.isChecked) "1" else "0"
                params[keyComida] = if (checkBoxComida.isChecked) "1" else "0"
                params[keyElectronica] = if (checkBoxElectronica.isChecked) "1" else "0"
                params[keyJoyeria] = if (checkBoxJoyeria.isChecked) "1" else "0"
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun bitmapToString(bitmap: Bitmap?): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imgBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imgBytes, Base64.DEFAULT)
    }
}
