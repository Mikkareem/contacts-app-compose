package com.techullurgy.contactsapp.data.remote

import com.techullurgy.contactsapp.data.remote.responses.RemoteContactResponse
import com.techullurgy.contactsapp.data.utils.Endpoint
import com.techullurgy.contactsapp.data.utils.ServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class RemoteContactsApiImpl(
    private val client: HttpClient
): RemoteContactsApi {
    override suspend fun getPaginatedContacts(page: Int): ServiceResult<RemoteContactResponse> = withContext(Dispatchers.IO) {
        try {
            val response = client.get(
                Endpoint.GetPagedContacts(page).getPath()
            )
            when(response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<RemoteContactResponse>()
                    ServiceResult.Success(data)
                }

                HttpStatusCode.NotFound -> ServiceResult.Failure("Requested Url not found")

                HttpStatusCode.InternalServerError -> ServiceResult.Failure("Something went wrong from our side, you Don't worry, Try again after sometime")

                HttpStatusCode.BadRequest -> ServiceResult.Failure("Bad Request")

                else -> ServiceResult.Failure("Unknown Error")
            }
        } catch(e: Exception) {
            ServiceResult.Failure(
                e.message ?: "Seems network is missing, Please check your Internet Connection"
            )
        }
    }
}