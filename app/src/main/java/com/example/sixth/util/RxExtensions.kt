package com.example.sixth.util

import io.reactivex.disposables.Disposable

fun Disposable.disposeIfNotDisposed() {
    if (!isDisposed) {
        dispose()
    }
}