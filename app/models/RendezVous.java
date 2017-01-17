package models;

import com.mongodb.DBObject;

public class RendezVous extends Document {
    private String idDemande;
    private String idMedecin;
    private String idTemps;
    private String idConsultation;
    private String motifs;

    public RendezVous(){
        
    }

    public RendezVous(String idDemande, String idMedecin, String idTemps){
        this.idDemande = idDemande;
        this.idMedecin = idMedecin;
        this.idTemps = idTemps;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(String idMedecin) {
        this.idMedecin = idMedecin;
    }

    public String getIdTemps() {
        return idTemps;
    }

    public void setIdTemps(String idTemps) {
        this.idTemps = idTemps;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getMotifs() {
        return motifs;
    }

    public void setMotifs(String motifs) {
        this.motifs = motifs;
    }

    @Override
    public DBObject toBson() {
        return null;
    }
}
