package ch.uzh.ifi.hase.soprafs24.rest.dto;


public class UserGetDTO {

    private Long id;
    private String name;
    private String username;
    private int coinBalance;
    private String address;
    private String phoneNumber;
    private float radius;

    public int getCoinBalance() { return coinBalance; }

    public void setCoinBalance(int coinBalance) { this.coinBalance = coinBalance; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public float getRadius() { return radius; }

    public void setRadius(float radius) { this.radius = radius; }

    public Long getId() {
    return id;
  }

    public void setId(Long id) {
    this.id = id;
  }

    public String getName() {
    return name;
  }

    public void setName(String name) {
    this.name = name;
  }

    public String getUsername() {
    return username;
  }

    public void setUsername(String username) {
    this.username = username;
  }



}
