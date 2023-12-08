package com.example.campusabode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val name = findViewById<TextView>(R.id.textView2)
        val user = FirebaseAuth.getInstance().currentUser
        name.text = user?.displayName
    }
}