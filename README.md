# Appium For Android

A consise setup and demonstration of using Appium for Android using Kotlin, JUint, UIAutomator and Gradle.

This documentation along with a sample app describes the steps needed to take to setup Appium to be used to test an Android app using a test script written in Kotlin and running from Android Studio. This is a very consise document and doesn't touch on Android testing or Appium. It assumes that you already know how to buid an Android app using Android Studio. While the setup instructions described in this documentation is for a MacOS machine, you should be able to make modifications if you are a Windows user.

I wrote this document along with a simple test app because after spending hours trying out countless tutortials on how to setup Appium with the latest version (Appium 2.0 but using version 9.3.0 of the client library), it became frustrating when I wasn't making any progress. All of the   articles or sample apps used an older version of Appium which is no longer compatible with the 9.3.0 library. Add to this that many of the scripts were written in languages like Python. Like what Android develoepr seriously writes test apps using Python?

Sadly, Appium's own documentation on setting up and testing an Android app is severely lacking - in spite of Appium being the most popular testing framework for mobile.



## Install Appium

At its core, Appium is just another Nodejs server. This means that you need to install NodeJs. There are plenty of instructions available on installing Nodejs, so I'm not going to touch on that here. After installing Nodejs, you can install Appium using the following npm command:

```bash
npm install -g appium
```



## Install Appium Doctor (Optional)

There's a utility app that you can install that checks to see if there are any potential problems that you might encounter when running Appium. Basically Appium Doctor checks to see if a bunch of configuration stuff is set correctly and lets you know where you may need to fix something for Appium to work without any issues. To be honest, I thought that this seemed rather ridiculous since the instructions on installing and setting up Appium from Appium's own documentation gives no indication of potential issues. However, when I tried running my script, it failed with an exception indicating that the environment variable ANDROID_HOME was not set. Running Appium Doctor, it also showed that this variable was not set but only indicated it as a warning. In reality, it should iindicate it not as warning but a "MUST FIX". While this was the only issue I had to fix, there were several other warnings reported by Appium Doctor. I wasn't going to waste my time trying to "fix" stuff that probably wasn't even broke. My recommendation is to not bother with Appium Doctor and only install it if you are having issues getting your test script to run. To install Appium Doctor, run:

```bash
npm install appium-doctor -g
```



## Set ANDROID_HOME environment variable

Android Studio doesn't need the environment variable ANDROID_HOME  to be set. It knows where your Android SDK is installed without having to rely on this variable. Appium however does need it. On MacOS, you create this in your .bash_profile file. This is normally a hidden file located under your user folder that you can view in Finder. You may need to unhide files if you can't see it.

The easiest way to determine the path to your Android SDK is to use Android Studio and click on the Preferences menu item and search for "Android Studio". Under the current version of Android Studio at the time of writing this (Koala 2024.1.1),  it's located under **Languages and Frameworks**. In your .bash_profile file, you set the environment variable:

```bash
export ANDROID_HOME=<path to your Android sdk>
```



## Starting Appium

If you just set the ANDROID_HOME environment, open up a new terminal and run the following command to start Appium:


```bash
> appium
```

Appium is an HTTP server and operates using the default port 4723. It receives REST commands in JSON format that are then sent to UIAutomater, which is the test framework used for testing inside of Android Studio. The client library that you will be using in your test script handles the formatting of the JSON data and makes the HTTP calls. If you ever want to know the version of Appium installed, just run the following command:

```bash
> appium --version
```
