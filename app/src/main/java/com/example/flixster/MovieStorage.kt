package com.example.flixster

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MovieStorage {
    private const val PREF_NAME = "movie_pref"
    private const val MOVIE_LIST_KEY = "movie_list"

    fun saveMovie(context: Context, movie: Movie) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val movies = getMovies(context)
        movies.add(movie)
        pref.edit {
            putString(MOVIE_LIST_KEY, Gson().toJson(movies))
        }
    }

    fun removeMovie(context: Context, movie: Movie) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val movies = getMovies(context)
        movies.remove(movie)
        pref.edit {
            putString(MOVIE_LIST_KEY, Gson().toJson(movies))
        }
    }

    fun getMovies(context: Context): MutableList<Movie> {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val moviesJson = pref.getString(MOVIE_LIST_KEY, null)
        return if (moviesJson != null) {
            Gson().fromJson(moviesJson, object : TypeToken<MutableList<Movie>>() {}.type)
        } else {
            mutableListOf()
        }
    }

    fun containsMovie(context: Context, movie: Movie): Boolean {
        val movies = getMovies(context)
        return movies.contains(movie)
    }
}
