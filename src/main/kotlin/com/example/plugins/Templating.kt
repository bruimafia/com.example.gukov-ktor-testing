package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun Application.configureTemplating() {
    routing {
        get("/html-dsl") {
            call.respondHtml {
                body {
                    // master top
                    h1 { +"HTML" }
                    // center master
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                    // bottom master
                }
            }
        }
    }
}
