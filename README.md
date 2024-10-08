<p align="center">
    <img src="../../blob/main/images/feature.png" width="840" height="131" />
</p>

# Appium For Android

A concise setup and demonstration of using Appium for Android using Jetpack Compose, Kotlin, JUint, UIAutomator and Gradle.

This documentation along with a sample app describes the steps needed to set up Appium to be used to test an Android app using a test script written in Kotlin and running from Android Studio. It also focuses on testing an app developed with Jetpack Compose. This is a very concise document and doesn't touch on Android testing or Appium. It assumes that you already know how to build an Android app using Android Studio. While the setup instructions described in this documentation is for a MacOS machine, you should be able to make minor modifications if you are a Windows user.

I wrote this document along with a simple test app because after spending hours trying out countless tutorials on how to set up Appium with the latest version (Appium 2.0 but using version 9.3.0 of the client library) and to have it work with Jetpack Compose, it became frustrating when I wasn't making any progress. All of the articles or sample apps used an older version of Appium which is no longer compatible with the 9.3.0 library. Add to this that many of the scripts were written in languages like Python. Seriously?? 

Sadly, Appium's own documentation on setting up and testing an Android app is severely lacking - in spite of Appium being the most popular testing framework for mobile. 

## Install Appium

At its core, Appium is just another Nodejs server. This means that you need to install NodeJs. There are plenty of instructions available on installing Nodejs, so I'm not going to touch on that here. After installing Nodejs, you can install Appium using the following npm command:

```bash
> npm install -g appium
```

## Install Appium Client Drivers

Prior to Appium 2.0, client drivers were included in the Appium installation. These libraries are used by Appium to communicate with the test framework such as UIAutomator or Espresso. You now need to install these libraries manually. To list the drivers that Appium supports, run:

```bash
> appium driver list
```

To install a driver run:

```bash
> appium driver install <driver name>
```

Because we are going to use UIAutomater, run this:

```bash
> appium driver install uiautomator2
```

For details on the UIAutomator driver, visit:

