package io.github.johannrosenberg.appium.ui.utils

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