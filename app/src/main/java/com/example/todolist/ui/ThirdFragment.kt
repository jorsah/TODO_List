package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.FragmentThirdBinding
import com.example.todolist.room.NoteDatabase
import com.example.todolist.viewmodel.NoteViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@DelicateCoroutinesApi
class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: NoteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        vm = ViewModelProvider(activity!!).get(NoteViewModel::class.java)
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemDao = NoteDatabase.getInstance(context!!.applicationContext)
        initItemData()

        binding.editBtn.setOnClickListener {
            vm.editMode = true
            findNavController().navigate(R.id.action_thirdFragment_to_SecondFragment)
        }

        binding.deleteBtn.setOnClickListener {
            findNavController().navigate(R.id.action_thirdFragment_to_FirstFragment)
            GlobalScope.launch(Dispatchers.IO) {
                vm.deleteItemFromDb(vm.note.value!!, itemDao)
            }
        }

    }

    private fun initItemData(){
        val value = vm.note.value
        it_name.text = value?.noteName
        it_description.text = value?.description
        item_date.text = value?.date
        item_category.text = value?.type
        Picasso.get().load(value?.image).into(image)
    }
}