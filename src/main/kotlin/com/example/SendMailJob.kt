package com.example

import org.quartz.Job
import org.quartz.JobExecutionContext

class SendMailJob : Job {

    override fun execute(context: JobExecutionContext?) {
        println("Ежедневное письмо отправлено")
        Utils.sendOfJavaMail()
//        CoroutineScope(Dispatchers.IO).launch {
//            Utils.sendOfKotlinMailer()
//        }
    }

}