package com.example.odev_7.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.odev_7.R
import com.example.odev_7.databinding.NoteListItemBinding
import com.example.odev_7.models.Note

class CustomNoteAdapter: RecyclerView.Adapter<CustomNoteAdapter.NoteViewHolder>(){

    private var notes = mutableListOf<Note>()

    class NoteViewHolder(val binding : NoteListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder){
            with(notes[position]){
                binding.txtNoteTitlePw.text = this.title
                binding.cvNoteListItem.setOnClickListener {view ->
                    var bundle = bundleOf("nid" to nid)
                    bundle.putInt("nid",notes[position].nid)
                    view.findNavController().navigate(R.id.action_homeFragment_to_noteDetailFragment,bundle)
                }
            }
        }
    }

    fun submitList(list : MutableList<Note>){
        notes = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return notes.count()
    }

}