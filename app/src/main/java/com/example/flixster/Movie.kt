package com.example.flixster

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

@Parcelize
data class Movie(
    val movieId: Int,
    val voteAverage: Double,
    private val posterPath: String?,
    val title: String,
    val overview: String,
    val releaseDate: String?,
    val genreIds: List<Int>,
    val originalLanguage: String?,
    private val backdropPath: String?,
    val popularity: Double,
    val voteCount: Int,
    val video: Boolean,
) : Parcelable {
    @IgnoredOnParcel
    val posterImageUrl: String? = if (posterPath != null) {
        "https://image.tmdb.org/t/p/w342/$posterPath"
    } else {
        null
    }

    @IgnoredOnParcel
    val backdropImageUrl: String? = if (backdropPath != null) {
        "https://image.tmdb.org/t/p/w780/$backdropPath"
    } else {
        null
    }

    companion object {
        fun fromJsonArray(movieJsonArray: JSONArray): List<Movie> {
            val movies = mutableListOf<Movie>()
            for (i in 0 until movieJsonArray.length()){
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add(
                    Movie(
                        movieJson.getInt("id"),
                        movieJson.getDouble("vote_average"),
                        movieJson.getString("poster_path"),
                        movieJson.getString("title"),
                        movieJson.getString("overview"),
                        movieJson.optString("release_date"),
                        genreIds = movieJson.getJSONArray("genre_ids").run {
                            (0 until length()).map { i -> getInt(i) }
                        },
                        movieJson.optString("original_language"),
                        movieJson.optString("backdrop_path"),
                        movieJson.getDouble("popularity"),
                        movieJson.getInt("vote_count"),
                        movieJson.getBoolean("video")
                    )
                )
            }
            return movies
        }
    }
}
