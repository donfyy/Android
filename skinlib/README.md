# 插件化换肤

## 原理

### 收集界面上所有的View

#### setContentView的源码

```java
class Activity {
    public void setContentView(@LayoutRes int layoutResID) {
        getWindow().setContentView(layoutResID);
        initWindowDecorActionBar();
    }
}
class PhoneWindow {
    @Override
    public void setContentView(int layoutResID) {
        // mContentParent 只在 installDecor() 中赋值
        if (mContentParent == null) {
            // 1.新建一个DecorView
            // 2.根据主题配置加载相应的xml文件，配置系统UI
            // 3.给mContentParent赋值
            installDecor();
        } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            // ...
        }
        if (hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            // ...
        } else {
            // 将xml文件转换成view树，并将该view树添加到mContentParent中 
            mLayoutInflater.inflate(layoutResID, mContentParent); // A
        }
        // ...
    }
}

class LayoutInflater {
    // A处的方法最终会调用到该方法
    public View inflate(XmlPullParser parser, @Nullable ViewGroup root, boolean attachToRoot) {
        // ...
        final Context inflaterContext = mContext;
        final AttributeSet attrs = Xml.asAttributeSet(parser);
        Context lastContext = (Context) mConstructorArgs[0];
        mConstructorArgs[0] = inflaterContext;
        View result = root;
        // ...
        // 处理merge标签
        if (TAG_MERGE.equals(name)) {
            // ... 
            rInflate(parser, root, inflaterContext, attrs, false);
        } else {
            // 首先生成view树的根结点
            final View temp = createViewFromTag(root, name, inflaterContext, attrs);
            // ...
            // 递归的构建整个view树
            rInflateChildren(parser, temp, attrs, true);
            // ...
            // 如果父节点存在并且需要将view树添加到父节点，则返回父节点
            if (root == null || !attachToRoot) {
                result = temp;
            }
        }
        // ...
        return result;
    }
    
     View createViewFromTag(View parent, String name, Context context, AttributeSet attrs,
             boolean ignoreThemeAttr) {
         // ...
         try {
             // 第一次尝试创建View
             View view = tryCreateView(parent, name, context, attrs);
             if (view == null) {
                 final Object lastContext = mConstructorArgs[0];
                 mConstructorArgs[0] = context;
                 try {
                     // 第二次尝试创建View
                     if (-1 == name.indexOf('.')) {
                         // 如果创建的是系统内置的view，则为name补齐前缀，然后调用createView
                         // createView(name, "android.view.", attrs)
                         // onCreateView(String name, AttributeSet attrs) ，
                         // 该方法被PhoneLayoutInflater子类覆盖 ，以优先使用兼容包中的View
                         view = onCreateView(context, parent, name, attrs);
                     } else {
                         view = createView(context, name, null, attrs);
                     }
                 } finally {
                     mConstructorArgs[0] = lastContext;
                 }
             }
             return view;
         } catch (Exception e) {
             // ...
         }
     }   

    public final View tryCreateView(@Nullable View parent, @NonNull String name,
        @NonNull Context context,
        @NonNull AttributeSet attrs) {
        // ...
        View view;
        // 依次调用mFactory2，mFactory创建name对应的view对象
        // 我们可以设置自定义的Factory然后接管view的创建过程，
        // 从而收集到该显示在Activity上的所有的View
        if (mFactory2 != null) {
            view = mFactory2.onCreateView(parent, name, context, attrs);
        } else if (mFactory != null) {
            view = mFactory.onCreateView(name, context, attrs);
        } else {
            view = null;
        }
        if (view == null && mPrivateFactory != null) {
            view = mPrivateFactory.onCreateView(parent, name, context, attrs);
        }
        return view;
    }
    // 主要通过反射创建对应View的实例
    public final View createView(@NonNull Context viewContext, @NonNull String name,
            @Nullable String prefix, @Nullable AttributeSet attrs)
            throws ClassNotFoundException, InflateException {
        // ...
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        // ...
        Class<? extends View> clazz = null;
        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                // 通过反射获取View对应的Class字节码
                clazz = Class.forName(prefix != null ? (prefix + name) : name, false,
                        mContext.getClassLoader()).asSubclass(View.class);

                // ...
                // 然后获取View的第二个构造方法 View(Context.class, AttributeSet.class)
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name, constructor);
            } else {
                // ...
            }
            Object lastContext = mConstructorArgs[0];
            mConstructorArgs[0] = viewContext;
            Object[] args = mConstructorArgs;
            args[1] = attrs;

            try {
                // 通过反射调用构造方法创建View
                final View view = constructor.newInstance(args);
                if (view instanceof ViewStub) {
                    // Use the same context when inflating ViewStub later.
                    final ViewStub viewStub = (ViewStub) view;
                    viewStub.setLayoutInflater(cloneInContext((Context) args[0]));
                }
                return view;
            } finally {
                mConstructorArgs[0] = lastContext;
            }
        } catch (Exception e) {
            // ...
        }
    }
}
```

