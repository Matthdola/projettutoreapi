package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Specialite extends Document {
    private String id;
    private String nom;
    private String domaine;

    public static List<Specialite> specialites;

    static {
        specialites = new ArrayList<Specialite>();
        specialites.add(new Specialite());
        specialites.add(new Specialite());
        specialites.add(new Specialite());
        specialites.add(new Specialite());
        specialites.add(new Specialite());
    }

    public static JacksonDBCollection<Specialite, String> collection = MongoDB.getCollection("specialites", Specialite.class, String.class);


    public Specialite(){

    }

    public Specialite(String nom){
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }


    @Override
    public DBObject toBson() {
        return null;
    }


    public static List<Specialite> findAll() {
        return Specialite.collection.find().toArray();
    }


    public static Specialite findById(String id){
        Specialite specialite = Specialite.collection.findOneById(id);
        return specialite;
    }

    public static List<Specialite> findByName(String name){
        final  List<Specialite> results = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            results.add((Specialite) cursor.next());
        }
        return results;
    }

    public static boolean remove(Specialite specialite){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(specialite.getId()) );
        try {
            Specialite.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(Specialite specialite){
        Specialite.collection.save(specialite);
    }

    public static void update(Specialite specialite){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(specialite.getId()) );
        collection.update(query, specialite.toBson());
    }
}
