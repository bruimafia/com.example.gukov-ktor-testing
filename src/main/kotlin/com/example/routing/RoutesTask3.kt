package com.example.routing

import com.example.Utils
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val TASK3_HOURS_DEFAULT = 8
const val TASK3_MINUTES_DEFAULT = 0

fun Route.task3Routes() {
    route("tasks/t3") {
        get {
            val hours = call.request.queryParameters["h"]?.toInt() ?: TASK3_HOURS_DEFAULT
            val minutes = call.request.queryParameters["m"]?.toInt() ?: TASK3_MINUTES_DEFAULT
            call.respondText("Запущена задача 3 с ежедневной отправкой письма в ${Utils.setZero(hours)}:${Utils.setZero(minutes)}")
            runTask3(hours = hours, minutes = minutes)
        }

        get("/stop") {
            call.respondText("Задача 3 завершена")
            println("Задача 3 завершена")
            Utils.sendMailCoroutineJob?.cancel()
        }

        post {
            val params = call.receiveParameters()
            val hours = params["h"]?.toInt() ?: TASK3_HOURS_DEFAULT
            val minutes = params["m"]?.toInt() ?: TASK3_MINUTES_DEFAULT
            call.respondText("Запущена задача 3 с ежедневной отправкой письма в ${Utils.setZero(hours)}:${Utils.setZero(minutes)}")
            runTask3(hours = hours, minutes = minutes)
        }
    }
}

fun runTask3(hours: Int, minutes: Int) {
    println("Запущена задача 3 с ежедневной отправкой письма в ${Utils.setZero(hours)}:${Utils.setZero(minutes)}")

    // Вариант 1 создания планировщика
    Utils.sendMailCoroutineJob?.cancel()
    Utils.sendMailCoroutineJob = Utils.startWithCoroutineTimer(hours = hours, minutes = minutes)

    // Вариант 2 создания планировщика
//    Utils.startWithTimer(hours = hours, minutes = minutes)

    // Вариант 3 создания планировщика (сначала сделал так, но как мне потом уточнили - лучше не использовать сторонние библиотеки)
//    Utils.startWithJob(hours = hours, minutes = minutes, name = "task3")
}