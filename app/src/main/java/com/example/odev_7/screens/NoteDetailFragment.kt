package com.example.odev_7.screens

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.odev_7.R
import com.example.odev_7.databases.DB
import com.example.odev_7.databinding.FragmentNoteDetailBinding
import com.example.odev_7.interfaces.INoteDetailListener
import com.example.odev_7.models.Note
import com.example.odev_7.utils.Util.showAddNotePopUp
import com.example.odev_7.utils.Util.showToast
import com.example.odev_7.viewModels.NoteDetailViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class NoteDetailFragment : Fragment() , INoteDetailListener{

    private  var _binding : FragmentNoteDetailBinding? = null
    private var nid : Int? = null
    private var note : Note? = null
    private lateinit var noteDetailViewModel: NoteDetailViewModel
    var cv = ContentValues()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            nid = bundle.getInt("nid")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listenEvents()
        setNoteFields()
        registerButtons()
    }

    private fun init(){
        noteDetailViewModel = ViewModelProvider(this)[NoteDetailViewModel::class.java]
        noteDetailViewModel.db = DB(requireContext())

        if(nid != null){
            noteDetailViewModel.getNoteById(nid!!)
        }
    }

    private fun registerButtons(){
        binding.btnDeleteNote.setOnClickListener(btnDeleteClickListener)
        binding.btnEditTitle.setOnClickListener(btnEditTitleClickListener)
        binding.btnCheckTitle.setOnClickListener(btnCheckTitleClickListener)
        binding.btnCheckDescription.setOnClickListener(btnCheckDescriptionClickListener)
        binding.btnEditDescription.setOnClickListener(btnEditDescriptionClickListener)
        binding.btnBack.setOnClickListener(btnBackClickListener)
    }

    private val btnBackClickListener = View.OnClickListener {
        findNavController().popBackStack()
    }

    private var btnDeleteClickListener = View.OnClickListener {
        nid?.let {noteId->
            deleteNote(noteId)
            findNavController().popBackStack()
        }
    }


    private var btnEditTitleClickListener = View.OnClickListener {
        binding.titleRow.visibility = View.GONE
        binding.editTitleRow.visibility = View.VISIBLE
    }

    private var btnEditDescriptionClickListener = View.OnClickListener {
        binding.descriptionRow.visibility = View.GONE
        binding.editDescriptionRow.visibility = View.VISIBLE
    }

    private var btnCheckTitleClickListener = View.OnClickListener {
        note?.let {currentNote->
            val title = binding.etTitle.text.toString()

            //fill contentValues object
            cv.put("title",title)
            cv.put("description",currentNote.description)
            cv.put("modified_at",getCurrentDate())

            //Update note
            updateNote(currentNote.nid,cv)

            //Clear CV object
            cv.clear()

            //Get Updated Note
            noteDetailViewModel.getNoteById(currentNote.nid)

            //Change Visibilities
            binding.titleRow.visibility = View.VISIBLE
            binding.editTitleRow.visibility = View.GONE
        }
    }


    private var btnCheckDescriptionClickListener = View.OnClickListener {
        note?.let {currentNote->
            val description = binding.etDescription.text.toString()

            //fill contentValues object
            cv.put("title",currentNote.title)
            cv.put("description",description)
            cv.put("modified_at",getCurrentDate())

            //Update note
            updateNote(currentNote.nid,cv)

            //Clear CV object
            cv.clear()

            //Get Updated Note
            noteDetailViewModel.getNoteById(currentNote.nid)

            //Change Visibilities
            binding.editDescriptionRow.visibility = View.GONE
            binding.descriptionRow.visibility = View.VISIBLE
        }
    }

    private fun getCurrentDate() : String{
        val calendar = Calendar.getInstance().time
        return DateFormat.getDateInstance().format(calendar)
    }

    private fun setNoteFields(){
        note?.let { currentNote->
            binding.txtNoteTitle.text = currentNote.title
            binding.txtNoteDescription.text = currentNote.description
            Log.d("createdAt",currentNote.createdAt)
            Log.d("modifiedAt",currentNote.modifiedAt.toString())
            if(currentNote.modifiedAt != null && currentNote.modifiedAt != currentNote.createdAt){
                binding.txtCreatedAt.text = "modified at ${currentNote.modifiedAt}"
            }else{
                binding.txtCreatedAt.text = "created at ${currentNote.createdAt}"
            }
        }
    }

    private fun listenEvents(){
        noteDetailViewModel.note.observe(viewLifecycleOwner){result->
            result?.let {currentNote->
                note = currentNote
                binding.etTitle.setText(currentNote.title)
                binding.etDescription.setText(currentNote.description)
                setNoteFields()
            }
        }
    }

    override fun getNote(nid: Int) {
        noteDetailViewModel.getNoteById(nid)
    }

    override fun updateNote(nid : Int, cv : ContentValues) {
        noteDetailViewModel.updateNote(nid,cv)
    }

    override fun deleteNote(nid: Int) {
        noteDetailViewModel.deleteNote(nid)
        if(noteDetailViewModel.deleteResult.value as Int > 0){
            showToast("Note Deleted Successfully")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}