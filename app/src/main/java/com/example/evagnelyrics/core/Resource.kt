package com.example.evagnelyrics.core

sealed class Resource<T>(data: T? = null, msg: String? = null) {
    data class Success<T>(val data: T? = null) : Resource<T>(data)
    data class Error<T>(val msg: String) : Resource<T>(msg = msg)
}
