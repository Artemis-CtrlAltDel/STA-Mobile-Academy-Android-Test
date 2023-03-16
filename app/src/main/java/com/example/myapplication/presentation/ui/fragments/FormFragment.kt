package com.example.myapplication.presentation.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.myapplication.presentation.viewmodels.SharedViewModel
import com.example.myapplication.databinding.FragmentFormBinding
import com.example.myapplication.other.Constants
import com.example.myapplication.other.PermsUtils
import com.example.myapplication.presentation.fragments.FirstFragmentDirections
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class FormFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var cameraIntent: Intent
    private var cameraBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormBinding.inflate(layoutInflater, container, false)

        cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        sharedViewModel.onFormValidated = { user ->

            sharedViewModel.insertUser(user)
            Navigation.findNavController(binding.root)
                .navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment())
        }

        bindViews()
        bindErrors()
        handleActions()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViews() {
        binding.includeAbout.imageUri.isVisible = false
        binding.includeError.errorWrapper.isVisible = false
    }

    private fun handleActions() {
        binding.fab.setOnClickListener {

            bindErrors()

            sharedViewModel.validateForm(
                fname = binding.includePersonal.fname.text.toString().trim().trim(),
                lname = binding.includePersonal.lname.text.toString().trim(),
                email = binding.includePersonal.email.text.toString().trim(),
                phone = binding.includePersonal.phone.text.toString().trim(),
                fax = binding.includePersonal.fax.text.toString().trim(),
                country = binding.includeAbout.country.text.toString().trim(),
                city = binding.includeAbout.city.text.toString().trim(),
                job = binding.includeAbout.job.text.toString().trim(),
                bio = binding.includeAbout.bio.text.toString().trim()
            )
        }

        binding.includeAbout.image.setOnClickListener {
            PermsUtils.requestCameraPermission(this)
            if (PermsUtils.hasCameraPermission(requireContext())) {
                startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST_CODE)
            }
        }
    }

    private fun bindErrors() {
        sharedViewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.includeError.errorWrapper.isVisible = !it.isNullOrBlank()
            binding.includeError.error.text = it.trim()
        }
    }

    private fun togglePreviewVisibility() {
        binding.includeAbout.imageUri.isVisible = PermsUtils.hasCameraPermission(requireContext())
        binding.includeAbout.imageUri.text = cameraBitmap.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            cameraBitmap = data?.extras?.get("data") as Bitmap
            binding.includeAbout.imageUri.text = cameraBitmap.toString()
        }
    }

    /** Perms **/
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        togglePreviewVisibility()

        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            PermsUtils.requestCameraPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        togglePreviewVisibility()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}