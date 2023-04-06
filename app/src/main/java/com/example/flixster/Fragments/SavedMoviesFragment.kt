package com.example.flixster.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flixster.Movie
import com.example.flixster.MovieAdapter
import com.example.flixster.R
import com.example.flixster.SavedMovieAdapter
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class SavedMoviesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: SavedMovieAdapter
    private lateinit var movieList: MutableList<Movie>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_movies, container, false)

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.rvMovies)
        recyclerView.layoutManager = LinearLayoutManager(context)
        movieList = readMoviesFromLocalMemory()
        movieAdapter = SavedMovieAdapter(requireContext())
        recyclerView.adapter = movieAdapter
        Log.d("SavedMoviesFragment", "Adapter connected")

        return view
    }

    private fun readMoviesFromLocalMemory(): MutableList<Movie> {
        var movies: MutableList<Movie> = mutableListOf()

        try {
            val fileInput = requireContext().openFileInput("saved_movies")
            val inputStream = ObjectInputStream(fileInput)
            movies = inputStream.readObject() as MutableList<Movie>
            inputStream.close()
            fileInput.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return movies
    }

    fun saveMoviesToLocalMemory() {
        try {
            val fileOutput = requireContext().openFileOutput("saved_movies", 0)
            val outputStream = ObjectOutputStream(fileOutput)
            outputStream.writeObject(movieList)
            outputStream.close()
            fileOutput.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        saveMoviesToLocalMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveMoviesToLocalMemory()
    }
}
