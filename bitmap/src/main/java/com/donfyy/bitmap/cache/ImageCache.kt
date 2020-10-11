package com.donfyy.bitmap.cache

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.LruCache
import com.blankj.utilcode.util.LogUtils
import com.donfyy.bitmap.BuildConfig
import com.donfyy.bitmap.cache.disk.DiskLruCache
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.util.*

object ImageCache {
    private lateinit var lruCache: LruCache<String, Bitmap?>

    // 复用池，用于复用Bitmap占据的内存
    private lateinit var reusablePool: MutableSet<WeakReference<Bitmap>>
    private lateinit var diskLruCache: DiskLruCache
    fun init(context: Context, dir: String) {
        reusablePool = Collections.synchronizedSet(HashSet())
        // 初始化内存缓存
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 返回应用占据的内存大小，单位：MB
        val memoryClass = am.memoryClass
        // memoryClass * 1024 * 1024 / 8
        lruCache = object : LruCache<String, Bitmap?>(135600 * 3 /*memoryClass * 1024 * 1024 / 8*/) {
            // 返回的一张图片大小
            override fun sizeOf(key: String, value: Bitmap?): Int {
                LogUtils.d(value?.allocationByteCount)
                return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    // 注意，此处应该返回该Bitmap所占据的内存大小
                    value?.allocationByteCount ?: 0
                } else {
                    // 返回Bitmap像素所占用的内存大小
                    value?.byteCount ?: 0
                }
            }

            override fun entryRemoved(evicted: Boolean, key: String?, oldValue: Bitmap?, newValue: Bitmap?) {
                oldValue?.let {
                    if (oldValue.isMutable) {
                        // 3.0 bitmap 缓存 native
                        // <8.0  bitmap 缓存 java
                        // 8.0 native
                        // 将从内存缓存中移除的可修改Bitmap加入到复用池用来复用内存
                        reusablePool.add(WeakReference(oldValue, getReferenceQueue()))
                    } else {
                        oldValue.recycle()
                    }
                }
            }
        }
        try {
            // 初始化磁盘缓存
            diskLruCache = DiskLruCache.open(File(dir), BuildConfig.VERSION_CODE, 1, 10 * 1024 * 1024.toLong())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 放入磁盘缓存
     */
    fun putBitmap2Disk(key: String, bitmap: Bitmap) {
        var snapshot: DiskLruCache.Snapshot? = null
        var os: OutputStream? = null
        try {
            snapshot = diskLruCache.get(key)
            if (snapshot == null) {
                val edit = diskLruCache.edit(key)
                if (edit != null) {
                    os = edit.newOutputStream(0)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os)
                    edit.commit()
                }
            }
        } catch (e: IOException) {
            LogUtils.e(e)
        } finally {
            snapshot?.close()
            if (os != null) {
                try {
                    os.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 从磁盘缓存获取bitmap
     *
     * @param key
     * @param reusable
     * @return
     */
    fun getBitmapFromDisk(key: String, reusable: Bitmap?): Bitmap? {
        var snapshot: DiskLruCache.Snapshot? = null
        var bitmap: Bitmap? = null
        try {
            snapshot = diskLruCache.get(key)
            if (snapshot == null) {
                return null
            }
            val inputStream = snapshot.getInputStream(0)
            val options = BitmapFactory.Options()
            options.inMutable = true
            options.inBitmap = reusable
            bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            if (bitmap != null) {
                lruCache.put(key, bitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            snapshot?.close()
        }
        return bitmap
    }

    private var referenceQueue: ReferenceQueue<Bitmap>? = null
    var shutDown = false
    private fun getReferenceQueue(): ReferenceQueue<Bitmap> {
        if (referenceQueue == null) {
            referenceQueue = ReferenceQueue()
            Thread(Runnable {
                while (!shutDown) {
                    try {
                        val remove = referenceQueue!!.remove()
                        val bitmap = remove.get()
                        if (bitmap != null && !bitmap.isRecycled) {
                            bitmap.recycle()
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }).start()
        }
        return referenceQueue!!
    }

    /**
     * 把bitmap 放入内存
     *
     * @param key
     * @param bitmap
     */
    fun putBitmap2Memory(key: String, bitmap: Bitmap) {
        lruCache.put(key, bitmap)
    }

    /**
     * 从内存中取出bitmap
     *
     * @param key
     * @return
     */
    fun getBitmapFromMemory(key: String): Bitmap? {
        return lruCache.get(key)
    }

    fun clearMemory() {
        lruCache.evictAll()
    }

    /**
     * 3.0 之前不能复用
     * 3.0-4.4 宽高一样，inSampleSize = 1
     * 4.4 只要小于等于就行了
     *
     * @param w
     * @param h
     * @param inSampleSize
     * @return
     */
    fun getReusable(w: Int, h: Int, inSampleSize: Int): Bitmap? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return null
        }
        var reusable: Bitmap? = null
        val iterator = reusablePool.iterator()
        while (iterator.hasNext()) {
            val bitmap = iterator.next().get()
            if (bitmap != null) {
                if (checkInBitmap(bitmap, w, h, inSampleSize)) {
                    reusable = bitmap
                    iterator.remove()
                    break
                }
            } else {
                iterator.remove()
            }
        }
        return reusable
    }

    /**
     * 校验bitmap 是否满足条件
     *
     * @param bitmap
     * @param w
     * @param h
     * @param inSampleSize
     * @return
     */
    private fun checkInBitmap(bitmap: Bitmap, w: Int, h: Int, inSampleSize: Int): Boolean {
        var w = w
        var h = h
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return bitmap.width == w && bitmap.height == h && inSampleSize == 1
        }
        if (inSampleSize > 1) {
            w /= inSampleSize
            h /= inSampleSize
        }
        val byteCount = w * h * getBytesPerPixel(bitmap.config)
        // 图片内存 系统分配内存
        return byteCount <= bitmap.allocationByteCount
    }

    /**
     * 通过像素格式计算每一个像素占用多少字节
     *
     * @param config
     * @return
     */
    private fun getBytesPerPixel(config: Bitmap.Config): Int {
        return if (config == Bitmap.Config.ARGB_8888) {
            4
        } else 2
    }
}