[GitHub - appium/appium-uiautomator2-driver: Appium driver for Android UIAutomator2](https://github.com/appium/appium-uiautomator2-driver)



If you plan on using the Espresso testing framework instead, run:

```bash
> appium driver install espresso
```

For details on the Espresso driver, visit:

[GitHub - appium/appium-espresso-driver: Espresso integration for Appium](https://github.com/appium/appium-espresso-driver)



I'm not sure if the mac driver is needed, so I installed that one as well just to be safe. I'm presume this driver is used for testing MacOS desktop apps, but better to be safe than sorry:

```bash
> appium driver install mac 
```

## Install Appium Doctor (Optional)

There's a utility app that you can install that checks to see if there are any potential problems that you might encounter when running Appium. Basically Appium Doctor checks to see if a bunch of configuration stuff is set correctly and lets you know where you may need to fix something for Appium to work without any issues. To be honest, I thought that this seemed rather ridiculous since the instructions on installing and setting up Appium from Appium's own documentation gives no indication of potential issues. However, when I tried running my script, it failed with an exception indicating that the environment variable ANDROID_HOME was not set. Running Appium Doctor, it also showed that this variable was not set but only indicated it as a warning. In reality, it should indicate it not as a warning but a "MUST FIX". While this was the only issue I had to fix, there were several other warnings reported by Appium Doctor. I wasn't going to waste my time trying to "fix" stuff that probably wasn't even broken. My recommendation is to not bother with Appium Doctor and only install it if you are having issues getting your test script to run. To install Appium Doctor, run:

```bash
> npm install appium-doctor -g
```

## Set ANDROID_HOME environment variable

Android Studio doesn't need the environment variable ANDROID_HOME  to be set. It knows where your Android SDK is installed without having to rely on this variable. Appium however does need it. On MacOS, you create this in your .bash_profile file. This is normally a hidden file located under your user folder that you can view in Finder. You may need to unhide files if you can't see it.

The easiest way to determine the path to your Android SDK is to use Android Studio and click on the Preferences menu item and search for "Android SDK". Under the current version of Android Studio at the time of writing this (Koala 2024.1.1),  it's located under **Languages and Frameworks**. In your .bash_profile file, you set the environment variable:

```bash
export ANDROID_HOME=<path to your Android sdk>
```

## Starting Appium

If you just set the ANDROID_HOME environment, open up a new terminal and run the following command to start Appium:

```bash
> appium
```

Appium is an HTTP server and operates using the default port 4723. It receives REST commands in JSON format that are then sent to UIAutomater, which is the test framework used for testing your app. This testing can be done either in Android Studio, or from a terminal and running gradle tests. Running gradle tests is necessary if you are using an CI/CD environment. The client library that you will be using in your test script handles the formatting of the JSON data and makes the HTTP calls. If you ever want to know the version of Appium installed, just run the following command:

```bash
> appium --version
```

### The test app

In order for the Appium client driver to find Compose elements in an Android app, each component needs to set **testTag** to a unique identifier. This is set using the Modifier object. You also need to set **testTagsAsResourceId** to true. However, you only need to set **testTagsAsResourceId** to true just once but it must be set on a composable element that is high up in the UI hierarchy, as close to root as possible. This is often set on the Scaffold. The preferred way of setting **testTagsAsResourceId** is to create an extension function as follows:

```kotlin
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.setTagAndId(tag: String): Modifier {
    return this
        .semantics { this.testTagsAsResourceId = true }
        .testTag(tag)
}
```

You then call **setTagAndId** on every element that you need to have access to with the Appium driver. The following code is the app being tested and shows how **testTagsAsResourceId** is used:

```kotlin
class MainActivity : ComponentActivity() {
    private val ctx: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppiumForAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize().setTagAndId("scaffold")) { _ ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            modifier = Modifier.setTagAndId("btnRollTheDice"),
                            onClick = {
                            Toast.makeText(ctx, "Yeah, you just won a million dollars!", LENGTH_LONG).show()
                        },
                            content = {
                                Text("Roll the dice")
                            })
                    }
                }
            }
        }
    }
}
```

In a production app, you are normally going to have multiple screens with elements like buttons, text fields and such. It is easy to create a new composable by just copying it and modifying it. But if you fail to give a copied button a new identifier in the tag, only the first one will be found that shows up in the UI hierarchy. How you assign unique identifiers to elements is something you need to consider. Often, you will also be making reusable components and therefore you will probably need to pass in a unique identifier as a parameter.

Assigning literal strings as identifiers is a bad idea. You might spell the string incorrectly. You might also accidentally use the same identifier elsewhere. And of course, many apps are usually developed in teams where more than one developer works on the app. Even if you have a file containing the identifiers as constants, you would have to constantly update your local copy to be sure you don't use an identifier name that another developer has already chosen.

One solution that you can use to avoid the problem with using literal strings is to use a sealed class and define objects that represent the identifier. This is what the sample app uses:

```kotlin
sealed class ElementIdentifiers {
    object scaffold: ElementIdentifiers()
    object btnRollTheDice : ElementIdentifiers()
    object txtUsername : ElementIdentifiers()
    object txtPassword : ElementIdentifiers()
    object btnLogin : ElementIdentifiers()

    fun id(): String {
        var id = this.toString()
        val startPos = id.indexOf("$") + 1
        val endPos = id.indexOf("@")
        return id.substring(startPos, endPos)
    }
}
```

An improvement on this is to add some prefix to the object name to identify what screen it belongs to. This makes it easier for multiple developers to avoid naming colliisions. Using the code above, it is still possible for two developers to use the same name and during a git merge, no conflict would be detected in code, but the test code would still end up finding the wrong element.  So instead of just **txtUsername**, use something like this:

```kotlin
object loginScreen_txtUsername : ElementIdentifiers()
```

The **id** function is used to convert an object to a string representation. Objects in a sealed class must be unique. If you place this sealed class in its own file, it becomes easy to maintain and the test script can use it as well. Using this sealed class, the main activity's code shown above can be rewritten as follows:

```kotlin
class MainActivity : ComponentActivity() {
    private val ctx: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppiumForAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize().setTagAndId(ElementIdentifiers.scaffold.id())) { _ ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            modifier = Modifier.setTagAndId(ElementIdentifiers.btnRollTheDice.id()),
                            onClick = {
                            Toast.makeText(ctx, "Yeah, you just won a million dollars!", LENGTH_LONG).show()
                        },
                            content = {
                                Text("Roll the dice")
                            })
                    }
                }
            }
        }
    }
}
```

## Creating a test script

Before we create a test script, we need an app to test. We're going to keep it really simple. The source code in this project displays a single screen with a button in the center. Jetpack Compose is used to create the screen. When you click on the button, a Toast is shown with a message. That's it. Once you can get Appium to test a simple screen with just a button, you can spend your time figuring out how to add more code to do stuff like entering text into a textfield.

If I get time, I'll update this project to include more things that you can test for, but to be honest, my approach to testing is far more conservative than most developers. I'm of the opinion that all you really need for UI testing is to fill in fields, click buttons and scroll. You then take a screen snapshot and compare it to a previous screen snapshot and if the two are not the same, this tells you something has changed and the test should fail. If the new screen is now the correct one to go with, you need to now make this the "good" screenshot that future tests will be compared to. This is a very simplified way of describing it. In reality, you do need to set up your CI/CD to handle this, which is outside the scope of this document. Finally, you need unit testing for non-UI stuff, and for the most part JUnit is good enough for that.



**Gradle Dependencies - libs.versions.toml**

In the **libs.versions.toml** file add the following. Note: the JUnit stuff is normally included in a project when you create a new Android project but I prefer to group all test dependencies together. Just delete the stuff that you already have in **libs.versions.toml**:

```toml
[versions]

appium = "9.3.0"
junit = "4.13.2"
junitVersion = "1.2.1"
junitJupiter = "5.8.2"
seleniumJava = "4.1.4"
seleniumRemoteDriver = "4.23.1"

[libraries]

junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
org-junit-jupiter = { group = "org.junit.jupiter", name = "junit-jupiter-api", version.ref = "junitJupiter" }
org-junit-jupiter-engine = { group = "org.junit.jupiter", name = "junit-jupiter-engine", version.ref = "junitJupiter" }
io-appium-java-client = { group = "io.appium", name = "java-client", version.ref = "appium" }
org-seleniumhq-selenium = { group = "org.seleniumhq.selenium", name = "selenium-java", version.ref = "seleniumJava" }
org-seleniumhq-selenium-remote-driver = { group = "org.seleniumhq.selenium", name = "selenium-remote-driver", version.ref = "seleniumRemoteDriver" }
```

If you are using Groovy DSL, add the following to your **build.gradle** dependencies:

```kotlin
dependencies {
    testImplementation libs.junit
    androidTestImplementation libs.androidx.junit
    androidTestImplementation libs.androidx.ui.test.junit4
    androidTestImplementation libs.org.junit.jupiter
    androidTestImplementation libs.org.junit.jupiter.engine
    implementation libs.io.appium.java.client
    androidTestImplementation libs.org.seleniumhq.selenium
    androidTestImplementation libs.org.seleniumhq.selenium.remote.driver
}
```

If you are using Kotlin DSL, add the following to your **build.gradle.kts** dependencies:

```groovy
dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.org.junit.jupiter)
    androidTestImplementation(libs.org.junit.jupiter.engine)
    implementation(libs.io.appium.java.client)
    androidTestImplementation(libs.org.seleniumhq.selenium)
    androidTestImplementation(libs.org.seleniumhq.selenium.remote.driver)
}
```

The last step is to now create the test script. Here is the test script used in the sample app:

```kotlin
class AppiumForAndroidTest {
    private lateinit var driver: AndroidDriver

    @Test
    fun testRollTheDice() {

        val options = UiAutomator2Options()
            .setUdid("711KPRW0598622") // Obtain this from running "adb devices" in a terminal.
            .setAppPackage("io.github.johannrosenberg.appium")
            .setAppActivity("MainActivity")

        val driver = AndroidDriver(URL("http://127.0.0.1:4723"), options)

        // This will take a screenshot of the UI and return the bitmap as an array of bytes.
        val screenshotBytes = driver.getScreenshotAs(OutputType.BYTES)

        // This will take a screenshot of the UI and store it to your local disk. If you move your
        // mouse over the variable, screenshotFile, the path will be indicated. A File object is
        // returned.
        val screenshotFile = driver.getScreenshotAs(OutputType.FILE)

        /*
            Write some code to test a previous snapshot of the screen with the current snapshot.
            The test will pass if the screens are identical.

            Tip: Instead of comparing bitmaps, use the array of bytes instead. Run the array
            through a function that calculates either a checksum or hashcode. Instead of loading
            the previous snapshot, load the checksum or hashcode for that screenshot instead. Then
            compare checksums (or hashcodes). This is much faster. You still want to store the
            bitmap as a file however because if the test fails, the developer still needs to
            look at the "good" bitmap and compare it to the "bad" one.

            assertTrue(goodChecksum == currentChecksum, "Screenshots must be identical")
         */

        try {
            val button = findElement(ElementIdentifiers.btnRollTheDice, driver)
            button.click()

            // Do some more testing after pressing the button...

        } finally {
            // Handle any exceptions.
        }
    }

    @After
    fun tearDown() {
        if (::driver.isInitialized) {
            // Calling driver.quit() will terminate the app. If you want the app to keep running, don't call quit().
            driver.quit()
        }
    }
}

fun findElement(elemId: ElementIdentifiers, driver: AndroidDriver): WebElement {
    val id1 = "new UiSelector().resourceId(\"" + elemId.id() + "\")"
    return  driver.findElement(AppiumBy.androidUIAutomator(id1))
}
```
