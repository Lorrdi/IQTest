package com.lorrdi.iqtest.data.dto

sealed class ErrorState {
    object NetworkError : ErrorState()
    data class ServerError(val code: Int) : ErrorState()
    data class UnknownError(val message: String) : ErrorState()
}