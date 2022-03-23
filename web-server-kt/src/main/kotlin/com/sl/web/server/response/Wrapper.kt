package com.sl.web.server.response

data class Wrapper<T>(val code:Int, val message:String, val data:T)

fun<R> R.wrapper(code: Int,message: String) = Wrapper(code,message,this)
