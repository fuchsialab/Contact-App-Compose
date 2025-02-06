package com.fuchsia.contactapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuchsia.contactapp.data.entities.Contact
import com.fuchsia.contactapp.data.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(
    val repository: ContactRepository
) : ViewModel() {

    val _state = MutableStateFlow<AppState>(AppState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getContacts().collect { value ->
                _state.value = AppState.Data(value)
            }

        }

    }
    fun upsertContact(contact: Contact) = viewModelScope.launch {
        repository.upsertContact(contact)
    }
    fun deleteContact(contact: Contact) = viewModelScope.launch {
        repository.deleteContact(contact)
    }
}

sealed class AppState {
    data class Data(val data: List<Contact>) : AppState()
    object Loading : AppState()

}