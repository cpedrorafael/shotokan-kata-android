package com.guayaba.shotokankata.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AsyncUtil {
    companion object {
        val mainScope = CoroutineScope(Dispatchers.Main)
        val ioScope = CoroutineScope(Dispatchers.IO)
        val backgroundScope = CoroutineScope(Dispatchers.Default)
    }
}