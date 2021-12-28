
import com.vk.api.sdk.exceptions.ClientException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class Main {

    public static void main(String[] args) throws ClientException, IOException, SQLException, ClassNotFoundException {
        //var students = ParseCSV();
        //MakeBD(students);
        var v= new View();
        v.setVisible(true);
        System.out.println("bruh");
    }

    public static List<Student>  ParseCSV() throws ClientException, SQLException, IOException {
        String token = "708a2756bbe7b6de66817fce3cb3f0fc1636ae1b46717845e2236a0b11324149d53413cc57f017311b156";
        VkIp ipParser1 = new VkIp(token,474293321);
        String a =ipParser1.getGroupInf("iot_second_urfu", 1245);
        String filePath = "java.csv";
        Parser parser = new Parser(filePath,a);
        return parser.getStudents();
    }

    public static void MakeBD(List<Student> students) throws ClientException, SQLException, IOException, ClassNotFoundException {
        BData.Conn();
        BData.CreateDB();
        BData.WriteDB(students);
        BData.CloseDB();
    }
}
