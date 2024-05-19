package com.techullurgy.contactsapp.presentation.navigation

sealed class Screen(val name: String) {
    data object Initial: Screen("initial")
    data object GlobalSearch: Screen("global-search")
    data object RandomList: Screen("random-list")
    data object DeviceList: Screen("device-list")

    class RandomDetail(
        id: Long
    ): Screen(ROUTE) {
        val url = "random-detail/$id"

        companion object {
            // PARAMS
            const val ID = "id"

            const val ROUTE = "random-detail/{$ID}"
        }
    }

    class DeviceDetail(
        id: String
    ): Screen(ROUTE) {
        val url = "device-detail/$id"

        companion object {
            // PARAMS
            const val ID = "id"

            const val ROUTE = "device-detail/{$ID}"
        }
    }

    data object AddRandom: Screen("add-random")
    data object AddDevice: Screen("add-device")

    class EditRandom(
        id: Long
    ): Screen(ROUTE) {
        val url = "edit-random/$id"

        companion object {
            // PARAMS
            const val ID = "id"

            const val ROUTE = "edit-random/{$ID}"
        }
    }

    class EditDevice(
        id: String
    ): Screen(ROUTE) {
        val url = "edit-device/$id"

        companion object {
            // PARAMS
            const val ID = "id"

            const val ROUTE = "edit-device/{$ID}"
        }
    }
}