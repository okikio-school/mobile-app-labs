package com.example.notemy

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NewNotesScreen : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")

    private var id = -1
    private var extras: Bundle? = null
    private val colorMap = mapOf(
        "white" to R.color.white,
        "blue" to R.color.blue,
        "pastel pink" to R.color.pastel_pink,
        "deep green" to R.color.deep_green,
    )

    private var currentImageUri: String? = null
    private var currentColor = "white"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_notes_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Restore saved state (if any)
        if (savedInstanceState != null) {
            updateNoteData(savedInstanceState)
        }

        val backBtn = findViewById<Button>(R.id.back_btn)
        backBtn.setOnClickListener {
            if (id > 0) {
                onBackPressedDispatcher.onBackPressed()
            } else {
                // Call the launch method to switch to SecondActivity
                launch(MainActivity::class.java)
            }
        }

        val blueBtn = findViewById<Button>(R.id.color_btn_blue)
        val deepGreenBtn = findViewById<Button>(R.id.color_btn_deep_green)
        val pastelPink = findViewById<Button>(R.id.color_btn_pastel_pink)

        blueBtn.setOnClickListener {
            if (currentColor == "blue") {
                setColor("white")
            } else {
                setColor("blue")
            }
        }

        deepGreenBtn.setOnClickListener{
            if (currentColor == "deep green"){
                setColor("white")
            } else {
                setColor("deep green")
            }
        }

        pastelPink.setOnClickListener{
            if (currentColor == "pastel pink"){
                setColor("white")
            } else {
                setColor("pastel pink")
            }
        }

        val textTitle = findViewById<EditText>(R.id.titleText)
        val textSubTitle = findViewById<EditText>(R.id.subtitleText)
        val mainNote = findViewById<EditText>(R.id.descriptionMultiLine)

        val saveBtn = findViewById<Button>(R.id.save_btn)
        saveBtn.setOnClickListener { view ->
            val myDB = DatabaseHelper(this@NewNotesScreen)
            val isUpdated = if (id > 0) {
                myDB.updateData(
                    id.toString(),
                    textTitle.text.toString(),
                    textSubTitle.text.toString(),
                    mainNote.text.toString(),
                    currentColor
                )
            } else false
            val isInserted = if (id <= 0) {
                myDB.insertData(
                    textTitle.text.toString(),
                    textSubTitle.text.toString(),
                    mainNote.text.toString(),
                    currentColor,
                    currentImageUri
                )
            } else -1L

            if (isUpdated) {
                myDB.updateImage(id.toString(), currentImageUri.toString())
            }

            val toastMsg = if (isInserted > -1L || isUpdated) "Note Inserted" else "Something went Wrong"
            Toast.makeText(view.context, toastMsg, Toast.LENGTH_SHORT).show()

            if (isInserted > -1L || isUpdated) {
                if (isUpdated) {
                    onBackPressedDispatcher.onBackPressed()
                } else {
                    launch(MainActivity::class.java)
                }
            }
        }

        val uploadImageBtn = findViewById<Button>(R.id.uploadImageBtn)
        uploadImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        }
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            // Handle the result
            val imageUri = result.data?.data
            println("Image Intent Result: $result; Image URI: $imageUri")

            if (imageUri != null) {
                currentImageUri = imageUri.toString()

                updateNoteData()

                // Persist the permission to access the URI
                grantUriPermission(imageUri)

                val imageView = findViewById<ImageView>(R.id.imageView)
                imageView.setImageURI(Uri.parse(currentImageUri))
            }
        }
    }


    private fun grantUriPermission(uri: Uri) {
        // Get the flags from the intent
        val flags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        // Request permission for the content URI (persistable)
        contentResolver.takePersistableUriPermission(uri, flags)
    }

    // Save the instance state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val textTitle = findViewById<EditText>(R.id.titleText)
        val textSubTitle = findViewById<EditText>(R.id.subtitleText)
        val mainNote = findViewById<EditText>(R.id.descriptionMultiLine)

        // Save the current data to the instance state
        outState.putString("title", textTitle.text.toString())
        outState.putString("subtitle", textSubTitle.text.toString())
        outState.putString("description", mainNote.text.toString())
        outState.putString("color", currentColor)
    }

    // Restore the instance state
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore the saved data
        updateNoteData(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        updateNoteData()
    }

    private fun updateNoteData(instanceState: Bundle? = null) {
        extras = intent.extras
        id = extras?.getInt("id") ?: -1

        val textTitle = findViewById<EditText>(R.id.titleText)
        val textSubTitle = findViewById<EditText>(R.id.subtitleText)
        val mainNote = findViewById<EditText>(R.id.descriptionMultiLine)
        val textView3 = findViewById<TextView>(R.id.textView3)
        val imageView = findViewById<ImageView>(R.id.imageView)

        if (id > 0) {
            val databaseHelper = DatabaseHelper(this)
            val data = databaseHelper.getData(id.toString())

            val title = data[0]["title"].toString()
            val subtitle = data[0]["subtitle"].toString()
            val description = data[0]["description"].toString()
            val image = data[0]["image"] ?: null
            val imageString = image.toString()

            textTitle.setText(title)
            textSubTitle.setText(subtitle)
            mainNote.setText(description)
            textView3.text = "Edit Note"

            // Get the color from the map using the item's "color" value
            val colorKey = data[0]["color"].toString()
            setColor(colorKey)


            println("NewNoteScreen UpdateNoteData Image URI: $image")
            println("NewNoteScreen UpdateNoteData Current Image URI: $currentImageUri")
            if (image != null && imageString.isNotEmpty() && imageString != "null") {
                imageView.setImageURI(Uri.parse(imageString))
                currentImageUri = imageString
            }
        } else if (instanceState != null && instanceState.containsKey("title")) {
            textTitle.setText(instanceState.getString("title"))
            textSubTitle.setText(instanceState.getString("subtitle"))
            mainNote.setText(instanceState.getString("description"))
            setColor(instanceState.getString("color") ?: "white")
            textView3.text = "New Note Screen"
        } else {
            textTitle.setText("")
            textSubTitle.setText("")
            mainNote.setText("")
            textView3.text = "New Note Screen"
            setColor("white")
            imageView.setImageResource(R.drawable.ic_launcher_background)
            currentImageUri = null
        }
    }

    private fun setColor(color: String) {
        val noteBackground = findViewById<View>(R.id.background)
        val textTitle = findViewById<EditText>(R.id.titleText)
        val textSubTitle = findViewById<EditText>(R.id.subtitleText)
        val mainNote = findViewById<EditText>(R.id.descriptionMultiLine)

        val colorResId = if (color.isNotEmpty() && colorMap.containsKey(color)) {
            colorMap[color] ?: R.color.white
        } else R.color.white  // Default to white if no match

        // Use the correct context
        val colorRes = ContextCompat.getColor(this, colorResId)
        // Change the background color of the entire CoordinatorLayout
        noteBackground.setBackgroundColor(colorRes)
        currentColor = color

        if (color == "deep green") {
            textTitle.setTextColor(Color.WHITE)
            textSubTitle.setTextColor(Color.WHITE)
            mainNote.setTextColor(Color.WHITE)
        } else {
            textTitle.setTextColor(Color.BLACK)
            textSubTitle.setTextColor(Color.BLACK)
            mainNote.setTextColor(Color.BLACK)
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