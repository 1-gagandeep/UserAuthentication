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

class signin : AppCompatActivity() {
    private lateinit var uname : EditText
    private lateinit var upassword : EditText
    private lateinit var login_button : Button
    private lateinit var create_button : Button
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        uname = findViewById(R.id.username)
        upassword = findViewById(R.id.user_password)

        login_button = findViewById(R.id.login_button)
        create_button = findViewById(R.id.create_account_button)
        mAuth = Firebase.auth

        login_button.setOnClickListener {
            val email = uname.text.toString()
            val password = upassword.text.toString()
            login(email, password)
            uname.text.clear()
            upassword.text.clear()
        }
        create_button.setOnClickListener {
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                        startActivity(Intent(this, home::class.java))
                    Toast.makeText( this,"Logged in Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText( this,"Some Error Occured", Toast.LENGTH_SHORT).show()
                }
            }
    }
}