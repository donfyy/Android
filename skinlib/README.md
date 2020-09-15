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

### 加载插件中的皮肤资源

### 应用插件中的皮肤资源

### 自定义属性的管理