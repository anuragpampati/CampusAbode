package com.example.campusabode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var mainAct: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))
        // Get support action bar
        val appBar = supportActionBar

        //set App title
//        appBar!!.title = "Navigation View"


        //Display app icon in toolbar
//        appBar.setDisplayShowHomeEnabled(true)
//        appBar.setLogo(R.mipmap.ic_launcher)
//        appBar.setDisplayUseLogoEnabled(true)
//        val searchIcon = findViewById<ImageView>(R.id.searchIcon)
//        val searchEditText = findViewById<TextInputEditText>(R.id.emailEt)

//        searchIcon.setOnClickListener {
//            val searchText = searchEditText.text.toString().trim()
//
//            if (searchText.isNotEmpty()) {
//                val propertyListFragment = PropertyListRecyclerViewFragment( )
//                val bundle = Bundle()
//                bundle.putString("searchLocation", searchText)
//                propertyListFragment.arguments = bundle
//
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.meContainer, propertyListFragment)
//                    .addToBackStack(null)
//                    .commit()
//
//                mainAct.closeDrawer(GravityCompat.START)
//            } else {
//                // Handle empty search text if needed
//                Toast.makeText(this, "Enter a location to search", Toast.LENGTH_SHORT).show()
//            }
//        }

        mainAct = findViewById(R.id.mainAct)
        val navView = findViewById<NavigationView>(R.id.navView)
        val toggle = ActionBarDrawerToggle(this, mainAct, toolbar, R.string.nav_open, R.string.nav_close)
        mainAct.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction()
            .add(R.id.meContainer, PropertyListRecyclerViewFragment()).commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_item_1 -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.meContainer, PropertyListRecyclerViewFragment()).commit()
            }
            R.id.nav_item_2 -> {
                val intent = Intent(this, MyProperties::class.java)
                startActivity(intent)
            }
            R.id.nav_item_3 -> {
                val intent = Intent(this, EditProfile::class.java)
                startActivity(intent)
            }
            R.id.nav_item_4 -> {
                userSignOut()
            }
        }
        mainAct.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onStart() {
        super.onStart()
//        checkUserSignedIn()
    }
    fun userSignOut(){
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}