package com.example.mymusic.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import com.example.mymusic.MainActivity
import com.example.mymusic.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnLogin: Button
    private lateinit var tvTextLoginDangky: TextView
    private lateinit var edtgmail: EditText
    private lateinit var edtpassword: EditText
    private lateinit var tvforgotpasss: TextView
    private lateinit var cvLoginGoogle: CardView

    // Initialize Firebase Auth
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val TAG = "GoogleActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Anhxa()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            auth = Firebase.auth
            updateUI(currentUser)
        } else {
            auth = FirebaseAuth.getInstance()
        }
    }

    private fun Anhxa() {
        btnLogin = findViewById(R.id.btnLogin)
        tvTextLoginDangky = findViewById(R.id.tvTextLoginDangky)
        edtgmail = findViewById(R.id.edtgmail)
        edtpassword = findViewById(R.id.edtpassword)
        tvforgotpasss = findViewById(R.id.tvforgotpass)
        cvLoginGoogle = findViewById(R.id.cvLoginGoogle)
        tvforgotpasss.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPassActivity::class.java)
            startActivity(intent)
        }
        cvLoginGoogle.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        tvTextLoginDangky.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogin -> {
                Login()
            }
            R.id.tvTextLoginDangky -> {
                RegisterScreen()
            }
            R.id.cvLoginGoogle -> {
                LoginWithGG()
            }
        }
    }

    private fun LoginWithGG() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Log.e("ssss","sss3"+task.toString())
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
    }

    private fun Login() {
        val email = edtgmail.text.toString().trim()
        val pass = edtpassword.text.toString().trim()
        if (!ValidateEmail(email) || !ValidatePass(pass)) {
            return
        }else {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else
                    Toast.makeText(this, "Đăng nhập thất bại! ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun RegisterScreen() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun ValidateEmail(text: String): Boolean {
        if (text.isBlank()) {
            edtgmail.setError("Email rỗng")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            edtgmail.setError("Email không đúng định dạng")
            return false
        } else {
            edtgmail.setError(null)
            return true
        }
    }

    private fun ValidatePass(text: String): Boolean {
        if (text.isBlank()) {
            edtpassword.setError("Mật khẩu rỗng")
            return false
        } else if (text.length < 6) {
            edtpassword.setError("Nhập mật khẩu trên 6 kí tự")
            return false
        } else {
            edtpassword.setError(null)
            return true
        }
    }
}