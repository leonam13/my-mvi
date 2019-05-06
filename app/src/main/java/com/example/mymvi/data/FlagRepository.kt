package com.example.mymvi.data

import com.example.mymvi.mvi.Flag
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

interface FlagRepository {
    fun getFlags(code: String?): Observable<List<Flag>>
    fun removeAllFlags(): Observable<List<Flag>>
}

class FlagRepositoryImpl(private val flagDataState: FlagDataSource) : FlagRepository {

    override fun getFlags(code: String?): Observable<List<Flag>> =
        //Observable.error(NullPointerException("Lista Vazia"))
        Observable.just(
            code?.let { flagDataState.flags.filter { it.code == code } }
                ?: flagDataState.flags
        ).delay(2, TimeUnit.SECONDS)

    override fun removeAllFlags(): Observable<List<Flag>> =
        Observable.just(emptyList<Flag>()).delay(3, TimeUnit.SECONDS)
}