package com.example.mymvi.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymvi.allflags.AllFlagsAction.ClearAllFlagsAction
import com.example.mymvi.allflags.AllFlagsAction.LoadAllFlagsAction
import com.example.mymvi.allflags.AllFlagsIntent
import com.example.mymvi.allflags.AllFlagsIntent.ClearAllFlagsIntent
import com.example.mymvi.allflags.AllFlagsIntent.LoadAllFlagsIntent
import com.example.mymvi.allflags.AllFlagsProcessorHolder
import com.example.mymvi.allflags.AllFlagsResult
import com.example.mymvi.allflags.AllFlagsResult.ClearAllFlagsResult
import com.example.mymvi.allflags.AllFlagsResult.LoadAllFlagsResult
import com.example.mymvi.allflags.AllFlagsState
import com.example.mymvi.mvibase.MviViewModel
import com.raywenderlich.android.creaturemon.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

/**
 * Manage all the cycle here.
 *
 * To transform results in states that view will use to be rendered,
 * we create reducer functions to generate this states based on previous one.
 */
class AllFlagsViewModel(private val processor: AllFlagsProcessorHolder) :
    ViewModel(), MviViewModel<AllFlagsIntent, AllFlagsState> {

    private val intentsSubject: PublishSubject<AllFlagsIntent> = PublishSubject.create()
    private val statesObservable: Observable<AllFlagsState> = compose()

    private val intentFilter: ObservableTransformer<AllFlagsIntent, AllFlagsIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { i ->
                Observable.merge(
                    i.ofType(LoadAllFlagsIntent::class.java).take(1),
                    i.notOfType(LoadAllFlagsIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<AllFlagsIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<AllFlagsState> = statesObservable

    /**
     * intentFilter is only needed when need trigger an action not made by the user.
     * Map each intent to an action
     * compose with the processor to get the result of this action
     * scan to reduce to a new state, sending as reference the default state.
     * distinctUntilChanged to make sure we wont receive duplicate states and makes view rendered twice.
     * replay(1) to make sure a new subscriber will get the last emitted observable
     * autoConnect(0) to make sure the connection is immediate.
     */
    private fun compose(): Observable<AllFlagsState> = intentsSubject
        .compose(intentFilter)
        .map(this::actionFromIntent)
        .compose(processor.actionProcess)
        .scan(AllFlagsState.default(), reducer)
        .distinctUntilChanged()
        .replay(1)
        .autoConnect(0)

    /**
     * Map intents to Actions
     */
    private fun actionFromIntent(intent: AllFlagsIntent) = when (intent) {
        is LoadAllFlagsIntent -> LoadAllFlagsAction
        is ClearAllFlagsIntent -> ClearAllFlagsAction
    }

    companion object {
        private val reducer = BiFunction { previousState: AllFlagsState, result: AllFlagsResult ->
            when (result) {
                is LoadAllFlagsResult -> loadAllFlagsReducer(previousState, result)
                is ClearAllFlagsResult -> clearAllFlagsReducer(previousState, result)
            }
        }

        private fun loadAllFlagsReducer(
            previousState: AllFlagsState,
            result: LoadAllFlagsResult
        ): AllFlagsState =
            when (result) {
                is LoadAllFlagsResult.Success -> previousState.copy(
                    isLoading = false,
                    flags = result.flags,
                    errorMessage = null
                )
                is LoadAllFlagsResult.Loading -> previousState.copy(isLoading = true, errorMessage = null)
                is LoadAllFlagsResult.Error -> previousState.copy(isLoading = false, errorMessage = result.error)
            }

        private fun clearAllFlagsReducer(previousState: AllFlagsState, result: ClearAllFlagsResult): AllFlagsState =
            when (result) {
                is ClearAllFlagsResult.Success -> previousState.copy(
                    isLoading = false,
                    flags = result.emptyList,
                    errorMessage = null
                )
                is ClearAllFlagsResult.Loading -> previousState.copy(isLoading = true, errorMessage = null)
                is ClearAllFlagsResult.Error -> previousState.copy(isLoading = false, errorMessage = result.error)
            }
    }
}

class AllFlagsViewModelFactory(private val processor: AllFlagsProcessorHolder) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AllFlagsViewModel(processor) as T
    }
}