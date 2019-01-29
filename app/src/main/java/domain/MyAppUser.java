package domain;

/**
 * Created by harrysmac on 20/03/18.
 */

public class MyAppUser {

    private String name;
    private String email;
    private AvailablePreferences availablePreferences;
    private SetPreferences setPreferences;

    public MyAppUser(){};

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public AvailablePreferences getAvailablePreferences() {
        return availablePreferences;
    }

    public void setAvailablePreferences(AvailablePreferences availablePreferences) {
        this.availablePreferences = availablePreferences;
    }

    public SetPreferences getSetPreferences() {
        return setPreferences;
    }

    public void setSetPreferences(SetPreferences setPreferences) {
        this.setPreferences = setPreferences;
    }
}



