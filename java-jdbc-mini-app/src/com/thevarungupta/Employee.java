package com.thevarungupta;

public class Employee {
    private int id;
    private String name;
    private String lastname;
    private String email;

    public Employee(){

    }

    public Employee(int id, String name, String LastName, String email) {
        this.id = id;
        this.name = name;
        this.lastname = LastName;
        this.email = email;
    }

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
    public void setLastName(String LastName) {
        this.lastname = LastName;
    }

    public String getLastName() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        return("--------------Employee------------\nName: " + name +"\nLast Name: " + lastname + "\nEmail: " + email);
    }
}

