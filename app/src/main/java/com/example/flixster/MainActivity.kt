package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.example.flixster.Fragments.NowPlaying
import com.example.flixster.Fragments.NowTrending
import com.example.flixster.Fragments.NowUpcoming
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up Fragment Manager and Bottom Navigation Bar
        fragmentManager = supportFragmentManager
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragmentToShow: Fragment = when (item.itemId) {
                R.id.action_play -> NowPlaying()
                R.id.action_trend -> NowTrending()
                R.id.action_upcome -> NowUpcoming()
                else -> NowPlaying()
            }
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentToShow)
                .commit()
            true
        }

        // Set default value
        bottomNavigationView.selectedItemId = R.id.action_play
    }
}
