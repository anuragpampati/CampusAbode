package com.example.campusabode

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusabode.databinding.ActivityAddPropertyBinding
import com.example.campusabode.databinding.ActivityMainBinding

import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddProperty : AppCompatActivity() {

    private lateinit var binding: ActivityAddPropertyBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var adapter: MyAdapter
    private val itemList = mutableListOf<Item>()
    private var name: String = ""
    private var email: String = ""
    private var location: String = ""
    private var price: String = ""
    private var phone: String = ""
    private var bedrooms: String = ""
    private var bathrooms: String = ""
    private var availability: String = ""
    private var mapurl: String = ""
    private var youtubeurl: String = ""
    private val selectedImageUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        adapter = MyAdapter(itemList)

        binding.choosePhotoButton.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    .apply {
                        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        type = "image/*"
                    }
            startActivityForResult(galleryIntent, 1)
        }

        binding.uploadButton.setOnClickListener {
            name = binding.editTextName.text.toString()
            email = binding.editTextEmail.text.toString()
            location = binding.editTextLocation.text.toString()
            price=binding.editTextPrice.text.toString()
            phone=binding.editTextPhoneNumber.text.toString()
            bedrooms=binding.editTextBedroom.text.toString()
            bathrooms=binding.editTextBathroom.text.toString()
            availability=binding.editAvailability.text.toString()
            mapurl=binding.editMapurl.text.toString()
            youtubeurl=binding.edityoutubeurl.text.toString()


            if (name.isNotEmpty() && email.isNotEmpty() && location.isNotEmpty() && selectedImageUris.isNotEmpty() &&price.isNotEmpty() && phone.isNotEmpty() && bedrooms.isNotEmpty() && bathrooms.isNotEmpty() &&availability.isNotEmpty() &&mapurl.isNotEmpty() && youtubeurl.isNotEmpty()) {
                uploadImagesToFirebase(name, email, location, selectedImageUris , price,phone, bedrooms, bathrooms, availability, mapurl, youtubeurl)
            } else {
                Toast.makeText(this, "Please enter all details and choose at least one photo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.switchTabButton.setOnClickListener {
            val intent = Intent(this, MyProperties::class.java)
            startActivity(intent)
        }
    }



    private fun uploadImagesToFirebase(name: String, email: String, location: String, imageUris: List<Uri>, price: String,phone : String,bedrooms: String,bathrooms:String,availability: String,mapurl:String,youtubeurl:String) {
        // Create a reference for the user
        val userRef = database.reference.child("users").push()
        userRef.child("name").setValue(name)
        userRef.child("email").setValue(email)
        userRef.child("location").setValue(location)
        userRef.child("price").setValue(price)
        userRef.child("phone").setValue(phone)
        userRef.child("bedrooms").setValue(bedrooms)
        userRef.child("bathrooms").setValue(bathrooms)
        userRef.child("availability").setValue(availability)
        userRef.child("mapurl").setValue(mapurl)
        userRef.child("youtubeurl").setValue(youtubeurl)

        // Create a reference for the user's images
        val imagesRef = userRef.child("images")

        // Iterate through each image URI in the list
        for (imageUri in imageUris) {
            val imageName = UUID.randomUUID().toString()
            val storageReference: StorageReference = storage.reference.child("images/$imageName")

            storageReference.putFile(imageUri)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        // Create a reference for each image under "images" node
                        val imageRef = imagesRef.push()
                        imageRef.setValue(imageUrl)

                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("FirebaseUpload", "Error uploading image", e)
                }
        }
    }

    // In your onActivityResult method
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
//            val selectedImageUris = mutableListOf<Uri>()

            if (data.clipData != null) {
                // Multiple images selected
                for (i in 0 until data.clipData!!.itemCount) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    selectedImageUris.add(imageUri)
                }
            } else if (data.data != null) {
                // Single image selected
                val imageUri = data.data!!
                selectedImageUris.add(imageUri)
            }

            // Upload the images to Firebase
//            if (selectedImageUris.isNotEmpty()) {
//                val name = binding.editTextName.text.toString()
//                val email = binding.editTextEmail.text.toString()
//                val location = binding.editTextLocation.text.toString()
//
//                uploadImagesToFirebase(name, email, location, selectedImageUris)
//            }
        }
    }



}
