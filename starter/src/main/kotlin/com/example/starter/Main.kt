package com.example.starter

import io.vertx.core.Vertx

fun main() {
  val vertx = Vertx.vertx()

  // ✅ 正确写法
  vertx.deployVerticle(MainVerticle::class.java.name)
    .onSuccess { deploymentId ->
      println("📦 Deployed MainVerticle with ID: $deploymentId")
    }
    .onFailure { error ->
      println("❌ Failed to deploy: ${error.message}")
    }
}
