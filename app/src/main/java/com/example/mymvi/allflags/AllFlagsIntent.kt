package com.example.mymvi.allflags

import com.example.mymvi.mvibase.MviIntent

/**
 * Describe all the user intents of this screen.
 *
 * Objects for events and Data classes for data.
 **/

sealed class AllFlagsIntent : MviIntent {
    data class LoadAllFlagsIntent(val code: String? = null) : AllFlagsIntent()
    object ClearAllFlagsIntent : AllFlagsIntent()
}