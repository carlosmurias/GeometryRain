package games.ameba.geometryrain;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;

    private String username;
    private String password;
    private String name;
    private String surname;
    private String address;
    private String country;
    private String email;
    private String phone;
    private Boolean admin = false;


    public User() {

    };

    /**
     * comprova els atributs de l'usuari
     * @return true si cap atribut té valor nul
     */
    public boolean canRegister() {
        return  username != null &&
                password != null &&
                name != null &&
                surname != null &&
                address != null &&
                country != null &&
                email != null &&
                phone != null &&
                admin != null;
    }

    /**
     * crea un hashmap amb els atributs d'usuari
     * @return el hashmap amb els atributs
     */
    @Deprecated
    @Exclude
    public Map<String, Object> getHashMap() {
        Map<String, Object> userHashMap = new HashMap<>();
        if (username!=null)
            userHashMap.put("username", username);
        if (name!=null)
            userHashMap.put("name", name);
        if (surname!=null)
            userHashMap.put("surname", surname);
        if (address!=null)
            userHashMap.put("address", address);
        if (country!=null)
            userHashMap.put("country", country);
        if (email!=null)
            userHashMap.put("email", email);
        if (phone!=null)
            userHashMap.put("phone", phone);
        if (admin!=null)
            userHashMap.put("admin", admin);
        return userHashMap;
    }

    /*
     * Getters and setters.
     */

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public boolean setUsername(String username) {
        // Username must have more than 4 characters
        if (username.length()<4)
            return false;
        this.username = username;
        return true;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) {
        // Password must have more than 8 characters
        if (password.length()<8)
            return false;
        this.password = password;
        return true;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        this.name = name;
        return true;
    }

    public String getSurname() {
        return surname;
    }

    public boolean setSurname(String surname) {
        this.surname = surname;
        return true;
    }

    public String getAddress() {
        return address;
    }

    public boolean setAddress(String address) {
        this.address = address;
        return true;
    }

    public String getCountry() {
        return country;
    }

    public boolean setCountry(String country) {
        this.country = country;
        return true;
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String email) {
        this.email = email;
        return true;
    }

    public String getPhone() {
        return phone;
    }

    public boolean setPhone(String phone) {
        this.phone = phone;
        return true;
    }

    public boolean setAdmin(boolean admin) {
        this.admin = admin;
        return true;
    }
    public boolean getAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", admin=" + admin +
                '}';
    }
}