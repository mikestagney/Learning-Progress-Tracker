package tracker;

import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInterface {

    String input;
    Scanner scanner;
    String[] currentInfo;
    final static String EXIT_COMMAND = "exit";
    String lettersNumbers = "[A-Za-z0-9]";
    String numbersAndSpace = "[0-9]+\\s";
    Pattern namePattern;
    Pattern emailPattern;
    Pattern pointsEnterPattern;
    Repository repository;

    UserInterface() {
        scanner = new Scanner(System.in);
        currentInfo = new String[StudentInfo.NUMBER_FIELDS.getIndex()];
        namePattern = Pattern.compile(lettersNumbers + "+([-']?" + lettersNumbers + "+)*");
        emailPattern = Pattern.compile(lettersNumbers + "+([.]?" + lettersNumbers + "+)?[@]" + lettersNumbers + "+[.]" + lettersNumbers + "+");
        pointsEnterPattern = Pattern.compile(numbersAndSpace + numbersAndSpace + numbersAndSpace + numbersAndSpace + "[0-9]+");
        repository = new Repository();
    }

    public void start() {
        System.out.println("Learning Progress Tracker");
        do {
            input = scanner.nextLine().trim();
            switch (input) {
                case "": {
                    System.out.println("No input");
                    break;
                }
                case EXIT_COMMAND: {
                    System.out.println("Bye");
                    break;
                }
                case "add students": {
                    addStudents();
                    break;
                }
                case "list": {
                    repository.printStudents();
                    break;
                }
                case "add points": {
                    addPoints();
                    break;
                }
                case "find": {
                    findStudent();
                    break;
                }
                case "statistics": {
                    getStatistics();
                    break;
                }
                case "back": {
                    System.out.println("Enter 'exit' to exit the program");
                    break;
                }
                case "notify": {
                    repository.notifyStudents();
                    break;
                }
                default:
                    System.out.println("Error: unknown command!");
            }
        } while (!input.equals(EXIT_COMMAND));
    }

    private void getStatistics() {
        Stats stats = new Stats(repository);
        System.out.println("Type the name of a course to see details or 'back' to quit:");
        stats.OverallStats();
        courseDetails(stats);
    }
    private void courseDetails(Stats stats) {
        boolean keepGoing = true;
        do {
            input = scanner.nextLine().trim().toUpperCase();
            switch (input) {
                case "JAVA": {
                    stats.getCourseStats(Courses.JAVA);
                    break;
                }
                case "DSA": {
                    stats.getCourseStats(Courses.DSA);
                    break;
                }
                case "DATABASES": {
                    stats.getCourseStats(Courses.DBASE);
                    break;
                }
                case "SPRING": {
                    stats.getCourseStats(Courses.SPRING);
                    break;
                }
                case "BACK": {
                    keepGoing = false;
                    break;
                }
                default:
                    System.out.println("Unknown course.");
            }
        } while (keepGoing);
    }
    private void findStudent() {
        System.out.println("Enter an id or 'back' to return:");
        while (true) {
            input = scanner.nextLine().strip();
            if (input.equals("back")) {
                break;
            }
            int id;
            try {
                id = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Not an integer");
                continue;
            }
            if (repository.invalidStudent(id)) {
                System.out.printf("No student is found for id=%s.\n", input);
                continue;
                }
            Student student = repository.getStudent(id);
            System.out.println(student);
        }
    }
    private void addPoints() {
        System.out.println("Enter an id and points or 'back' to return:");
        while (true) {
            input = scanner.nextLine().strip();
            if (input.equals("back")) {
                break;
            }
            String[] tokens = input.split(" ");
            int id;
            try {
                id = Integer.parseInt(tokens[0]);
                if (repository.invalidStudent(id)) {
                    throw new Exception("");
                }
            } catch (Exception e) {
                System.out.printf("No student is found for id=%s.\n", tokens[0]);
                continue;
            }
            if (!pointsEnterPattern.matcher(input).matches()) {
                System.out.println("Incorrect points format.");
                continue;
            }
            repository.addPoints(tokens);
            System.out.println("Points updated.");
        }
    }
    public void addStudents() {
        System.out.println("Enter student credentials or 'back' to return");
        int studentsCounter = 0;
        while (true) {
            input = scanner.nextLine().strip();
            if (input.equals("back")) {
                break;
            }
            if (inputInvalid()) {
                System.out.println("Incorrect credentials.");
                continue;
            }
            if (nameInvalid(StudentInfo.FIRST_NAME.getIndex())) {
                System.out.println("Incorrect first name.");
                continue;
            }
            if (nameInvalid(StudentInfo.LAST_NAME.getIndex())) {
                System.out.println("Incorrect last name.");
                continue;
            }
            if (emailInvalid()) {
                System.out.println("Incorrect email.");
                continue;
            }
            if (repository.checkEmailUsed(currentInfo[StudentInfo.EMAIL.getIndex()])) {
                System.out.println("This email is already taken.");
                continue;
            }
            repository.store(currentInfo);
            System.out.println("The student has been added.");
            studentsCounter++;
        }
        System.out.printf("Total %d students have been added.\n", studentsCounter);
    }
    public boolean inputInvalid() {
        boolean isValid = false;
        int firstSpace = input.indexOf(" ");
        int lastSpace = input.lastIndexOf(" ");
        if (firstSpace == lastSpace || firstSpace == -1 || lastSpace == -1) {
            isValid = true;
        } else {
           fillInfoArray(firstSpace, lastSpace);
        }
        return isValid;
    }
    public boolean nameInvalid(int index) {
    boolean nameInvalid = false;
    String[] names = currentInfo[index].split(" ");
    for (String name : names)
        if (name.length() < 2 || !namePattern.matcher(name).matches()) {
            nameInvalid = true;
            break;
        }
    return nameInvalid;
    }
    public boolean emailInvalid() {
        return !emailPattern.matcher(currentInfo[StudentInfo.EMAIL.getIndex()]).matches();
    }
    public void fillInfoArray(int firstSpace, int lastSpace) {
        currentInfo[StudentInfo.FIRST_NAME.getIndex()] = input.substring(0, firstSpace);
        currentInfo[StudentInfo.LAST_NAME.getIndex()] = input.substring(firstSpace + 1, lastSpace);
        currentInfo[StudentInfo.EMAIL.getIndex()] = input.substring(lastSpace + 1);
    }
}
