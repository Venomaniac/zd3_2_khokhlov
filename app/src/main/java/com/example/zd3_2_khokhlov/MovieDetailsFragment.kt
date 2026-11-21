package com.example.zd3_2_khokhlov

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class MovieDetailsFragment : Fragment() {

    companion object {
        private const val ARG_ID = "imdb_id"
        fun newInstance(id: String) = MovieDetailsFragment().apply {
            arguments = Bundle().apply { putString(ARG_ID, id) }
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_movie_details, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        btnBack.requestFocus()

        OmdbApi.movieDetails(requireContext(), requireArguments().getString(ARG_ID)!!, { d ->
            if (!isAdded) return@movieDetails
            view.findViewById<TextView>(R.id.tvTitle).text = d.title
            view.findViewById<TextView>(R.id.tvMeta).text =
                "${d.year}  •  ${d.runtime}  •  IMDb ${d.imdbRating}"
            view.findViewById<TextView>(R.id.tvInfo).text =
                "Жанр: ${d.genre}\nРежиссёр: ${d.director}\nАктёры: ${d.actors}"
            view.findViewById<TextView>(R.id.tvPlot).text = d.plot
            Glide.with(this).load(d.poster).into(view.findViewById<ImageView>(R.id.ivPoster))
        }, { err ->
            if (isAdded) Toast.makeText(requireContext(), err, Toast.LENGTH_LONG).show()
        })
    }
}
