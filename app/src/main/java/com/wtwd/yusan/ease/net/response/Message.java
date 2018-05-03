package com.wtwd.yusan.ease.net.response;

/**
 * Created by XJM on 2018/4/25.
 */

public class Message {

    private Long  message_id;                  //消息ID

    private String messageIdStr;

    private String title;           //标题

    private String content;             //内容

    private String create_time;      //创建时间

    private Integer type;          //0是官方公告，1是系统消息

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

    public String getMessageIdStr() {
        return messageIdStr;
    }

    public void setMessageIdStr(String messageIdStr) {
        this.messageIdStr = messageIdStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
