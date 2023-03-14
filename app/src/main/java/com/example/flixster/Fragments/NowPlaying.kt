package com.example.flixster.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixster.Movie
import com.example.flixster.MovieAdapter
import com.example.flixster.R
import okhttp3.Headers
import org.json.JSONException

class NowPlaying : Fragment() {

    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMovies = view.findViewById(R.id.rvMovies)
        val movieAdapter = MovieAdapter(requireContext(), movies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(requireContext())

        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
        client.get(url, object : JsonHttpResponseHandler() {

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure: $statusCode")
            }

            override fun onSuccess(
                statusCode: Int,
                headers: Headers?,
                json: JSON?
            ) {
                Log.i(TAG, "onSuccess: $statusCode")
                json?.let {
                    try {
                        val movieJsonArray = it.jsonObject.getJSONArray("results")
                        movies.addAll(Movie.fromJsonArray(movieJsonArray))
                        movieAdapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        Log.e(TAG, "Encountered exception: $e")
                    }
                }
            }
        })

        view.findViewById<Button>(R.id.btn_search).setOnClickListener {
            val text = view.findViewById<EditText>(R.id.et_search).text.toString()
            val url = "https://api.themoviedb.org/3/search/movie?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US&query=$text&page=1&include_adult=false"
            client.get(url, object : JsonHttpResponseHandler() {

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.e(TAG, "onFailure: $statusCode")
                }

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers?,
                    json: JSON?
                ) {
                    Log.i(TAG, "onSuccess: $statusCode")
                    json?.let {
                        try {
                            val movieJsonArray = it.jsonObject.getJSONArray("results")
                            movies.clear()
                            movies.addAll(Movie.fromJsonArray(movieJsonArray))
                            movieAdapter.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            Log.e(TAG, "Encountered exception: $e")
                        }
                    }
                }
            })
        }
    }

    companion object {
        private const val TAG = "NowPlaying"
    }
}
