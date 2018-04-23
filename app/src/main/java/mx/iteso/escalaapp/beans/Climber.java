package mx.iteso.escalaapp.beans;

/**
 * Created by aceve on 03/03/2018.
 */

public class Climber {
    private String firstname;
    private String lastname;
    private String gym;
    private String photo;
    private String thumb;
    private String description;
    private String city;
    private String state;
    private String owner = "false";

    public Climber() {
        this.firstname = "";
        this.lastname = "";
        this.gym = "";
        this.description = "";
        this.city = "";
        this.state = "";
        this.photo = "";
        this.thumb = "";
    }

    public Climber(String firstname, String lastname, String gym, String photo) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gym = gym;
        this.photo = photo;
    }

    public Climber(String firstname, String lastname, String gym, String photo, String description, String city, String state) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gym = gym;
        this.photo = photo;
        this.description = description;
        this.city = city;
        this.state = state;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Climber{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gym='" + gym + '\'' +
                ", photo=" + photo +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }
}
