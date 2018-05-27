package com.example.leo.manager;

public class CallLogItem {
    private String name;
    private String number;
    private long date;
    private int type;
    private String address;

    public CallLogItem(String number,String name,long date,int type,String address){
        this.number=number;
        this.date=date;
        this.type=type;
        this.address=address;
        this.name=name;
    }

    public int getType() {
        return type;
    }

    public long getDate() {
        return date;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
