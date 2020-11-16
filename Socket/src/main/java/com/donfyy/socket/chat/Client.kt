package com.donfyy.socket.chat

import java.io.*
import java.net.InetAddress
import java.net.Socket

fun main() {
    val s = Socket(InetAddress.getLocalHost(), 61126)
    // 从服务端读取信息
    val input = BufferedReader(InputStreamReader(s.getInputStream()))
    println(input.readLine())
    // 向服务端发送信息
    val output = PrintWriter(BufferedWriter(OutputStreamWriter(s.getOutputStream())))
    val input2 = BufferedReader(InputStreamReader(System.`in`))
    println("客户端运行。。。")
    var msg = input2.readLine()
    while ("exit" != msg) {
        output.println(msg)
        output.flush()
        println(input.readLine())
        msg = input2.readLine()
    }
    output.println(msg)
    output.flush()
    println("客户端退出。。。")
}