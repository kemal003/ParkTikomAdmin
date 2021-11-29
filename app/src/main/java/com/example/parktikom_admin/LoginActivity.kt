package com.example.parktikom_admin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import com.example.parktikom_admin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private var doubleBackExit = false
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.header_drawable))

        mAuth = Firebase.auth

        binding.username.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString() + "@parktikom.com"
            val password = binding.password.text.toString()
            login(username, password)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            goHome(currentUser)
        }
    }

    private fun login(email: String, password: String){
        binding.progressBarLogin.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser
                    binding.progressBarLogin.visibility = View.GONE
                    binding.username.text.clear()
                    binding.password.text.clear()
                    goHome(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    binding.progressBarLogin.visibility = View.GONE
                    binding.password.text.clear()
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goHome(user: FirebaseUser?) {
        val home = Intent(this, HomeActivity::class.java)
        startActivity(home)
    }

    override fun onBackPressed() {
        if (doubleBackExit){
            super.onBackPressed()
            onDestroy()
        }

        this.doubleBackExit = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackExit = false }, 1000)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val usernameInput = binding.username.text.toString().trim()
            val passwordInput = binding.password.text.toString()

            if (!usernameInput.isEmpty() && !passwordInput.isEmpty()){
                binding.loginButton.isEnabled = true
            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
}