//package com.example.userauthentication
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.google.firebase.FirebaseException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthOptions
//import com.google.firebase.auth.PhoneAuthProvider
//import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import java.util.concurrent.TimeUnit
//
//class phone : AppCompatActivity() {
//    private lateinit var edtPhoneNUmber: EditText
//    private lateinit var btnSendOtp: Button
//    private lateinit var edtVerifyOtp: EditText
//    private lateinit var btnVerifyOtp: Button
//    private lateinit var mAuth: FirebaseAuth
//    var verificationId = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_phone)
//
//        edtPhoneNUmber = findViewById(R.id.edt_otp)
//        btnSendOtp = findViewById(R.id.btn_otp)
//        edtVerifyOtp = findViewById(R.id.edt_verify_otp)
//        btnVerifyOtp = findViewById(R.id.btn_verify_otp)
//
//        mAuth = Firebase.auth
//
//        btnSendOtp.setOnClickListener {
//            val number = "+91${edtPhoneNUmber.text}"
//            sendVerficationCode(number)
//        }
//
//        btnVerifyOtp.setOnClickListener {
//            val otp = edtVerifyOtp.text.toString()
//            verifyCode(otp)
//        }
//    }
//
//    private fun verifyCode(code:String) {
//        val credentail = PhoneAuthProvider.getCredential(verificationId, code)
//        signInWithCredentials(credentail)
//    }
//
//    private fun signInWithCredentials(phoneAuthCredential: PhoneAuthCredential) {
//        mAuth.signInWithCredential(phoneAuthCredential)
//            .addOnCompleteListener {  task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Phone Number Verified Successfully", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, home::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(this, "Phone Number Verification Failed", Toast.LENGTH_SHORT).show()
//                }
//                }
//    }
//
//    private fun sendVerficationCode(number:String) {
//        val options = PhoneAuthOptions.newBuilder(mAuth)
//            .setPhoneNumber(number)
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(verificationCallBack)
//            .build()
//
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    val verificationCallBack:OnVerificationStateChangedCallbacks =
//        object : OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onVerificationFailed(p0: FirebaseException) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onCodeSent(s: String, p1: PhoneAuthProvider.ForceResendingToken) {
//                super.onCodeSent(s, p1)
//                verificationId = s
//            }
//        }
//}

package com.example.userauthentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class phone : AppCompatActivity() {
    private lateinit var edtPhoneNumber: EditText
    private lateinit var btnSendOtp: Button
    private lateinit var edtVerifyOtp: EditText
    private lateinit var btnVerifyOtp: Button
    private lateinit var mAuth: FirebaseAuth
    private var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_phone)

        edtPhoneNumber = findViewById(R.id.edt_otp)
        btnSendOtp = findViewById(R.id.btn_otp)
        edtVerifyOtp = findViewById(R.id.edt_verify_otp)
        btnVerifyOtp = findViewById(R.id.btn_verify_otp)

        mAuth = FirebaseAuth.getInstance()

        btnSendOtp.setOnClickListener {
            val phoneInput = edtPhoneNumber.text.toString().trim()
            if (phoneInput.length != 10) {
                Toast.makeText(this, "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val number = "+91$phoneInput"
            sendVerificationCode(number)
        }

        btnVerifyOtp.setOnClickListener {
            val otp = edtVerifyOtp.text.toString().trim()
            if (otp.length != 6) {
                Toast.makeText(this, "Enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            verifyCode(otp)
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(verificationCallback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("PhoneAuth", "Sending OTP to $number")
    }

    private fun verifyCode(code: String) {
        if (verificationId.isEmpty()) {
            Toast.makeText(this, "OTP not sent yet", Toast.LENGTH_SHORT).show()
            return
        }
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredentials(credential)
    }

    private fun signInWithCredentials(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Phone Number Verified Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, home::class.java))
                    finish()
                } else {
                    val errorMsg = task.exception?.message ?: "Unknown error"
                    Log.e("PhoneAuth", "Sign-in failed: $errorMsg")
                    Toast.makeText(this, "Verification Failed: $errorMsg", Toast.LENGTH_LONG).show()
                }
            }
    }

    private val verificationCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("PhoneAuth", "Verification completed automatically")
            signInWithCredentials(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            val errorMsg = exception.message ?: "Unknown error"
            Log.e("PhoneAuth", "Verification failed: $errorMsg")
            Toast.makeText(this@phone, "Failed to send OTP: $errorMsg", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(s: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(s, token)
            verificationId = s
            Log.d("PhoneAuth", "Code sent successfully, verificationId: $s")
            Toast.makeText(this@phone, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
        }
    }
}