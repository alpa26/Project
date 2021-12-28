import lombok.SneakyThrows;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class View extends JFrame {
    private final Graphic gr= new Graphic();
    private final ArrayList<Button> buttons = new ArrayList<Button>();
    private final ArrayList<XChartPanel> list = new ArrayList<XChartPanel>();
    private  XChartPanel newpanel;
    private JTextField field  =new JTextField();
    Container c;
    public View() throws SQLException, IOException, ClassNotFoundException {
        super("Graphics");
        this.setBounds(1,1,2000, 620);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Graphic gr= new Graphic();
        newpanel=new XChartPanel<CategoryChart>(gr.GiveGraphicFromTasks("Куланчеев Евгений","2. Базовый синтаксис. Типы","tasks"));
        c= this.getContentPane();
        var ba =new BorderLayout();
        ba.setVgap(2);
        c.setLayout(ba);
        setList();
        setButtons();
        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,1));
        for(var b: buttons) buttonPanel.add(b);
        c.add(buttonPanel,BorderLayout.WEST);
        c.add(field,BorderLayout.PAGE_START);
        c.add(list.get(2),BorderLayout.CENTER);
    }

    public void setList() throws SQLException, IOException, ClassNotFoundException {
        list.add(new XChartPanel<CategoryChart>(gr.GiveGraphicFromTasks("Куланчеев Евгений","2. Базовый синтаксис. Типы","tasks")));
        list.add(new XChartPanel<CategoryChart>(gr.GiveGraphicFromTasks("Куланчеев Евгений","2. Базовый синтаксис. Типы","questions")));
        list.add(new XChartPanel<PieChart>(gr.GetGraphicFromGenders()));
        list.add(new XChartPanel<PieChart>(gr.GetGraphicFromCities()));
        list.add(new XChartPanel<PieChart>(gr.GetGraphicFromBirth()));
        var newlist =gr.GiveGraphicTasksCountFromGender("2. Базовый синтаксис. Типы","tasks");
        list.add(new XChartPanel<CategoryChart>(newlist.get(0)));
        list.add(new XChartPanel<CategoryChart>(newlist.get(1)));
        newlist =gr.GiveGraphicTasksCountFromGender("2. Базовый синтаксис. Типы","questions");
        list.add(new XChartPanel<CategoryChart>(newlist.get(0)));
        list.add(new XChartPanel<CategoryChart>(newlist.get(1)));
        for(var w:list) w.setBounds(10,10,700,600);
    }

    public void setButtons(){
        buttons.add(new Button("Вопросы по теме"));
        buttons.get(0).addActionListener(new ActionListener(){
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                for(var i:list) c.remove(i);
                c.remove(newpanel);
                if(field.getText().length()!=0){
                    newpanel=new XChartPanel<CategoryChart>(gr.GiveGraphicFromTasks(field.getText(),"2. Базовый синтаксис. Типы","tasks"));
                    c.add(newpanel);
                }else{
                    c.add(list.get(0),BorderLayout.CENTER);
                }
                repaint();
                pack();
            }
        });
        buttons.add(new Button("Задания по теме"));
        buttons.get(1).addActionListener(new ActionListener(){
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                for(var i:list) c.remove(i);
                c.remove(newpanel);
                if(field.getText().length()!=0){
                    newpanel=new XChartPanel<CategoryChart>(gr.GiveGraphicFromTasks(field.getText(),"2. Базовый синтаксис. Типы","questions"));
                    c.add(newpanel);
                }else{
                    c.add(list.get(1),BorderLayout.CENTER);
                }
                repaint();
                pack();
            }
        });
        buttons.add(new Button("По Гендеру"));
        buttons.get(2).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                for(var i:list) c.remove(i);
                c.add(list.get(2),BorderLayout.CENTER);
                repaint();
                pack();
            }
        });
        buttons.add(new Button("По Городу"));
        buttons.get(3).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                c.remove(newpanel);
                for(var i:list) c.remove(i);
                c.add(list.get(3),BorderLayout.CENTER);
                repaint();
                pack();
            }
        });
        buttons.add(new Button("По Возрасту"));
        buttons.get(4).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                c.remove(newpanel);
                for(var i:list) c.remove(i);
                c.add(list.get(4),BorderLayout.CENTER);
                repaint();
                pack();
            }
        });
        buttons.add(new Button("Вопросы по теме по полу и по географии"));
        buttons.get(5).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                c.remove(newpanel);
                for(var i:list) c.remove(i);
                c.add(list.get(5),BorderLayout.CENTER);
                c.add(list.get(6),BorderLayout.LINE_END);
                pack();
            }
        });
        buttons.add(new Button("Задания по теме по полу и по географии"));
        buttons.get(6).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                c.remove(newpanel);
                for(var i:list) c.remove(i);
                c.add(list.get(7),BorderLayout.CENTER);
                c.add(list.get(8),BorderLayout.LINE_END);
                pack();
            }
        });
    }
}
