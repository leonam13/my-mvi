package com.example.mymvi.mvibase

import io.reactivex.Observable

/**
 * View model should process user intents declared in View
 * and generate the correspondent states to it.
 */
interface MviViewModel<I : MviIntent, S : MviState> {

    fun processIntents(intents: Observable<I>)

    fun states(): Observable<S>
}