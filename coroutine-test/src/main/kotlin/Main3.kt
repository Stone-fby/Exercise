package org.example

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds










suspend fun main() {
    withContext(Dispatchers.Default) {
        // 启动 50,000 个协程，每个协程等待五秒，然后打印一个点
        printPeriods()
    }
}

suspend fun printPeriods() = coroutineScope { // this: CoroutineScope
    // 启动 50,000 个协程，每个协程等待五秒，然后打印一个点
    repeat(20) {
        this.launch {
            delay(2.seconds)
            println("."+ Thread.currentThread().name)
        }
    }
}
