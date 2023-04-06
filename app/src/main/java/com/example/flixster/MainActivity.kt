package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixster.Fragments.NowPlaying
import com.example.flixster.Fragments.NowTrending
import com.example.flixster.Fragments.NowUpcoming
import com.example.flixster.Fragments.SavedMoviesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Headers
import okhttp3.internal.http2.Header
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var searchView: SearchView
    private lateinit var client: AsyncHttpClient
    private lateinit var movies: MutableList<Movie>
    private lateinit var movieAdapter: MovieAdapter
    private val TAG = "MainActivity"

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
                R.id.action_saved_movies -> SavedMoviesFragment()
                else -> NowPlaying()
            }
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentToShow)
                .commit()
            true
        }

        // Set default value
        bottomNavigationView.selectedItemId = R.id.action_play

        // Set up search view
        searchView = findViewById(R.id.search_view)
        client = AsyncHttpClient()
        movies = mutableListOf()
        movieAdapter = MovieAdapter(this, movies)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchMovie(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun searchMovie(query: String) {
        val URL =
            "https://api.themoviedb.org/3/search/movie?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US&query=$query&page=1&include_adult=false"
        client.get(URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure.")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                try {
                    Log.i(TAG, "onSuccess. $statusCode")
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies.clear()
                    movies.addAll(Movie.fromJsonArray(movieJsonArray))
                    movieAdapter.notifyDataSetChanged()

                    // Update the RecyclerView in the currently displayed fragment
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    if (currentFragment is NowPlaying) {
                        currentFragment.rvMovies.adapter = movieAdapter
                    } else if (currentFragment is NowTrending) {
                        currentFragment.rvMovies.adapter = movieAdapter
                    } else if (currentFragment is NowUpcoming) {
                        currentFragment.rvMovies.adapter = movieAdapter
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e.")
                }
            }
        })
    }
}
