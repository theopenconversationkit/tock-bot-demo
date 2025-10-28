# Sample code using Bot API in Kotlin mode

Documentation: https://doc.tock.ai/tock/master/dev/bot-api.html

Source code contains :

- A sample html file for the chat widget: [web/index.html](web/index.html) that you need to customize by replacing [relative web connector rest path]
- Simple kotlin-based bot implementation
    - [common](common/src/main/kotlin/bot.kt): shared code between websocket & webhook implementation
    - [websocket](websocket/src/main/kotlin/StartWebsocket.kt): websocket mode launcher - just set the tock_bot_api_key env var
    - [webhook](webhook/src/main/kotlin/StartWebhook.kt): webhook launcher - just set the tock_bot_api_key env var

