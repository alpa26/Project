
import java.util.ArrayList;

public class Course {
    private  ArrayList<Topic> maxMarks= new ArrayList<Topic>();
    private final String Name;
    private  ArrayList<Topic> topics= new ArrayList<Topic>();

    public Course(String name){
        this.Name = name;
    }

    // Добавление
    public void setTopic(Topic topic) {
        this.topics.add(topic);
    }

    public void setTopics(ArrayList<Topic> newtopics) {
        for(Topic t: newtopics){
            Topic top = new Topic();
            top.setName(t.getName());
            top.setTasks(t.getTasks());
            this.topics.add(top);
        }
    }

    public void setTopicMaxMarks(Topic topic) {
        this.maxMarks.add(topic);
    }

    public void setMaxMarks(ArrayList<Topic> marks) {
        for(Topic t: marks){
            Topic top = new Topic();
            top.setName(t.getName());
            top.setTasks(t.getTasks());
            this.maxMarks.add(top);
        }
    }

    public void setMaxMarks(String[] tasks,String[] marks){
        setMarksToTopic(tasks,marks,"maxMarks");
    }

    public void setMarksToTopic(String[] tasks,String[] marks,String flag) {
        int i=0;
        boolean IsDZ=false;
        for(int j=2;j<tasks.length;j++){
            if(tasks[j].length()==2 && IsDZ && i!=topics.size()-1){
                i++;
            } else if(!IsDZ &&tasks[j].length()==2)
                IsDZ=true;
            if(flag.equals("topics"))
                topics.get(i).setTask(tasks[j],Integer.parseInt(marks[j]));
            else
                maxMarks.get(i).setTask(tasks[j],Integer.parseInt(marks[j]));
        }
        i=1;
    }

    public Integer getMaxMark() {
        return maxMarks.get(0).getTasks().get("ДЗ");
    }

    public void delTopic(int ind) {
        this.topics.remove(ind);
        this.maxMarks.remove(ind);
    }

    public String getName() {
        return Name;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public ArrayList<Topic> getMaxMarks() {
        return maxMarks;
    }

    public void showInf(){
        for(Topic topic:topics)
            System.out.println(topic.ToString());
    }
}
