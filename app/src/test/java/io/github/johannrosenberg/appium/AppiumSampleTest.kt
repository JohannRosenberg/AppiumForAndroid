package io.github.johannrosenberg.appium


import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import org.junit.After
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import java.net.URL


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AppiumForAndroidTest {
    private lateinit var driver: AndroidDriver

    @Test
    fun doSomething() {
        val options = UiAutomator2Options()
            .setUdid("711KPRW0598622") // Obtain this from running "adb devices" in a terminal.
            .setAppPackage("io.github.johannrosenberg.appium")
            .setAppActivity("MainActivity")

        val driver = AndroidDriver( // The default URL in Appium 1 is http://127.0.0.1:4723/wd/hub
            URL("http://127.0.0.1:4723"), options
        )

        //val screenshotBytes =  driver.getScreenshotAs(OutputType.BYTES)
        val screenshotFile =  driver.getScreenshotAs(OutputType.FILE)
        var x = 0
        x++


        try {

            //val button = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId("""fab”"")""”))
            val button = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"myCoolButton\")"))

            //val button = driver.findElement(AppiumBy.accessibilityId("myCoolButton"))

            //val el = driver.findElement(AppiumBy.xpath("//Button"))
            button.click()
            //driver.pageSource
        } finally {
            //driver.quit()
        }
    }

    @Test
    fun testLoginScreen() {
        // Find the username and password fields
        val usernameField = driver.findElement(By.id("username_field"))
        usernameField.click()

        // Enter valid credentials
        usernameField.sendKeys("Chewy")


        // Assert that the login was successful (e.g., check for a welcome message)
//        val welcomeMessage = driver.findElementById("welcome_message")
//        assertTrue(welcomeMessage.isDisplayed())
    }

    @After
    fun tearDown() {
        //driver.quit()
    }


/*    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }*/
}