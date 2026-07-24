package com.personalai.bridge.ai

object TextGenerator {

    fun generateReply(
        packageName: String,
        screenInfo: String
    ): String {

        return when {

            packageName.contains("whatsapp", true) ->
                "Hello!"

            packageName.contains("telegram", true) ->
                "Hello!"

            packageName.contains("instagram", true) ->
                "Hello!"

            packageName.contains("chrome", true) ->
                "ChatGPT"

            else ->
                "Hello!"
        }
    }
}
