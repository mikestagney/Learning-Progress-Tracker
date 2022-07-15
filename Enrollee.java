package tracker;


public class Enrollee {

    int id;
    int currentPoints;
    int totalCoursePoints;
    double percentCompleted;

    Enrollee(int id, double points, int totalCoursePoints) {
        this.id = id;
        currentPoints = (int) points;
        this.totalCoursePoints = totalCoursePoints;
        setPercentCompleted();
    }
    private void setPercentCompleted() {
        percentCompleted = (double) currentPoints / (double) totalCoursePoints * 100;
    }
    public int getCurrentPoints() {
        return this.currentPoints;
    }
    public int getId() {
        return this.id;
    }
    @Override
    public String toString() {
        return String.format("%5d %3d        %3.1f%%", id, currentPoints, percentCompleted);
    }
}
