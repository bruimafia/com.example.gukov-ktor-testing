package com.example.plugins

import com.example.routing.task1Routes
import com.example.routing.task2Routes
import com.example.routing.task3Routes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.p

// MASTER TEST TEST TEST
// mastr
fun Application.configureRouting() {
    routing {
        get("/") {
            // mas
            call.respondHtml(HttpStatusCode.OK) {
                body {
                    // gdhhfdj
                    p {
                        +"Для запуска задачи 1 перейдите на "
                        a {
                            attributes["href"] = "/tasks/t1"
                            +"/tasks/t1"
                        }
                        +" (интервал в миллисекундах передается в параметре [interval-ms], по умолчанию 1 мин)"
                    }
                    p {
                        +"Для запуска задачи 2 перейдите на "
                        a {
                            attributes["href"] = "/tasks/t2"
                            +"/tasks/t2"
                        }
                        +" (интервал в миллисекундах передается в параметре [interval-ms], по умолчанию 5 мин)"
                    }
                    p {
                        +"Для запуска задачи 3 перейдите на "
                        a {
                            attributes["href"] = "/tasks/t3"
                            +"/tasks/t3"
                        }
                        +" (время - часы и минуты - ежедневной отправки письма передается в параметрах [h] и [m], по умолчанию 08:00)"
                    }
                }
            }
        }

        get("/tasks/") {
            call.respondRedirect("/", permanent = true)
        }

        task1Routes()
        task2Routes()
        task3Routes()
    }
}
