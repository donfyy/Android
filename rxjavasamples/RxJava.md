# RxJava

## Observable

Observable.observeOn 用来设置事件回调的线程

Observable.subscribeOn 用来设置事件执行的线程

### Code Observable和Hot Observable

Code Observable: 只有当观察者订阅时，才开始发送数据流，并且每个观察者都会独立执行一遍数据流的发送

Hot Observale: 不管有没有观察者订阅，一旦被观察者创建了，就开始发送数据流，也就是数据流是实时发送的

ConnectableObservable

### RefCount

```java
Observable<Long> ob=  Observable.interval(200, TimeUnit.MILLISECONDS).publish().refCount();
```

如果有订阅者就发送数据流，无订阅者就停止发送数据流，再次订阅则重新开始发送（此处每个订阅者接收到的数据是相同的）

### Reply

```java
 ConnectableObservable<Long> ob=  Observable.interval(200, TimeUnit.MILLISECONDS).replay();
 ob.connect();
```

当和源 Observable 链接后，开始收集数据。当有 Observer 订阅的时候，就把收集到的数据线发给 Observer。然后和其他 Observer 同时接受数据

可以同时设置收集数据的个数及时间

### Cache

```java
Observable<Long> ob=  Observable.interval(200, TimeUnit.MILLISECONDS).take(5).cache();//只有当订阅者订阅后才开始发送数据
```

与Reply类似，订阅者全部取消后也不会停止发送。

ObservableOnSubscribe

ObservableEmitter

Observer

Disposable

## 应用场景

- 与Retrofit连用搭载网络框架
- 使用基于RxJava的开源类库如RxPermissions,RxBinding等等
- 所有用到异步的地方

## 优势

- 简洁
- 避免回调地狱



# 差异

## RxJava2.x与RxJava1.x的差异

### 1.Nulls

1.x可以发射null，但是2.x不支持了

### 2.Flowable

1.x里Obsevable支持背压，在2.x里引入了Flowable来支持背压，Observable不在支持背压了。

背压：在异步场景中，被观察者发送事件的速度远快于观察者的处理速度时，一种告诉上游被观察者降低发送速度的策略。

### 3.Single/Completable/Maybe

Single只能发送一个事件。Completable侧重于观察结果，而Maybe是上面两种的结合体，当只想要某个事件的执行结果（true or false)时使用。

### 4.线程调度相关

2.x中删除了Schedulers.immediate()这个线程环境。

### 5.Function相关

2.x中移除了Func1，Func2...FuncN，用Function替换了Func1，BiFunction替换了Func 2..N。并且，它们都增加了throws Exception。一些操作不用再去try-catch了。

### 6.其他操作符相关

Consumer和BiConsumer替换了Action1和Action2，后面的Action都被替换了，只保留了ActionN。

RxJava1 -> RxJava2

- onCompleted -> onComplete
- Func1 -> Function
- Func2 -> BiFunction
- CompositeSubscription -> CompositeDisposable
- limit 被移除了 用take替代之



## 源码分析

### 线程切换

```kotlin
 Flowable.fromCallable {
                Thread.sleep(1000)
                return@fromCallable "Done"
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
                    }
```

```java
// Assembly Time
Flowable.fromCallable(MyCallable): new FlowableFromCallable<>(MyCallable)
FlowableFromCallable.subscribeOn(Schedulers.io()): new FlowableSubscribeOn<>(FlowableFromCallable, Schedulers.io(), true)
FlowableSubscribeOn.observeOn(AndroidSchedulers.mainThread()): new FlowableObserveOn<>(FlowableSubscribeOn, AndroidSchedulers.mainThread(), false, bufferSize)
// Subscription Time
FlowableObserveOn.subscribe(MySubscriber): LambdaSubscriber
	new LambdaSubscriber<>(MySubscriber, onError, onComplete, FlowableInternalHelper.RequestMax.INSTANCE)
	FlowableObserveOn.subscribeActual(LambdaSubscriber)
		FlowableSubscribeOn.subscribe(new ObserveOnSubscriber<>(LambdaSubscriber, AndroidSchedulers.mainThread().createWorker(), false, bufferSize))
			FlowableSubscribeOn.subscribeActual(ObserveOnSubscriber)
				ObserveOnSubscriber.onSubscribe(new SubscribeOnSubscriber<>(ObserveOnSubscriber, Schedulers.io().createWorker(), FlowableFromCallable, true));
					LambdaSubscriber.onSubscribe(ObserveOnSubscriber);
					SubscribeOnSubscriber.request(bufferSize);//do nothing 因为SubscribeOnSubscriber.onSubscribe方法还未调用，upstream还没有被设置
				Schedulers.io().createWorker().schedule(SubscribeOnSubscriber);//在子线程中执行
					SubscribeOnSubscriber.run
						FlowableFromCallable.subscribe(SubscribeOnSubscriber)
							DeferredScalarSubscription<T> deferred = new DeferredScalarSubscription<>(SubscribeOnSubscriber);
							SubscribeOnSubscriber.onSubscribe(deferred);
								SubscribeOnSubscriber.requestUpstream(bufferSize, deferred)
									DeferredScalarSubscription.request(bufferSize)
										DeferredScalarSubscription.compareAndSet(NO_REQUEST_NO_VALUE, HAS_REQUEST_NO_VALUE)
							// Runtime
							deferred.complete(MyCallable.call());
								SubscribeOnSubscriber.onNext(v);
									ObserveOnSubscriber.onNext(v);
										ObserveOnSubscriber.trySchedule();
											AndroidSchedulers.mainThread().createWorker().schedule(ObserveOnSubscriber) //切换到主线程了
												ObserveOnSubscriber.runAsync()
													LambdaSubscriber.onNext(v);
														MySubscriber.accept(v);
								SubscribeOnSubscriber.onComplete(v);
```



# 参考链接

- [ReactiveX/RxJava文档中文版](https://mcxiaoke.gitbooks.io/rxdocs/content/)
- [RxJava2.0——从放弃到入门](https://www.jianshu.com/p/cd3557b1a474)
- [RxJava入门之路（一）](https://www.cnblogs.com/lyysz/p/6344507.html)
- [【知识整理】这可能是最好的RxJava 2.x 入门教程（一）](https://www.cnblogs.com/liushilin/p/7058302.html)
- [RxJava入门之介绍与基本运用](https://www.jb51.net/article/92309.htm)










