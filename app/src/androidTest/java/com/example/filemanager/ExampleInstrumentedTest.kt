package com.example.filemanager

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.filemanager.constants.LOG_TAG
import com.example.filemanager.constants.STORAGE
import com.example.filemanager.constants.pathDeleteFiles

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        println(appContext.resources.displayMetrics.widthPixels)
        assertEquals("com.example.filemanager", appContext.packageName)
    }

    @Test
    fun test() {
        val file = File("$STORAGE/did")
        Log.d(LOG_TAG, file.parent!!)
        Log.d(LOG_TAG, file.canonicalPath)
        Log.d(LOG_TAG, file.extension)
        Log.d(LOG_TAG, file.nameWithoutExtension)
        Log.d(LOG_TAG, file.invariantSeparatorsPath)
        Log.d(LOG_TAG, file.isAbsolute.toString())
    }
}