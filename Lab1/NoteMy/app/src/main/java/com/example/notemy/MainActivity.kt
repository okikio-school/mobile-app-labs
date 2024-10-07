package com.example.notemy

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = findViewById<FloatingActionButton>(R.id.floating_action_button)
        button.setOnClickListener {
            // Call the launch method to switch to SecondActivity
            launch(NewNotesScreen::class.java)
        }

        // Create an instance of the DatabaseHelper class
        val myDB = DatabaseHelper(this@MainActivity)
        val dataList = myDB.listData()

        // Get reference to the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        // Set up the adapter
        val adapter = NoteAdapter(dataList)

        // Attach the adapter and a layout manager to the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Perform search when user submits the query
                if (query != null) performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Debounce search while the user types
                if (newText != null) performSearch(newText)
                return true
            }
        })

        updateRecyclerView(dataList)
    }


    override fun onResume() {
        super.onResume()

        val myDB = DatabaseHelper(this@MainActivity)
        val searchView = findViewById<SearchView>(R.id.searchView)

        val query = searchView.query
        val dataList = (
            if (query.isEmpty()) myDB.listData()
            else myDB.searchData(query.toString())
        );

        updateRecyclerView(dataList)
    }

    private fun performSearch(query: String) {
        val myDB = DatabaseHelper(this@MainActivity)
        val searchResults = (
            if (query.isEmpty()) myDB.listData()
            else myDB.searchData(query)
        );

        updateRecyclerView(searchResults)
    }

    private fun updateRecyclerView(dataList: MutableList<Map<String, Any>>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val emptyView = findViewById<TextView>(R.id.empty_view)

        val adapter = recyclerView.adapter as NoteAdapter
        adapter.updateData(dataList)

        if (recyclerView.isEmpty()) {
            recyclerView.visibility = RecyclerView.GONE
            emptyView.visibility = TextView.VISIBLE
        } else {
            recyclerView.visibility = RecyclerView.VISIBLE
            emptyView.visibility = TextView.GONE
        }
    }

    /**
     * Method to switch to a specified activity
     * @param activityClass the activity class to launch
     */
    private fun launch(activityClass: Class<*>) {
        // Create an Intent to start the specified activity
        val intent = Intent(this, activityClass)
        // Start the new activity
        startActivity(intent)
    }
}