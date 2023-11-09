package com.example.routing

import com.example.Utils
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

const val TASK2_INTERVAL_MS_DEFAULT = 60_000 * 5

fun Route.task2Routes () {
    route("tasks/t2") {
        get {
            val interval = call.request.queryParameters["interval-ms"]?.toInt() ?: TASK2_INTERVAL_MS_DEFAULT
            call.respondText("Запущена задача 2 с интервалом ${Utils.millisecondsFormat(interval.milliseconds)}")
            runTask2(interval = interval)
        }

        get("/stop") {
            call.respondText("Все задачи 2 завершены")
            Utils.stopAllTasks(Utils.tasks2JobList, 2)
        }

        post {
            val params = call.receiveParameters()
            val interval = params["interval-ms"]?.toInt() ?: TASK2_INTERVAL_MS_DEFAULT
            call.respondText("Запущена задача 2 с интервалом ${Utils.millisecondsFormat(interval.milliseconds)}")
            runTask2(interval = interval)
        }
    }
}

fun runTask2(interval: Int) {
    println("Запущена задача 2 с интервалом ${Utils.millisecondsFormat(interval.milliseconds)}")
    val job = CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            delay(interval.toLong())
            println("Прошло ${Utils.millisecondsFormat(interval.milliseconds)}и тебе пора меня выключить")
        }
    }
    Utils.tasks2JobList.add(job)
}