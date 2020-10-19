package com.giriseematechme.bankingforms.RoomDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "form_table")
public class Form {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String address;
    private String email;
    private int age;
    private String phoneNumber;

    public Form(String name, String address, String email,String phoneNumber,int age) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.age = age;
    }

}
