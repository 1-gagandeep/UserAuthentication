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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class signup : AppCompatActivity() {

    private lateinit var fname : EditText
    private lateinit var lname : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var res_button : Button
    private lateinit var login_ac_button : Button
    private lateinit var mAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        fname = findViewById(R.id.first_name)
        lname = findViewById(R.id.last_name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        res_button = findViewById(R.id.register_button)
        login_ac_button = findViewById(R.id.login_account_button)
        mAuth = Firebase.auth

        res_button.setOnClickListener {
            val user_email = email.text.toString()
            val user_password = password.text.toString()
            signup(user_email, user_password)
            fname.text.clear()
            lname.text.clear()
            email.text.clear()
            password.text.clear()
        }
        login_ac_button.setOnClickListener {
            startActivity(Intent(this, signin::class.java))
        }

    }

    private fun signup(user_email: String, user_password: String) {
        mAuth.createUserWithEmailAndPassword(user_email, user_password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, signin::class.java))
                    Toast.makeText( this,"Signned up Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText( this,"Some Error Occured", Toast.LENGTH_SHORT).show()
                }
            }
    }

}