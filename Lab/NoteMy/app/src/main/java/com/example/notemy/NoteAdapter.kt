package com.example.notemy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.notemy.databinding.ActivityViewNoteBinding

class NoteAdapter(private var itemList: MutableList<Map<String, Any>>) : RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {
    private val colorMap = mapOf(
        "white" to R.color.white,
        "blue" to R.color.blue,
        "pastel pink" to R.color.pastel_pink,
        "deep green" to R.color.deep_green,
    )

    // ViewHolder class that holds reference to the item views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val subtitle: TextView = itemView.findViewById(R.id.subtitle)
        val description: TextView = itemView.findViewById(R.id.description)
        val card: CardView = itemView.findViewById(R.id.note_card)
    }

    // Inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_view_design, parent, false)
        return MyViewHolder(view)
    }

    // Bind data to the item view
    override fun onBindViewHolder(holder: MyViewHolder, index: Int) {
        println("Binding data for item at index $index: ${itemList[index]}")
        val item = itemList[index]
        holder.title.text = item["title"].toString()
        holder.subtitle.text = item["subtitle"].toString()
        holder.description.text = item["description"].toString()

        // Get the color from the map using the item's "color" value
        val colorKey = item["color"].toString()
        setColor(colorKey, holder)

        holder.card.setOnClickListener { v ->
            val intent = Intent(v.context, ViewNote::class.java)
            val options = Bundle().apply {
                putInt("id", Integer.parseInt(item["id"].toString()))
            }

            // Attach the options to the intent as extras
            intent.putExtras(options)

            startActivity(v.context, intent, null)
        }
    }

    private fun setColor(color: String, holder: NoteAdapter.MyViewHolder) {
        val colorResId = if (color.isNotEmpty() && colorMap.containsKey(color)) {
            colorMap[color] ?: R.color.white
        } else R.color.white  // Default to white if no match

        // Use the correct context
        val colorRes = ContextCompat.getColor(holder.itemView.context, colorResId)
        // Set the card background color
        holder.card.setCardBackgroundColor(colorRes)

        if (color == "deep green") {
            holder.title.setTextColor(Color.WHITE)
            holder.subtitle.setTextColor(Color.WHITE)
            holder.description.setTextColor(Color.WHITE)
        } else {
            holder.title.setTextColor(Color.BLACK)
            holder.subtitle.setTextColor(Color.BLACK)
            holder.description.setTextColor(Color.BLACK)
        }
    }

    // Return the number of items in the data list
    override fun getItemCount(): Int {
        return itemList.size
    }

    // Method to update the dataset
    fun updateData(newItems: MutableList<Map<String, Any>>) {
        itemList = newItems
        notifyDataSetChanged() // This refreshes the RecyclerView with new data
    }
}
