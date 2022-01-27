package com.example.todolist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.model.NoteModel
import kotlinx.android.synthetic.main.item_layout.view.*

class NoteAdapter(
    private var noteList: List<NoteModel>,
    private var callback: OnItemClickedListener
) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.item_name
        val itemType: TextView = itemView.item_type
        val isDone: ImageView = itemView.item_image
        fun bind() {
            itemView.setOnClickListener {
                callback.onItemClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val noteItem = noteList[position]
        holder.bind()

        holder.itemName.text = noteItem.noteName
        holder.itemType.text = noteItem.type
        if (noteItem.done) {
            holder.isDone.setImageResource(R.drawable.img_1)
        } else holder.isDone.setImageResource(R.drawable.img)
    }

    override fun getItemCount(): Int = noteList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNotesList(list: List<NoteModel>) {
        noteList = list
        notifyDataSetChanged()
    }
}

interface OnItemClickedListener {
    fun onItemClicked(position: Int)
}
