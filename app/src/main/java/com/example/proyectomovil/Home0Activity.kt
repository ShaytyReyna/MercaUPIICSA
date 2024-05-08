package com.example.proyectomovil

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import com.example.proyectomovil.databinding.ActivityHome0Binding
import com.example.proyectomovil.ui.login.LoginActivity
import androidx.navigation.findNavController

class Home0Activity : AppCompatActivity() {
    private lateinit var binding: ActivityHome0Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home0)

        binding = ActivityHome0Binding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val botoncito = findViewById<Button>(R.id.butoncito)

        botoncito.setOnClickListener{*/
        binding.butoncito.setOnClickListener{
            /*val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)*/
            Toast.makeText(this, "Butoncito funciona", Toast.LENGTH_LONG).show()
            setContentView(R.layout.activity_login)
        }
    }
}