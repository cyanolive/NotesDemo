package com.example.notesdemo.note.notedetail

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
import com.example.notesdemo.common.startWithFade
import com.example.notesdemo.common.toEditable
import com.example.notesdemo.note.notedetail.buildlogic.NoteDetailInjector
import kotlinx.android.synthetic.main.fragment_note_detail.*

class NoteDetailView : Fragment() {
    private lateinit var viewModel: NoteDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel =
            ViewModelProvider(
                this,
                NoteDetailInjector(requireActivity().application).provideNoteDetailViewModelFactory()
            ).get(NoteDetailViewModel::class.java)

        showLoadingState()
        observeViewModel()
        setUpClickListener()

        (frag_note_detail.background as AnimationDrawable).startWithFade()

        viewModel.handleEvent(NoteDetailEvent.OnStart(NoteDetailViewArgs.fromBundle(arguments!!).creationDate))
    }

    private fun observeViewModel() {
        viewModel.error.observe(
            viewLifecycleOwner,
            Observer {
                showErrorState(it)
            }
        )

        viewModel.note.observe(
            viewLifecycleOwner,
            Observer {
                lbl_note_detail_date.text = it.creationDate
                edt_note_detail_text.text = it.contents.toEditable()
            }
        )

        viewModel.confirmed.observe(
            viewLifecycleOwner,
            Observer {
                findNavController().navigate(R.id.noteListView)
            }
        )

        viewModel.deleted.observe(
            viewLifecycleOwner,
            Observer {
                findNavController().navigate(R.id.noteListView)
            }
        )
    }

    private fun showErrorState(errorMessage: String?) {
        makeToast(errorMessage!!)
        findNavController().navigate(R.id.noteListView)
    }

    private fun setUpClickListener() {
        imb_toolbar_done.setOnClickListener {
            viewModel.handleEvent(NoteDetailEvent.OnConfirm(edt_note_detail_text.text.toString()))
        }

        imb_toolbar_delete.setOnClickListener {
            viewModel.handleEvent(NoteDetailEvent.OnDelete)
        }
    }

    private fun showLoadingState() {
        (imv_note_detail_satellite.drawable as AnimationDrawable).start()
    }
}