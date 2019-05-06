package com.example.mymvi.injection

import com.example.mymvi.allflags.AllFlagsProcessorHolder
import com.example.mymvi.data.FlagDataSource
import com.example.mymvi.data.FlagRepositoryImpl
import com.example.mymvi.mvi.AllFlagsViewModelFactory

object AppInjector {

    fun getAllFlagsViewModel(): AllFlagsViewModelFactory = AllFlagsViewModelFactory(getAllFlagsProcessor())

    private fun getAllFlagsProcessor() = AllFlagsProcessorHolder(getFlagsRepository())

    private fun getFlagsRepository() = FlagRepositoryImpl(FlagDataSource)
}