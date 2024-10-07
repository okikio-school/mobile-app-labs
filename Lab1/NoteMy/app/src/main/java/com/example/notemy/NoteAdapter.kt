package com.example.notemy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val itemList: MutableList<Map<String, Any>>) : RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    // ViewHolder class that holds reference to the item views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val subtitle: TextView = itemView.findViewById(R.id.subtitle)
        val description: TextView = itemView.findViewById(R.id.description)
    }

    // Inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_view_design, parent, false)
        Toast.makeText(parent.context, "onCreateViewHolder", Toast.LENGTH_SHORT).show()
        return MyViewHolder(view)
    }

    // Bind data to the item view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        holder.title.text = item["title"].toString()
        holder.subtitle.text = item["subtitle"].toString()
        holder.description.text = item["description"].toString()
    }

    // Return the number of items in the data list
    override fun getItemCount(): Int {
        return itemList.size
    }
}