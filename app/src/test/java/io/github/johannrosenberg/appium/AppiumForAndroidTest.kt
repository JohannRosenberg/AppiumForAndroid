package io.github.johannrosenberg.appium


import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import io.github.johannrosenberg.appium.ui.utils.ElementIdentifiers
import org.junit.After
import org.junit.Test
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebElement
import java.net.URL


class AppiumForAndroidTest {
    private lateinit var driver: AndroidDriver

    @Test
    fun doSomething() {

        val options = UiAutomator2Options()
            .setUdid("711KPRW0598622") // Obtain this from running "adb devices" in a terminal.
            .setAppPackage("io.github.johannrosenberg.appium")
            .setAppActivity("MainActivity")

        val driver = AndroidDriver(URL("http://127.0.0.1:4723"), options)

        // This will take a screenshot of the UI and return the bitmap as an array of bytes.
        val screenshotBytes = driver.getScreenshotAs(OutputType.BYTES)

        // This will take a screenshot of the UI and store it to your local disk If you move your
        // mouse over the variable, screenshotFile, the path will be indicated. A File object is
        // returned.
        val screenshotFile = driver.getScreenshotAs(OutputType.FILE)

        try {
            val button = findElement(ElementIdentifiers.btnRollTheDice, driver)
            button.click()

        } finally {
            // Calling driver.quit() will terminate the app. If you want the app to keep running, don't call quit().
            driver.quit()
        }
    }

    @After
    fun tearDown() {
        driver.quit()
    }
}

fun findElement(elemId: ElementIdentifiers, driver: AndroidDriver): WebElement {
    val id1 = "new UiSelector().resourceId(\"" + elemId.id() + "\")"
    return  driver.findElement(AppiumBy.androidUIAutomator(id1))
}