package com.example.flixster.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixster.Movie
import com.example.flixster.MovieAdapter
import com.example.flixster.R
import okhttp3.Headers
import org.json.JSONException


class NowUpcoming : Fragment() {

    private val movies2 = mutableListOf<Movie>()
    private lateinit var rvMovies2: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMovies2 = view.findViewById(R.id.rvMovieUpcoming)
        val movieAdapter2 = MovieAdapter(requireContext(), movies2)
        rvMovies2.adapter = movieAdapter2
        rvMovies2.layoutManager = LinearLayoutManager(requireContext())
        val client = AsyncHttpClient()
        client.get(NOW_PLAYING, object : JsonHttpResponseHandler(){
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure.")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                try {
                    Log.i(TAG, "onSuccess. $statusCode")
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies2.addAll(Movie.fromJsonArray(movieJsonArray))
                    movieAdapter2.notifyDataSetChanged()
                }
                catch (e: JSONException) { Log.e(TAG, "Encountered exception $e.") }
            }

        })
    }

    companion object {
        private const val TAG = "NowPlaying"
        private const val NOW_PLAYING = "https://api.themoviedb.org/3/movie/upcoming?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
    }


}