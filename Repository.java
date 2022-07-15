package tracker;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Repository {

    Set<String> emailAddresses;
    public Map<Integer, Student> studentMap;
    public Double[] submissions;
    public Double[] totalScores;
    public static final int NUMBER_OF_COURSES = Courses.NUMBER_COURSES;

    Repository() {
        emailAddresses = new HashSet<>();
        studentMap = new LinkedHashMap<>();
        submissions = new Double[NUMBER_OF_COURSES];
        totalScores = new Double[NUMBER_OF_COURSES];
        Arrays.fill(submissions, 0.0);
        Arrays.fill(totalScores, 0.0);
    }

    public void store(String[] currentInfo) {
        Student student = new Student(currentInfo[StudentInfo.FIRST_NAME.getIndex()],
                        currentInfo[StudentInfo.LAST_NAME.getIndex()],
                        currentInfo[StudentInfo.EMAIL.getIndex()]);

        studentMap.put(student.id, student);
        emailAddresses.add(currentInfo[StudentInfo.EMAIL.getIndex()]);
    }

    public boolean checkEmailUsed(String email) {
        return emailAddresses.contains(email);
    }
    public boolean invalidStudent(int id) {
        return !studentMap.containsKey(id);
    }
    public void addPoints(String[] tokens) {
        int id = Integer.parseInt(tokens[0]);
        Student student = studentMap.get(id);
        String[] updatedScores = Arrays.copyOfRange(tokens, 1, tokens.length);
        for (int i = 0; i < updatedScores.length; i++) {
            int currentScore = Integer.parseInt(updatedScores[i]);
            if (currentScore > 0) {
                submissions[i]++;
                totalScores[i] += currentScore;
            }
        }
        student.updateScores(updatedScores);
        studentMap.put(id, student);
    }
    public Student getStudent(int id) {
        return studentMap.get(id);
    }

    public void printStudents() {
        if (studentMap.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students:");
            studentMap.forEach((id, student) -> System.out.println(id));
        }
    }
    public void notifyStudents() {
        AtomicReference<Integer> counter = new AtomicReference<>(0);

        studentMap.forEach((id, student) -> {
            boolean isNotified = false;
            for (Courses course : Courses.values()) {
                if (student.scores[course.ordinal()] >= course.points && !student.notify[course.ordinal()]) {
                    isNotified = true;
                    student.notify[course.ordinal()] = true;
                    System.out.printf("To: %s\n", student.email);
                    System.out.print("Re: Your Learning Progress\n");
                    System.out.printf("Hello, %s %s! You have accomplished our %s course!\n", student.firstName, student.lastName, course.name);
                }
            }
            if (isNotified) {
                counter.getAndSet(counter.get() + 1);
            }
        });
        System.out.printf("Total %s students have been notified.\n", counter.get());
    }
}
