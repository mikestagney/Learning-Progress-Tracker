package tracker;

public enum Courses {

    JAVA("Java", 600),
    DSA("DSA", 400),
    DBASE("Databases",480),
    SPRING("Spring",550);

    String name;
    int points;
    static final int NUMBER_COURSES = 4;

    Courses(String name, int points) {
        this.name = name;
        this.points = points;
    }
}
