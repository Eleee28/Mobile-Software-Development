package com.example.intent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// MainActivity: This is the first screen where users will enter their name and surname
class MainActivity : AppCompatActivity() {

    // onCreate: This method runs when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Set the layout for the activity

        // Find the input fields and buttons by their ID in the layout file
        val nameInput = findViewById<EditText>(R.id.nameInput)   // Input field for the name
        val surnameInput = findViewById<EditText>(R.id.surnameInput)  // Input field for the surname
        val submitButton = findViewById<Button>(R.id.submitButton)  // Button to submit the form
        val clearButton = findViewById<Button>(R.id.clearButton)    // Button to clear the form

        // Set a click listener for the submit button to perform an action when clicked
        submitButton.setOnClickListener {
            // Get the name and surname entered by the user, trimming any extra spaces
            val name = nameInput.text.toString().trim()
            val surname = surnameInput.text.toString().trim()

            // Check if both name and surname are provided
            if (name.isNotEmpty() && surname.isNotEmpty()) {
                // Create an intent to navigate to the SayHelloActivity
                val intent = Intent(this, SayHelloActivity::class.java).apply {
                    // Put the name and surname into the intent to pass them to the next activity
                    putExtra("EXTRA_NAME", name)
                    putExtra("EXTRA_SURNAME", surname)
                }
                startActivity(intent)  // Start the SayHelloActivity
            } else {
                // Show a message (toast) if name or surname is missing
                Toast.makeText(this, "Please enter both name and surname", Toast.LENGTH_SHORT).show()
            }
        }

        // Set a click listener for the clear button to reset the input fields
        clearButton.setOnClickListener {
            nameInput.text.clear()  // Clear the name input field
            surnameInput.text.clear()  // Clear the surname input field
        }
    }
}
