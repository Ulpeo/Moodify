package com.example.moodify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.moodify.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

//google dependencies

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

// for Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Sign out functionality
        /*binding.buttonSignOut.setOnClickListener({
            FirebaseAuth.getInstance().signOut()
            checkUser()
        })*/

        //delete shadow behind the icons
        binding.bottomNavigationView.background = null

        //make the item placeholder (to align icons in bottomBar) disabled
        //binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        //BottomBar which display different fragment depends on user's action
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true)


        bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.meditate ->{
                    loadFragment(Meditate())
                    true
                }
                R.id.reaction ->{
                    loadFragment(addMood())
                    true
                }
                R.id.call ->{
                    loadFragment(Call())
                    true
                }
                R.id.profile ->{
                    loadFragment(Profile())
                    true
                }
                R.id.home ->{
                    loadFragment(Home())

                    true
                }

                else -> false

            }


        }
        // display new entry fragment
        binding.btnAddEntry.setOnClickListener {
            loadFragment(NewEntryFragment())
            true
        }
    }

    //function for the app menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.appmenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            super.onOptionsItemSelected(item)
        }
        when (item?.itemId) {
            R.id.logout -> {FirebaseAuth.getInstance().signOut()
                checkUser()}

        }
        return true
    }
    //function to display fragments
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)

        transaction.replace(binding.mainFrag.id,fragment)
        transaction.commit()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        checkUser()
    }

    // check if there's a logged in user
    fun checkUser() {
        val currentUser = auth.currentUser
        if (currentUser == null){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        } else {
            binding.helloWorld.text = "Welcome, " + auth.currentUser!!.email
            loadFragment(Home())
        }
    }
}