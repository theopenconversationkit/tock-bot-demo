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
import ai.tock.bot.connector.web.webButton
import ai.tock.bot.connector.web.webMessage
import ai.tock.bot.definition.Intent
import ai.tock.shared.property

val apiKey = property("tock_bot_api_key", "MY API KEY")

val bot = newBot(
    apiKey,
    newStory("greetings") {
        end("Hello $message")
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
    unknownStory {
        end {
            //custom model sample
            webMessage(
                "Sorry - not understood",
                webButton("Card", Intent("card")),
                webButton("Carousel", Intent("carousel"))
            )
        }
    }
)
