package com.example.leo.manager;

public class MsgItem {
    private String msgContent;
    private String msgSender;
    private int imageId;
    private long date;
    private int MSG_TYPE;
    public MsgItem(String msgContent,String msgSender,long date,int imageId,int type){
        this.date=date;
        this.msgContent=msgContent;
        this.msgSender=msgSender;
        this.imageId=imageId;
        this.MSG_TYPE=type;
    }
    public String getMsgContent() {
        return msgContent;
    }

    public String getMsgSender() {
        return msgSender;
    }

    public int getImageId() {
        return imageId;
    }

    public long getDate() {
        return date;
    }

    public int getMSG_TYPE() {
        return MSG_TYPE;
    }
}
