package com.example.odev_7.components

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.odev_7.databases.DB
import com.example.odev_7.databinding.FragmentAddNotePopUpBinding
import com.example.odev_7.interfaces.IHomeListener
import com.example.odev_7.screens.HomeFragment
import com.example.odev_7.utils.Util.showToast
import java.util.*


class AddNotePopUpFragment : DialogFragment() {

    private  var _binding : FragmentAddNotePopUpBinding? = null
    private lateinit var db : DB
    private var selectedDate = ""
    private val calendar: Calendar = Calendar.getInstance()
    private val binding get() = _binding!!
    private var _listener : IHomeListener? = null

    companion object {
        const val TAG = "AddNotePopUpFragment"
    }

    fun setListener(listener : IHomeListener){
        _listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNotePopUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
    }

    private fun init(){
        db = DB(requireContext())
    }

    private fun registerEvents(){
        binding.btnDatePicker.setOnClickListener(btnDateClickListener)
        binding.btnAddNote.setOnClickListener(btnAddNoteClickListener)
    }

    private val btnAddNoteClickListener = View.OnClickListener {
        val noteTitle = binding.etTitle.text.toString()
        val noteDescription = binding.etDescription.text.toString()

        if(noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
            _listener?.saveNote(noteTitle,noteDescription,selectedDate)
            dismiss()
        }else{
            showToast("All Fields Are Required")
        }
    }

    private val btnDateClickListener = View.OnClickListener {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view, year, month, dayOfMonth ->
                var monthValue = "${month + 1}"
                if ((month + 1) < 10) {
                    monthValue = "0$monthValue"
                }
                selectedDate = "$dayOfMonth.$monthValue.$year"
                binding.txtSelectedDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )

        datePickerDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}