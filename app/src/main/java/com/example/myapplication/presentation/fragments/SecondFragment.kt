package com.example.myapplication.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSecondBinding
import com.example.myapplication.other.PermsUtils
import com.example.myapplication.presentation.viewmodels.SharedViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class SecondFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var callIntent: Intent

    private lateinit var emailIntent: Intent
    private lateinit var emailChooser: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(layoutInflater, container, false)

        bindViews()
        handleActions()


        callIntent =
            Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${binding.phone.text}") }

        emailIntent = Intent(Intent.ACTION_SEND)

        emailChooser = Intent.createChooser(
            emailIntent.apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, binding.email.text)
            },
            getString(R.string.fragment_2_intent_chooser_title, binding.email.text)
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViews() = with(binding) {

        sharedViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            it.image?.let { bitmap ->
                Glide.with(requireContext()).load(bitmap).into(image)
            } ?: run { Glide.with(requireContext()).load(R.drawable.img).into(image) }
            name.text = getString(
                R.string.fragment_2_name,
                it.fname, it.lname
            )
            country.text = getString(
                R.string.fragment_2_country,
                it.country
            )
            bio.text = getString(
                R.string.fragment_2_bio,
                it.bio
            )
            nameQuote.text = getString(
                R.string.fragment_2_name_quoted,
                it.fname, it.lname
            )
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
        binding.phone.setOnClickListener {

            PermsUtils.requestCallPermission(this)

            if (PermsUtils.hasCallPermission(requireContext())) {
                startActivity(callIntent)
            }
        }

        binding.email.setOnClickListener {
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