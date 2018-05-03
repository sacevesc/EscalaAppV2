package mx.iteso.escalaapp.beans;

/**
 * Created by aceve on 04/03/2018.
 */


public class Competition {
    private String name;
    private String gym;
    private String description;
    private String image;
    private String thumb;
    private String no_rounds, no_categorys;
    private String day, month, year;
    private String owner;
    private String[] climbers, judges;
    private String compKey;
    private String participants;


    public Competition() {
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getNo_rounds() {
        return no_rounds;
    }

    public void setNo_rounds(String no_rounds) {
        this.no_rounds = no_rounds;
    }

    public String getNo_categorys() {
        return no_categorys;
    }

    public void setNo_categorys(String no_categorys) {
        this.no_categorys = no_categorys;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String[] getClimbers() {
        return climbers;
    }

    public void setClimbers(String[] climbers) {
        this.climbers = climbers;
    }

    public String[] getJudges() {
        return judges;
    }

    public void setJudges(String[] judges) {
        this.judges = judges;
    }

    public String getCompKey() {
        return compKey;
    }

    public void setCompKey(String compKey) {
        this.compKey = compKey;
    }
}

