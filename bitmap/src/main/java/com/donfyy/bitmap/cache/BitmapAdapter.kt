package com.donfyy.bitmap.cache

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.donfyy.bitmap.R
import com.donfyy.bitmap.cache.BitmapAdapter.BitmapViewHolder

class BitmapAdapter(private val context: Context) : RecyclerView.Adapter<BitmapViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BitmapViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_item, viewGroup, false)
        return BitmapViewHolder(view)
    }

    override fun onBindViewHolder(bitmapViewHolder: BitmapViewHolder, i: Int) {

        // 原始方法获取bitmap
//        val bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_mv_w);

        // 第一种优化
//        Bitmap bitmap = ImageResize.resizeBitmap(context, R.drawable.icon_mv, 80, 80, false);

        // 第二种优化
        var bitmap = ImageCache.getBitmapFromMemory(i.toString())
        LogUtils.d("使用内存缓存$bitmap")
        if (bitmap == null) {
            val reusable = ImageCache.getReusable(60, 60, 1)
            LogUtils.d("使用复用缓存$reusable")
            bitmap = ImageCache.getBitmapFromDisk(i.toString(), reusable)
            LogUtils.d("使用磁盘缓存$bitmap")
            if (bitmap == null) {
                // 网络获取
                bitmap = ImageResize.resizeBitmap(context, R.drawable.icon_mv, 80, 80, false, reusable)
//                bitmap = ImageResize.resizeBitmap(context, R.drawable.icon_mv, 80, 80, false, null)
                // 放入内存
                ImageCache.putBitmap2Memory(i.toString(), bitmap)
                // 放入磁盘
                ImageCache.putBitmap2Disk(i.toString(), bitmap)
            }
        }
        bitmapViewHolder.iv.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return 1000
    }

    class BitmapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv: ImageView = itemView.findViewById(R.id.iv)
    }
}