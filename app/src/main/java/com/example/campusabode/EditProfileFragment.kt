

package com.example.campusabode

// EditProfileFragment.kt
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.example.campusabode.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            val newName = binding.newNameEditText.text.toString().trim()
//            val newEmail = binding.newEmailEditText.text.toString().trim()
            if (newName.isNotEmpty()) {
                updateDisplayName(newName)
            }
//            if(newEmail.isNotEmpty()){
//                updateDisplayEmail(newEmail)
//            }
        }
    }

    private fun updateDisplayName(newName: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()
        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateDisplayNameInDatabase(newName)
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }
//    private fun updateDisplayEmail(newEmail: String) {
//        val user = FirebaseAuth.getInstance().currentUser
//
//        user?.updateEmail(newEmail)?.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                updateDisplayEmailInDatabase(newEmail)
//                activity?.supportFragmentManager?.popBackStack()
//            } else {
//                Log.e("email","email is not changed")
//            }
//        }
//    }



    private fun updateDisplayNameInDatabase(newName: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance()

        userId?.let {
            val userRef = database.getReference("users").child(it)
            userRef.child("displayName").setValue(newName)
        }
    }
//    private fun updateDisplayEmailInDatabase(newEmail: String) {
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        database = FirebaseDatabase.getInstance()
//
//        userId?.let {
//            val userRef = database.getReference("users").child(it)
//            userRef.child("email").setValue(newEmail) // Change "displayName" to "email"
//        }
//    }

}
