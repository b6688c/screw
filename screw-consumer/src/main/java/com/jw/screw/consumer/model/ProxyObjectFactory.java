package com.jw.screw.consumer.model;


import com.jw.screw.common.metadata.ServiceMetadata;
import com.jw.screw.common.util.Requires;
import com.jw.screw.consumer.ConnectWatch;
import com.jw.screw.consumer.Consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * screw
 * @author jiangw
 * @date 2020/12/8 17:19
 * @since 1.0
 */
public class ProxyObjectFactory {

    private Consumer consumer;

    private ServiceMetadata serviceMetadata;

    private ConnectWatch connectWatch;

    private boolean isAsync = false;

    private final AtomicBoolean isAvailable = new AtomicBoolean(false);

    public static ProxyObjectFactory factory() {
        return new ProxyObjectFactory();
    }

    public ProxyObjectFactory consumer(Consumer consumer) {
        this.consumer = consumer;
        return this;
    }

    public ProxyObjectFactory metadata(ServiceMetadata serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
        return this;
    }

    public ProxyObjectFactory isAsync(boolean isAsync) {
        this.isAsync = isAsync;
        return this;
    }

    public ProxyObjectFactory connectWatch(ConnectWatch connectWatch) {
        this.connectWatch = connectWatch;
        return this;
    }

    /**
     * 根据api产生一个新代理目标对象，此时消费者必须要与提供者依赖于同一个api包。
     * 比如：调用的目标是DemoService。那么生成一个DemoService的对象，直接调用里面方法产生rpc的结果
     * @param clazz 目标service
     * @param <T> service返回的结果的泛型
     * @return 返回的结果，如果不是void的话
     */
    public <T> T newProxyInstance(final Class<T> clazz) {
        checkArguments();
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{ clazz }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return rpcInvoke(clazz.getSimpleName(), method.getName(), method.getReturnType(), args);
            }
        });
        return clazz.cast(proxy);
    }

    /**
     * 直接进行远程调用，消费者与服务提供者不必需要依赖同一个api包也能进行rpc。到这是一种不保证可靠性的做法
     * @param serviceName 目标的服务名如DemoService
     * @param methodName 目标方法名
     * @param args 目标方法的参数
     * @return rpc调用的结果
     */
    public Object remoteInvoke(String serviceName, String methodName, Object[] args) throws InterruptedException {
        checkArguments();
        return rpcInvoke(serviceName, methodName, Object.class, args);
    }

    private Object rpcInvoke(String serviceName, String methodName, Class<?> returnType, Object[] args) throws InterruptedException {
        // 直连情况
        if (connectWatch == null) {
            return rpcInvokeResult(serviceName, methodName, returnType, args);
        }
        if (!isAvailable.get()) {
            boolean connectAvailable = connectWatch.waitForAvailable(10000);
            if (connectAvailable) {
                isAvailable.set(true);
            }
        }
        if (isAvailable.get()) {
            return rpcInvokeResult(serviceName, methodName, returnType, args);
        } else {
            return null;
        }
    }

    private Object rpcInvokeResult(String serviceName, String methodName, Class<?> returnType, Object[] args) {
        if (isAsync) {
            consumer.asyncCall(serviceMetadata, serviceName, methodName, returnType, args);
            return null;
        }
        return consumer.call(serviceMetadata, serviceName, methodName, returnType, args);
    }

    private void checkArguments() {
        Requires.isNull(consumer, "consumer");
        Requires.isNull(serviceMetadata, "consumer");
    }

}
