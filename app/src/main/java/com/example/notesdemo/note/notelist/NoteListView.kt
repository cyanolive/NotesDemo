package com.example.notesdemo.note.notelist

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notesdemo.R
import com.example.notesdemo.common.makeToast
import com.example.notesdemo.note.notelist.buildlogic.NoteListInjector
import kotlinx.android.synthetic.main.fragment_note_list.*

class NoteListView : Fragment() {

    private lateinit var viewModel: NoteListViewModel
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rec_list_fragment.adapter = null
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            NoteListInjector(requireActivity().application).provideNoteListViewModelFactory()
        ).get(NoteListViewModel::class.java)

        (imv_space_background.drawable as AnimationDrawable).start()

        showLoadingState()
        setUpAdapter()
        observeViewModel()
        setUpClickListener()

        viewModel.handleEvent(NoteListEvent.OnStart)
    }

    private fun showLoadingState() = (imv_satellite_animation.drawable as AnimationDrawable).start()

    private fun setUpAdapter() {
        adapter = NoteListAdapter()
        adapter.event.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.handleEvent(it)
            }
        )
        rec_list_fragment.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.error.observe(
            viewLifecycleOwner,
            Observer {
                showErrorState(it)
            }
        )

        viewModel.notelist.observe(
            viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
                if (it.isNotEmpty()) {
                    (imv_satellite_animation.drawable as AnimationDrawable).stop()
                    imv_satellite_animation.visibility = View.INVISIBLE
                    rec_list_fragment.visibility = View.VISIBLE
                }
            }
        )

        viewModel.editNote.observe(
            viewLifecycleOwner,
            Observer {
                startNoteDetailView(it)
            }
        )
    }

    private fun startNoteDetailView(creationDate: String) {
        findNavController().navigate(
            NoteListViewDirections.actionNoteListViewToNoteDetailView(
                creationDate
            )
        )
    }

    private fun showErrorState(errorMessage: String) = makeToast(errorMessage)

    private fun setUpClickListener() {
        fab_create_new_item.setOnClickListener {
            startNoteDetailView("")
        }

        imv_toolbar_auth.setOnClickListener {
            findNavController().navigate(NoteListViewDirections.actionNoteListViewToLoginActivity())
        }
    }
}