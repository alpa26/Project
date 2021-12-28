import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BData {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void Conn() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:BDStudents.s3db");
    }

    // --------Создание таблицы--------
    public static void CreateDB() throws ClassNotFoundException, SQLException {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'pers_name' text,'gender' text, 'idVk' INT,'isBanned' INT, 'dateOfBirth' text, 'city' text, 'photo' text);");
        statmt.execute("CREATE TABLE if not exists 'students' ('id_st' INT , 'id_cour' INT,'group' text, FOREIGN KEY('id_st') REFERENCES 'users'('id'));");
        statmt.execute("CREATE TABLE if not exists 'courses' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'Max' INT, FOREIGN KEY('id') REFERENCES 'students'('id_cour'));");
        statmt.execute("CREATE TABLE if not exists 'tasks' ('id_tasks' text , 'name_topic' text,'name_tasks' text);");
        System.out.println("Таблица создана или уже существует.");
    }

    // --------Заполнение таблицы--------
    public static void WriteDB(List<Student> students) throws SQLException, ClassNotFoundException {
        Course c= students.get(0).getCourse();
        statmt.execute("INSERT INTO 'courses' ('name', 'Max') VALUES ('"+c.getName()+"', "+c.getMaxMark()+"); ");
        StringBuilder request1 = new StringBuilder("CREATE TABLE if not exists 'scores' ('id_st' INT PRIMARY KEY, 'id_cour' INT");
        int h=0;
        for(Topic t: c.getTopics()) {
            String  topic =t.getName();
            for(String i :t.getTasks().keySet()) {
                statmt.execute("INSERT INTO 'tasks' ('id_tasks', 'name_topic','name_tasks')  VALUES ('task"+h+"', '"+topic+"', '"+i+"'); ");
                request1.append(", 'task"+h+"' INT");
                h++;
            }
        }
        request1.append(",FOREIGN KEY('id_st') REFERENCES 'students'('id'));");
        statmt.execute(request1.toString());

        request1 = new StringBuilder("INSERT INTO 'scores' ('id_st', 'id_cour'");
        for(int i=0;i<h;i++)
            request1.append(", 'task"+i+"'");
        request1.append(") VALUES ("+(-1)+", "+1);
        for(Topic t: c.getMaxMarks())
            for(String task :t.getTasks().keySet()){
                request1.append(", "+t.getTasks().get(task));
            }
        request1.append("); ");
        statmt.execute(request1.toString());

        int a=0;
        int id_st =1;
        for(Student st: students){
            if(st.getisBanned())
                a=1;
            statmt.execute("INSERT INTO 'users' ('pers_name','gender', 'idVk', 'isBanned', 'dateOfBirth', 'city', 'photo') VALUES ('"+st.getName()+"', '"+st.getGender()+"', "+st.getId()+", "+a+", '"+st.getDateOfBirth()+"', '"+st.getCity()+"', '"+st.getPhoto()+"'); ");
            statmt.execute("INSERT INTO 'students' ('id_st', 'id_cour', 'group') VALUES ("+id_st+", "+1+", '"+st.getGroup()+"'); ");
            request1 = new StringBuilder("INSERT INTO 'scores' ('id_st', 'id_cour'");
            for(int i=0;i<h;i++)
                request1.append(", 'task"+i+"'");
            request1.append(") VALUES ("+id_st+", "+1);
            for(Topic t: st.getCourse().getTopics())
                for(String task :t.getTasks().keySet()){
                    request1.append(", "+t.getTasks().get(task));
                }
            request1.append("); ");
            statmt.execute(request1.toString());
            id_st++;
        }
        System.out.println("Таблица заполнена");
    }


    // --------select * from students Вывод таблицы--------
    public static List<Student> ReadDB(String request) throws ClassNotFoundException, SQLException
    {
        List<Student> students = new ArrayList<Student>();
        Map<String,HashMap<String,String>> tasks = new HashMap<String,HashMap<String,String>>();
        ArrayList<Topic> topics = new ArrayList<Topic>();
        ArrayList<Topic> MaxScores = new ArrayList<Topic>();
        statmt = conn.createStatement();
        resSet = statmt.executeQuery("SELECT * FROM tasks");
        Topic topic = new Topic();
        while(resSet.next()) {
            String id_tasks = resSet.getString("id_tasks");
            String name_topic = resSet.getString("name_topic");
            String name_tasks = resSet.getString("name_tasks");
            if(!topic.getName().equals(name_topic)){
                topics.add(topic);
                MaxScores.add(topic);
                topic = new Topic();
                topic.setName(name_topic);
            }
            topic.setTask(name_tasks,0);
            if(!tasks.containsKey(name_topic))
                tasks.put(name_topic, new HashMap<>());
            tasks.get(name_topic).put(name_tasks,id_tasks);
        }

        resSet = statmt.executeQuery("SELECT * FROM scores where  scores.id_st= -1");
        while(resSet.next()) {
            for(int i=0;i<MaxScores.size();i++){
                for(String j: MaxScores.get(i).getTasks().keySet()) {
                    MaxScores.get(i).getTasks().put(j,
                            resSet.getInt(tasks.get(MaxScores.get(i).getName()).get(j)));
                }
            }
        }

        resSet = statmt.executeQuery(request);
        while(resSet.next()) {
            Student st = new Student(resSet.getString("pers_name"),
                    resSet.getString("group")
            );
            st.setPhoto(resSet.getString("photo"));
            st.setId(resSet.getInt("idVk"));
            st.setGender1(resSet.getString("gender"));
            st.setCity(resSet.getString("city"));
            if(resSet.getInt("isBanned")==0)
                st.setIsBanned(false);
            else  st.setIsBanned(true);
            st.setdateOfBirth(resSet.getString("dateOfBirth"));
            Course course = new Course(
                    resSet.getString("name"));
            course.setTopics(topics);
            course.setMaxMarks(MaxScores);
            for(int i=0;i<course.getTopics().size();i++){
                for(String j: course.getTopics().get(i).getTasks().keySet()) {
                    course.getTopics().get(i).getTasks().put(j,
                            resSet.getInt(tasks.get(course.getTopics().get(i).getName()).get(j)));
                }
            }
            st.setCourse(course);
            students.add(st);
        }
        return students;
    }
    public static Map<String,Integer> GiveCorTasksCount(String name,String topic1,String t) throws ClassNotFoundException, SQLException
    {
        Map<String,Integer> rezult = new HashMap<String,Integer>();
        List<Student> students = ReadDB("SELECT * FROM users\n" +
                " INNER JOIN courses\n" +
                "\t     ON courses.id = 1\n" +
                " INNER JOIN students\n" +
                "\t     ON users.id = students.id_st\n" +
                " INNER JOIN scores\n" +
                "\t     ON users.id = scores.id_st where users.pers_name = '"+name+"'");
        for(Student st: students){
            for(int j=0;j<st.getCourse().getTopics().size();j++){
                if(!st.getCourse().getTopics().get(j).getName().equals(topic1))
                    continue;
                Topic topic = st.getCourse().getTopics().get(j);
                for(String n :  topic.getTasks().keySet()) {
                    if(t.equals("questions") && n.contains("Контрольный вопрос")){
                        rezult.put(n,topic.getTasks().get(n));
                    }
                    if(t.equals("tasks") &&  !n.contains("Контрольный вопрос") && n.length()>2){
                        rezult.put(n,topic.getTasks().get(n));
                    }

                }

            }
        }
        return rezult;
    }

    public static Map<String,HashMap<String,Integer>> GiveCorTasksCountFromGender(String name,String t) throws ClassNotFoundException, SQLException
    {
        Map<String,HashMap<String,Integer>> rezult = new HashMap<String,HashMap<String,Integer>>();
        rezult.put("Gender", new HashMap<>());
        rezult.put("Cities", new HashMap<>());
        List<Student> students = ReadDB("SELECT * FROM users\n" +
                " INNER JOIN courses\n" +
                "\t     ON courses.id = 1\n" +
                " INNER JOIN students\n" +
                "\t     ON users.id = students.id_st\n" +
                " INNER JOIN scores\n" +
                "\t     ON users.id = scores.id_st");
        for(Student st: students){
            int c=0;
            for(int j=0;j<st.getCourse().getTopics().size();j++){
                if(!st.getCourse().getTopics().get(j).getName().equals(name))
                    continue;
                Topic topic = st.getCourse().getTopics().get(j);
                for(String n :  topic.getTasks().keySet()) {
                    if(t.equals("questions") && n.contains("Контрольный вопрос")){
                        if(topic.getTasks().get(n) == 1)
                            c++;
                    }
                    if(t.equals("tasks") &&  !n.contains("Контрольный вопрос") && n.length()>2){
                        if(topic.getTasks().get(n) == students.get(0).getCourse().getMaxMarks().get(j).getTasks().get(n))
                            c++;
                    }
                }
            }
            if(rezult.get("Gender").containsKey(st.getGender()))
                rezult.get("Gender").put(st.getGender(),rezult.get("Gender").get(st.getGender())+c);
            else
                rezult.get("Gender").put(st.getGender(),c);
            if(rezult.get("Cities").containsKey(st.getCity()))
                rezult.get("Cities").put(st.getCity(),rezult.get("Cities").get(st.getCity())+c);
            else
                rezult.get("Cities").put(st.getCity(),c);
        }
        return rezult;
    }

    public static Map<String,Integer> GetFromCities() throws ClassNotFoundException, SQLException
    {
        Map<String,Integer> rezult = new HashMap<String,Integer>();
        statmt = conn.createStatement();
        resSet = statmt.executeQuery("select city,count(id)as c from users group by users.city");
        while(resSet.next()) {
            String city =resSet.getString("city");
            if(city.equals("null"))
                city="Не указано";
            rezult.put(city,resSet.getInt("c"));
        }
        return rezult;
    }

    public static Map<String,Integer> GetFromGenders() throws ClassNotFoundException, SQLException
    {
        Map<String,Integer> rezult = new HashMap<String,Integer>();
        statmt = conn.createStatement();
        resSet = statmt.executeQuery("select gender,count(id) as c from users group by users.gender");
        while(resSet.next()) {
            rezult.put(resSet.getString("gender"),resSet.getInt("c"));
        }
        return rezult;
    }

    public static Map<String,Integer> GetFromBirth() throws ClassNotFoundException, SQLException
    {
        Map<String,Integer> rezult = new HashMap<String,Integer>();
        statmt = conn.createStatement();
        resSet = statmt.executeQuery("select dateOfBirth,count(id) as c from users group by users.dateOfBirth");
        while(resSet.next()) {
            String dateOfBirth = resSet.getString("dateOfBirth");
            if(dateOfBirth.equals("null"))
                rezult.put("-",resSet.getInt("c"));
            else if(dateOfBirth.length()==4||dateOfBirth.length()==5||dateOfBirth.length()==3){
                if(rezult.containsKey("19"))
                    rezult.put("19",rezult.get("19")+resSet.getInt("c"));
                else
                    rezult.put("19",resSet.getInt("c"));
            } else{
                String[] f= dateOfBirth.split("\\.");
                int year = Integer.parseInt(f[2]);
                if(rezult.containsKey(Integer.toString(2021-year)))
                    rezult.put(Integer.toString(2021-year),rezult.get(Integer.toString(2021-year))+resSet.getInt("c"));
                else
                    rezult.put(Integer.toString(2021-year),resSet.getInt("c"));
            }
        }
        return rezult;
    }
    // --------Закрытие--------
    public static void CloseDB() throws ClassNotFoundException, SQLException {
        conn.close();
        statmt.close();
        resSet.close();
    }

}
