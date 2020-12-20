package com.jw.screw.consumer.filter;


import com.jw.screw.common.exception.RemoteSendException;
import com.jw.screw.common.exception.RemoteTimeoutException;
import com.jw.screw.common.transport.body.RequestBody;

/**
 * screw
 * @author jiangw
 * @date 2020/12/8 17:18
 * @since 1.0
 */
public interface Filter extends Comparable<Filter> {

    /**
     * 调用过滤器
     * @param request 请求body
     * @param context 过滤器上下文对象，过滤器可以从里面取一些有用的对象
     * @param next 下一个过滤链
     * @throws InterruptedException 1
     * @throws RemoteTimeoutException
     * @throws RemoteSendException
     */
    <T extends FilterContext> void doFilter(RequestBody request, T context, FilterChain next) throws InterruptedException, RemoteTimeoutException, RemoteSendException;

    /**
     * 设置当前过滤器的权重，权重越低越优先执行
     * @return
     */
    Integer weight();
}
