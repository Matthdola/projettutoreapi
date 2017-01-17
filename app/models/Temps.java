package models;

import com.mongodb.DBObject;

import java.time.LocalDate;
import java.time.LocalTime;


public class Temps extends Document {
    private LocalDate jours;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    public Temps(){

    }

    public LocalDate getJours() {
        return jours;
    }

    public void setJours(LocalDate jours) {
        this.jours = jours;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }


    @Override
    public DBObject toBson() {
        return null;
    }

    public int compareTo(Temps temps){
        if(temps == null)

        if(this.jours.equals(temps.jours)
                && this.heureDebut.equals(temps.heureDebut)
                && this.heureFin.equals(temps.heureFin)){
            return 0;
        }
        return 1;
    }
}
