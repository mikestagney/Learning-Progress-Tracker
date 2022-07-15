package tracker;

public enum StudentInfo {

    NUMBER_FIELDS(3),
    FIRST_NAME(0),
    LAST_NAME(1),
    EMAIL(2);

    int index;

    StudentInfo(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

}