#### 注册Activity的生命周期回调方法并收集需要换肤的View

```kotlin
    @JvmStatic
    // 初始化插件化换肤框架
    fun init(app: Application) {
        context = app
        // 使用SharedPreference保存当前用户使用的皮肤
        sp = SkinPreference(app)
        // 注册Activity的生命周期回调，实现Activity无入侵性换肤
        app.registerActivityLifecycleCallbacks(ActivityLifecycleCallback()) // B
        // 加载用户选择的皮肤
        loadSkin(sp.skinPath)
    }
```

```java
class Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // ...
        // Activity在这里分发create事件
        dispatchActivityCreated(savedInstanceState);
        // ... 
    }

    private void dispatchActivityCreated(@Nullable Bundle savedInstanceState) {
        // 该方法将create事件分发给Application，因此在上文B处的回调会收到通知
        // 注意，此方法是父类中的方法，会先于Activity子类的onCreate方法体的执行
        // 因此我们可以在setContentView被调用之前设置好LayoutInflaterFactory
        getApplication().dispatchActivityCreated(this, savedInstanceState);
        // ...
    }
}
```

```kotlin
class ActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
       try {
            // 创建自定义的工厂
            val factory = LayoutInflaterFactory()
            // 通过反射修改mFactorySet的值，注意在29的源码中该字段不可以被反射，但是mFactory2仍然可以，
            // 因此需要在适配29
            if (sLayoutInflaterFactory2Field == null) {
                sLayoutInflaterFactory2Field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            }
            sLayoutInflaterFactory2Field?.let {
                it.isAccessible = true
                it.set(activity.layoutInflater, false)
            }
            // 把工厂放到activity的LayoutInflater中，该方法内部的实现反射了mFactory2字段。。
            LayoutInflaterCompat.setFactory2(activity.layoutInflater, factory)
            // 保存工厂与activity的映射关系，以便在activity销毁时移除该工厂
            map[activity] = factory
            // 观察SkinManager，以便在用户点击换肤按钮时更新view
            SkinManager.addObserver(factory)
        } catch (e: NoSuchFieldException) {
        }
    }
    override fun onActivityDestroyed(activity: Activity) {
        // 状态清理
        val factory = map.remove(activity)
        SkinManager.deleteObserver(factory)
    }
}
```

```kotlin
// 该工厂用来接管view的创建过程，以便在view创建后，该view需要换肤的属性。
class LayoutInflaterFactory : LayoutInflater.Factory2 {
    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return if (-1 == name.indexOf('.')) {
            // 如果创建的是系统内置的view，则为name补齐前缀，然后调用createView
            // createView(name, "android.view.", attrs)
            // onCreateView(String name, AttributeSet attrs) ，
            // 该方法被PhoneLayoutInflater子类覆盖 ，以优先使用兼容包中的View
            createSdkView(context, parent, name, attrs)
        } else {
            createView(context, name, null, attrs)
        }
    }

    private fun createSdkView(context: Context, parent: View?, name: String, attrs: AttributeSet): View? {
        sClassPrefixList.forEach {
            val view = createView(context, name, it, attrs)
            if (view != null) {
                return view
            }
        }
        return null
    }

    private fun createView(viewContext: Context, name: String, prefix: String?, attr: AttributeSet): View? {
        var constructor: Constructor<out View?>? = sConstructorMap[name]
        var clazz: Class<out View?>? = null
        return try {
            if (constructor == null) {
                // 通过反射获取View对应的Class字节码
                clazz = Class.forName(if (prefix != null) prefix + name else name, false,
                        viewContext.classLoader).asSubclass(View::class.java)
                // 然后获取View的第二个构造方法 View(Context.class, AttributeSet.class)
                constructor = clazz.getConstructor(*sConstructorSignature)
                constructor.isAccessible = true
                sConstructorMap[name] = constructor
            }
            // 通过反射调用构造方法创建View
            constructor.newInstance(viewContext, attr).apply { 
                // 构造出view后，开始收集该view上需要换肤的属性
                // 注意需要换肤的属性是预定义在主app中的，不可以随意变更。
                this?.let { skinViewManager.look(it, attr) } 
            }
        } catch (e: Exception) {
        }
    }
}
```

