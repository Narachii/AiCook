package org.example.project.entity

sealed class Result<out T : Any> {
    data object Loading : Result<Nothing>()
    data class Success <out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: String) : Result<Nothing>()
}