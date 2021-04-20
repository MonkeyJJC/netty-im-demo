package com.jjc.service.netty.im.demo.common.meaasge;

/**
 * @description: demo例子，实际im系统的话，消息协议其实只需要指定data就可以了，然后可以通过一个业务处理服务进行消息模板数据填充包括msgId生成等
 * @author: jjc
 * @createTime: 2021/4/21
 */
public class SingleChatMessage implements Message {
    /**
     * 类型 - 单聊消息发送请求
     */
    public static final String TYPE = "SINGLE_CHAT_MESSAGE";
    /**
     * 发送方
     */
    private String fromUser;
    /**
     * 发送给的用户
     */
    private String toUser;
    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private String content;

    public String getFromUser() {
        return fromUser;
    }

    public SingleChatMessage setFromUser(String fromUser) {
        this.fromUser = fromUser;
        return this;
    }

    public String getToUser() {
        return toUser;
    }

    public SingleChatMessage setToUser(String toUser) {
        this.toUser = toUser;
        return this;
    }

    public String getMsgId() {
        return msgId;
    }

    public SingleChatMessage setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public SingleChatMessage setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "SingleChatMessage{" +
                "fromUser='" + fromUser + '\'' +
                "toUser='" + toUser + '\'' +
                ", msgId='" + msgId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}