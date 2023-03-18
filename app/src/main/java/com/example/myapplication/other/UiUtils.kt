package com.example.myapplication.other

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isNotEmpty
import com.example.myapplication.databinding.ImageModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

object UiUtils {

    fun toggleNightMode() =
        AppCompatDelegate.setDefaultNightMode(
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )

    fun showBottomSheetDialog(context: Context, binding: ImageModalBottomSheetBinding) {

        if (binding.root.parent != null) {
            (binding.root.parent as ViewGroup).removeView(binding.root)
        }

        val dialog = BottomSheetDialog(context).apply { setContentView(binding.root) }
        dialog.show()
    }
}