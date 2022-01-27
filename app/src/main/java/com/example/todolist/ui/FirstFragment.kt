package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.adapter.NoteAdapter
import com.example.todolist.adapter.OnItemClickedListener
import com.example.todolist.databinding.FragmentFirstBinding
import com.example.todolist.room.NoteDatabase
import com.example.todolist.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class FirstFragment : Fragment(), OnItemClickedListener {
    private lateinit var layoutManager: LinearLayoutManager
    private var _binding: FragmentFirstBinding? = null
    private lateinit var noteAdapter: NoteAdapter
    private val binding get() = _binding!!
    private lateinit var vm: NoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val itemDao = NoteDatabase.getInstance(context!!.applicationContext)

        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        activity?.fab?.show()
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        noteAdapter = NoteAdapter(listOf(), this)

        vm = ViewModelProvider(activity!!).get(NoteViewModel::class.java)
        noteAdapter.setNotesList(vm.getItemListFromDB(itemDao))

        //recycler view init
        binding.itemRv.layoutManager = layoutManager
        binding.itemRv.adapter = noteAdapter

        //filter button clicked
        binding.filterButton.setOnClickListener {
            vm.getByCategory(binding.filterSpinner.selectedItem.toString(), itemDao)
        }

        //observer
        vm.itemListLiveData.observe(this@FirstFragment, {
            noteAdapter.setNotesList(it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(position: Int) {
        vm.note.value = vm.itemListLiveData.value?.get(position)
        findNavController().navigate(R.id.action_FirstFragment_to_thirdFragment)
        activity?.fab?.hide()
    }

}