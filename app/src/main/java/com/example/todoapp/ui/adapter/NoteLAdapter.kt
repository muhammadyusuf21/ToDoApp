package com.example.todoapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.persistence.Note
import kotlinx.android.synthetic.main.note_items.view.*
import java.util.*
import javax.inject.Inject


class NoteAdapter(
    noteList: List<Note>,
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>(), Filterable {

    private var notes = mutableListOf<Note>()

    @Inject

    lateinit var allNotes: List<Note>

    init {
        notes.addAll(noteList)
    }

    // Method #1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_items, parent, false)
        return ViewHolder(
            view,
            interaction
        )
    }

    // Method #2
    override fun getItemCount(): Int {
        return notes.size
    }


    // Method #3
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = notes[position])
    }


    // Method #4
    fun swap(notes: List<Note>) {
        val diffCallback = DiffCallback(this.notes, notes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.notes.clear()
        this.notes.addAll(notes)
        diffResult.dispatchUpdatesTo(this)
    }


    // Method #5
    class ViewHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        // Method #6
        fun bind(item: Note) {
            itemView.txtTitle.text = item.title
            itemView.txtDescription.text = item.description
            itemView.txtTag.text = item.tag

            //Handle item click
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
        }

    }

    // Method #7
    interface Interaction {
        fun onItemSelected(position: Int, item: Note)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val keywords = constraint.toString()
                if (keywords.isEmpty()) {
                    allNotes = notes
                }
                else{
                    val filteredList = ArrayList<Note>()
                    for (note in notes) {
                        if (note.toString().toLowerCase(Locale.ROOT).contains(keywords.toLowerCase(Locale.ROOT)))
                            filteredList.add(note)
                    }
                    allNotes = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = allNotes
                return  filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                allNotes = results?.values as List<Note>
                notifyDataSetChanged()
            }
        }
    }

}