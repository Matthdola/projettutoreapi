package models;

import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Patient extends Utilisateur {
    private String codeAssurance;
    private String idAssureur;
    private List<String> maladieChroniques;
    private List<String> antecedents;
    private List<String> allergies;
    public static List<Patient> patients;


    static {
        patients = new ArrayList<Patient>();
        patients.add(new Patient("1111111111111", "Paperclips 1",
                "Paperclips description 1"));
        patients.add(new Patient("2222222222222", "Paperclips 2",
                "Paperclips description "));
        patients.add(new Patient("3333333333333", "Paperclips 3",
                "Paperclips description 3"));
        patients.add(new Patient("4444444444444", "Paperclips 4",
                "Paperclips description 4"));
        patients.add(new Patient("5555555555555", "Paperclips 5", "Paperclips description 5"));
    }

    public Patient(){

    }

    public String typeUtilisateur(){
        return "patient";
    }

    public String getCodeAssurance() {
        return codeAssurance;
    }

    public void setCodeAssurance(String codeAssurance) {
        this.codeAssurance = codeAssurance;
    }

    public String getIdAssureur() {
        return idAssureur;
    }

    public void setIdAssureur(String idAssureur) {
        this.idAssureur = idAssureur;
    }

    public Patient(String ean, String name, String description){

    }

    @Override
    public DBObject toBson() {
        return null;
    }

    public String toString(){
        return String.format("%s - %s", codeAssurance, idAssureur);
    }

    public static List<Patient> findAll() {
        return new ArrayList<>(patients);
    }

    public static Patient findById(String id){
        for (Patient candidate : patients){
            if(candidate.getId().equals(id)){
                return candidate;
            }
        }
        return null;
    }

    public static List<Patient> findByName(String term){
        final  List<Patient> results = new ArrayList<>();
        for (Patient candidate : patients){
            if(candidate.codeAssurance.equals(term)){
                results.add(candidate);
            }
        }
        return results;
    }

    public static boolean remove(Patient patient){
        return patients.remove(patient);
    }

    public static void save(Patient patient){
        patients.add(patient);
    }

    public static void update(Patient patient){
        Predicate<Patient> patientPredicate = p -> p.getId().equals(patient.getId());
        patients.removeIf(patientPredicate);
        patients.add(patient);
    }

    public List<String> getMaladieChroniques() {
        return maladieChroniques;
    }

    public void setMaladieChroniques(List<String> maladieChroniques) {
        this.maladieChroniques = maladieChroniques;
    }

    public List<String> getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(List<String> antecedents) {
        this.antecedents = antecedents;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}
