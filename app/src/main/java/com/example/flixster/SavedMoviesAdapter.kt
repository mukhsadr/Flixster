package com.example.flixster


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson

const val MOVIE_EXTRA_SAVED = "MOVIE_EXTRA"

class SavedMovieAdapter(private val context: Context) : RecyclerView.Adapter<SavedMovieAdapter.ViewHolder>() {

    private var movies = mutableListOf<Movie>()

    init {
        // load the saved movies from local storage
        val jsonString = context.getSharedPreferences("movie_pref", Context.MODE_PRIVATE).getString("movie_list", "[]")
        //Log.d("SavedMovieAdapter", "jsonString: $jsonString")
        val savedMovies = Gson().fromJson(jsonString, Array<Movie>::class.java)
        //Log.d("SavedMovieAdapter", "savedMovies: $savedMovies")
        movies.addAll(savedMovies)
        Log.d("SavedMoviesFragment", "movies size: ${movies.size}")
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        Log.d("SavedMovieAdapter", "movie: $movie")
        holder.bind(movie)
    }


    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)
        private val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            Glide.with(context).load(movie.posterImageUrl).into(ivPoster)
        }

        override fun onClick(view: View?) {
            val movie = movies[adapterPosition]
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA_SAVED, movie)
            context.startActivity(intent)
        }

    }

}
