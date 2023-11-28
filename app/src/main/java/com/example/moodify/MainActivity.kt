package com.example.moodify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.moodify.databinding.ActivityMainBinding

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


    }
}