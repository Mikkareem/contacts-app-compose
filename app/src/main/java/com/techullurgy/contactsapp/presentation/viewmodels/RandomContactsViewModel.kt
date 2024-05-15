package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.contactsapp.data.utils.ServiceResult
import com.techullurgy.contactsapp.domain.ContactsRepository
import com.techullurgy.contactsapp.domain.mappers.toRandomContact
import com.techullurgy.contactsapp.presentation.models.RandomContactsListScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomContactsViewModel(
    private val contactsRepository: ContactsRepository
): ViewModel() {

    private val _state = MutableStateFlow(RandomContactsListScreenState())
    val state: StateFlow<RandomContactsListScreenState> get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            contactsRepository.getRandomContacts().collectLatest { contacts ->
                if(contacts.isEmpty()) {
                    fetchAllContacts()
                } else {
                    _state.update {
                        it.copy(
                            contacts = contacts.map { ct -> ct.toRandomContact() },
                            error = ""
                        )
                    }
                }
            }
        }
    }

    private fun fetchAllContacts() {
        viewModelScope.launch {
            when(val result = contactsRepository.getPaginatedRandomContacts(1)) {
                is ServiceResult.Success -> {
                    _state.update {
                        it.copy(
                            error = ""
                        )
                    }
                }
                is ServiceResult.Failure -> {
                    _state.update {
                        it.copy(
                            error = result.error
                        )
                    }
                }
            }
        }
    }

    fun onLoadMore() {

    }
}