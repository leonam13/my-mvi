package com.example.mymvi.allflags

import com.example.mymvi.allflags.AllFlagsAction.*
import com.example.mymvi.allflags.AllFlagsResult.*
import com.example.mymvi.data.FlagRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalArgumentException

/**
 * Used to process each action and get the corresponding result.
 * Each action has a processor, so we merge all them to get only one observable stream.
 *
 * This logic can be done in view-model, but in order to organize the project,
 * we do it here.
 */
class AllFlagsProcessorHolder(private val flagRepository: FlagRepository) {


    private val loadAllFlagsProcessor =
        ObservableTransformer<LoadAllFlagsAction, LoadAllFlagsResult> { actions ->
            actions.flatMap { action ->
                flagRepository.getFlags(action.code)
                    .map { flags -> LoadAllFlagsResult.Success(flags) }
                    .cast(LoadAllFlagsResult::class.java)
                    .onErrorReturn { LoadAllFlagsResult.Error(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.trampoline())
//                    .observeOn(Schedulers.trampoline())
                    .startWith(LoadAllFlagsResult.Loading)
            }
        }

    private val clearAllFlagsProcessor =
        ObservableTransformer<ClearAllFlagsAction, ClearAllFlagsResult> { actions ->
            actions.flatMap {
                flagRepository.removeAllFlags()
                    .map { ClearAllFlagsResult.Success(it) }
                    .cast(ClearAllFlagsResult::class.java)
                    .onErrorReturn { ClearAllFlagsResult.Error(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.trampoline())
//                    .observeOn(Schedulers.trampoline())
                    .startWith(ClearAllFlagsResult.Loading)
            }
        }

    internal var actionProcess =
        ObservableTransformer<AllFlagsAction, AllFlagsResult> { actions ->
            actions.publish { action ->
                Observable.merge(
                    action.ofType(LoadAllFlagsAction::class.java).compose(loadAllFlagsProcessor),
                    action.ofType(ClearAllFlagsAction::class.java).compose(clearAllFlagsProcessor)
                )
                    .mergeWith(
                        action.filter { a -> a !is LoadAllFlagsAction && a !is ClearAllFlagsAction }
                            .flatMap { e -> Observable.error<AllFlagsResult>(IllegalArgumentException("Invalid Type $e")) }
                    )
            }
        }
}