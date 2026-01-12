package org.example

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.*







// 定义一个挂起函数
suspend fun greet() {
    println("The greet() on the thread: ${Thread.currentThread().name}")
    println("The greet()2 on the thread: ${Thread.currentThread().name}")
    // 碰到delay就会释放线程
    delay(duration = 2.seconds)
    // 这里的 delay() 函数模拟一个挂起 API 调用
    // 可以在这里添加挂起 API 调用, 例如网络请求
}

suspend fun main() {
    //最先打印
    println("Main start on: ${Thread.currentThread().name}")

    // 在共享线程池上运行此代码块中的代码
    withContext(Dispatchers.Default) { // this: CoroutineScope
        this.launch {
            greet()
        }

        // 启动另一个协程
        this.launch {
            println("The CoroutineScope.launch() on the thread: ${Thread.currentThread().name}")
            delay(duration = 1.seconds)
        }

        println("The withContext() on the thread: ${Thread.currentThread().name}")
    }
    withContext(Dispatchers.Default) {
        this.launch {
            println("The runBlocking() on the thread: ${Thread.currentThread().name}")
        }
    }
    //最后执行吗
    println("The coroutineContext() on the thread: ${Thread.currentThread().name}")
}