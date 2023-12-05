package dev.sergiobelda.todometer.benchmarks.baselineprofile.app.android

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect(
            packageName = "dev.sergiobelda.todometer",
            includeInStartupProfile = true
        ) {
            pressHome()
            startActivityAndWait()

            device.wait(Until.hasObject(By.res("task_list")), 5000)
            val taskList = device.findObject(By.res("task_list"))
            taskList.setGestureMargin(device.displayWidth / 5)
            taskList.fling(Direction.DOWN)
            device.waitForIdle()
        }
    }
}
