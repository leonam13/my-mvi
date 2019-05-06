package com.example.mymvi.data

import com.example.mymvi.mvi.Flag
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class FlagRepository(private val flagDataState: FlagDataSource) {

    fun getFlags(code: String?): Observable<List<Flag>> =
        //Observable.error(NullPointerException("Lista Vazia"))
        Observable.just(
            code?.let { flagDataState.flags.filter { it.code == code } }
                ?: flagDataState.flags
        ).delay(3, TimeUnit.SECONDS)

    fun removeAllFlags(): Observable<List<Flag>> =
        Observable.just(emptyList<Flag>()).delay(3, TimeUnit.SECONDS)
}