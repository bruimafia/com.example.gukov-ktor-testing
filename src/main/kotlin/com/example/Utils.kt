package com.example

import at.quickme.kotlinmailer.delivery.mailerBuilder
import at.quickme.kotlinmailer.delivery.send
import at.quickme.kotlinmailer.email.emailBuilder
import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.*
import kotlinx.coroutines.Job
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
import org.simplejavamail.api.mailer.config.TransportStrategy
import java.util.*
import java.util.Calendar
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

object Utils {

    private const val EMAIL_HOST = "smtp.mail.ru"
    private const val EMAIL_PORT = 465
    private const val EMAIL_USERNAME = "sg_brui@mail.ru"
    private const val EMAIL_PASSWORD = "mR8e5BZrfA24BhPNA90R"
    private const val EMAIL_FROM = "sg_brui@mail.ru"
    private const val EMAIL_TO = "sbrujg@mail.ru"
    private const val EMAIL_SUBJECT = "8 утра и это снова я"
    private const val EMAIL_PLAIN_TEXT = "Возьми меня на работу. Мой email @email"

    var sendMailCoroutineJob: Job? = null
    val tasks1JobList = mutableListOf<Job>()
    val tasks2JobList = mutableListOf<Job>()

    fun millisecondsFormat(intervalDuration: Duration) =
        intervalDuration.toComponents { hours, minutes, seconds, nanoseconds ->
            with(StringBuilder()) {
                if (hours.toInt() != 0) append("$hours ч ")
                if (minutes != 0) append("$minutes мин ")
                if (seconds != 0) append("$seconds с ")
                if (nanoseconds != 0) append("$nanoseconds нс ")
                this.toString()
            }
        }

    fun stopAllTasks(jobList: MutableList<Job>, taskNumber: Int) {
        println("Все задачи $taskNumber завершены")
        jobList.forEach(Job::cancel)
    }

    fun setZero(number: Int) = if (number < 10) "0$number" else "$number"

    private fun isSendToday(currentTime: Long, requiredTime: Long) =
        currentTime <= requiredTime

    fun startWithCoroutineTimer(hours: Int, minutes: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            val c = Calendar.getInstance()
            val currentHour = c.get(Calendar.HOUR_OF_DAY)
            val currentMinute = c.get(Calendar.MINUTE)

            val currentTime = (currentHour.hours + currentMinute.minutes).inWholeMilliseconds
            val requiredTime = (hours.hours + minutes.minutes).inWholeMilliseconds

            val requiredDelay =
                if (isSendToday(currentTime, requiredTime))
                    requiredTime - currentTime
                else
                    (24.hours - (currentTime - requiredTime).milliseconds).inWholeMilliseconds

            println("Задержка равна ${millisecondsFormat(requiredDelay.milliseconds)}")

            delay(requiredDelay)
            while (true) {
                println("Ежедневное письмо отправлено")
                sendOfJavaMail()
                delay(24.hours.inWholeMilliseconds)
            }
        }

    fun startWithTimer(hours: Int, minutes: Int) {
        val c = Calendar.getInstance()
        val currentHour = c.get(Calendar.HOUR_OF_DAY)
        val currentMinute = c.get(Calendar.MINUTE)

        val currentTime = (currentHour.hours + currentMinute.minutes).inWholeMilliseconds
        val requiredTime = (hours.hours + minutes.minutes).inWholeMilliseconds

        val requiredDelay =
            if (isSendToday(currentTime, requiredTime))
                requiredTime - currentTime
            else
                (24.hours - (currentTime - requiredTime).milliseconds).inWholeMilliseconds

        println("Задержка равна ${millisecondsFormat(requiredDelay.milliseconds)}")

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                println("Ежедневное письмо отправлено")
                sendOfJavaMail()
            }
        }, requiredDelay, 24.hours.inWholeMilliseconds)
    }

    fun startWithJob(hours: Int, minutes: Int, name: String) {
        val scheduler: Scheduler
        val schedulerFactory: SchedulerFactory = StdSchedulerFactory()
        scheduler = schedulerFactory.scheduler
        scheduler.start()

        val jobId = "job-$name"
        val triggerId = "trigger-$name"

        val jobKey = JobKey.jobKey(jobId)
        scheduler.deleteJob(jobKey)

        val job: JobDetail = JobBuilder.newJob(SendMailJob::class.java)
            .withIdentity(jobId)
            .build()

        val trigger: Trigger = TriggerBuilder.newTrigger()
            .withIdentity(triggerId)
            .withSchedule(
                CronScheduleBuilder.dailyAtHourAndMinute(hours, minutes)
            )
            .build()

        scheduler.scheduleJob(job, trigger)
    }

    fun sendOfJavaMail() {
        val properties = Properties()
        properties["mail.smtp.host"] = EMAIL_HOST
        properties["mail.smtp.port"] = EMAIL_PORT
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.ssl.enable"] = "true"
        val session = Session.getDefaultInstance(properties, null)

        val transport = session.getTransport("smtp")
        transport.connect(EMAIL_USERNAME, EMAIL_PASSWORD)

        val message = MimeMessage(session)
        message.setFrom(InternetAddress(EMAIL_FROM))
        message.setRecipient(Message.RecipientType.TO, InternetAddress(EMAIL_TO))
        message.subject = EMAIL_SUBJECT
        message.setText(EMAIL_PLAIN_TEXT)
        transport.sendMessage(message, message.allRecipients)
    }

    suspend fun sendOfKotlinMailer() {
        val email = emailBuilder {
            from(EMAIL_FROM)
            to(EMAIL_TO)
            withSubject(EMAIL_SUBJECT)
            withPlainText(EMAIL_PLAIN_TEXT)
        }

        val mailer = mailerBuilder(
            host = EMAIL_HOST,
            port = EMAIL_PORT,
            username = EMAIL_USERNAME,
            password = EMAIL_PASSWORD,
            builder = {
                withTransportStrategy(TransportStrategy.SMTPS)
                trustingAllHosts(true)
            })

        email.send(mailer)
    }
}