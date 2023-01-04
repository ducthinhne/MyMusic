package com.example.mymusic.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    //view
    private lateinit var edtUsername: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtConfPass: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnSignUp: Button
    private lateinit var tvRedirectLogin: TextView

    // Initialize Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        Anhxa()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnRegister -> {
                Register()
            }
            R.id.tvLogin -> {
                BackLogin()
            }
        }
    }

    private fun BackLogin() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }

    private fun Register() {
        val name = edtUsername.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val pass = edtPass.text.toString().trim()
        val confirmPassword = edtConfPass.text.toString().trim()
        if (!ValidateEmail(email) || !ValidateName(name) || !ValidatePass(pass)) {
            return
        }
        if (pass != confirmPassword) {
            Toast.makeText(this, "Mật khẩu và Xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT)
                .show()
            return
        }
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                BackLogin()
            } else {
                Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun Anhxa() {
        edtUsername = findViewById(R.id.edtname)
        edtEmail = findViewById(R.id.edtemail)
        edtConfPass = findViewById(R.id.edtrepass)
        edtPass = findViewById(R.id.edtpass)
        btnSignUp = findViewById(R.id.btnRegister)
        tvRedirectLogin = findViewById(R.id.tvLogin)
        btnSignUp.setOnClickListener(this)
        tvRedirectLogin.setOnClickListener(this)
    }

    private fun ValidateEmail(text: String): Boolean {
        if (text.isBlank()) {
            edtEmail.setError("Email rỗng")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            edtEmail.setError("Email không đúng định dạng")
            return false
        } else {
            edtEmail.setError(null)
            return true
        }
    }

    private fun ValidateName(text: String): Boolean {
        if (text.isBlank()) {
            edtUsername.setError("Tên rỗng")
            return false
        } else {
            edtUsername.setError(null)
            return true
        }
    }

    private fun ValidatePass(text: String): Boolean {
        if (text.isBlank()) {
            edtPass.setError("Mật khẩu rỗng")
            return false
        } else if (text.length < 6) {
            edtPass.setError("Nhập mật khẩu trên 6 kí tự")
            return false
        } else {
            edtPass.setError(null)
            return true
        }
    }
}