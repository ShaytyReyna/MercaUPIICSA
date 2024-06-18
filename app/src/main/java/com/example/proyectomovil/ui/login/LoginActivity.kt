package com.example.proyectomovil.ui.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.proyectomovil.Home0Activity
import com.example.proyectomovil.PerfilVendedor
import com.example.proyectomovil.R
import com.example.proyectomovil.data.model.User
import com.example.proyectomovil.databinding.ActivityLoginBinding
import com.example.proyectomovil.registro_usuario
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    private var boleta: EditText?=null
    private var contra: EditText?=null

    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        boleta = findViewById<TextInputEditText>(R.id.TIboleta)
        contra = findViewById<TextInputEditText>(R.id.TIcontra)
        // Inicializa el objeto User
        user = User()


        // loggedInUser = LoggedInUser(boleta?.text.toString())
        /*
                binding.btnLogIn?.setOnClickListener{
                    Log.d("LoginActivity", "Botón registro presionado")

                    //Validaciones
                    var isValid = true

                    if(!validarCampoNoVacio(boleta,  "Boleta")){
                        isValid=false
                    }else if (!validarBoleta(boleta)) {
                        Toast.makeText(this, "La boleta debe tener exactamente 10 dígitos y solo números.", Toast.LENGTH_SHORT).show()
                        isValid = false
                    }

                    if (!validarCampoNoVacio(contra, "Contraseña")){
                        isValid=false
                    }else if (!validarContrasena(contra)) {
                        Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres, incluyendo al menos un número, una letra minúscula y una letra mayúscula.", Toast.LENGTH_SHORT).show()
                        isValid = false
                    }

                    if(isValid){
                        //Asigna los datos al objeto User
                        user.boleta = boleta
                        user.contrasena =contra

                        val intent = Intent(this, Home0Activity::class.java)
                        startActivity(intent)
                    }

                }

               */


    }


    fun clicLogIn(view: View){
        val queue = Volley.newRequestQueue(this)

        //Toast.makeText(this, "${boleta}" ,Toast.LENGTH_SHORT ).show()
        var isValid = true

        if(!validarCampoNoVacio(boleta?.text.toString(),  "Boleta")){
            isValid=false
        }else if (!validarBoleta(boleta?.text.toString())) {
            Toast.makeText(this, "La boleta debe tener exactamente 10 dígitos y solo números.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!validarCampoNoVacio(contra?.text.toString(), "Contraseña")){
            isValid=false
        }else if (!validarContrasena(contra?.text.toString())) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres, incluyendo al menos un número, una letra minúscula y una letra mayúscula.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if(isValid){
            //Asigna los datos al objeto User
            user.boleta = boleta?.text.toString()
            user.contrasena =contra?.text.toString()
            //val url = "http://192.168.1.70//movil/login.php?idBoleta=${boleta?.text.toString()}"
            val url = "http://192.168.100.129:8080//movil/login.php?idBoleta=${boleta?.text.toString()}"
            //val url = "http://10.109.77.160:8080//movil/login.php?idBoleta=${boleta?.text.toString()}"

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,url,null,
                { response ->
                    val contraBD =   response.getString("Contra")
                    //Toast.makeText(this, response.getString("Contra") ,Toast.LENGTH_SHORT ).show()

                    if (contraBD == contra?.text.toString() ){
                        Toast.makeText(this,"Datos correctos", Toast.LENGTH_LONG ).show()
                        //home vendedor
                        val intent = Intent(this, PerfilVendedor::class.java).apply { putExtra("boletaI", boleta?.text.toString() ) }
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"La contraseña no coincide", Toast.LENGTH_LONG ).show()
                    }
                }, { error ->
                    //Toast.makeText(this,"No existe esa boleta",Toast.LENGTH_LONG).show()
                    Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show()
                    Log.e("LoginActivity", error.toString())
                }

            )
            queue.add(jsonObjectRequest)
        }
    }



    fun btnRegistro(view: View){
        val intent = Intent(this, registro_usuario::class.java)
        startActivity(intent)
    }

    // Funciones de validación
    private fun validarCampoNoVacio(campo: String, nombreCampo: String): Boolean {
        return if (campo.isEmpty()) {
            Toast.makeText(this, "El campo $nombreCampo no puede estar vacío.", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun validarBoleta(boleta: String): Boolean {
        return boleta.length == 10 && boleta.matches("\\d+".toRegex())
    }

    private fun validarContrasena(contraseña: String): Boolean {
        val regex = """^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)[A-Za-z\d]{8,}$""".toRegex()
        return contraseña.matches(regex)
    }

}

