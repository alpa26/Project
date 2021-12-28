import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private  List<String> fileLines;
    private List<Human> humans;
    private List<Student> students;

    public Parser (String filePath,String a) throws IOException, SQLException {
        fileLines = Files.readAllLines(Paths.get(filePath));
        humans = ParseVkUsers(a);
        students = ParseProductCsv();
        ConnectCsvVk();
    }

    public  void  ConnectCsvVk() throws SQLException {
        for(Student stud: students)
            for(Human h: humans){
                if(!stud.getName().equals(h.getName()) && !(stud.getLastName()+stud.getFirstName()).equals(h.getLastName()+h.getFirstName()) )
                    continue;
                stud.setdateOfBirth(h.getDateOfBirth());
                stud.setCity(h.getCity());
                stud.setGender1(h.getGender());
                stud.setIsBanned(h.getisBanned());
                stud.setId(h.getId());
                stud.setPhoto(h.getPhoto());
            }
        System.out.println("bruh");
    }


    public  List<Student> ParseProductCsv(){
        List<Student> products = new ArrayList<Student>();
        String[] topics = fileLines.get(0).split(";");
        String[] tasks = fileLines.get(1).split(";");
        String[] maxVal= fileLines.get(2).split(";");
        fileLines.remove(2);
        fileLines.remove(1);
        fileLines.remove(0);

        for (String fileLine : fileLines) {
            String[] splitedText = fileLine.split(";");
            String name = splitedText[0];
            Student student = new Student(name,splitedText[1]);
            student.setCourse(GetCourse(topics));
            student.setCourse(tasks,splitedText,maxVal);
            products.add(student);
        }
        fileLines = null;
        return products;
    }

    public static Course GetCourse(String[] topics) {
        Course newCourse = new Course("Java");
        for(String el : topics){
            if(!el.isEmpty()){
                Topic topic = new Topic();
                topic.setName(el);
                newCourse.setTopic(topic);
                Topic topic1 = new Topic();
                topic1.setName(el);
                newCourse.setTopicMaxMarks(topic1);
            }
        }
        // листы maxMarks и topics почему то содержат первый элемент с именем "", не смотря на условие в цикле
        newCourse.delTopic(0);
        return newCourse;
    }

    public  List<Human> ParseVkUsers(String information){
        List<Human> humans = new ArrayList<Human>();
        String newInformation = information.substring(36,information.length()-4);
        String f ="\\}"+","+"\\{";
        String[] b = newInformation.split(f);
        for(String str1 : b)
        {
            String[] inf = str1.split("\",\"|,\"");
            String name = inf[1].split(":")[1];
            if(name.equals("DELETED"))
                name = inf[1].split(":")[1];
            else
                name = inf[2].split(":")[1]+" "+inf[1].split(":")[1];
            Human human = new Human(name.replaceAll("\"",""));
            human.setId(Integer.parseInt(inf[0].split(":")[1]));
            for(int i=3; i< inf.length;i++)
            {
                String[] g = inf[i].split("\":\"");
                if(g[0].equals("deactivated"))
                    human.setIsBanned(true);
                else if(g[0].equals("photo_max_orig"))
                    human.setPhoto(g[1].replaceAll("\"|\"}",""));
                else if(g[0].split("\":")[0].equals("sex"))
                    human.setGenderCsv(Integer.parseInt(g[0].split("\":")[1]));
                else if(g[0].equals("title"))
                    human.setCity(g[1].replaceAll("\"}",""));
                else if(g[0].equals("bdate"))
                    human.setdateOfBirth(g[1].replaceAll("\"",""));
            }
            humans.add(human);
        }
        return humans;
    }

    public List<String> getFileLines() {
        return fileLines;
    }
    public List<Student> getStudents() {
        return students;
    }
}
