package com.example.zd3_2_khokhlov

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(
    private var items: List<Movie>,
    private val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val poster: ImageView = v.findViewById(R.id.ivPoster)
        val title: TextView = v.findViewById(R.id.tvTitle)
        val year: TextView = v.findViewById(R.id.tvYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))

    override fun onBindViewHolder(h: VH, position: Int) {
        val m = items[position]
        h.title.text = m.title
        h.year.text = "Год: ${m.year}\nНажмите OK, чтобы узнать подробности"
        Glide.with(h.poster).load(m.poster)
            .placeholder(R.drawable.kazan).error(R.drawable.kazan).into(h.poster)
        h.itemView.setOnClickListener { onClick(m) }
    }

    override fun getItemCount() = items.size

    fun submit(list: List<Movie>) {
        items = list
        notifyDataSetChanged()
    }
}
