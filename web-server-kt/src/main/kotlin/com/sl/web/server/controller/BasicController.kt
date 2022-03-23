package com.sl.web.server.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BasicController:CoroutineScope {

    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

}