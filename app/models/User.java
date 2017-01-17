package models;

import play.data.validation.Constraints;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class User {
    @Constraints.Required
    public String password;

    public String passwordSalt;

    @Constraints.Required
    public String name;

    public String email;

    public String telephone;

    public byte[] image;

    public static List<User> users;

    static {
        users = new ArrayList<User>();
        users.add(new User("Matthias", "matthiasdola@gmail.com ",
                "matthias"));
        users.add(new User("Natacha", "natacha@gmail.com",
                "natacha "));
        users.add(new User("Odile", "odile@gmail.com",
                "odile"));
        users.add(new User("Benoit", "benoit@gmail.com",
                "benoit"));
    }

    public User() {

    }

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String toString(){
        return String.format("%s - %s", name, name);
    }

    public static List<User> findAll() {
        return new ArrayList<User>(users);
    }

    public static User findByEan(String name){
        for (User candidate : users){
            if(candidate.name.equals(name)){
                return candidate;
            }
        }
        return null;
    }

    public static User findByNameAndPassword(String name, String password){
        for (User candidate : users){
            if(candidate.name.equals(name)&&candidate.password.equals(password)){
                return candidate;
            }
        }
        return null;
    }

    public static boolean remove(User user){
        return users.remove(user);
    }

    public void save(){
        users.remove(findByEan(this.name));
        users.add(this);
    }
}
