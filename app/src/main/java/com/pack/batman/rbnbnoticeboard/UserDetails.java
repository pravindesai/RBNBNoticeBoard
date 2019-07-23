package com.pack.batman.rbnbnoticeboard;

public class UserDetails {

    private String id,name,phone,email,dept,roll;

    public UserDetails() { }

    public UserDetails(String id, String name, String phone, String email, String dept, String roll) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dept = dept;
        this.roll = roll;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDept() {
        return dept;
    }

    public String getRoll() {
        return roll;
    }
}
