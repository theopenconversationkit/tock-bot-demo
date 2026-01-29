/*
 * Copyright (C) 2017/2019 VSCT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.tock.demo.common

import ai.tock.bot.api.client.newBot
import ai.tock.bot.api.client.newStory
import ai.tock.bot.api.client.unknownStory
import ai.tock.bot.connector.web.webConnectorType
import ai.tock.bot.connector.web.webMessage
import ai.tock.bot.connector.web.webPostbackButton
import ai.tock.bot.definition.Intent
import ai.tock.shared.property
import ai.tock.translator.raw
import kotlinx.coroutines.delay

val apiKey = property("tock_bot_api_key", "MY API KEY")

val bot = newBot(
    apiKey,
    newStory("greetings") {
        println("received : $message")
        end("Hello $message")
    },
    newStory("stream") {
        send("Streaming test...")
        enableStreaming()
        for (i in 1..10) {
            send("$i + ".raw)
            delay(400)
        }
        send(" = ${(1..10).sum()}".raw)
        disableStreaming()
        end("That's all folks!")
    },
    newStory("stream_component") {
        send("Streaming component test...")
        enableStreaming()
        for (i in 1..10) {
            send(
                newCard(
                    title = "$i + ".raw,
                    subTitle = ".".raw,
                )
            )
            delay(400)
        }

        send(
            newCard(
                " = ${(1..10).sum()}".raw,
                actions = listOf(
                    newAction("Action1"),
                    newAction("Tock", "https://doc.tock.ai")
                )
            )
        )
        disableStreaming()
        end("That's all folks!")
    },
    newStory("card") {
        //cleanup entities
        val test = entityText("location")
        entities.clear()
        end(
            newCard(
                test ?: "Hey",
                "Where are you going?",
                newAttachment("https://upload.wikimedia.org/wikipedia/commons/2/22/Heckert_GNU_white.svg"),
                newAction("Action1"),
                newAction("Tock", "https://doc.tock.ai")
            )
        )
    },
    newStory("carousel") {
        end(
            newCarousel(
                listOf(
                    newCard(
                        "Card 1",
                        null,
                        newAttachment("https://upload.wikimedia.org/wikipedia/commons/2/22/Heckert_GNU_white.svg"),
                        newAction("Action1"),
                        newAction("Tock", "https://doc.tock.ai")
                    ),
                    newCard(
                        "Card 2",
                        null,
                        newAttachment("https://doc.tock.ai/fr/images/header.jpg"),
                        newAction("Action1"),
                        newAction("Tock", "https://doc.tock.ai")
                    )
                )
            )
        )
    },
    newStory("web") {
        end {
            //custom model sample
            when (targetConnectorType) {
                webConnectorType ->
                    webMessage(
                        "Web",
                        webPostbackButton("Card", Intent("card")),
                        webPostbackButton("Carousel", Intent("carousel")),
                        webPostbackButton("Streaming", Intent("stream")),
                        webPostbackButton("Streaming Component", Intent("stream_component")),
                        webPostbackButton("Markdown", Intent("markdown")),
                        webPostbackButton("Switching story", Intent("switch_story"))
                    )

                else -> "[unsupported]"
            }
        }
    },
    newStory("markdown") {
        end {
            """
                - **bold**
                - *italic*
                - ~~strike~~

                ---
                
                [google link](https://www.google.com)
                
                ---
                
                # Titre 1
                ## Titre 2
                ### Titre 3
                
                ---
                
                ![GNU image](https://upload.wikimedia.org/wikipedia/commons/2/22/Heckert_GNU_white.svg)
            """.trimIndent().raw
        }
    },
    newStory("switch_story") {
        send("starting switching current story to stream...")
        handleAndSwitchStory("stream")
    },
    unknownStory {
        end("Sorry - not understood")
    }
)
