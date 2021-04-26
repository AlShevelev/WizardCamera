package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view

import android.os.Bundle
import android.view.View
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.di.EditorFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.databinding.FragmentEditorBinding
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBaseMVVM
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand

class EditorFragment : FragmentBaseMVVM<FragmentEditorBinding, EditorFragmentViewModel>() {
    override fun provideViewModelType(): Class<EditorFragmentViewModel> = EditorFragmentViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_editor

    override fun linkViewModel(binding: FragmentEditorBinding, viewModel: EditorFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun inject() {
        App.injections.get<EditorFragmentComponent>().inject(this)

    }

    override fun releaseInjection() {
        App.injections.release<EditorFragmentComponent>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.shareButton.setOnClickListener { viewModel.onShareShotClick(binding.galleryPager.currentItem) }
    }

    override fun processViewCommand(command: ViewCommand) {
//        when(command) {
//            is ShareShotCommand -> shareShot(command.shot)
//            is EditShotCommand -> findNavController().navigate(R.id.action_galleryFragment_to_editorFragment)
//        }
    }
}