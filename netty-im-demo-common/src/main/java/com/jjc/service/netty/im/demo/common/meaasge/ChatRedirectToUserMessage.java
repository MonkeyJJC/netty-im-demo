package com.jjc.service.netty.im.demo.common.meaasge;

/**
 * @description: (长连接)转发给用户的消息
 * @author: jjc
 * @createTime: 2021/4/21
 */
public class ChatRedirectToUserMessage implements Message {
    public static final String TYPE = "CHAT_REDIRECT_TO_USER_REQUEST";
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

    public String getMsgId() {
        return msgId;
    }

    public ChatRedirectToUserMessage setFromUser(String fromUser) {
        this.fromUser = fromUser;
        return this;
    }

    public String getFromUser() {
        return fromUser;
    }

    public ChatRedirectToUserMessage setToUser(String toUser) {
        this.toUser = toUser;
        return this;
    }

    public String getToUser() {
        return toUser;
    }

    public ChatRedirectToUserMessage setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ChatRedirectToUserMessage setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "ChatRedirectToUserRequest{" +
                "fromUser='" + fromUser + '\'' +
                "toUser='" + toUser + '\'' +
                "msgId='" + msgId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}