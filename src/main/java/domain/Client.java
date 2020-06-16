package domain;

import java.util.ArrayList;
import java.sql.Date;

public class Client {

    public int id;
    private String name;
    private Date dateOfJoin;
    private String phoneNumber;
    private String address;


    public Client(int id, String name, Date dateOfJoin, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.dateOfJoin = dateOfJoin;
        this.phoneNumber = phoneNumber;
        this.address = address;

    }

    public Client(String name, Date dateOfJoin, String phoneNumber, String address) {
        this.name = name;
        this.dateOfJoin = dateOfJoin;
        this.phoneNumber = phoneNumber;
        this.address = address;

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

    public Date getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(Date dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Client ID: "+this.id+"\n Name: "+
                this.name+"\n Join Date: "+dateOfJoin+
                "\nPhone Number: "+this.phoneNumber+
                "\nAddress: "+this.address+"\n";
    }
}
