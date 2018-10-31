package br.com.museuid.model;

public class Employee {
  private String id;
  private String userName;
  private String name;
  private String email;
  private String phoneNumber;
  private String address;

  public Employee(String id, String userName, String name, String email, String phoneNumber, String address) {
    this.id = id;
    this.userName = userName;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }

  public Employee(String id, String userName, String name, String phoneNumber, String address) {
    this.id = id;
    this.userName = userName;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}
