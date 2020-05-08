package com.jd.livedatacalladapter

import retrofit2.Response

// Generic Wrapper Class
@Suppress("unused")
sealed class ApiResponse<T> {

    companion object {

        fun <T> create(throwable: Throwable): ApiResponse<T> {
            return ApiErrorResponse(throwable.message ?: "Unknown Error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            if (response.isSuccessful) {

                var body = response.body()
                return if (body == null || response.code() == 204) {
                    return ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body = body)
                }

            } else {

                return ApiErrorResponse(
                    response.errorBody()?.string() ?: response.message().toString()
                )
            }
        }
    }


    class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()
    class ApiErrorResponse<T>(val error: String) : ApiResponse<T>()
    class ApiEmptyResponse<T>() : ApiResponse<T>()

}