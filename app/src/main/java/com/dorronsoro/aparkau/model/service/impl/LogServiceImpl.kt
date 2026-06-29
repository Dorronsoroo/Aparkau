package com.dorronsoro.aparkau.model.service.impl

import android.util.Log
import com.dorronsoro.aparkau.model.service.LogService
import javax.inject.Inject

class LogServiceImpl @Inject constructor() : LogService {
    override fun logNonFatalCrash(throwable: Throwable) {
        Log.e("AparkauApp", throwable.message ?: "Error desconocido", throwable)
    }
}