```kotlin
package com.donfyy.skinlib

import android.util.AttributeSet
import android.view.View
import com.donfyy.skinlib.utils.ThemeUtils

class SkinViewManager {
    private val skinViews = mutableListOf<SkinView>()
    // 查找view在xml中用到的属性值并保存到集合中，收集某个view上需要换肤的属性集合
    fun look(view: View, attrs: AttributeSet) {
        val map = HashMap<String, Int>()
        for (i in 0 until attrs.attributeCount) {
            val attrName = attrs.getAttributeName(i)
            // skinAttrs预置了需要换肤的属性 如 "background" "src" 等
            if (attrName !in skinAttrs) continue
            val attrValue = attrs.getAttributeValue(i)
            val resId = when {
                attrValue.startsWith("@") -> attrValue.substring(1).toInt()
                attrValue.startsWith("?") -> {
                    // 使用属性引用的资源
                    ThemeUtils.getResId(view.context, intArrayOf(attrValue.substring(1).toInt()))[0]
                }
                // 硬编码的属性值无法改变
                else -> 0
            }
            if (resId != 0) {
                map[attrName] = resId
            }
        }
        if (map.isNotEmpty() || view is SkinViewSupport) {
            // 不得不说，操作符重载加上高阶函数真是香
            skinViews += SkinView(view, map).apply { applySkin() }
        }
    }
}
```

至此，完成了在Activity上的所有需要换肤view的收集。该方案的优点在于对activity无侵入性。

### 加载插件中的皮肤资源

#### 系统是如何加载APK中的资源的？

