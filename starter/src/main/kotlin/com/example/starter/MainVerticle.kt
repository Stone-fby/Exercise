package com.example.starter

import io.vertx.cassandra.CassandraClient
import io.vertx.cassandra.CassandraClientOptions
import io.vertx.core.Future
import io.vertx.core.VerticleBase
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import java.net.InetSocketAddress
import java.util.UUID

class MainVerticle : VerticleBase() {
  private lateinit var cassandraClient: CassandraClient

  override fun start(): Future<*> {
    System.setProperty("datastax-java-driver.basic.load-balancing-policy.local-datacenter", "datacenter1")
    // 1️⃣ 配置连接信息 (使用你刚创建的账号)
    val cassandraOptions = CassandraClientOptions()
      .addContactPoint(InetSocketAddress("localhost", 9042))
      .setKeyspace("demo_db") // 直接连接到我们刚创建的 demo_db
      .setUsername("oppuser") // 你的新用户名
      .setPassword("opppassword") // 你的新密码


    cassandraClient = CassandraClient.create(vertx, cassandraOptions)

    val router = Router.router(vertx)

    // 2️⃣ 接口 A: 插入新用户 (GET请求模拟，方便浏览器测试)
    // 访问: http://localhost:8888/add?name=zhangsan&email=zhangsan@example.com

    router.post("/add").handler { ctx ->

      // 2. 从 POST Body 中获取 JSON 数据
      // 假设客户端发送的是: { "name": "Jack", "email": "jack@alibaba.com" }
      val body = ctx.body().asJsonObject()

      // 安全获取字段，如果没有则给默认值
      val name = body.getString("name") ?: "Unknown"
      val email = body.getString("email") ?: "Unknown"
      val id = UUID.randomUUID()

      // 3. 数据库操作 (逻辑不变)
      cassandraClient.prepare("INSERT INTO users (id, name, email) VALUES (?, ?, ?)")
        .compose { preparedStatement ->
          val statement = preparedStatement.bind(id, name, email)
          cassandraClient.execute(statement)
        }
        .onSuccess {
          // 返回 JSON 响应比较规范
          ctx.response()
            .putHeader("content-type", "application/json")
            .end("""{"status": "success", "id": "$id", "name": "$name"}""")
        }
        .onFailure { err ->
          ctx.response()
            .setStatusCode(500)
            .end("❌ Error: ${err.message}")
        }
    }

    // 3️⃣ 接口 B: 查询所有用户
    // 访问: http://localhost:8888/list
    router.get("/list").handler { ctx ->

      // 🔍 执行查询语句
      cassandraClient.executeWithFullFetch("SELECT * FROM users")
        .onSuccess { rows ->
          // 把结果转成 JSON 数组返回
          val userList = rows.map { row ->
            json {
              obj(
                "id" to row.getUuid("id").toString(),
                "name" to row.getString("name"),
                "email" to row.getString("email")
              )
            }
          }

          ctx.response()
            .putHeader("content-type", "application/json")
            .end(userList.toString())
        }
        .onFailure { err ->
          ctx.response().setStatusCode(500).end("❌ Error: ${err.message}")
        }
    }

    // 2️⃣ 接口 A: 插入新用户 (GET请求模拟，方便浏览器测试)
   // 访问: http://localhost:8888/add?name=Tom
    router.get("/ad").handler { ctx ->
      val name = ctx.request().getParam("name") ?: "Unknown"
      val id = UUID.randomUUID().toString() // 生成一个随机ID

      // 📝 执行插入语句
      val query = "INSERT INTO users (id, name, email) VALUES ($id, '$name', 'demo@email.com')"

      cassandraClient.execute(query)
        .onSuccess {
          ctx.response().end("✅ Saved user: $name (ID: $id)")
        }
        .onFailure { err ->
          ctx.response().setStatusCode(500).end("❌ Error: ${err.message}")
        }
    }



    return vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888)
      .onSuccess { println("🚀 Server started at http://localhost:8888") }
  }

  override fun stop(): Future<*> {
    return cassandraClient.close()
  }
}
