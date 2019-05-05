package com.example.mymvi.mvibase

import io.reactivex.Observable

/**
 * View should generate all possible user intents
 * and every time state is changed, should render to match it.
 */
interface MviView<I : MviIntent, in S : MviState> {

    fun render(state: S)

    fun intents(): Observable<I>
}