package com.example.myapplication.presentation.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.pojo.User

class SharedViewModel : ViewModel() {

    /** data sharing **/

    var userDetails = MutableLiveData<User>(null)
        private set

    init {
        refresh()
    }

    fun setUser(value: User) {
        userDetails.value = value
    }

    fun refresh() = userDetails


    /** form validation **/

    var errorMessage = MutableLiveData("")
        private set

    private var isValid = true

    var onFormValidated: (value: User) -> Unit = {}

    fun validateForm(
        fname: String,
        lname: String,
        email: String,
        phone: String,
        fax: String,
        country: String,
        city: String,
        job: String,
        bio: String

    ) {
        if (!isFormValid(fname, lname, email, phone, fax, country, city, job, bio)) return

        clearErrors()
        onFormValidated(User(fname, lname, email, phone, fax, country, city, job, bio))
    }

    private fun isFormValid(
        fname: String,
        lname: String,
        email: String,
        phone: String,
        fax: String,
        country: String,
        city: String,
        job: String,
        bio: String

    ): Boolean {
        clearErrors()

        validatePersonalData(fname, lname, email, phone, fax)
        validateAdditionalData(country, city, job, bio)

        return isValid
    }

    private fun validateAdditionalData(
        country: String,
        city: String,
        job: String,
        bio: String
    ) {

        if (country.isBlank() ||
            city.isBlank() ||
            job.isBlank() ||
            bio.isBlank()
        ) {
            isValid = false
        }

        if (country.isBlank()) {
            errorMessage.value += "Country is missing\n"
        }
        if (city.isBlank()) {
            errorMessage.value += "City is missing\n"
        }
        if (job.isBlank()) {
            errorMessage.value += "Job is missing\n"
        }
        if (bio.isBlank()) {
            errorMessage.value += "Bio is missing\n"
        }
    }

    private fun validatePersonalData(
        fname: String,
        lname: String,
        email: String,
        phone: String,
        fax: String
    ) {

        if (fname.isBlank() ||
            lname.isBlank() ||
            email.isBlank() ||
            phone.isBlank() ||
            fax.isBlank()
        ) {
            isValid = false
        }

        if (fname.isBlank()) {
            errorMessage.value += "First name is missing\n"
        }
        if (lname.isBlank()) {
            errorMessage.value += "Last name is missing\n"
        }
        if (email.isBlank()) {
            errorMessage.value += "Email address is missing\n"
        }
        if (phone.isBlank()) {
            errorMessage.value += "Phone number is missing\n"
        }
        if (fax.isBlank()) {
            errorMessage.value += "Fax is missing\n"
        }
    }

    private fun clearErrors() {
        errorMessage.value = ""
    }
}