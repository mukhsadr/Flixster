package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var rbVoteAverage: RatingBar
    private lateinit var ivPoster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        rbVoteAverage = findViewById(R.id.rbVoteAverage)
        ivPoster = findViewById(R.id.ivPoster)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        rbVoteAverage.rating = movie.voteAverage.toFloat()
        Glide.with(this).load(movie.posterImageUrl).into(ivPoster)

    }

    companion object {
        private const val TAG = "DetailActivity"
    }
}