
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Graphic {
    public PieChart GetGraphicFromCities() throws SQLException, ClassNotFoundException, IOException {
        BData.Conn();
        Map<String,Integer> rez = BData.GetFromCities();
        PieChart chart  = new PieChartBuilder().width(800).height(600).title("По городам").build();
        for(var i: rez.keySet()) chart.addSeries(i, rez.get(i));
        BData.CloseDB();
        return chart;
    }

    public PieChart GetGraphicFromGenders() throws SQLException, ClassNotFoundException, IOException {
        BData.Conn();
        Map<String,Integer> rez = BData.GetFromGenders();
        PieChart chart  = new PieChartBuilder().width(800).height(600).title("По гендеру").build();
        for(var i: rez.keySet()) chart.addSeries(i, rez.get(i));
        BData.CloseDB();
        return chart;
    }

    public  PieChart GetGraphicFromBirth() throws SQLException, ClassNotFoundException, IOException {
        BData.Conn();
        Map<String,Integer> rez = BData.GetFromBirth();
        PieChart chart  = new PieChartBuilder().width(800).height(600).title("По возрасту").build();
        for(var i: rez.keySet()) chart.addSeries(i, rez.get(i));
        BData.CloseDB();
        return chart;
    }

    public  CategoryChart GiveGraphicFromTasks(String name,String topic,String t) throws ClassNotFoundException, SQLException {
        BData.Conn();
        Map<String,Integer> rez = BData.GiveCorTasksCount(name,topic,t);
        CategoryChart chart = new CategoryChart(800, 600, Styler.ChartTheme.Matlab);
        chart.setTitle("Кол-во выполненных в теме"+ topic);
        chart.setXAxisTitle("Задания");
        chart.setYAxisTitle("Кол-во баллов");
        var y= new Integer[rez.size()];
        var x = new String[rez.size()];
        var j=0;
        for( var i: rez.keySet()){
            y[j]=rez.get(i);
            x[j]=i;
            j++;
        }
        chart.addSeries(t, Arrays.asList(x), Arrays.asList(y));
        chart.getStyler().setXAxisLabelRotation(270);
        BData.CloseDB();
        return chart;
    }

    public List<CategoryChart> GiveGraphicTasksCountFromGender(String topic,String t) throws ClassNotFoundException, SQLException {
        BData.Conn();
        List<CategoryChart> list = new ArrayList<>();
        var rez = BData.GiveCorTasksCountFromGender(topic,t);
        CategoryChart chart1 = new CategoryChartBuilder().width(800).height(600).title("Gender Histogram").xAxisTitle("Пол").yAxisTitle("Кол-во").build();
        CategoryChart chart2 = new CategoryChartBuilder().width(800).height(600).title("Cities Histogram").xAxisTitle("Города").yAxisTitle("Кол-во").build();
        var y1= new Integer[rez.get("Gender").size()];
        var x1 = new String[rez.get("Gender").size()];
        var j=0;
        for(var a: rez.get("Gender").keySet()){
            y1[j]=rez.get("Gender").get(a);
            x1[j]=a;
            j++;
        }
        j=0;
        var y2= new Integer[rez.get("Cities").size()];
        var x2 = new String[rez.get("Cities").size()];
        for(var a: rez.get("Cities").keySet()){
            y2[j]=rez.get("Cities").get(a);
            x2[j]=a;
            j++;
        }
        chart1.addSeries(t, Arrays.asList(x1), Arrays.asList(y1));
        chart2.addSeries(t, Arrays.asList(x2), Arrays.asList(y2));
        chart2.getStyler().setXAxisLabelRotation(270);
        list.add(chart1);
        list.add(chart2);
        BData.CloseDB();
        return  list;
    }
}
