package com.example.userauthentication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.userauthentication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class signin : AppCompatActivity() {
    private lateinit var uname: EditText
    private lateinit var upassword: EditText
    private lateinit var loginButton: Button
    private lateinit var createButton: Button
    private lateinit var forgetPassword: Button
    private lateinit var btnGoogle: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleSignInLauncher: androidx.activity.result.ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        // Initialize views
        uname = findViewById(R.id.username)
        upassword = findViewById(R.id.user_password)
        forgetPassword = findViewById(R.id.forget_password)
        loginButton = findViewById(R.id.login_button)
        createButton = findViewById(R.id.create_account_button)
        btnGoogle = findViewById(R.id.btn_google)

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Set click listeners
        loginButton.setOnClickListener {
            val email = uname.text.toString()
            val password = upassword.text.toString()
            login(email, password)
        }

        createButton.setOnClickListener {
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
            finish()
        }

        forgetPassword.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
            finish()
        }

        // Password hiding functionality
        val passwordEditText = findViewById<EditText>(R.id.user_password)
        passwordEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (passwordEditText.right - passwordEditText.compoundDrawables[2].bounds.width())) {
                    val currentInputType = passwordEditText.inputType
                    if (currentInputType == InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT) {
                        // Show password
                        passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        passwordEditText.compoundDrawables[2].setState(intArrayOf(android.R.attr.state_checked))
                    } else {
                        // Hide password
                        passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        passwordEditText.compoundDrawables[2].setState(intArrayOf())
                    }
                    passwordEditText.setSelection(passwordEditText.text.length) // Keep cursor at end
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Configure Google Sign-In
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .requestProfile() // Ensure account picker includes profile data
            .build()

        // Initialize GoogleSignInClient
        gsc = GoogleSignIn.getClient(this, gso)

        // Initialize ActivityResultLauncher for Google Sign-In
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuth(account.idToken)
                } catch (e: ApiException) {
                    Toast.makeText(this, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Google Sign-In Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoogle.setOnClickListener {
            gsc.signOut().continueWithTask {
                gsc.revokeAccess()
            }.addOnCompleteListener {
                Toast.makeText(this, "Signed out, launching picker", Toast.LENGTH_SHORT).show()
                val signInIntent = gsc.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }
        }
    }

    private fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, home::class.java))
                    uname.text.clear()
                    upassword.text.clear()
                    Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun firebaseAuth(idToken: String?) {
        if (idToken != null) {
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        if (user != null) {
                            val map = hashMapOf<String, Any?>()
                            map["id"] = user.uid
                            map["name"] = user.displayName
                            map["email"] = user.email
                            map["photo"] = user.photoUrl?.toString()
                            database.getReference("Users").child(user.uid).setValue(map)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        startActivity(Intent(this, home::class.java))
                                        finish()
                                        Toast.makeText(this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this, "Failed to save user data: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "User is null after sign-in", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Firebase Auth Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Google ID Token is null", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001 // Unused but kept for reference
    }
}