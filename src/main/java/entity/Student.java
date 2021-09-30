package entity;

public class Student {
    private String id;
    private String name;
    private String absences;
    private String P1;
    private String P2;
    private String P3;
    private String situation;
    private String gradeForFinalTest;

    public Student(String id, String name, String absences, String p1, String p2, String p3, String situation, String gradeForFinalTest) {
        this.id = id;
        this.name = name;
        this.absences = absences;
        P1 = p1;
        P2 = p2;
        P3 = p3;
        this.situation = situation;
        this.gradeForFinalTest = gradeForFinalTest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbsences() {
        return absences;
    }

    public void setAbsences(String absences) {
        this.absences = absences;
    }

    public String getP1() {
        return P1;
    }

    public void setP1(String p1) {
        P1 = p1;
    }

    public String getP2() {
        return P2;
    }

    public void setP2(String p2) {
        P2 = p2;
    }

    public String getP3() {
        return P3;
    }

    public void setP3(String p3) {
        P3 = p3;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getGradeForFinalTest() {
        return gradeForFinalTest;
    }

    public void setGradeForFinalTest(String gradeForFinalTest) {
        this.gradeForFinalTest = gradeForFinalTest;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", absences='" + absences + '\'' +
                ", P1='" + P1 + '\'' +
                ", P2='" + P2 + '\'' +
                ", P3='" + P3 + '\'' +
                ", situation='" + situation + '\'' +
                ", gradeForFinalTest='" + gradeForFinalTest + '\'' +
                '}';
    }
}
