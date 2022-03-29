package com.sl.web.server.response

data class Wrapper<T>(val code: Int, val message: String, val data: T)

fun <R> R.wrapper(code: Int, message: String) = Wrapper(code, message, this)

fun String.wrapperWithToken(code: Int, message: String) = Wrapper(code, message, SimpleToken(this))

data class SimpleToken(val token: String)
