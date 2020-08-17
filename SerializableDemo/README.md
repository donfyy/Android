# 序列化



## 基本概念

序列化：将对象转化成字节序列。

反序列化：将字节序列转化成对象。

1.在系统底层，数据以字节序列的方式存储和传输。为了进程间通信，需要将对象序列化，即转化成字节序列。当字节序列到达接收进程时，进程为了识别这些数据，需要反序列化字节序列，即转化成对象。

2.进程间通信，本地数据存储，网络数据传输都离不开序列化。对不同的应用场景选择合适的序列化方案对应用的性能有极大的影响。

3.广义上讲：序列化就是将对象转化成可存储传输的数据格式，反序列化就是将可存储传输的数据格式还原成对象。

4.Java语言提供了Serializable接口，Android提供了Parcelable接口。



不同的计算机语言里，相同的数据结构可以有不同的二进制表示方式。比如C里的字符串可以直接被传输层使用，而Java里的字符串



## 目的

- 永久地保存对象数据
- 在网络上传输数据
- 在进程间传输数据（Activity与Activity之间）
- Jvm运行时对象才能存在，假如需要在jvm停止时保存对象



## 序列化协议特性

### 通用性

- 技术上，协议是否支持跨平台，跨语言。
- 流行程度，序列化和反序列化需要多方参与，很少人使用的协议往往意味着昂贵的学习成本。另一方面，不流行的协议往往缺乏稳定而成熟的跨语言，跨平台的公共包。

### 鲁棒性

- 成熟度不够
- 语言平台的不公平性（这是啥玩意）

### 可调式性/可读性

- 支持不到位
- 访问限制

### 性能

性能包括两方面，时间复杂度和空间复杂度。

- 空间开销（Verbosity），序列化需要在原有的数据上加上描述字段，以为反序列化之用。如果序列化过程引入的额外开销过高，会给磁盘和网络带来压力。对于海量分布式存储系统，数据量往往以TB为单位，巨大的额外空间开销意味着高额的成本。
- 时间开销（Complexity），复杂的协议会导致较长的解析时间，这可能会使序列化和反序列化阶段成为整个系统的瓶颈。

### 可扩展性和兼容性

移动互联网时代，业务系统需求的更新周期变得更快。新的需求不断涌现，老的系统还要继续维护。如果序列化协议具有更好的可扩展性，支持自动增加新的业务字段，而不影响老的业务，这将大大提升系统的灵活度。

### 安全性/访问限制

在序列化选型的过程中，安全性的考虑往往发生在跨局域网访问的场景。当通讯发生在公司之间或者跨机房的时候，出于安全性的考虑，对跨局域网的访问往往被限制为基于http/https的80和443端口。如果使用的序列化协议没有兼容而成熟的http传输层框架支持，可能会导致以下三种结果之一：

- 因为访问限制而降低服务可用性
- 被迫重新实现安全协议而导致实施成本提高
- 开放更多的端口和协议访问，牺牲安全性



Android的Parcelable也有安全漏洞

### 参考链接

[漏洞预警]: https://www.anquanke.com/post/id/103570



## 常见的序列化和反序列化协议

### XML&SOAP

xml是一种常见的序列化和反序列化协议，具有跨机器，跨语言的特点。

soap(simple object access protocol)是一种被广泛应用的基于xml的结构化消息传递协议。

### JSON (Javascript object notation)

json起源于弱类型语言javascript，它的产生来自于一种被称为“Assosiative array"的概念。本质就是采用“Attribute-Value"的方式来描述对象。在javascript和PHP中类的描述方式就是Associative array。

json的如下优点，是使它成为最广泛使用的序列化协议之一。

- Associative array 符合工程师对对象的理解
- 具有XML易读性的优点
- 与XML相比更加简洁。序列化后的XML文件大小接近json文件的两倍
- 与XML相比协议简单，解析速度更快
- javascript原生支持，被广泛应用于浏览器应用中，是ajax的事实标准协议。
- associative array使得其具有更好的兼容性和可扩展性

### ProtoBuf

ProtoBuf具备了众多的优秀协议所需的众多典型特征。

- 标准的IDL和IDL编译器，这使得其对工程师非常友好
- 序列化数据非常简洁紧凑。与XML相比，序列化后的数据量约为1/3到1/10
- 解析速度也快，比XML快20-100倍
- 提供了友好的动态库，使用简单



# Android程序员该如何选择序列化方案

## Serializable接口

```java
public interface Serializable {
}
```

Serializable用来标示当前类可以被ObjectOutputStream序列化，以及被ObjectInputStream反序列化。

```java
public class Student implements Serializable {
  	// serialVersionUID 当前类的版本号
  	private static final long serialVersionUID = 1;
  	// Course也需要实现Serializable接口
  	private List<Course> courses;
  	// transient标记的成员变量不参与序列化
  	private transient Date createTime;
  	// 静态成员变量不参与序列化
  	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
}

public class Course implements Serializable {
  	...
}
```

Serializable的特点

- 可序列化类中，未实现Serializable的成员变量/属性/状态无法序列化与反序列化
- 反序列化一个类的过程中，非可序列化的属性将会调用无参构造函数重新创建，因此这个属性的无参构造函数必须可以被访问，否则运行时出错。
- 实现Serializable接口的子类也是可以序列化的。

