package com.example.mymusic.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusic.R
import com.google.firebase.auth.FirebaseAuth


class ForgotPassActivity : AppCompatActivity() {
    private lateinit var btnsendmail: Button
    private lateinit var edtemail: EditText
    private lateinit var progressBar: ProgressBar

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        auth = FirebaseAuth.getInstance()
        Anhxa()
        btnsendmail.setOnClickListener {  resetpasswoord()}
    }

    private fun resetpasswoord() {
        val emailaddress = edtemail.getText().toString().trim()
        progressBar.visibility=View.VISIBLE
        auth.sendPasswordResetEmail(emailaddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar.visibility=View.GONE
                    Toast.makeText(applicationContext, "Kiểm tra Email của bạn", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    progressBar.visibility=View.GONE
                    Toast.makeText(this@ForgotPassActivity,"Không tìm thấy tài khoản của bạn" ,Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    private fun Anhxa(){
        btnsendmail=findViewById(R.id.btnsendmail)
        edtemail=findViewById(R.id.edtgmailforgot)
        progressBar=findViewById(R.id.progressBar)
    }
}