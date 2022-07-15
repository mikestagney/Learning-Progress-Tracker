package tracker;

import java.util.Arrays;

public class Student {

    String firstName;
    String lastName;
    String email;
    int id;
    int[] scores;
    boolean[] notify;
    static int idCounter = 10000;

    Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        id = idCounter;
        idCounter++;
        scores = new int[Repository.NUMBER_OF_COURSES];
        notify = new boolean[Repository.NUMBER_OF_COURSES];
        Arrays.fill(scores, 0);
        Arrays.fill(notify, false);
    }
    public void updateScores(String[] updatedScores) {
        for (int i = 0; i < scores.length; i++) {
            scores[i] += Integer.parseInt(updatedScores[i]);
        }
    }
    @Override
    public String toString() {
       return String.format("%d points: Java=%d; DSA=%d; Databases=%d; Spring=%d", id, scores[0], scores[1], scores[2], scores[3]);
    }

}
