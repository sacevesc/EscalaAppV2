package mx.iteso.escalaapp.beans;

/**
 * Created by aceve on 03/03/2018.
 */

public class Gym {
    private String name;
    private String description;
    private String city;
    private String state;
    private String address;
    private String image;
    private String owner;
    private String phone;
    private String key;
    private String thumb;
    private String members;
    private String podiums;
    private String host;

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getPodiums() {
        return podiums;
    }

    public void setPodiums(String podiums) {
        this.podiums = podiums;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Gym() {
        this.name = "NONE";
        this.description = null;
        this.city = null;
        this.state = null;
        this.address = null;
        this.image = null;
        this.owner = null;
        this.phone = null;
    }


    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Gym(String name, String description, String city, String state, String address, String image, String owner, String phone) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
        this.address = address;
        this.image = image;
        this.owner = owner;
        this.phone = phone;
    }

    public Gym(String name, String description, String city, String photo) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.image = photo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String photo) {
        this.image = photo;
    }

    @Override
    public String toString() {
        return name;
    }
}