package com.example.myapplication.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.other.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    /** data sharing **/

    var userDetails = MutableLiveData<User>(null)
        private set

    /** database calling **/

    var userList = Pager(config = PagingConfig(Constants.PAGING_SIZE)) {
        repository.getUserList()
    }.liveData.cachedIn(viewModelScope)
        private set

    fun getUser(id: Long) {
        userDetails.value = repository.getUser(id)
    }

    fun insertUser(user: User) = repository.insertUser(user)
    fun deleteUser(vararg user: User) = repository.deleteUser(*user)
    fun truncate() = repository.truncate()


    /** form validation **/

    var errorMessage = MutableLiveData("")
        private set

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
        onFormValidated(User(fname, lname, email, phone, fax, country, city, job, bio).also {
            it.joinedTimestamp = Calendar.getInstance().timeInMillis
        })
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

        val isPersonalValid = validatePersonalData(fname, lname, email, phone, fax)
        val isAdditionalValid = validateAdditionalData(country, city, job, bio)

        return isPersonalValid && isAdditionalValid
    }

    private fun validateAdditionalData(
        country: String,
        city: String,
        job: String,
        bio: String
    ): Boolean {

        var isValid = true

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

        return isValid
    }

    private fun validatePersonalData(
        fname: String,
        lname: String,
        email: String,
        phone: String,
        fax: String
    ): Boolean {

        var isValid = true

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

        return isValid
    }

    private fun clearErrors() {
        errorMessage.value = ""
    }
}