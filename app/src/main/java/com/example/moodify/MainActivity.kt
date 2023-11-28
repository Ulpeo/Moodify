package com.example.moodify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.moodify.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

//google dependencies

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.mainFrag.id,fragment)
        transaction.commit()
    }
}