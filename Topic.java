import java.util.HashMap;
import java.util.Map;

public class Topic {
    private  String name="";
    private  Map<String,Integer> tasks = new HashMap<String,Integer>();

    public Topic(){
    }

    public String getName() {
        return name;
    }

    public void setName(String newname) {
         name=newname;
    }

    public Map<String, Integer> getTasks() {
        return tasks;
    }
    public void setTasks( Map<String,Integer> newtasks){
        this.tasks = new HashMap<String,Integer>(newtasks);
    }

    public void setTask(String task, int value){
        int i=0;
        String key1=task;
        while(tasks.containsKey(key1)) {
            i++;
            key1= task+i;
        }
        tasks.put(key1,value);
    }

    public void delTask(String task){
        tasks.remove(task);
    }

    public String ToString(){
        return getName() + " " +  tasks.toString();
    }
}
