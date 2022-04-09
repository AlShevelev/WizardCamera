package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions

import android.content.Context
import android.content.Intent

abstract class ExternalActionHelperBase {
    protected fun checkIntent(intent: Intent, context: Context): Boolean {
        return intent.resolveActivity(context.packageManager) != null
    }
}