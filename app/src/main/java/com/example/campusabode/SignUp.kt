package com.example.campusabode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.campusabode.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()
        val accountExists = findViewById<TextView>(R.id.accountExists)
        accountExists.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener{
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passET.text.toString().trim()
            val confirmPassword = binding.confirmPassEt.text.toString().trim()
            val displayName = binding.displayName.text.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && displayName.isNotEmpty()){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                        if(it.isSuccessful){
                            val user = FirebaseAuth.getInstance().currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build()
                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { profileUpdateTask ->
                                    if (profileUpdateTask.isSuccessful) {
                                        val intent = Intent(this, Login::class.java)
                                        startActivity(intent)
                                    }
                                }

                        }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Password is not Matching", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}