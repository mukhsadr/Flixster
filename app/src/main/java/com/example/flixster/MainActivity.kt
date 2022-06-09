package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.flixster.Fragments.NowPlaying
import com.example.flixster.Fragments.NowTrending
import com.example.flixster.Fragments.NowUpcoming
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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

        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_play


    }
    val TAG = "MainActivity"

    companion object {
        private const val NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
    }


}