package com.example.mymvi

import com.example.mymvi.allflags.AllFlagsIntent
import com.example.mymvi.allflags.AllFlagsProcessorHolder
import com.example.mymvi.allflags.AllFlagsState
import com.example.mymvi.data.FlagRepository
import com.example.mymvi.mvi.AllFlagsViewModel
import com.example.mymvi.mvi.Flag
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.IllegalArgumentException


class AllFlagsViewModelTest {

    @Mock
    lateinit var flagRepository: FlagRepository
    private lateinit var viewModel: AllFlagsViewModel
    private lateinit var testObserver: TestObserver<AllFlagsState>
    private lateinit var flags: List<Flag>


    @Before
    fun setUp() {
        flagRepository = mock(FlagRepository::class.java)

        viewModel = AllFlagsViewModel(AllFlagsProcessorHolder(flagRepository))

        flags = listOf(
            Flag(1, "Brasil", "https://cdn.countryflags.com/thumbs/brazil/flag-400.png", "SA"),
            Flag(2, "Reino Unido", "https://cdn.countryflags.com/thumbs/united-kingdom/flag-400.png", "EU")
        )

        testObserver = viewModel.states().test()
    }

    @Test
    fun loadAllFlagsFromRepositoryAndDisplayIntoView() {
        `when`(flagRepository.getFlags(null)).thenReturn(Observable.just(flags))

        viewModel.processIntents(Observable.just(AllFlagsIntent.LoadAllFlagsIntent()))

        testObserver.assertValueAt(1, AllFlagsState::isLoading)
        testObserver.assertValueAt(2) { state -> !state.isLoading }
        testObserver.assertValueAt(2) { state: AllFlagsState -> state.errorMessage == null }
    }

    @Test
    fun loadAllFlagsFromRepositoryAndDisplayError() {
        `when`(flagRepository.getFlags(null)).thenReturn(Observable.error(IllegalArgumentException("Some Error")))

        viewModel.processIntents(Observable.just(AllFlagsIntent.LoadAllFlagsIntent()))

        testObserver.assertValueAt(1, AllFlagsState::isLoading)
        testObserver.assertValueAt(2) { state: AllFlagsState -> state.errorMessage != null }
    }

}