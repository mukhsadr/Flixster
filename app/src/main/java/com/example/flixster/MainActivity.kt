package com.example.flixster

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.flixster.Fragments.NowPlaying
import com.example.flixster.Fragments.NowTrending
import com.example.flixster.Fragments.NowUpcoming
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main_land)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land)
        }

        // set up Fragment Manager and Bottom Navigation Bar;
        val fragmentManager: FragmentManager = supportFragmentManager
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener(){
            item ->
            var fragmentToShow: Fragment? = null
            when(item.itemId){
                R.id.action_play -> {
                    fragmentToShow = NowPlaying()
                }
                R.id.action_trend -> {
                    fragmentToShow = NowTrending()
                }
                R.id.action_upcome -> {
                    fragmentToShow = NowUpcoming()
                }
            }
            if (fragmentToShow != null){
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }
            true
        }
        // set default value;
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_play
    }
}