package com.example.mymvi.allflags

import com.example.mymvi.mvi.Flag
import com.example.mymvi.mvibase.MviResult

/**
 * Each action should produce a result,
 * it result has an state, so we create
 * nested sealed classes representing the possible
 * states while getting each result.

 * Objects for events and Data classes for data.
 **/

sealed class AllFlagsResult : MviResult {

    sealed class LoadAllFlagsResult() : AllFlagsResult() {
        object Loading : LoadAllFlagsResult()
        data class Success(val flags: List<Flag>) : LoadAllFlagsResult()
        data class Error(val error: Throwable) : LoadAllFlagsResult()
    }

    sealed class ClearAllFlagsResult : AllFlagsResult() {
        object Loading : ClearAllFlagsResult()
        data class Success(val emptyList: List<Flag>) : ClearAllFlagsResult()
        data class Error(val error: Throwable) : ClearAllFlagsResult()
    }
}