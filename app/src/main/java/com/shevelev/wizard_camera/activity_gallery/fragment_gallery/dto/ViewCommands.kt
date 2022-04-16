package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto

import android.net.Uri
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_commands.ViewCommand

class ShareShotCommand(val contentUri: Uri):  ViewCommand

class EditShotCommand(val shot: PhotoShot):  ViewCommand