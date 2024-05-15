package com.techullurgy.contactsapp.data.utils

// https://randomuser.me/api?seed=contact&page=2&results=5&inc=gender,name,picture,phone,cell,id,email

sealed class Endpoint(
    private val endpoint: String
) {
    class GetPagedContacts(page: Int, count: Int = 25): Endpoint("page=$page&results=$count&inc=gender,name,picture,phone,cell,id,email")

    companion object {
        private const val BASE_URL = "https://randomuser.me/api"
    }

    fun getPath() = "$BASE_URL?seed=contact&$endpoint"
}
