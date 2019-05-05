package com.example.mymvi.allflags

import com.example.mymvi.mvi.Flag
import com.example.mymvi.mvibase.MviState

/**
 * We identify and map all the proprieties
 * that we need update on view in each state.
 */
data class AllFlagsState(
    val isLoading: Boolean,
    val flags: List<Flag>,
    val errorMessage: Throwable?
) : MviState {
    companion object {
        //Generate a default state (ex: when opening the app)
        fun default() = AllFlagsState(
            isLoading = false,
            flags = emptyList(),
            errorMessage = null
        )
    }
}