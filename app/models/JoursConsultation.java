package models;

import com.mongodb.DBObject;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by matth on 28/09/2016.
 */
public class JoursConsultation extends Document {
    private ArrayList<JourConsul> jours;

    public JoursConsultation(){

    }

    public ArrayList<JourConsul> getJours() {
        return jours;
    }

    public void setJours(ArrayList<JourConsul> jours) {
        this.jours = jours;
    }

    @Override
    public DBObject toBson() {
        return null;
    }

    public class JourConsul{
        private String jour;
        private LocalTime debut;
        private LocalTime fin;

        public JourConsul(){

        }

        public JourConsul(String jour, LocalTime deb, LocalTime fin){
            this.jour = jour;
            this.debut = deb;
            this.fin = fin;
        }

        public String getJour() {
            return jour;
        }

        public void setJour(String jour) {
            this.jour = jour;
        }

        public LocalTime getDebut() {
            return debut;
        }

        public void setDebut(LocalTime debut) {
            this.debut = debut;
        }

        public LocalTime getFin() {
            return fin;
        }

        public void setFin(LocalTime fin) {
            this.fin = fin;
        }


    }
}
