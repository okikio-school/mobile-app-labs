package com.example.notemy

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.notemy.databinding.ActivityViewNoteBinding

class  ViewNote : AppCompatActivity() {

    private lateinit var binding: ActivityViewNoteBinding

    private var id: Int = -1
    private var extras: Bundle? = null
    private val colorMap = mapOf(
        "white" to R.color.white,
        "blue" to R.color.blue,
        "pastel pink" to R.color.pastel_pink,
        "deep green" to R.color.deep_green,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        // Enable the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle back button press
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Set fab click listener
        binding.fab.setOnClickListener { view ->
            val intent = Intent(view.context, NewNotesScreen::class.java)
            val options = Bundle().apply {
                putInt("id", id)
            }

            intent.putExtras(options)
            startActivity(intent)
        }

        binding.deleteFab.setOnClickListener { view ->
            val databaseHelper = DatabaseHelper(this)
            val isDeleted = databaseHelper.deleteData(id.toString())

            val toastMsg = if (isDeleted) "Note deleted" else "Something went Wrong";
            Toast.makeText(view.context, toastMsg, Toast.LENGTH_SHORT).show();

            if (isDeleted) {
                launch(MainActivity::class.java)
                id = -1
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateNoteData()
    }

    private fun updateNoteData() {
        // Retrieve the bundle from the intent's extras
        extras = intent.extras
        id = extras?.getInt("id") ?: -1

        val databaseHelper = DatabaseHelper(this)
        val data = if (id > 0) databaseHelper.getData(id.toString()) else null;

        // Use the values from the bundle
        binding.toolbar.title = (extras?.getString("title") ?: data?.get(0)?.get("title") ?: "Empty Note Title").toString()
        binding.toolbar.subtitle = (extras?.getString("subtitle") ?: data?.get(0)?.get("subtitle") ?: "Empty Note Subtitle").toString()

        val imageRef = extras?.getString("image") ?: data?.get(0)?.get("image")
        val imageString = imageRef.toString()
        if (imageRef != null && imageString.isNotEmpty() && imageString != "null") {
            val image = binding.contentScrolling.noteImage
            image?.setImageURI(Uri.parse(imageString))
//            image?.setImageBitmap()
        }

        // Get the color from the map using the item's "color" value
        val colorKey = (extras?.getString("color") ?: data?.get(0)?.get("color")).toString()
        setColor(colorKey, binding)

        // You can now use subtitle, description, and color in the activity as needed
        if (binding.contentScrolling.noteDescription != null) {
            binding.contentScrolling.noteDescription!!.text =
                (extras?.getString("description") ?: data?.get(0)?.get("description") ?: "Empty Description").toString()
        } else {
            binding.contentScrolling.noteDescription?.text = "Empty Description"
        }

    }

    private fun setColor(color: String, binding: ActivityViewNoteBinding) {
        val colorResId = if (color.isNotEmpty() && colorMap.containsKey(color)) {
            colorMap[color] ?: R.color.white
        } else R.color.white  // Default to white if no match

        // Use the correct context
        val colorRes = ContextCompat.getColor(this, colorResId)
        // Change the background color of the entire CoordinatorLayout
        binding.root.setBackgroundColor(colorRes)

        if (color == "deep green") {
            binding.contentScrolling.noteDescription?.setTextColor(Color.WHITE)
        } else {
            binding.contentScrolling.noteDescription?.setTextColor(Color.BLACK)
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