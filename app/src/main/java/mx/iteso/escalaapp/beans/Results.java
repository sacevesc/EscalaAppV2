package mx.iteso.escalaapp.beans;

import java.util.ArrayList;

public class Results {
    private Climber climber;
    private String resultsKey;
    private String sum;
    private String firstname;
    private String lastname;
    private int boulder_round;
    private String ranking;
    private ArrayList<Boulder> boulders = new ArrayList<>();

    public ArrayList<Boulder> getBoulders() {
        return boulders;
    }

    public void setBoulders(ArrayList<Boulder> boulders) {
        this.boulders = boulders;
    }

    public String getResultsKey() {
        return resultsKey;
    }

    public void setResultsKey(String resultsKey) {
        this.resultsKey = resultsKey;
    }

    public Climber getClimber() {
        return climber;
    }

    public void setClimber(Climber climber) {
        this.climber = climber;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public int getBoulder_round() {
        return boulder_round;
    }

    public void setBoulder_round(int boulder_round) {
        this.boulder_round = boulder_round;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
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


}
