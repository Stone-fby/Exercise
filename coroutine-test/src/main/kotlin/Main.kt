package org.example

// 导入 kotlin.time.Duration 以秒为单位表示持续时间
import kotlin.time.Duration.Companion.seconds

import kotlinx.coroutines.*



suspend fun main() {
    println("[0] Main 开始运行: ${Thread.currentThread().name}")
    withContext(Dispatchers.Default) {
        this.launch {
            println("[1] 进入 withContext: ${Thread.currentThread().name}")
        }

        withContext(Dispatchers.Default) {

        }
        // --- 第一阶段：Boss 进入 coroutineScope ---
        coroutineScope {
            // 启动大儿子
            this.launch {
                // 启动孙子
                this.launch {
                    println("[A-1] 孙子协程 醒来: ${Thread.currentThread().name}")
                }
                println("[A] 大儿子协程 启动完毕: ${Thread.currentThread().name}")
            }

            // 启动二儿子
            this.launch {
                this.launch {
                    println("[B-1] 孙子协程 醒来: ${Thread.currentThread().name}")
                }
                println("[B] 二儿子协程 醒来: ${Thread.currentThread().name}")
            }

            // Boss 自己的活
            println("[Boss] 我在 Scope 内部打印: ${Thread.currentThread().name}")
        }

        // --- 墙：Boss 必须在这里等上面所有人都干完，才能往下走 ---
        println("[2] coroutineScope 结束了，Boss 继续走: ${Thread.currentThread().name}")

        // --- 第二阶段：Boss 启动三儿子 ---
        this.launch {
            println("[C] 三儿子协程 (Scope 之外): ${Thread.currentThread().name}")
        }
    }


}
