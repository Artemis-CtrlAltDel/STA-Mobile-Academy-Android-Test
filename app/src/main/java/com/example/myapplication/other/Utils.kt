package com.example.myapplication.other

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

object Utils {

    fun createImageSourceChooser(context: Context, tempFile: File): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        camIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileProvider.getUriForFile(
                context, context.packageName + ".provider",
                tempFile
            )
        )
        val chooser = Intent.createChooser(camIntent, "Select Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intent))

        return chooser
    }

    fun isValidEmail(email: String) = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+").matches(email)
    fun isValidPhoneFax(number: String) = Regex("^(\\+212|\\(212\\)|0)\\s?[5-7][0-9]{8}\$").matches(number)
}