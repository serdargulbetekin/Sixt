package com.example.sixth.modules.cars

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sixth.modules.cars.CarsItem
import com.example.sixth.modules.cars.CarsRepository
import com.example.sixth.util.disposeIfNotDisposed
import com.example.sixth.util.optMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class CarsViewModel @Inject constructor(
    private val carsRepository: CarsRepository
) : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _carsList = MutableLiveData<List<CarsItem>>()
    val carsList: LiveData<List<CarsItem>>
        get() = _carsList

    private var disposable: Disposable? = null

    init {
        request()
    }

    fun request() {
        _progress.postValue(true)
        disposable?.disposeIfNotDisposed()
        disposable = carsRepository.getCars().subscribe({
            _progress.postValue(false)
            _carsList.postValue(it)
        }, {
            _progress.postValue(false)
            _errorMessage.postValue(it.optMessage())
        })
    }

    fun onPause() {
        disposable?.disposeIfNotDisposed()
    }

    companion object {
        var CACHED_DATA: List<CarsItem>? = null

    }
}