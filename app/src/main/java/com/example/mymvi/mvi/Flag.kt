package com.example.mymvi.mvi

sealed class FlagState {
    object LoadingState : FlagState()
    data class DataState(val flags: List<Flag>) : FlagState()
    data class ErrorState(val message: String?) : FlagState()
    object FinishState : FlagState()
}

data class Flag(
    val id: Int,
    val name: String,
    val url: String
)

