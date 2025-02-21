package com.example.userauthentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btn1 = findViewById<Button>(R.id.signin_button)
        val btn2 = findViewById<Button>(R.id.signup_button)

        btn1.setOnClickListener {
            val intent = Intent(this, signin::class.java)
            startActivity(intent)
        }

        btn2.setOnClickListener {
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
        }
    }
}