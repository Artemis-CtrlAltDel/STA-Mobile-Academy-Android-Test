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
            rationale = "Can not launch the camera unless you grant it permission",
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
            rationale = "Can not dial the call unless you grant it permission",
            requestCode = Constants.CAMERA_REQUEST_CODE,
            perms = arrayOf(Manifest.permission.CALL_PHONE)
        )
}