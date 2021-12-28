import java.util.ArrayList;

public class Student extends Human{
    private final String group;
    private ArrayList<Course> courses = new ArrayList<Course>();
    public Student(String name, String group)
    {
        super(name);
        this.group=group;
    }

    public void setCourse(Course course) {
        this.courses.add(course);
    }

    public void setCourse(String[] tasks,String[] marks, String [] max) {
        courses.get(0).setMarksToTopic(tasks,marks,"topics");
        courses.get(0).setMaxMarks(tasks,max);
    }

    public Course getCourse() {
        return courses.get(0);
    }

    public String getGroup() { return group; }

    public void showAllInf()
    {
        showInf();
        courses.get(0).showInf();
    }
}
