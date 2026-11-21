package com.example.zd3_2_khokhlov

import com.google.gson.annotations.SerializedName

data class Channel(val name: String, val query: String)

data class Movie(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Poster") val poster: String?,
    val imdbID: String
)

data class SearchResponse(
    @SerializedName("Search") val search: List<Movie>?,
    @SerializedName("Response") val response: String?,
    @SerializedName("Error") val error: String?
)

data class MovieDetails(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("Runtime") val runtime: String?,
    @SerializedName("Genre") val genre: String?,
    @SerializedName("Director") val director: String?,
    @SerializedName("Actors") val actors: String?,
    @SerializedName("Plot") val plot: String?,
    @SerializedName("Poster") val poster: String?,
    val imdbRating: String?
)
