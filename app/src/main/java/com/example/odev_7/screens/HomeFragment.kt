package com.example.odev_7.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.odev_7.adapters.CustomNoteAdapter
import com.example.odev_7.databases.DB
import com.example.odev_7.databinding.FragmentHomeBinding
import com.example.odev_7.interfaces.IHomeListener
import com.example.odev_7.utils.Util.showAddNotePopUp
import com.example.odev_7.utils.Util.showToast
import com.example.odev_7.viewModels.HomeViewModel

class HomeFragment : Fragment() , IHomeListener {

    private  var _binding: FragmentHomeBinding? = null
    private lateinit var customNoteAdapter: CustomNoteAdapter
    private lateinit var navController: NavController
    private lateinit var homeViewModel: HomeViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    //define instances
    private fun init(view : View){
        navController = Navigation.findNavController(view)
        customNoteAdapter = CustomNoteAdapter()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.db = DB(requireContext())
        homeViewModel.getAllNotes()
    }

    private fun registerEvents(){
        binding.btnAddNote.setOnClickListener {
            showAddNotePopUp(this)
        }
        binding.notesRecyclerView.adapter = customNoteAdapter
        binding.notesRecyclerView.setHasFixedSize(true)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun saveNote(title: String, description: String, createdAt: String) {
        homeViewModel.saveNote(title,description,createdAt)
    }

    private fun listenEvents(){
        homeViewModel.saveNotestatus.observe(viewLifecycleOwner){ result ->
            if(result){
                showToast("Note Added Succcesfully")
                homeViewModel.getAllNotes()
            }
        }

        homeViewModel.notes.observe(viewLifecycleOwner){result ->
            result?.let {
                customNoteAdapter.submitList(it)

                //Check notes empty status
                homeViewModel.notes.value?.let {notes ->
                    if(notes.isEmpty()){
                        binding.txtNotAddedNotesYet.visibility = View.VISIBLE
                    }else{
                        binding.txtNotAddedNotesYet.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        listenEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}