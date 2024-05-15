package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.contactsapp.data.utils.ServiceResult
import com.techullurgy.contactsapp.domain.ContactsRepository
import com.techullurgy.contactsapp.presentation.models.RandomContactDetailScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomContactDetailViewModel(
    private val contactsRepository: ContactsRepository
): ViewModel() {

    private val _state = MutableStateFlow(RandomContactDetailScreenState())
    val state = _state.asStateFlow()

    fun getContactDetail(contactId: Long) {
        viewModelScope.launch {
            val result = contactsRepository.getRandomContactDetailFor(contactId = contactId)
            _state.update {
                it.copy(
                    randomContact = result,
                    error = ""
                )
            }
        }
    }
}