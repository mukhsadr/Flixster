package com.example.flixster

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide
import java.io.ObjectOutputStream

class DetailActivity : AppCompatActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var rbVoteAverage: RatingBar
    private lateinit var ivPoster: ImageView
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        rbVoteAverage = findViewById(R.id.rbVoteAverage)
        ivPoster = findViewById(R.id.ivPoster)
        btnSave = findViewById(R.id.btn_save)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        rbVoteAverage.rating = movie.voteAverage.toFloat()
        Glide.with(this).load(movie.posterImageUrl).into(ivPoster)

        val isSaved = MovieStorage.containsMovie(this, movie)
        updateButtonState(isSaved)
        btnSave.setOnClickListener {
            val isSaved = MovieStorage.containsMovie(this, movie)
            if (isSaved) {
                MovieStorage.removeMovie(this, movie)
                saveMoviesToLocalMemory(MovieStorage.getMovies(this))
                Toast.makeText(this, "Removed from Bookmarks", Toast.LENGTH_SHORT).show()
            } else {
                MovieStorage.saveMovie(this, movie)
                saveMoviesToLocalMemory(MovieStorage.getMovies(this))
                Toast.makeText(this, "Added to Bookmarks", Toast.LENGTH_SHORT).show()
            }
            updateButtonState(!isSaved)
        }
    }

    private fun updateButtonState(isSaved: Boolean) {
        btnSave.text = if (isSaved) "Remove from Bookmarks" else "Add to Bookmarks"
    }

    private fun saveMoviesToLocalMemory(movies: List<Movie>) {
        try {
            val fileOutput = applicationContext.openFileOutput("saved_movies", Context.MODE_PRIVATE)
            val outputStream = ObjectOutputStream(fileOutput)
            outputStream.writeObject(movies)
            outputStream.close()
            fileOutput.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "DetailActivity"
    }
}