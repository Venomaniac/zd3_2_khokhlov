package com.example.zd3_2_khokhlov

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val channels = listOf(
        Channel("Action", "action"),
        Channel("Comedy", "comedy"),
        Channel("Horror", "horror"),
        Channel("Drama", "drama"),
        Channel("Sci-Fi", "space")
    )

    private var allMovies: List<Movie> = emptyList()
    private lateinit var adapter: MovieAdapter
    private lateinit var progress: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var tvChannel: TextView
    private lateinit var etSearch: EditText
    private lateinit var container: View
    private lateinit var menu: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progress)
        tvEmpty = findViewById(R.id.tvEmpty)
        tvChannel = findViewById(R.id.tvChannel)
        etSearch = findViewById(R.id.etSearch)
        container = findViewById(R.id.fragment_container)
        menu = findViewById(R.id.menu)

        adapter = MovieAdapter(emptyList()) { openDetails(it) }
        findViewById<RecyclerView>(R.id.rvMovies).apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = this@MainActivity.adapter
        }

        buildMenu()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = applyFilter()
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })

        supportFragmentManager.addOnBackStackChangedListener {
            container.visibility =
                if (supportFragmentManager.backStackEntryCount > 0) View.VISIBLE else View.GONE
        }

        loadChannel(channels[0])
    }

    private fun buildMenu() {
        channels.forEach { ch ->
            val item = TextView(this).apply {
                text = ch.name
                textSize = 16f
                setTextColor(getColor(R.color.white))
                setPadding(32, 14, 32, 14)
                isFocusable = true
                background = getDrawable(R.drawable.bg_menu_item)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = 16 }
                setOnClickListener { loadChannel(ch) }
                setOnFocusChangeListener { _, hasFocus ->
                    setTextColor(getColor(if (hasFocus) R.color.black else R.color.white))
                }
            }
            menu.addView(item)
        }
    }

    private fun loadChannel(ch: Channel) {
        for (i in 0 until menu.childCount) menu.getChildAt(i).isSelected = (i == channels.indexOf(ch))
        tvChannel.text = "My quests – ${ch.name}"
        etSearch.setText("")
        progress.visibility = View.VISIBLE
        tvEmpty.visibility = View.GONE
        adapter.submit(emptyList())

        OmdbApi.searchMovies(this, ch.query, { list ->
            allMovies = list
            progress.visibility = View.GONE
            applyFilter()
        }, { err ->
            progress.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
            Toast.makeText(this, err, Toast.LENGTH_LONG).show()
        })
    }

    private fun applyFilter() {
        val q = etSearch.text.toString().trim()
        val list = if (q.isEmpty()) allMovies
        else allMovies.filter { it.title.contains(q, ignoreCase = true) }
        adapter.submit(list)
        tvEmpty.visibility = if (list.isEmpty() && progress.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    private fun openDetails(m: Movie) {
        container.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MovieDetailsFragment.newInstance(m.imdbID))
            .addToBackStack(null)
            .commit()
    }
}
