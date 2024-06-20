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
import org.json.JSONException
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

        if (boleta == null || productoid == null) {
            Toast.makeText(this, "Datos insuficientes para editar el producto", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarDatosProducto()

        btnBuscar.setOnClickListener { showFileChooser() }

        btnSubir = findViewById(R.id.buttonagregar)
        btnSubir.setOnClickListener {
            uploadImage()
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

    private fun cargarDatosProducto() {
        val url = "http://192.168.100.129:8080/movil/EditarP.php?idVendedor=${boleta}&idProducto=${productoid}"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    if (response.length() > 0) {
                        val jsonObject = response.getJSONObject(0)

                        et.setText(jsonObject.getString("ProductoNombre"))
                        et1.setText(jsonObject.getString("ProductoPrecio"))

                        val accesorios = jsonObject.getInt("Accesorios")
                        val comida = jsonObject.getInt("Comida")
                        val electronica = jsonObject.getInt("Electronica")
                        val joyeria = jsonObject.getInt("Joyeria")

                        checkBoxAccesorios.isChecked = accesorios == 1
                        checkBoxComida.isChecked = comida == 1
                        checkBoxElectronica.isChecked = electronica == 1
                        checkBoxJoyeria.isChecked = joyeria == 1
                    } else {
                        Toast.makeText(this, "No se encontraron datos del producto", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al procesar los datos del producto", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }

    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imagePickerLauncher.launch(Intent.createChooser(intent, "Selecciona una imagen"))
    }

    private fun uploadImage() {
        val uploadUrl = "http://192.168.100.129:8080/movil/ActualizarP.php"
        bitmap?.let {
            progressBar.visibility = ProgressBar.VISIBLE
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.POST, uploadUrl,
                Response.Listener<String?> { response ->
                    progressBar.visibility = ProgressBar.GONE
                    Snackbar.make(findViewById(android.R.id.content), response ?: "Error", Snackbar.LENGTH_LONG).show()
                    // Informar a la actividad anterior (PerfilVendedor) que se han realizado cambios
                    val intent = Intent()
                    setResult(RESULT_OK, intent)
                    finish() // Cerrar la actividad actual
                },
                Response.ErrorListener { error ->
                    progressBar.visibility = ProgressBar.GONE
                    Snackbar.make(findViewById(android.R.id.content), error.message ?: "Error", Snackbar.LENGTH_LONG).show()
                    Log.e("NewProducto", "Error en la solicitud HTTP: ${error.message}")
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val imagen = getStringImagen(it)
                    val nombre = et.text.toString().trim()
                    val precio = et1.text.toString().trim()

                    val params: MutableMap<String, String> = HashMap()
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

            Volley.newRequestQueue(this).add(stringRequest)
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