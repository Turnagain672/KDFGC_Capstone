package com.example.capstone2

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @Test
    fun appContext_hasCorrectPackageName() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.capstone2", appContext.packageName)
    }

    @Test
    fun appContext_isNotNull() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assert(appContext != null)
    }

    @Test
    fun appName_isCorrect() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val appName = appContext.applicationInfo.loadLabel(appContext.packageManager).toString()
        assertEquals("Capstone2", appName)
    }

    @Test
    fun targetContext_matchesAppContext() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val appPackage = targetContext.packageName
        assert(appPackage.contains("capstone2"))
    }
}