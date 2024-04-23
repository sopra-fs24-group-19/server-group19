package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserPostDTO {

    private String name;

    private String username;

    private String password;

    private String address;
    
    private String latitude;

    private String longitude;

    private String phoneNumber;

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

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public void setAddress(String address) {this.address = address;}

    public String getAddress() {return address;}

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getPhoneNumber() {return phoneNumber;}



}
