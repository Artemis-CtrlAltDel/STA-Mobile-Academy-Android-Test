package com.example.myapplication.other

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions

object PermsUtils {

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
}