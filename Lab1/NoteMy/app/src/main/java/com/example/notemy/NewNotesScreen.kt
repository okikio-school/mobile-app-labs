package com.example.notemy

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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

        val button1 = findViewById<Button>(R.id.back_btn);
        button1.setOnClickListener {
            // Call the launch method to switch to SecondActivity
            launch(MainActivity::class.java)
        }


        val button2 = findViewById<Button>(R.id.save_btn);
        button2.setOnClickListener {
            // Call the launch method to switch to SecondActivity
            launch(MainActivity::class.java)
        }
        val blueBtn = findViewById<Button>(R.id.color_btn_blue)
        val deepGreenBtn = findViewById<Button>(R.id.color_btn_deep_green)
        val pastelPink = findViewById<Button>(R.id.color_btn_pastel_pink)
        val noteBackground = findViewById<View>(R.id.background)
        val textTitle = findViewById<EditText>(R.id.editTextText)
        val textSubTitle = findViewById<EditText>(R.id.editTextText2)
        val mainNote = findViewById<EditText>(R.id.editTextTextMultiLine)
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