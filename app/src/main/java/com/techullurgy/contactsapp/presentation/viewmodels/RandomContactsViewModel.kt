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
        _state.update {
            it.copy(
                pageLoading = true
            )
        }
        viewModelScope.launch {
            contactsRepository.getRandomContacts().collectLatest { contacts ->
                if(contacts.isEmpty()) {
                    fetchAllContacts()
                } else {
                    _state.update {
                        it.copy(
                            pageLoading = false,
                            contacts = contacts.map { ct -> ct.toRandomContact() },
                            pageError = ""
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
                            pageLoading = false,
                            pageError = ""
                        )
                    }
                }
                is ServiceResult.Failure -> {
                    _state.update {
                        it.copy(
                            pageLoading = false,
                            pageError = result.error
                        )
                    }
                }
            }
        }
    }

    fun onLoadMore() {
        _state.update {
            it.copy(
                loadMoreLoading = true
            )
        }
        viewModelScope.launch {
            val nextPage = contactsRepository.getLastAvailablePage() + 1
            when (val result = contactsRepository.getPaginatedRandomContacts(nextPage)) {
                is ServiceResult.Success -> {
                    _state.update {
                        it.copy(
                            error = "",
                            loadMoreLoading = false
                        )
                    }
                }

                is ServiceResult.Failure -> {
                    _state.update {
                        it.copy(
                            error = result.error,
                            loadMoreLoading = false
                        )
                    }
                }
            }
        }
    }
}