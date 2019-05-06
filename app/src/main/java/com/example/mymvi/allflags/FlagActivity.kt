package com.example.mymvi.allflags

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymvi.R
import com.example.mymvi.allflags.AllFlagsIntent.ClearAllFlagsIntent
import com.example.mymvi.allflags.AllFlagsIntent.LoadAllFlagsIntent
import com.example.mymvi.injection.AppInjector
import com.example.mymvi.injection.AppInjector.getAllFlagsViewModel
import com.example.mymvi.mvi.AllFlagsViewModel
import com.example.mymvi.mvi.Flag
import com.example.mymvi.mvibase.MviView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_flag.*

class FlagActivity : AppCompatActivity(), MviView<AllFlagsIntent, AllFlagsState> {

    private lateinit var viewModel: AllFlagsViewModel
    private var adapter = FlagAdapter()
    private val disposable = CompositeDisposable()

    private val loadAllFlagsPublisher = PublishSubject.create<LoadAllFlagsIntent>()
    private val clearAllFlagsPublisher = PublishSubject.create<ClearAllFlagsIntent>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flag)
        viewModel = ViewModelProviders
            .of(this, getAllFlagsViewModel())
            .get(AllFlagsViewModel::class.java)
        setUpRecycler()
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    /**
     * Each new state emitted should be render
     * and in onStar we say to view-model start observe the intents.
     */
    private fun bind() {
        disposable.add(viewModel.states().subscribe { render(it) })
        viewModel.processIntents(intents())
    }

    override fun render(state: AllFlagsState) {
        setLoading(state.isLoading)
        updateData(state.flags)
        state.errorMessage?.let { showError(it) }
    }

    private fun setLoading(showLoading: Boolean) {
        if (showLoading) loading.show() else loading.hide()
    }

    /**
     * Here we merge all the intents that should be observable to view-model
     */
    override fun intents(): Observable<AllFlagsIntent> = Observable.merge(loadFlags(), clearFlags())

    /**
     * We transform our publishers in observables.
     * In case the item is called for the user it should be a publisher,
     * Otherwise if the system calls to the intent, can be a simple Observable.
     */
    private fun clearFlags(): Observable<ClearAllFlagsIntent> = clearAllFlagsPublisher

    private fun loadFlags(): Observable<LoadAllFlagsIntent> = loadAllFlagsPublisher
    //Observable.just(LoadAllFlagsIntent)

    private fun setListeners() {
        allFlagsBtn.setOnClickListener { loadAllFlagsPublisher.onNext(LoadAllFlagsIntent()) }
        europeBtn.setOnClickListener { loadAllFlagsPublisher.onNext(LoadAllFlagsIntent(it.tag as String)) }
        southAmericaBtn.setOnClickListener { loadAllFlagsPublisher.onNext(LoadAllFlagsIntent(it.tag as String)) }
        clearBtn.setOnClickListener { clearAllFlagsPublisher.onNext(ClearAllFlagsIntent) }
    }

    private fun setUpRecycler() {
        flagRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        flagRv.adapter = adapter
    }

    private fun updateData(flags: List<Flag>) {
        adapter.items = flags
        flagRv.visibility = View.VISIBLE
    }

    private fun showError(state: Throwable) {
        Toast.makeText(this, getString(R.string.error_load_flag), Toast.LENGTH_SHORT).show()
        Log.e("LOGHELPER", "Erro ${state.localizedMessage}")
    }
}