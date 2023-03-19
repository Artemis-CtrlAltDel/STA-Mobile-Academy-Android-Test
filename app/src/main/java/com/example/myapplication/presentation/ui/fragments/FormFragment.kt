package com.example.myapplication.presentation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFormBinding
import com.example.myapplication.databinding.ImageModalBottomSheetBinding
import com.example.myapplication.other.PermsUtils
import com.example.myapplication.other.UiUtils
import com.example.myapplication.other.Utils
import com.example.myapplication.presentation.viewmodels.SharedViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class FormFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var imageFromCamera: ActivityResultLauncher<Intent>
    private lateinit var imageFromGallery: ActivityResultLauncher<String>

    private lateinit var imageIntent: Intent
    private var imageUri: Uri? = null

    private lateinit var modalBottomSheetBinding: ImageModalBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFormBinding.inflate(layoutInflater, container, false)

        modalBottomSheetBinding = ImageModalBottomSheetBinding.inflate(layoutInflater)

        imageFromGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) {
                imageUri = it
                bindPreviews()
            }

        imageFromCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                bindPreviews()
            }

        imageIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        sharedViewModel.onFormValidated = { user ->

            sharedViewModel.insertUser(user.apply { image = imageUri })

            Navigation.findNavController(binding.root)
                .navigate(FormFragmentDirections.actionFormFragmentToListFragment())
        }

        bindViews()
        handleActions()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViews() {
        bindErrors()
        bindPreviews()
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
            UiUtils.showBottomSheetDialog(
                requireActivity(),
                modalBottomSheetBinding
            )
        }

        modalBottomSheetBinding.fromCamera.setOnClickListener {

            PermsUtils.requestCameraPermission(this)

            if (PermsUtils.hasCameraPermission(requireContext())) {
                imageIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Utils.prepareImageUri(requireContext())
                )

                imageFromCamera.launch(imageIntent)
            }
        }

        modalBottomSheetBinding.fromGallery.setOnClickListener {

            PermsUtils.requestGalleryPermission(this)

            if (PermsUtils.hasGalleryPermission(requireContext())) {
                imageFromGallery.launch("image/*")
            }
        }
    }

    private fun bindErrors() {
        sharedViewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.includeError.errorWrapper.isVisible = it.isNotBlank()
            binding.includeError.error.text = it.trim()
        }
    }

    private fun bindPreviews() {
        binding.includeAbout.imagePreview.isVisible = imageUri.toString().isNotBlank()
        binding.includeAbout.imagePreview.setImageURI(imageUri)
    }

    /** Perms **/
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        bindPreviews()

        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            PermsUtils.requestCameraPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        bindPreviews()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}