```java
class Context {
    // 从getDrawable开始分析系统是如何获取到drawable的
    public final Drawable getDrawable(@DrawableRes int id) {
        // 该Resources实例是在ContextImpl中赋值的
        return getResources().getDrawable(id, getTheme());
    }       
}
class Resources {
    // getDrawable最终调用到该方法
    public Drawable getDrawableForDensity(@DrawableRes int id, int density, @Nullable Theme theme) {
        final TypedValue value = obtainTempTypedValue();
        try {
            final ResourcesImpl impl = mResourcesImpl;
            // 这里可以看到Resources从 ResourcesImpl 去获取数据
            impl.getValueForDensity(id, density, value, true);
            return impl.loadDrawable(this, value, id, density, theme);
        } finally {
            releaseTempTypedValue(value);
        }
    }
}
class ResourcesImpl {
    // ResourcesImpl 则从 mAssets 处获取数据 mAssets 是一个 AssetManager 的对象
    // 因此可以得出结论 AssetManager 负责apk资源的加载与获取 
    // 接下来查看 AssetManager 是如何创建的
    // 从这里可以看出一个Resources有一个ResourcesImpl，一个ResourcesImpl又有一个AssetManager // B
    void getValueForDensity(@AnyRes int id, int density, TypedValue outValue,
            boolean resolveRefs) throws NotFoundException {
        boolean found = mAssets.getResourceValue(id, density, outValue, resolveRefs);
        if (found) {
            return;
        }
        throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id));
    }
}
class ContextImpl {
    static ContextImpl createAppContext(ActivityThread mainThread, LoadedApk packageInfo,
            String opPackageName) {
        if (packageInfo == null) throw new IllegalArgumentException("packageInfo");
        ContextImpl context = new ContextImpl(null, mainThread, packageInfo, null, null, null, 0,
                null, opPackageName);
        // setResources方法用于设置一个Resources实例，该方法在创建各种上下文中被调用
        // 如createActivityContext,createSystemUiContext
        // 接下来查看LoadedApk中的Resources是如何创建的
        context.setResources(packageInfo.getResources());
        return context;
    }
}
class LoadedApk {
    public Resources getResources() {
        if (mResources == null) {
            final String[] splitPaths;
            // ...
            mResources = ResourcesManager.getInstance().getResources(null, mResDir, // A
                    splitPaths, mOverlayDirs, mApplicationInfo.sharedLibraryFiles,
                    Display.DEFAULT_DISPLAY, null, getCompatibilityInfo(),
                    getClassLoader());
        }
        return mResources;
    }
}
class ResourcesManager {
    public @Nullable Resources getResources(@Nullable IBinder activityToken,
            @Nullable String resDir,
            @Nullable String[] splitResDirs,
            @Nullable String[] overlayDirs,
            @Nullable String[] libDirs,
            int displayId,
            @Nullable Configuration overrideConfig,
            @NonNull CompatibilityInfo compatInfo,
            @Nullable ClassLoader classLoader) {
        try {
            final ResourcesKey key = new ResourcesKey(
                    resDir, // 应用apk文件的路径
                    splitResDirs,
                    overlayDirs,
                    libDirs,
                    displayId,
                    overrideConfig != null ? new Configuration(overrideConfig) : null, // Copy
                    compatInfo);
            classLoader = classLoader != null ? classLoader : ClassLoader.getSystemClassLoader();
            return getOrCreateResources(activityToken, key, classLoader);
        } finally {
            Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
        }
    }

    private @Nullable Resources getOrCreateResources(@Nullable IBinder activityToken,
            @NonNull ResourcesKey key, @NonNull ClassLoader classLoader) {
        synchronized (this) {
            // ... 
            // 下面的代码创建了一个新的Resources对象
            ResourcesImpl resourcesImpl = createResourcesImpl(key); // C
            if (resourcesImpl == null) {
                return null;
            }
            // Add this ResourcesImpl to the cache.
            mResourceImpls.put(key, new WeakReference<>(resourcesImpl));
            final Resources resources;
            if (activityToken != null) {
                resources = getOrCreateResourcesForActivityLocked(activityToken, classLoader,
                        resourcesImpl, key.mCompatInfo);
            } else {
                // A处传入的参数为null，因此activityToken 为空执行如下代码
                resources = getOrCreateResourcesLocked(classLoader, resourcesImpl, key.mCompatInfo);
            }
            return resources;
        }
    }
    private @NonNull Resources getOrCreateResourcesLocked(@NonNull ClassLoader classLoader,
            @NonNull ResourcesImpl impl, @NonNull CompatibilityInfo compatInfo) {
        // ...
        // 创建了一个新的Resources
        Resources resources = compatInfo.needsCompatResources() ? new CompatResources(classLoader)
                : new Resources(classLoader);
        // 使用了传进来的ResourcesImpl对象，
        // 根据B处的结论，回到C处进入createResourcesImpl方法查看ResourcesImpl是如何创建的
        resources.setImpl(impl);
        // ...
        return resources;
    }
    private @Nullable ResourcesImpl createResourcesImpl(@NonNull ResourcesKey key) {
        final DisplayAdjustments daj = new DisplayAdjustments(key.mOverrideConfiguration);
        daj.setCompatibilityInfo(key.mCompatInfo);
        final AssetManager assets = createAssetManager(key); // D
        if (assets == null) {
            return null;
        }
        final DisplayMetrics dm = getDisplayMetrics(key.mDisplayId, daj);
        final Configuration config = generateConfig(key, dm);
        // ResourcesImpl对象在此处创建出来了，同时传入了上文D处创建出来的AssetManager对象
        final ResourcesImpl impl = new ResourcesImpl(assets, dm, config, daj);
        // ...
        return impl;
    }
    // 这个方法创建了一个AssetManager，并加载了ResourcesKey中的资源
    protected @Nullable AssetManager createAssetManager(@NonNull final ResourcesKey key) {
        final AssetManager.Builder builder = new AssetManager.Builder();
        if (key.mResDir != null) {
            try {
                // 在这里将应用 apk 的路径添加到 AssetManager 中
                // 这里的源码做了修改 Android8调用的是 AssetManager的构造方法，然后调用 AssetManager.addAssetPath
                // 幸运地是无参构造方法与addAssetPath并没有被删除，因此我们可以使用这两个方法创建一个AssetManager
                builder.addApkAssets(loadApkAssets(key.mResDir, false /*sharedLib*/, false /*overlay*/));
            } catch (IOException e) {
                Log.e(TAG, "failed to add asset path " + key.mResDir);
                return null;
            }
        }
        // ...
        return builder.build();
    }
}

```
#### 加载皮肤包中的资源

