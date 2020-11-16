package com.donfyy.socket.naive

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.ServerSocket

fun main() {
    // 1.创建服务端ServerSocket，指定要监听的端口
    val serverSocket = ServerSocket()
    serverSocket.bind(InetSocketAddress(61126))
//    val serverSocket = ServerSocket(61126)
    // 2.监听端口，等待来自客户端的连接，连接成功返回一个到客户端的Socket
    val socket = serverSocket.accept()
    // 3.获取输入流读取客户端的消息，获取输出流向客户端发送消息
    val input = BufferedReader(InputStreamReader(socket.getInputStream()))
    var msg = input.readLine()
    while (msg != null) {
        println(msg)
        msg = input.readLine()
    }
//    val output = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
    socket.close()
}
