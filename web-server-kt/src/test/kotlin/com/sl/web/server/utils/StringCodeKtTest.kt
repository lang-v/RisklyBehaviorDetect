package com.sl.web.server.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class StringCodeKtTest {

    @Test
    fun testTimeStampValid(){
        val old = Date(System.currentTimeMillis() - 10000)
        val new = Date(System.currentTimeMillis() + 10000)
        assert(!old.time.isValid())
        assert(new.time.isValid())

        val t = Date(1648630821264)
        println(t)
        assert((1648630821264L).isValid())
    }

    @Test
    fun testTokenIllegal(){
        val token = "GzQFHBQYCyMzRRofHgQnPQ0RAgJvaS0gHxYaCQw2DwdYXUZZUX1WTUNXW15QZA=="
        assert(token.isLegalToken())
    }
}