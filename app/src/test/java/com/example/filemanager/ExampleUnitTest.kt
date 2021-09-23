package com.example.filemanager

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.absoluteValue

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        F.values().forEach { println(it.name) }
    }
}

enum class F() {
    txt, frd
}