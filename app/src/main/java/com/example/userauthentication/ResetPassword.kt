package com.example.userauthentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {

    private lateinit var edt_reset_pass: EditText
    private lateinit var btn_reset_pass: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        edt_reset_pass = findViewById(R.id.edt_reset)
        btn_reset_pass = findViewById(R.id.btn_reset)

        mAuth = FirebaseAuth.getInstance()

        btn_reset_pass.setOnClickListener {
            val email_ = edt_reset_pass.text.toString()
            mAuth.sendPasswordResetEmail(email_)
                .addOnSuccessListener {
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                    edt_reset_pass.text.clear()
                    startActivity(Intent(this, signin::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }
        }

    }
}