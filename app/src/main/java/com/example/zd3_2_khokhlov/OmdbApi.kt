package com.example.zd3_2_khokhlov

import android.content.Context
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.net.URLEncoder

object OmdbApi {
    private const val API_KEY = "fe413f89"
    private const val BASE = "https://www.omdbapi.com/"
    private val gson = Gson()

    fun searchMovies(
        ctx: Context, query: String,
        onOk: (List<Movie>) -> Unit, onFail: (String) -> Unit
    ) {
        val queue = Volley.newRequestQueue(ctx)
        val result = mutableListOf<Movie>()

        fun page(n: Int) {
            val url = BASE + "?apikey=$API_KEY&type=movie&page=$n&s=" +
                URLEncoder.encode(query, "UTF-8")
            queue.add(StringRequest(url, { resp ->
                val r = gson.fromJson(resp, SearchResponse::class.java)
                if (r.search != null) result.addAll(r.search)
                when {
                    n == 1 && r.search != null -> page(2)
                    result.isNotEmpty() -> onOk(result)
                    else -> onFail(r.error ?: "Пусто")
                }
            }, { err ->
                if (result.isNotEmpty()) onOk(result) else onFail(err.message ?: "Ошибка сети")
            }))
        }
        page(1)
    }

    fun movieDetails(
        ctx: Context, imdbId: String,
        onOk: (MovieDetails) -> Unit, onFail: (String) -> Unit
    ) {
        val url = BASE + "?apikey=$API_KEY&plot=full&i=" + imdbId
        Volley.newRequestQueue(ctx).add(StringRequest(url, { resp ->
            onOk(gson.fromJson(resp, MovieDetails::class.java))
        }, { err -> onFail(err.message ?: "Ошибка сети") }))
    }
}
