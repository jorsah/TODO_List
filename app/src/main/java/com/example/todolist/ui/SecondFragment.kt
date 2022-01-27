package com.example.todolist.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.FragmentSecondBinding
import com.example.todolist.model.NoteModel
import com.example.todolist.room.NoteDatabase
import com.example.todolist.viewmodel.NoteViewModel
import com.example.todolist.viewmodel.SecondViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val PICK_IMAGE = 1
    private var imageView: Uri? = null
    private lateinit var vm: NoteViewModel
    private lateinit var secondViewModel: SecondViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm = ViewModelProvider(activity!!).get(NoteViewModel::class.java)
        secondViewModel = ViewModelProvider(this).get(SecondViewModel::class.java)
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemDao = NoteDatabase.getInstance(context!!.applicationContext)
        editModeInit(resources.getStringArray(R.array.Categories))

        binding.addImage.setOnClickListener {
            startActivityForResult(
                Intent.createChooser(secondViewModel.createGalleryIntent(), "Select Picture"),
                PICK_IMAGE
            )
        }

        binding.saveButton.setOnClickListener {
            val note: NoteModel = noteInit(secondViewModel.getCurrentData())
            if (vm.editMode){
                GlobalScope.launch(Dispatchers.IO) {
                    vm.updateNote(note, itemDao)
                }
            }else{
                GlobalScope.launch(Dispatchers.IO) {
                    vm.addItemListToDB(note, itemDao)
                }
            }
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageView = data?.data
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageView)

            binding.addImage.setImageBitmap(bitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun editModeInit(arr: Array<String>) {
        val value = vm.note.value
        if (vm.editMode) {
            item_name_et.append(value?.noteName)
            category_spinner.setSelection(vm.getIndex(arr))
            note_details_et.append(value?.description)
            Picasso.get().load(value?.image).into(add_image)
            value?.let {
                done_checkbox.isChecked = it.done
            }
        }
    }

    private fun noteInit(date: String): NoteModel {
        return if (vm.editMode) {
            NoteModel(
                noteName = binding.itemNameEt.text.toString(),
                type = binding.categorySpinner.selectedItem.toString(),
                done = binding.doneCheckbox.isChecked,
                description = binding.noteDetailsEt.text.toString(),
                image = secondViewModel.getImageUri(context!!, binding.addImage.drawToBitmap())
                    .toString(),
                date = date,
                id = vm.note.value?.id!!
            )
        } else {
            NoteModel(
                noteName = binding.itemNameEt.text.toString(),
                type = binding.categorySpinner.selectedItem.toString(),
                done = binding.doneCheckbox.isChecked,
                description = binding.noteDetailsEt.text.toString(),
                image = secondViewModel.getImageUri(context!!, binding.addImage.drawToBitmap())
                    .toString(),
                date = date
            )
        }
    }
}

