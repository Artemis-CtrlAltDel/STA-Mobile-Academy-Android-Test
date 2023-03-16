package com.example.myapplication.presentation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailsBinding
import com.example.myapplication.other.PermsUtils
import com.example.myapplication.presentation.viewmodels.SharedViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class DetailsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var callIntent: Intent

    private lateinit var emailIntent: Intent
    private lateinit var emailChooser: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)

        sharedViewModel.getUser(
            fname = requireActivity().intent.getStringExtra("user-details-fname").toString(),
            lname = requireActivity().intent.getStringExtra("user-details-lname").toString(),
        )

        bindViews()
        handleActions()

        callIntent =
            Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${binding.includeContact.phone.text}")
            }

        emailIntent = Intent(Intent.ACTION_SEND)

        emailChooser = Intent.createChooser(
            emailIntent.apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, binding.includeContact.email.text)
            },
            getString(R.string.fragment_2_intent_chooser_title, binding.includeContact.email.text)
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViews() =
        sharedViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            with(binding.includePrimaryDetails) {
                it.image?.let { bitmap ->
                    Glide.with(requireContext()).load(bitmap).into(image)
                } ?: run {
                    Glide.with(requireContext()).load(R.drawable.img).into(image)
                }
                name.text = getString(
                    R.string.fragment_2_name,
                    it.fname, it.lname
                )
                country.text = getString(
                    R.string.fragment_2_country,
                    it.country
                )
            }

            with(binding.includeSecondaryDetails) {
                bio.text = getString(
                    R.string.fragment_2_bio,
                    it.bio
                )
                nameQuote.text = getString(
                    R.string.fragment_2_name_quoted,
                    it.fname, it.lname
                )
            }

            with(binding.includeContact) {
                phone.text = getString(
                    R.string.fragment_2_phone,
                    it.phone
                )
                fax.text = getString(
                    R.string.fragment_2_fax,
                    it.fax
                )
                email.text = getString(
                    R.string.fragment_2_email,
                    it.email
                )
                city.text = getString(
                    R.string.fragment_2_city,
                    it.city
                )
            }

        }

    private fun handleActions() {
        binding.includeContact.phone.setOnClickListener {

            PermsUtils.requestCallPermission(this)

            if (PermsUtils.hasCallPermission(requireContext())) {
                startActivity(callIntent)
            }
        }

        binding.includeContact.email.setOnClickListener {
            startActivity(emailChooser)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            PermsUtils.requestCameraPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        //
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