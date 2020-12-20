package com.jw.screw.remote;

/**
 *    4   │   1   │    1   │     8     │      4      │
 *  MAGIC    Sign     Type   Invoke Id    Body Length                   Body Content
 * @author jiangw
 */
public class Protocol {

    /**
     * 作为接收的请求头校验
     */
    public static final int MAGIC = 0x66;

    /**
     * 获取的请求头魔数
     */
    private int validateMagic;

    /**
     * 判断消息是请求还是响应
     */
    private byte sign;

    /**
     * 判断当前请求的类型，可能存在的值
     * 1.发布服务
     * 2.订阅服务
     */
    private byte type;

    /**
     * 一次请求的id
     */
    private long invokeId;

    /**
     * 消息的长度
     */
    private int bodyLength;

    public byte getSign() {
        return sign;
    }

    public void setSign(byte sign) {
        this.sign = sign;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(long invokeId) {
        this.invokeId = invokeId;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public int getValidateMagic() {
        return validateMagic;
    }

    public void setValidateMagic(int validateMagic) {
        this.validateMagic = validateMagic;
    }

    public enum State {
        /**
         * 请求头的魔数
         */
        HEADER_MAGIC,

        /**
         * 请求头方式
         */
        HEADER_SIGN,

        /**
         * 请求头类型或者是装填
         */
        HEADER_TYPE,

        /**
         * 请求调用id
         */
        HEADER_INVOKER_ID,

        /**
         * 请求体长度
         */
        HEADER_BODY_LENGTH,

        /**
         * 体
         */
        BODY
    }

    public interface TransportType {
        /**
         * 远程请求
         */
        byte REMOTE_REQUEST = 1;

        /**
         * 远程响应
         */
        byte REMOTE_RESPONSE = 2;

        /**
         * Ack的处理
         */
        byte ACK = 3;
    }

    public interface Code {

        /**
         * 未知的类型
         */
        byte UNKNOWN = 0;

        /**
         * 发生一些未知的情况时
         */
        byte FAILED = 1;

        /**
         * 心跳
         */
        byte HEART_BEATS = 2;

        /**
         * 远程请求的调用
         */
        byte RPC_REQUEST = 66;

        /**
         * 远程调用的响应
         */
        byte RPC_RESPONSE = 67;

        /**
         * 生产者服务注册
         */
        byte SERVICE_REGISTER = 68;

        /**
         * 消费者服务订阅
         */
        byte SERVICE_SUBSCRIBE = 69;

        /**
         * 注册中心响应订阅
         */
        byte RESPONSE_SUBSCRIBE = 70;

        /**
         * 服务下线通知
         */
        byte SERVICE_OFFLINE = 71;

        /**
         * 单播
         */
        byte UNICAST = 72;

        /**
         * 广播
         */
        byte BROADCAST = 73;

    }
}
