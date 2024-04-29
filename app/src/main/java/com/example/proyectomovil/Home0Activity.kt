package com.example.proyectomovil

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.content.Intent
import com.example.proyectomovil.databinding.ActivityHome0Binding
import com.example.proyectomovil.ui.login.LoginActivity

class Home0Activity : AppCompatActivity() {
    private lateinit var binding: ActivityHome0Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home0)

        binding = ActivityHome0Binding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnLoginHome0.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}