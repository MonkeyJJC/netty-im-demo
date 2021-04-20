package com.jjc.service.netty.im.demo.common.meaasge;

/**
 * @description: 仅demo，实际应该根据groupId，并结合写扩散或读扩散等进行消息推送
 * @author: jjc
 * @createTime: 2021/4/21
 */
public class GroupChatMessage implements Message {
    public static final String TYPE = "GROUP_CHAT_MESSAGE";
    /**
     * 发送方
     */
    private String fromUser;
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

    public GroupChatMessage setFromUser(String fromUser) {
        this.fromUser = fromUser;
        return this;
    }

    public String getContent() {
        return content;
    }

    public GroupChatMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public String getMsgId() {
        return msgId;
    }

    public GroupChatMessage setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    @Override
    public String toString() {
        return "GroupChatMessage{" +
                "msgId='" + msgId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}