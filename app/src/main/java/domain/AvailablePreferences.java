package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harrysmac on 13/04/18.
 */

public class AvailablePreferences {

    private List<String> exercisePref = new ArrayList<>();
    private List<String> learningPref = new ArrayList<>();
    private List<String> relaxationPref = new ArrayList<>();

    public AvailablePreferences(){};

    public void addExercisePref(String str) {
        this.exercisePref.add(str);
    }

    public List<String> getExercisePref() {
        return exercisePref;
    }

    public void setExercisePref(List<String> exercisePref) {
        this.exercisePref = exercisePref;
    }

    public void addLearningPref(String str) {
        this.learningPref.add(str);
    }

    public List<String> getLearningPref() {
        return learningPref;
    }

    public void setLearningPref(List<String> learningPref) {
        this.learningPref = learningPref;
    }

    public void addRelaxtionPref(String str) {
        this.relaxationPref.add(str);
    }

    public List<String> getRelaxationPref() {
        return relaxationPref;
    }

    public void setRelaxationPref(List<String> relaxationPref) {
        this.relaxationPref = relaxationPref;
    }


}
