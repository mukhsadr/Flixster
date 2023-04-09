package com.example.flixster

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers
import java.io.ObjectOutputStream

private const val YOUTUBE_API_KEY = "AIzaSyDhu35CJFcqScdG7RZmf6DAHhx58Yef-ks"
private const val TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class DetailActivity : YouTubeBaseActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var tvVoteCount: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvLang: TextView
    private lateinit var ivPoster: ImageView
    private lateinit var ivBackDrop: ImageView
    private lateinit var btnSave: Button
    private lateinit var ytPlayerView: YouTubePlayerView
    private lateinit var rbVoteCount: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        ivPoster = findViewById(R.id.ivPoster)
        ivBackDrop = findViewById(R.id.ivBackDrop)
        btnSave = findViewById(R.id.btn_save)
        ytPlayerView = findViewById(R.id.player)
        tvReleaseDate = findViewById(R.id.tvReleaseDate)
        tvLang = findViewById(R.id.tvLang)
        tvVoteCount = findViewById(R.id.tvVoteCount)
        rbVoteCount = findViewById(R.id.rbVoteCount)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        Glide.with(this).load(movie.posterImageUrl).into(ivPoster)
        Glide.with(this).load(movie.backdropImageUrl).into(ivBackDrop)
        tvReleaseDate.text = movie.releaseDate+"__:______Released Date"
        tvLang.text = movie.originalLanguage + "__:__Original Language"
        tvVoteCount.text = movie.voteCount.toString() + "__:_________Vote Count"
        rbVoteCount.rating = movie.voteAverage.toFloat()


        var client = AsyncHttpClient()
        client.get(TRAILER_URL.format(movie.movieId), object :JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess")
                val results = json.jsonObject.getJSONArray("results")
                if (results.length() > 0) {
                    val youtube_key = results.getJSONObject(0).getString("key")
                    ytPlayerView.visibility = View.VISIBLE
                    ivPoster.visibility = View.GONE
                    initializeYouTube(youtube_key)
                } else {
                    ytPlayerView.visibility = View.GONE
                    Glide.with(this@DetailActivity).load(movie.posterImageUrl).into(ivPoster)
                    ivPoster.visibility = View.VISIBLE
                }
            }
        })


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

    private fun initializeYouTube(youtubeKey: String) {
        ytPlayerView.initialize(YOUTUBE_API_KEY, object :YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess")
                player?.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
                player?.cueVideo(youtubeKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.e(TAG, "onInitializationFailure")
            }

        })
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