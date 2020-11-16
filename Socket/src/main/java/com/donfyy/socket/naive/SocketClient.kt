package com.donfyy.socket.naive

import java.io.PrintWriter
import java.net.Socket

fun main() {
    // 创建一个客户端Socket，指定要连接的服务端的ip地址和端口
    val socket = Socket("127.0.0.1", 61126)
    // 获取输出流，向服务端发送消息
    val output = PrintWriter(socket.getOutputStream())
    output.println("hello server!")
    // 将在客户端缓冲队列里的数据发送到服务端
    output.flush()
    // 关闭输出流
    socket.shutdownOutput()
    // 关闭连接
    socket.close()
}
