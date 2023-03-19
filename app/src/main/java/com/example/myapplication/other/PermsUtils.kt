package com.example.myapplication.other

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions

object PermsUtils {

    fun hasCameraPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context = context,
            perms = arrayOf(Manifest.permission.CAMERA)
        )

    fun requestCameraPermission(host: Fragment) =
        EasyPermissions.requestPermissions(
            host = host,
            rationale = "Unable to launch the camera unless you grant it permission",
            requestCode = Constants.CAMERA_REQUEST_CODE,
            perms = arrayOf(Manifest.permission.CAMERA)
        )

    fun hasCallPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context = context,
            perms = arrayOf(Manifest.permission.CALL_PHONE)
        )

    fun requestCallPermission(host: Fragment) =
        EasyPermissions.requestPermissions(
            host = host,
            rationale = "Unable to place the call unless you grant it permission",
            requestCode = Constants.CALL_REQUEST_CODE,
            perms = arrayOf(Manifest.permission.CALL_PHONE)
        )

    fun hasGalleryPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context = context,
            perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        )

    fun requestGalleryPermission(host: Fragment) {
        EasyPermissions.requestPermissions(
            host = host,
            rationale = "Unable to access your gallery unless you grant it permission",
            requestCode = Constants.GALLERY_REQUEST_CODE,
            perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        )
    }
}