package com.example.notemy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NewNotesScreen : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_notes_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backBtn = findViewById<Button>(R.id.back_btn);
        backBtn.setOnClickListener {
            // Call the launch method to switch to SecondActivity
            launch(MainActivity::class.java)
        }

        val blueBtn = findViewById<Button>(R.id.color_btn_blue)
        val deepGreenBtn = findViewById<Button>(R.id.color_btn_deep_green)
        val pastelPink = findViewById<Button>(R.id.color_btn_pastel_pink)
        val noteBackground = findViewById<View>(R.id.background)

        var currentColor = "white"
        val colorSelection = arrayOf("white", "blue", "pastel pink", "deep green")

        blueBtn.setOnClickListener{
            if (currentColor == "blue"){
                currentColor = "white"
                val color = ContextCompat.getColor(this, R.color.white)
                noteBackground.setBackgroundColor(color)
            } else {
                val color = ContextCompat.getColor(this, R.color.blue)
                noteBackground.setBackgroundColor(color)
                currentColor = "blue"
            }
        }
        deepGreenBtn.setOnClickListener{
            if (currentColor == "deep green"){
                currentColor = "white"
                val color = ContextCompat.getColor(this, R.color.white)
                noteBackground.setBackgroundColor(color)
            } else {
                val color = ContextCompat.getColor(this, R.color.deep_green)
                noteBackground.setBackgroundColor(color)
                currentColor = "deep green"
            }
        }
        pastelPink.setOnClickListener{
            if (currentColor == "pastel pink"){
                currentColor = "white"
                val color = ContextCompat.getColor(this, R.color.white)
                noteBackground.setBackgroundColor(color)
            } else {
                val color = ContextCompat.getColor(this, R.color.pastel_pink)
                noteBackground.setBackgroundColor(color)
                currentColor = "pastel pink"
            }
        }

        val textTitle = findViewById<EditText>(R.id.titleText)
        val textSubTitle = findViewById<EditText>(R.id.subtitleText)
        val mainNote = findViewById<EditText>(R.id.descriptionMultiLine)

        val saveBtn = findViewById<Button>(R.id.save_btn);
        saveBtn.setOnClickListener {
            // Call the launch method to switch to SecondActivity

            val myDB = DatabaseHelper(this@NewNotesScreen)
            val isInserted = myDB.insertData(
                textTitle.text.toString(),
                textSubTitle.text.toString(),
                mainNote.text.toString(),
                currentColor
            )

            val toastMsg = if (isInserted) "Data Inserted" else "Something went Wrong";
            Toast.makeText(this@NewNotesScreen, toastMsg, Toast.LENGTH_SHORT).show();

            launch(MainActivity::class.java)
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