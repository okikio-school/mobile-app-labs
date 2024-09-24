package com.example.notemy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
        blueBtn.setOnClickListener{
            noteBackground.setBackgroundColor(R.color.blue)
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