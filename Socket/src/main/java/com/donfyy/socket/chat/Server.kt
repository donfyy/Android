package com.donfyy.socket.chat

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

fun main() {
    val ss = ServerSocket(61126)
    val clients = CopyOnWriteArrayList<Socket>()
    val executor = Executors.newCachedThreadPool()
    println("服务端运行中....")
    while (true) {
        val client = ss.accept()
        clients += client
        executor.execute {
            val id = clients.size
            println("客户端 $id 连接了：${client.inetAddress}")
            val input = BufferedReader(InputStreamReader(client.getInputStream()))
            val output = PrintWriter(client.getOutputStream())
            output.println("hi: number ${clients.size}")
            output.flush()
            var msg = input.readLine()
            while ("exit" != msg) {
                output.println(msg)
                output.flush()
                msg = input.readLine()
            }

            println("客户端 $id 关闭了 $msg")
            client.close()
            clients -= client
        }
    }
}