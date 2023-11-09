package com.example.routing

import com.example.Utils
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

const val TASK1_INTERVAL_MS_DEFAULT = 60_000

fun Route.task1Routes() {
    route("tasks/t1") {
        get {
            val interval = call.request.queryParameters["interval-ms"]?.toInt() ?: TASK1_INTERVAL_MS_DEFAULT
            call.respondText("Запущена задача 1 с интервалом ${Utils.millisecondsFormat(interval.milliseconds)}")
            runTask1(interval = interval)
        }

        get("/stop") {
            call.respondText("Все задачи 1 завершены")
            Utils.stopAllTasks(Utils.tasks1JobList, 1)
        }

        post {
            val params = call.receiveParameters()
            val interval = params["interval-ms"]?.toInt() ?: TASK1_INTERVAL_MS_DEFAULT
            call.respondText("Запущена задача 1 с интервалом ${Utils.millisecondsFormat(interval.milliseconds)}")
            runTask1(interval = interval)
        }
    }
}

fun runTask1(interval: Int) {
    println("Запущена задача 1 с интервалом ${Utils.millisecondsFormat(interval.milliseconds)}")
    val job = CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            delay(interval.toLong())
            println("Прошло ${Utils.millisecondsFormat(interval.milliseconds)}")
        }
    }
    Utils.tasks1JobList.add(job)
}