经过上一小节的分析，我们可以利用AssetManager创建出一个用于加载皮肤包中的Resources对象

```kotlin
    @JvmStatic
    fun loadSkin(skinPath: String?) {
        if (skinPath.isNullOrEmpty()) {
            resourceManager = ResourceManager(context.resources)
            sp.reset()
        } else {
            try {
                val appResources = context.resources
                // 通过反射创建一个AssetManager对象
                val assetManager = AssetManager::class.java.newInstance()
                // 调用addAssetPath方法加载apk中的资源
                val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
                addAssetPath.invoke(assetManager, skinPath)
                // 至此我们就得到了可以加载皮肤包中资源的对象，那么我们应该如何读取皮肤包中的资源呢？
                val skinResource = Resources(assetManager, appResources.displayMetrics, appResources.configuration)
                val packageName = context.packageManager?.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)?.packageName
                sp.skinPath = skinPath
                resourceManager = ResourceManager(appResources, skinResource, packageName)
            } catch (e: Exception) {
                resourceManager = ResourceManager(context.resources)
                sp.reset()
                LogUtils.d("Exception: ", e)
            }
        }
        setChanged()
        notifyObservers()
    }
```

### 应用插件中的皮肤资源

res目录下的资源文件会被aapt打包至apk里。aapt在编译打包资源文件时会执行以下两个操作

- 为每一个非assets资源生成一个id，该id以常量的形式定义在R.java中
- 生成resources.arsc文件，描述具有id值的资源的配置信息，也就是一个资源索引表

有了资源id，我们就可以通过 ```java resources.getColor(R.color.colorPrimary); ``` 这种方式获取资源的信息。
问题在于皮肤apk中与宿主apk中同名资源对应的id并不相同，那么如何获取皮肤apk中的同名资源呢？
Resources里提供了如下API：
```java
class Resources {
    // 一个资源的名字具有如下格式："package:type/entry". 该API根据资源的名字返回了资源的id
    public int getIdentifier(String name, String defType, String defPackage) {
        return mResourcesImpl.getIdentifier(name, defType, defPackage);
    }
    // 根据资源id返回资源名
    public String getResourceEntryName(@AnyRes int resid) throws NotFoundException {
        return mResourcesImpl.getResourceEntryName(resid);
    }
    // 根据资源id返回资源类型名
    public String getResourceTypeName(@AnyRes int resid) throws NotFoundException {
        return mResourcesImpl.getResourceTypeName(resid);
    }
}
```
因此我们可以利用上述API，先获取宿主apk中的资源id(resId)对应的资源名(name)及资源类型(type)，
然后再根据该资源名(name)和资源类型(type)获取皮肤中的资源id(skinResId)。
然后再去拿着皮肤中的资源id(skinResId)去加载皮肤中的资源。
这就是加载皮肤资源的核心原理。

```kotlin

class ResourceManager(private val appResources: Resources,
                      private val skinResources: Resources? = null,
                      private val pkgName: String? = null) : ISKinResource {
    // 读取皮肤包中的资源
    override fun getColor(resId: Int): Int {
        return if (isUsingDefaultSkin) {
            appResources.getColor(resId)
        } else {
            // 获取皮肤apk中同名的资源id
            val skinId = getIdentifier(resId)
            if (skinId == 0) appResources.getColor(resId)
            // 读取皮肤apk的资源
            else skinResources!!.getColor(skinId)
        }
    }
    override fun getIdentifier(resId: Int): Int {
        if (isUsingDefaultSkin) return resId
        // 在宿主apk中读取资源类型名
        val typeName = appResources.getResourceTypeName(resId)
        // 在宿主apk中读取资源名
        val entryName = appResources.getResourceEntryName(resId)
        // 读取皮肤apk中的同名资源id ， pkgName就是皮肤apk的包名。
        return skinResources?.getIdentifier(entryName, typeName, pkgName) ?: resId
    }
}
```

至此就完成了插件化换肤的主要框架。

### 自定义属性的管理

## 参考链接

- [Android 逆向笔记 —— ARSC 文件格式解析](https://juejin.im/post/6844903854165753863#heading-8)
- [Android应用程序资源的编译和打包过程分析](https://blog.csdn.net/luoshengyang/article/details/8744683)
- [Android应用程序资源的查找过程分析](https://blog.csdn.net/luoshengyang/article/details/8806798)

