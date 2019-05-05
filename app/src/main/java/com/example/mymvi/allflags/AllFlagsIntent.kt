package com.example.mymvi.allflags

import com.example.mymvi.mvibase.MviIntent

/**
 * Describe all the user intents of this screen.
 *
 * Objects for events and Data classes for data.
 **/

sealed class AllFlagsIntent : MviIntent {
    object LoadAllFlagsIntent : AllFlagsIntent()
    object ClearAllFlagsIntent : AllFlagsIntent()
}