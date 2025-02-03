package com.example.intent

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// SayHelloActivity: This screen shows a greeting message with the user's name and surname
class SayHelloActivity : AppCompatActivity() {

    // onCreate: This method runs when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_say_hello)  // Set the layout for this activity

        // Find the TextView where the greeting message will be displayed
        val greetingTextView = findViewById<TextView>(R.id.greetingTextView)

        // Get the name and surname passed from MainActivity
        val name = intent.getStringExtra("EXTRA_NAME")
        val surname = intent.getStringExtra("EXTRA_SURNAME")

        // Create the greeting message using the name and surname
        val greetingMessage = "Hello, $name $surname!"
        greetingTextView.text = greetingMessage  // Display the greeting message in the TextView

        // Change the text color based on the length of the name
        if (name != null && name.length > 5) {
            greetingTextView.setTextColor(Color.BLUE)  // If the name is long, set text color to blue
        } else {
            greetingTextView.setTextColor(Color.BLACK)  // Otherwise, set text color to black
        }
    }
}
