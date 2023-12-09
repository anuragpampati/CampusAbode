package com.example.campusabode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.example.campusabode.databinding.ActivityEditProfileBinding  // Remove this line

class EditProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val editButton = findViewById<Button>(R.id.profilebutton)

        editButton.setOnClickListener {
            val editProfileFragment = EditProfileFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, editProfileFragment)
                .addToBackStack(null)
                .commit()
        }

        val auth = FirebaseAuth.getInstance().currentUser?.uid
        val name = findViewById<TextView>(R.id.profilename)
        val user = FirebaseAuth.getInstance().currentUser
        name.text = user?.displayName
        val email = findViewById<TextView>(R.id.profileemail)
        email.text = user?.email
    }
}
