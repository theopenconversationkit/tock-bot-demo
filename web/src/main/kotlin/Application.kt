import io.ktor.http.ContentType
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main(vararg args: String) {
    val webConnectorPath =
        args.getOrNull(0)
            ?: System.getenv("WEB_CONNECTOR_PATH")
            ?: "io/app/new_assistant/web"
    val content =
        ::main::class.java.getResource("/index.html")
            .readText()
            .replace("[relative web connector rest path]", webConnectorPath)

    embeddedServer(Netty, port = System.getenv("PORT")?.toIntOrNull() ?: 3001) {
        routing {

            get("/") {
                call.respondText(contentType = ContentType.Text.Html, text = content)
            }
        }
    }.start(wait = true)
}
