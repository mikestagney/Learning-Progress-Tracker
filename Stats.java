package tracker;

import java.util.*;

public class Stats {

    Double[] studentsEnrolled;
    Double[] averageScores;
    Repository repository;

    Stats(Repository repository) {
        this.repository = repository;
        studentsEnrolled = new Double[Repository.NUMBER_OF_COURSES];
        findEnrollment();
        averageScores = new Double[Repository.NUMBER_OF_COURSES];
        findAverageScores();
    }
    public void OverallStats() {
        printBestWorst("Most", "Least", "popular", studentsEnrolled);
        printBestWorst("Highest", "Lowest", "activity", repository.submissions);
        printBestWorst("Easiest", "Hardest", "course", averageScores);
    }
    private void printBestWorst(String bestName, String worstName, String category, Double[] array) {
        String valueBest = "n/a";
        String valueWorst = "n/a";
        List<Integer> highestIndex = findGreatestIndexes(array);
        List<Integer> lowestIndex = findLeastIndexes(array);
        if (areStudentsEnrolled()) {
            valueBest = getCourseList(highestIndex);
            lowestIndex.removeAll(highestIndex);
            if (!lowestIndex.isEmpty()) {
                valueWorst = getCourseList(lowestIndex);
            }
        }
        System.out.printf("%s %s: %s\n", bestName, category, valueBest);
        System.out.printf("%s %s: %s\n", worstName, category, valueWorst);
    }
    private boolean areStudentsEnrolled() {
        return Arrays.stream(studentsEnrolled).anyMatch(students -> students > 0);
    }

    public void getCourseStats(Courses course) {
        List<Enrollee> courseEnrollment = new ArrayList<>();

        int courseIndex = course.ordinal();
        repository.studentMap.forEach((id, student) -> {
            if (student.scores[courseIndex] > 0 ) {
                Enrollee enrollee = new Enrollee(id, student.scores[courseIndex], course.points);
                courseEnrollment.add(enrollee);
            }
        });

        courseEnrollment.sort(Comparator.comparing(Enrollee::getCurrentPoints)
                .reversed()
                .thenComparing(Enrollee::getId));

        System.out.println(course.name);
        System.out.println("id     points    completed");
        courseEnrollment.forEach(System.out::println);
    }

    private String getCourseList(List<Integer> indexes) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < indexes.size(); index++) {
            builder.append(Courses.values()[indexes.get(index)].name);
            if (indexes.size() > 1 && index < indexes.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
    private void findEnrollment() {
        Arrays.fill(studentsEnrolled, 0.0);
        repository.studentMap.forEach((id, student) -> {
            for (int i = 0; i < Repository.NUMBER_OF_COURSES; i++) {
                if (student.scores[i] > 0) {
                    studentsEnrolled[i]++;
                }
            }
        });
    }
    private void findAverageScores() {
        for (int i = 0; i < Repository.NUMBER_OF_COURSES; i++) {
            averageScores[i] = repository.totalScores[i] / repository.submissions[i];
        }
    }
    private  List<Integer> findGreatestIndexes(Double[] array) {
        Double highest = Arrays.stream(array)
                .mapToDouble(d -> d)
                .max()
                .orElse(0.0);

        return findIndexesWithValue(array, highest);
    }
    private List<Integer> findLeastIndexes(Double[] array) {
        Double lowest = Arrays.stream(array)
                .mapToDouble(d -> d)
                .min()
                .orElse(0.0);

        return findIndexesWithValue(array, lowest);
    }
    private List<Integer> findIndexesWithValue(Double[] array, Double value) {
        List<Integer> indexes = new ArrayList<>();
        for (int index = 0; index < array.length; index++) {
            if (array[index].equals(value)) {
                indexes.add(index);
            }
        }
        return indexes;
    }
}