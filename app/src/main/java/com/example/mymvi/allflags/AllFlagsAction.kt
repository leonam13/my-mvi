package com.example.mymvi.allflags

import com.example.mymvi.mvibase.MviAction

/**
 * Each user intent has a corresponding actions.

 * Objects for events and Data classes for data.
 **/

sealed class AllFlagsAction : MviAction {
    data class LoadAllFlagsAction(val code: String? = null) : AllFlagsAction()
    object ClearAllFlagsAction : AllFlagsAction()
}