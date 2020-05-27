package com.shevelev.wizard_camera.gallery_activity.dto

import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand

data class ShareShotCommand(val shot: PhotoShot):  ViewCommand