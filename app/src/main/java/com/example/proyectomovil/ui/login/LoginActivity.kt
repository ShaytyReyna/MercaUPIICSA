package com.example.proyectomovil.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.proyectomovil.Home0Activity
import com.example.proyectomovil.MainActivity
import com.example.proyectomovil.databinding.ActivityLoginBinding

import com.example.proyectomovil.R
import com.example.proyectomovil.data.model.User
import com.example.proyectomovil.registro_usuario
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el objeto User
        user = User()

        binding.btnLogIn?.setOnClickListener{
            Log.d("LoginActivity", "Botón registro presionado")

            val boleta = findViewById<TextInputEditText>(R.id.TIboleta).text.toString()
            val contra = findViewById<TextInputEditText>(R.id.TIcontra).text.toString()

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

        binding.btnRegistro?.setOnClickListener{
            val intent = Intent(this, registro_usuario::class.java)
            startActivity(intent)
        }

        //val loading = binding.loading
        /*
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }*/
    }
/*
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }*/
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

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}