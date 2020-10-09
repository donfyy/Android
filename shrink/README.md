# 瘦身

## svg矢量图

## 动态库打包配置

so库是ndk编译出来的动态库，由c/c++编写，无法跨平台，每一个平台都有一个对应的so库。

每个cpu架构都对应着一个abi:armeabi，armeabi-v7a，arm64- v8a，x86，x86_64，mips

一般我们只要配置 armeabi-v7a即可。

```groovy
android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a"
        }
    }
}
```

## 赢钱宝瘦身

不瘦身：
53MB

so库分离：
31MB

png图片压缩

