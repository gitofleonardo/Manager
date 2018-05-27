package com.example.leo.manager;

public class ContactItem{
    private String phoneNumber;
    private String name;

    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ContactItem(String name, String phoneNumber){
        this.name=name;
        this.phoneNumber=phoneNumber;
    }
}
