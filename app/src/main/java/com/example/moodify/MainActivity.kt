package com.example.moodify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class MainActivity : AppCompatActivity(), Login.Callbacks {

    private lateinit var auth: FirebaseAuth
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Sign out functionality
        binding.buttonSignOut.setOnClickListener({
            FirebaseAuth.getInstance().signOut()
            checkUser()
        })

        //delete shadow behind the icons
        binding.bottomNavigationView.background = null

        //make the item placeholder (to align icons in bottomBar) disabled
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        //BottomBar which display different fragment depends on user's action
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

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
                else -> false

            }


        }


        bottomNavigationView.selectedItemId = -1

    }

    //function to display fragments
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.mainFrag.id,fragment)
        transaction.commit()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        checkUser()
    }

    fun checkUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.helloWorld.text = currentUser.email
            loadFragment(GetEventFragment())
        } else {
            binding.helloWorld.text = "Hello World!"
            loadFragment(Login())
        }
    }

    override fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("ITM", "createUserWithEmail:success")
                    val user = auth.currentUser
                    loadFragment(GetEventFragment())
                    binding.helloWorld.text = "New User: ${user?.email ?: "No user!"}"
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ITM", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    binding.helloWorld.text = "Fail!"
                }
            }
    }

    override fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("ITM", "signInWithEmail:success")
                    val user = auth.currentUser
                    loadFragment(GetEventFragment())
                    binding.helloWorld.text = "Current User: ${user?.email ?: "No User!"}"
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ITM", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    binding.helloWorld.text = "Fail!"
                }
            }
    }
}