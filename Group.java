
import java.util.ArrayList;

public class Group {
    private final String name;
    private final ArrayList<Student> students =new ArrayList<Student>();
    public Group (String name)
    {
        this.name=name;
    }

    public void setStudents(Student student) {
        students.add(student);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
