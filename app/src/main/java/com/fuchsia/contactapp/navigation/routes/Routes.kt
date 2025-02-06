package com.fuchsia.contactapp.navigation.routes

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    object HomeScreen : Routes()
    @Serializable
    data class AddEditScreen(val id: Int? = null) : Routes()
}