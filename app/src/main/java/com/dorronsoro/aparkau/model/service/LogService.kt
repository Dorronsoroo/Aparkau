package com.dorronsoro.aparkau.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}

