package com.example.campusabode

import Item
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusabode.databinding.ActivityAddPropertyBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class AddProperty : AppCompatActivity() {
    private var progressBar: ProgressBar? = null
    private lateinit var binding: ActivityAddPropertyBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var adapter: MyAdapter
    private val itemList = mutableListOf<Item>()
    private var location: String = ""
    private var price: String = ""
    private var phone: String = ""
    private var bedrooms: String = ""
    private var bathrooms: String = ""
    private var availability: String = ""
    private var mapurl: String = ""
    private var youtubeurl: String = ""
    private var description:String=""
    private val selectedImageUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        adapter = MyAdapter(itemList)


        progressBar = findViewById(R.id.progressBar)



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
            val user = FirebaseAuth.getInstance().currentUser
            val name = user?.displayName.toString()
            val email = user?.email.toString()
            description = binding.editTextDescription.text.toString()
            location = binding.editTextLocation.text.toString()
            price=binding.editTextPrice.text.toString()
            phone=binding.editTextPhoneNumber.text.toString()
            bedrooms=binding.editTextBedroom.text.toString()
            bathrooms=binding.editTextBathroom.text.toString()
            availability=binding.editAvailability.text.toString()
            mapurl=binding.editMapurl.text.toString()
            youtubeurl=binding.edityoutubeurl.text.toString()



            if (name.isNotEmpty() && email.isNotEmpty() && location.isNotEmpty()
                && selectedImageUris.isNotEmpty()
                &&price.isNotEmpty() && phone.isNotEmpty() && bedrooms.isNotEmpty() && bathrooms.isNotEmpty() &&availability.isNotEmpty() &&mapurl.isNotEmpty() && youtubeurl.isNotEmpty()) {
                uploadImagesToFirebase(name, email,description, location, selectedImageUris , price,phone, bedrooms, bathrooms, availability, mapurl, youtubeurl)
            } else {
                Toast.makeText(this, "Please enter all details and choose at least one photo", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun uploadImagesToFirebase(name: String, email: String,description: String, location: String, imageUris: List<Uri>, price: String,phone : String,bedrooms: String,bathrooms:String,availability: String,mapurl:String,youtubeurl:String) {
        // Create a reference for the user
//        val descr = findViewById<EditText>(R.id.editTextDescription)
//        val cp = findViewById<Button>(R.id.choosePhotoButton)
//        val loc = findViewById<EditText>(R.id.editTextLocation)
//        val pr = findViewById<EditText>(R.id.editTextPrice)
//        val ph = findViewById<EditText>(R.id.editTextPhoneNumber)
//        val ba = findViewById<EditText>(R.id.editTextBedroom)
//        val bd = findViewById<EditText>(R.id.editTextBathroom)
//        val av = findViewById<EditText>(R.id.editAvailability)
//        val mp = findViewById<EditText>(R.id.editMapurl)
//        val yt = findViewById<EditText>(R.id.edityoutubeurl)
        progressBar?.visibility = View.VISIBLE
//        descr?.visibility = View.GONE
//        cp?.visibility = View.GONE
//        loc?.visibility = View.GONE
//        pr?.visibility = View.GONE
//        ph?.visibility = View.GONE
//        ba?.visibility = View.GONE
//        bd?.visibility = View.GONE
//        av?.visibility = View.GONE
//        mp?.visibility = View.GONE
//        yt?.visibility = View.GONE
        val user = FirebaseAuth.getInstance().currentUser
        val userR = database.reference.child("users")
        val userRef = userR.child(user?.uid.toString()).push()

        userRef.child("name").setValue(name)
        userRef.child("email").setValue(email)
        userRef.child("description").setValue(description)
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
                        Toast.makeText(this, "Property uploaded successfully", Toast.LENGTH_SHORT).show()
                        progressBar?.visibility = View.GONE
//                        descr?.visibility = View.VISIBLE
//                        cp?.visibility = View.VISIBLE
//                        loc?.visibility = View.VISIBLE
//                        pr?.visibility = View.VISIBLE
//                        ph?.visibility = View.VISIBLE
//                        ba?.visibility = View.VISIBLE
//                        bd?.visibility = View.VISIBLE
//                        av?.visibility = View.VISIBLE
//                        mp?.visibility = View.VISIBLE
//                        yt?.visibility = View.VISIBLE
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
        }
    }



}
