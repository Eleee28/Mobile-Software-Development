package com.example.birds


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter class for displaying a list of Bird items in a RecyclerView
class BirdAdapter(private var birds: List<Bird>) : RecyclerView.Adapter<BirdAdapter.BirdViewHolder>() {

    // ViewHolder class that represents each item view in the RecyclerView
    inner class BirdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val birdName: TextView = itemView.findViewById(R.id.birdName) // TextView for bird name
        val birdImage: ImageView = itemView.findViewById(R.id.birdImage) // ImageView for bird image
        val birdDescription: TextView = itemView.findViewById(R.id.birdDescription) // TextView for bird description
    }

    // Creates a new ViewHolder by inflating the item layout (item_bird.xml)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bird, parent, false)
        return BirdViewHolder(view)
    }

    // Binds data to the views in each ViewHolder based on the position
    override fun onBindViewHolder(holder: BirdViewHolder, position: Int) {
        val bird = birds[position]
        holder.birdName.text = bird.name // Sets bird name
        holder.birdDescription.text = bird.description // Sets bird description
        holder.birdImage.setImageResource(bird.imageResId) // Sets bird image
    }

    // Returns the total number of items in the data list
    override fun getItemCount(): Int {
        return birds.size
    }

    // Updates the list of birds and notifies RecyclerView of data change
    fun updateBirds(newBirds: List<Bird>) {
        birds = newBirds
        notifyDataSetChanged()
    }
}
