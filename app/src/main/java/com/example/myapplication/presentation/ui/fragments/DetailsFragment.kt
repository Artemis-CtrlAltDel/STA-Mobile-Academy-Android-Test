package com.example.myapplication.presentation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.databinding.FragmentDetailsBinding
import com.example.myapplication.other.PermsUtils
import com.example.myapplication.other.loadImage
import com.example.myapplication.presentation.viewmodels.SharedViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class DetailsFragment : Fragment(), EasyPermissions.PermissionCallbacks, OnClickListener {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val navArgs: DetailsFragmentArgs by navArgs()

    private lateinit var callIntent: Intent
    private var fax: String? = null
    private var phone: String? = null

    private lateinit var emailIntent: Intent
    private lateinit var emailChooser: Intent
    private var emailAddress: String? = null

    private lateinit var mapsIntent: Intent
    private var mapsUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)

        sharedViewModel.getUser(navArgs.userId)

        emailIntent = Intent(Intent.ACTION_SEND)

        callIntent = Intent(Intent.ACTION_DIAL)

        mapsIntent = Intent(Intent.ACTION_VIEW).setPackage("com.google.android.apps.maps")

        bindViews()
        handleActions()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViews() =
        sharedViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            mapsUri = Uri.parse("geo:0,0?q=${it.city}, ${it.country}")
            emailAddress = it.email
            phone = it.phone
            fax = it.fax

            with(binding.includePrimaryDetails) {

                toggleDetailsVisibility(it, country, jop, dotSep)

                image.loadImage(requireContext(), avatar = it.avatar, it.image, R.drawable.img)
                name.text = getString(
                    R.string.fragment_2_name,
                    it.fname, it.lname
                )
                jop.text = getString(
                    R.string.fragment_2_job,
                    it.job
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
                        ?: "Hi, my name is ${it.fname} ${it.lname}\nFrom an API (reqres.in/api)\nThat's pretty much about it."
                )
                nameQuote.text = getString(
                    R.string.fragment_2_name_quoted,
                    it.fname, it.lname
                )
            }

            with(binding.includeContact) {

                toggleDetailsVisibility(it, phone, fax, city)

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

    private fun toggleDetailsVisibility(user: User, vararg view: View) {
        view.forEach {
            it.isVisible = user.isLocal
        }
    }

    private fun handleActions() {

        binding.includeContact.phone.setOnClickListener(this)
        binding.includeContact.fax.setOnClickListener(this)

        binding.includeContact.email.setOnClickListener {
            emailChooser = Intent.createChooser(
                emailIntent.apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, emailAddress)
                },
                getString(R.string.fragment_2_intent_chooser_title, emailAddress)
            )
            startActivity(emailChooser)
        }

        binding.includeContact.city.setOnClickListener {
            startActivity(mapsIntent.also { it.data = mapsUri })
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            PermsUtils.requestCallPermission(this)
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

    override fun onClick(p0: View?) {
        PermsUtils.requestCallPermission(this)

        if (PermsUtils.hasCallPermission(requireContext())) {
            startActivity(callIntent.also {
                it.data =
                    if (p0?.id == R.id.fax) Uri.parse("tel:$fax")
                    else Uri.parse("tel:$phone")
            })
        }
    }
}