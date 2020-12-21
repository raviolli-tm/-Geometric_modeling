import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class My_in extends JFrame {

    String f = "";
    String[] Points_table_model = new String[] {"id","x", "y","z"};
    String[] Values_table_model = new String[] {"id", "x"};
    String[] Weight_table_model = new String[] {"id", "weight"};
    double x1;
    double x2;
    double y1;
    double y2;
    int current_type =0;
    int power;
    int current_number =0;
    int current_view =0;
    Boolean isHermitreWasPrevious=false;
    TextField Tx1;
    TextField Tx2;
    TextField Txbeta1;
    TextField Txbeta2;
    JScrollPane content_points;
    JScrollPane content_points_2;
    JScrollPane content_values;
    JScrollPane content_weight;
    JTable temp_points = new JTable(new String[][]{},Points_table_model);
    JTable temp_points_2 = new JTable(new String[][]{},Points_table_model);
    JTable temp_values = new JTable(new String[][]{},Values_table_model);
    JTable temp_weight = new JTable(new String[][]{},Weight_table_model);
    static JFrame F;
    JFrame JF;
    JPanel JP,JP0;
    JButton But_opened_BS;
    JButton But_closed_BS;
    JButton But_periodic_BS;
    JButton But_Bezier;
    JButton But_composite_Bezier;
    JButton But_weight_Bezier;
    JButton But_Beta_spline;
    JButton But_Hermite_curve;
    JButton But_NURBS;
    JButton But_CatmullRom;

    JButton But_preview;
    JButton But_construct_tables;
    JButton But_cancel;
    JButton But_take_previous;
    JButton But_save_tables;
    JButton But_draw;
    JButton But_xy_view;
    JButton But_xz_view;
    JButton But_zy_view;
    curve_calculation current_curve;
   static ArrayList<curve_calculation> curves = new ArrayList<curve_calculation>();
    ArrayList<Boolean> paint_polygonal_line = new ArrayList<>();

    private JCheckBox simple_line = new JCheckBox("<html>Сохранить вместе<br> с полигональной линией</html>");




    JLabel er;

    My_in() {
        int WI = 550;
        int HI;
        if(Toolkit.getDefaultToolkit().getScreenSize().height>800){
           HI = 800;
        }else{
            HI =Toolkit.getDefaultToolkit().getScreenSize().height;
        }

        JF = new JFrame();
        JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JF.setSize(WI, HI);
        JF.setTitle("Построение кривых");
        JF.setLayout(null);

        JP = new JPanel();
        JP.setBackground(Color.LIGHT_GRAY);
        JP.setLayout(null);
        JP.setSize(WI,HI);
        JP.setLocation(0,0);

        JP0 = new JPanel();
        JP0.setBackground(Color.RED);
        JP0.setLayout(null);
        int WIwithJP0;
        if(WI+750>Toolkit.getDefaultToolkit().getScreenSize().width) {
           WIwithJP0 = Toolkit.getDefaultToolkit().getScreenSize().width-550;
        }
        else {
            WIwithJP0=750;
        }
        JP0.setSize(WIwithJP0, HI);
        JP0.setLocation(550,0);


        But_opened_BS = new JButton("<html><center>Добавить <br> открытый B-spline</html>");
        But_closed_BS = new JButton("<html><center>Добавить <br> закрытый B-spline</html>");
        But_periodic_BS = new JButton("<html><center>Добавить <br> периодический B-spline</html>");
        But_Bezier = new JButton("<html><center>Добавить <br> кривую Безье</html>");
        But_composite_Bezier = new JButton("<html><center>Добавить <br> составную Безье</html>");
        But_weight_Bezier = new JButton("<html><center>Добавить <br> весовую Безье</html>");
        But_Beta_spline = new JButton("<html><center>Добавить <br> beta-spline</html>");
        But_Hermite_curve =  new JButton("<html><center>Добавить <br> кривую Эрмита</html>");
        But_NURBS = new JButton("<html><center>Добавить <br> NURBS</html>");
        But_CatmullRom = new JButton("<html><center>Добавить <br> кривую CatmullRom</html>");
        
        But_cancel = new JButton("<html>Отмена</html>");
        But_preview = new JButton("<html><center>Предварительный просмотр</html>");
        But_save_tables = new JButton("<html><center>Сохранить</html>");
        But_draw = new JButton("<html><center>Отрисовать</html>");
        But_construct_tables = new JButton("<html><center>Подготовить таблицы<br> для ввода значений</html>");
        But_take_previous = new JButton("<html><center>Взять предыдущие данные<br> для отрисовки этой кривой</html>");
        But_xy_view = new JButton("<html><center>XY</html>");
        But_xz_view = new JButton("<html><center>XZ</html>");
        But_zy_view = new JButton("<html><center>ZY</html>");

        But_construct_tables.setVisible(false);
        But_take_previous.setVisible(false);
        But_cancel.setVisible(false);
        But_preview.setVisible(false);
        But_save_tables.setVisible(false);
        But_draw.setVisible(false);
        But_xy_view.setEnabled(false);

        But_cancel.setSize(90, 40);
        But_preview.setSize(155, 40);
        But_save_tables.setSize(90, 40);
        But_draw.setSize(100, 50);
        But_construct_tables.setSize(230,40);
        But_take_previous.setSize(230,40);
        But_opened_BS.setSize(180, 50);
        But_closed_BS.setSize(180, 50);
        But_periodic_BS.setSize(180, 50);
        But_Bezier.setSize(180, 50);
        But_composite_Bezier.setSize(180, 50);
        But_weight_Bezier.setSize(180, 50);
        But_Hermite_curve.setSize(180, 50);
        But_Beta_spline.setSize(180, 50);
        But_NURBS.setSize(180, 50);
        But_CatmullRom.setSize(180, 50);
        But_xy_view.setSize(40,40);
        But_zy_view.setSize(40,40);
        But_xz_view.setSize(40,40);


        But_opened_BS.setLocation(30, 30);
        But_closed_BS.setLocation(WI-210, 100);
        But_periodic_BS.setLocation(WI-210, 30);
        But_Bezier.setLocation(30, 100);
        But_composite_Bezier.setLocation(30, 170);
        But_weight_Bezier.setLocation(30, 240);
        But_Hermite_curve.setLocation(WI-210, 170);
        But_Beta_spline.setLocation(WI-210, 240);
        But_NURBS.setLocation(WI-210, 310);
        But_CatmullRom.setLocation(30, 310);
        
        But_xy_view.setLocation(20,20);
        But_zy_view.setLocation(20,65);
        But_xz_view.setLocation(20,110);


        But_construct_tables.setLocation(25,85);
        But_take_previous.setLocation(270,85);
        But_cancel.setLocation(35,HI-110);
        But_preview.setLocation(WI/2-80, HI -110 );
        But_save_tables.setLocation(WI-140, HI -110);
        But_draw.setLocation(WI/2-But_draw.getSize().width/2, HI-120);




        JLabel Lx1 = new JLabel("Введите количество опорных точек: ");
        JLabel Lx2 = new JLabel("Введите степень B-сплайна: ");
        JLabel Lx1_2 = new JLabel("Ведите количество точек кривой Безье: ");
        JLabel Lx2_2 = new JLabel("Введите степень кривой Безье: ");

        Tx1 = new TextField("", 40);
        Tx2 = new TextField("", 40);
        Txbeta1 = new TextField("", 40);
        Txbeta2 = new TextField("", 40);
        Txbeta1.setVisible(false);
        Txbeta2.setVisible(false);
        Txbeta1.setSize(50, 20);
        Txbeta1.setLocation(140, 50);
        Txbeta2.setSize(50, 20);
        Txbeta2.setLocation(340, 50);


        Tx1.setLocation(WI-100, 20);
        Tx1.setSize(50, 20);
        Tx2.setLocation(WI-100, 50);
        Tx2.setSize(50, 20);
        Tx1.setVisible(false);
        Tx2.setVisible(false);

        Lx1.setLocation(34, 20);
        Lx1.setSize(250, 20);
        Lx2.setLocation(34, 40);
        Lx2.setSize(250, 40);
        Lx1_2.setLocation(34, 20);
        Lx1_2.setSize(250, 20);
        Lx2_2.setLocation(34, 40);
        Lx2_2.setSize(250, 40);

        Lx1.setVisible(false);
        Lx2.setVisible(false);
        Lx1_2.setVisible(false);
        Lx2_2.setVisible(false);
        JLabel Lx_points = new JLabel("<html><center>Таблица опрорных точек: </html>");
        JLabel Lx_param_values = new JLabel("<html><center>Вектор<br>параметризации</html>");

        Lx_points.setLocation(50, 130);
        Lx_points.setSize(150, 50);
        Lx_param_values.setLocation(250, 130);
        Lx_param_values.setSize(100, 50);
        Lx_points.setVisible(false);
        Lx_param_values.setVisible(false);

        er = new JLabel("");
        er.setLocation(10, HI - 180);
        er.setSize(450, 60);

        temp_points.setFillsViewportHeight(true);
        content_points = new JScrollPane(temp_points);
        temp_points_2.setFillsViewportHeight(true);
        content_points_2 = new JScrollPane(temp_points_2);
        temp_values.setFillsViewportHeight(true);
        content_values = new JScrollPane(temp_values);
        temp_weight.setFillsViewportHeight(true);
        content_weight = new JScrollPane(temp_weight);
      
        content_points.setMinimumSize(new Dimension(150,100));
        content_points.setSize(200,100);
        content_points.setLocation(20,180);

        content_points_2.setMinimumSize(new Dimension(150,100));
        content_points_2.setSize(200,100);
        content_points_2.setLocation(230,180);

        content_values.setMinimumSize(new Dimension(100,100));
        content_values.setSize(100,100);
        content_values.setLocation(250,180);

        content_weight.setMinimumSize(new Dimension(100,100));
        content_weight.setSize(100,100);
        content_weight.setLocation(250,180);

        content_weight.setVisible(false);
        content_points.setVisible(false);
        content_points_2.setVisible(false);
        content_values.setVisible(false);

        JP.add(But_opened_BS);
        JP.add(But_closed_BS);
        JP.add(But_periodic_BS);
        JP.add(But_Bezier);
        JP.add(But_save_tables);
        JP.add(But_preview);
        JP.add(But_cancel);
        JP.add(But_draw);
        JP.add(But_construct_tables);
        JP.add(But_take_previous);
        JP.add(But_composite_Bezier);
        JP.add(But_weight_Bezier);
        JP.add(But_Hermite_curve);
        JP.add(But_Beta_spline);
        JP.add(But_NURBS);
        JP.add(But_CatmullRom);

        JP0.add(But_xy_view);
        JP0.add(But_xz_view);
        JP0.add(But_zy_view);


        JP.add(Tx1);
        JP.add(Tx2);
        JP.add(Txbeta1);
        JP.add(Txbeta2);


        JP.add(Lx1,0);
        JP.add(Lx2,1);
        JP.add(Lx1_2,2);
        JP.add(Lx2_2,3);
        JP.add(Lx_points,4);
        JP.add(Lx_param_values,5);
        JP.add(er);
        JLabel Lx_info = new JLabel("<html>Beta2</html>");
        Lx_info.setLocation(260, 35);
        Lx_info.setSize(80, 50);
        Lx_info.setVisible(false);
        JP.add(Lx_info,6);

        Lx_info = new JLabel("<html><center>Весовая параметризация</html>");
        Lx_info.setLocation(400, 130);
        Lx_info.setSize(100, 50);
        Lx_info.setVisible(false);
        JP.add(Lx_info,10);


        JP.add(content_points);
        JP.add(content_values);
        JP.add(content_weight);
        JP.add(content_points_2);

        JF.add(JP,0);
        JF.add(JP0,1);
        JP0.setVisible(false);

        JF.setVisible(true);
        JF.setResizable(false);


        But_xy_view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current_view =0;
                JP0.removeAll();
                new Sample_Paint(current_curve, current_type, current_view, JP0);
                JP0.add(But_xy_view);
                JP0.add(But_zy_view);
                JP0.add(But_xz_view);
                JP0.setComponentZOrder(But_xy_view, 0);
                JP0.setComponentZOrder(But_xz_view, 0);
                JP0.setComponentZOrder(But_zy_view, 0);
                JP0.setVisible(true);
                JP0.repaint();
                But_xy_view.setEnabled(false);
                But_xz_view.setEnabled(true);

                But_zy_view.setEnabled(true);
            }
        });

        But_zy_view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               current_view =1;
                JP0.removeAll();
                new Sample_Paint(current_curve, current_type, current_view, JP0);
                JP0.add(But_xy_view);
                JP0.add(But_zy_view);
                JP0.add(But_xz_view);
                JP0.setComponentZOrder(But_xy_view, 0);
                JP0.setComponentZOrder(But_xz_view, 0);
                JP0.setComponentZOrder(But_zy_view, 0);
                JP0.setVisible(true);
                JP0.repaint();
                But_xz_view.setEnabled(true);
                But_xy_view.setEnabled(true);
                But_zy_view.setEnabled(false);

            }
        });
        But_xz_view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current_view =2;
                JP0.removeAll();
                new Sample_Paint(current_curve, current_type, current_view, JP0);
                JP0.add(But_xy_view);
                JP0.add(But_zy_view);
                JP0.add(But_xz_view);
                JP0.setComponentZOrder(But_xy_view, 0);
                JP0.setComponentZOrder(But_xz_view, 0);
                JP0.setComponentZOrder(But_zy_view, 0);
                JP0.setVisible(true);
                JP0.repaint();
                But_xy_view.setEnabled(true);
                But_zy_view.setEnabled(true);
                But_xz_view.setEnabled(false);
            }
        });
        But_construct_tables.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             //   set("<html><font size = '4' color = 'red'>Error</font><br>Диапазон опорных точек [14,20]</html>");
               But_save_tables.setEnabled(false);
               int max_power;
               set("");
                max_power=21;
               try {

                   switch (current_type) {
                       case 9:
                           But_construct_tables.setText("<html><center>Изменить число точек кривой CatmullRom</html>");
                           f = "CatmullRom";
                           Tx2.setText("3");

                           break;
                       case 8: {
                           But_construct_tables.setText("<html><center>Изменить число точек NURBS кривой</html>");
                           f = "NURBS кривой";

                           break;
                       }
                       case 6: {
                           But_construct_tables.setText("<html><center>Изменить число точек кривой Эрмита </html>");
                           f = "кривой Эрмита";
                           Tx2.setText("1");
                           break;
                       }
                       case 7: {
                           But_construct_tables.setText("<html><center>Изменить число точек beta-сплайна </html>");
                           f = "Beta-сплайна";
                           Tx2.setText("3");
                           break;
                       }
                       case 5:
                       case 1:
                       case -1:
                       case 4:
                           But_construct_tables.setText("<html><center>Изменить размеры таблицы и степень Безье</html>");
                           f = "Кривой Безье";
                           break;
                       case 0:
                       case 2:
                       case 3:
                           But_construct_tables.setText("<html><center>Изменить размеры таблицы и степень B-сплайна</html>");
                           f = "B-сплайна";
                           break;
                       default:
                           max_power=0;
                           f = "Not defined";
                           break;
                   }

                   Integer temp_power = Integer.parseInt(Tx2.getText());
                   Integer count_of_points = Integer.parseInt(Tx1.getText());


                       if (!((temp_power < 0) || (temp_power > max_power))) {
                           power = temp_power;


                               if ((temp_power < count_of_points) && (count_of_points <max_power+1)) {
                                   
                                   int table_length = (int)(18.5 * count_of_points) + (20 * (((29 - count_of_points) / 10) - 1));

                                   DefaultTableModel temp_points_model = new DefaultTableModel(new String[][]{}, Points_table_model);

                                   for (int i = 0; i < count_of_points; i++) {
                                       temp_points_model.addRow(new Object[]{i + 1, "", "",""});
                                   }

                                   JP.remove(content_points);
                                   temp_points = new JTable(temp_points_model);
                                   content_points = new JScrollPane(temp_points);
                                   content_points.setLocation(20, 180);
                                   content_points.setSize(200, table_length);
                                   JP.add(content_points);

                                   if(content_points_2.isVisible()){
                                       DefaultTableModel temp_points_model_2 = new DefaultTableModel(new String[][]{}, Points_table_model);
                                       for (int i = 0; i < count_of_points; i++) {
                                           temp_points_model_2.addRow(new Object[]{i + 1, "", "",""});
                                       }
                                       JP.remove(content_points_2);
                                       temp_points_2 = new JTable(temp_points_model_2);
                                       content_points_2 = new JScrollPane(temp_points_2);
                                       content_points_2.setLocation(230, 180);
                                       content_points_2.setSize(200, table_length);
                                       JP.add(content_points_2);
                                   }


                                   if(content_weight.isVisible()){
                                       DefaultTableModel temp_weight_model = new DefaultTableModel(new String[][]{}, Weight_table_model);
                                       for (int i = 0; i < count_of_points; i++) {
                                           temp_weight_model.addRow(new Object[]{i + 1, ""});
                                       }
                                       temp_weight = new JTable(temp_weight_model);
                                       Point temp_point = content_weight.getLocation();
                                       JP.remove(content_weight);
                                       content_weight = new JScrollPane(temp_weight);
                                       JP.add(content_weight);
                                       content_weight.setLocation(temp_point);

                                       content_weight.setSize(100, table_length);
                                   }
                                   if(content_values.isVisible()){
                                       DefaultTableModel temp_values_model = new DefaultTableModel(new String[][]{}, Values_table_model);
                                   for (int i = 0; i < count_of_points + power + 1; i++) {

                                       temp_values_model.addRow(new Object[]{i + 1, ""});
                                   }

                                       temp_values = new JTable(temp_values_model);
                                       JP.remove(content_values);
                                       content_values = new JScrollPane(temp_values);
                                       JP.add(content_values);
                                       content_values.setLocation(250, 180);
                                       if ((count_of_points + power + 1) < 24) {
                                           content_values.setSize(100, (int) (33 +18*(count_of_points + power)));
                                       } else {
                                           content_values.setSize(100, 400);
                                       }
                                   }
                                   JF.remove(JP);

                                   But_save_tables.setVisible(true);
                                   But_preview.setVisible(true);
                                   

                                   JF.add(JP);
                                   JF.repaint();

                               } else {
                                   er.setText("<html><font size = '4' color = 'red'>Error</font><br>Некорректное число точек " + f + ". Диапазон ["+(temp_power+1)+" ," + (max_power+1) + "] </html>");
                               }

                       } else {
                           er.setText("<html><font size = '4' color = 'red'>Error</font><br>Некорректная степень " + f + ". Диапазон [1," + max_power + "] </html>");
                       }

                   } catch(Exception w2){

                     er.setText("<html><font size = '4' color = 'red'>Error</font><br>Некорректная степень или число точек " + f + ". <br>Ожидалось значение типа Integer </html>");
                   }

            }
        });

        But_preview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

               
                set("");
                double[] ux;
                double[] uy;
                double[] uz;
                double[] uu;
                double[] uw;
                switch (current_type) {
                    case 9:
                        But_construct_tables.setText("<html><center>Изменить число точек кривой CatmullRom</html>");
                        f = "CatmullRom";

                        break;
                    case 8:
                        But_construct_tables.setText("<html><center>Изменить число точек NURBS кривой</html>");
                        f = "NURBS кривой";

                        break;
                    case 7:
                        But_construct_tables.setText("<html><center>Изменить число точек beta-сплайна  </html>");
                        f = "Beta-сплайна";
                        break;
                    case 6:
                        But_construct_tables.setText("<html><center>Изменить число точек кривой  Эрмита </html>");
                        f = "кривой Эрмита";
                        break;
                    case 5:
                    case 1:
                    case -1:
                    case 4:
                        But_construct_tables.setText("<html><center>Изменить размеры таблицы и степень кривой Безье</html>");
                        f = "кривой Безье";
                        break;
                    case 0:
                       
                    case 2:
                    case 3:
                        f = "B-сплайна";
                        But_construct_tables.setText("<html><center>Изменить размеры таблицы и степень B-сплайна</html>");
                        break;
                    default:
                        f = "Not defined";
                        break;
                }

               try {
                    TableModel read_points_table = temp_points.getModel();

                    int points_number = read_points_table.getRowCount();

                    if(content_points_2.isVisible()){
                        TableModel read_points_table_2 = temp_points_2.getModel();
                        ux = new double[points_number*2];
                        uy = new double[points_number*2];
                        uz = new double[points_number*2];
                        for (int i = 0; i < points_number; i++) {
                            ux[i] = Double.parseDouble((String) read_points_table.getValueAt(i, 1));
                            uy[i] = Double.parseDouble((String) read_points_table.getValueAt(i, 2));
                            uz[i] = Double.parseDouble((String) read_points_table.getValueAt(i, 3));
                            ux[i+points_number] = Double.parseDouble((String) read_points_table_2.getValueAt(i, 1));
                            uy[i+points_number] = Double.parseDouble((String) read_points_table_2.getValueAt(i, 2));
                            uz[i+points_number] = Double.parseDouble((String) read_points_table_2.getValueAt(i, 3));
                        }

                    }else {
                        if (current_type == 3) {
                            ux = new double[points_number+power];
                            uy = new double[points_number+power];
                            uz = new double[points_number+power];
                            for (int i = 0; i < points_number; i++) {
                                ux[i] = Double.parseDouble((String) read_points_table.getValueAt(i, 1));
                                uy[i] = Double.parseDouble((String) read_points_table.getValueAt(i, 2));
                                uz[i] = Double.parseDouble((String) read_points_table.getValueAt(i, 3));
                            }
                            for (int j=0, i = points_number; i < points_number+power; i++ , j++) {
                                ux[i] = Double.parseDouble((String) read_points_table.getValueAt(j, 1));
                                uy[i] = Double.parseDouble((String) read_points_table.getValueAt(j, 2));
                                uz[i] = Double.parseDouble((String) read_points_table.getValueAt(j, 3));
                            }
                        } else {
                            ux = new double[points_number];
                            uy = new double[points_number];
                            uz = new double[points_number];
                            for (int i = 0; i < points_number; i++) {
                                ux[i] = Double.parseDouble((String) read_points_table.getValueAt(i, 1));
                                uy[i] = Double.parseDouble((String) read_points_table.getValueAt(i, 2));
                                uz[i] = Double.parseDouble((String) read_points_table.getValueAt(i, 3));
                            }
                        }
                    }



                    Boolean isCorrectParametricVector = true;


                    if(content_values.isVisible()) {
                        uu = new double[points_number + power + 1];
                        TableModel read_values_table = temp_values.getModel();
                        for (int i = 0; i < points_number + power + 1; i++) {
                            uu[i] = Double.parseDouble((String) read_values_table.getValueAt(i, 1));
                        }

                    }
                    else {
                        if(current_type==3){
                            uu = new double[points_number + power+power + 1];
                            for (int i = 0; i < points_number + power + power + 1; i++) {
                                uu[i] = (double) i;
                            }


                        }else {
                            uu = new double[points_number + power + 1];
                            for (int i = 0; i < points_number + power + 1; i++) {
                                uu[i] = (double) i;
                            }
                        }
                    }

                    uw = new double[points_number];

                    if(content_weight.isVisible()) {

                        TableModel read_values_table = temp_weight.getModel();
                        for (int i = 0; i < points_number ; i++) {
                            uw[i] = Double.parseDouble((String) read_values_table.getValueAt(i, 1));
                        }
                    }else{

                        TableModel read_values_table = temp_weight.getModel();
                        for (int i = 0; i < points_number ; i++) {
                            uw[i] = 0;
                        }

                    }

                    if(isCorrectParametricVector) {
                        JF.setSize(WIwithJP0+WI,HI);

                        current_curve = new curve_calculation(power,ux, uy,uz,uw, uu);

                        if(Txbeta1.isVisible()){
                            current_curve.beta1 = Double.parseDouble(Txbeta1.getText());
                            current_curve.beta2 = Double.parseDouble(Txbeta2.getText());

                        }
                        else {
                            current_curve.beta1=0.1;
                            current_curve.beta2=0;
                        }
                        JP0.removeAll();

                        new Sample_Paint(current_curve, current_type, 0, JP0);


                        JP0.add(But_xy_view);
                        JP0.add(But_zy_view);
                        JP0.add(But_xz_view);
                        JP0.setComponentZOrder(But_xy_view, 0);
                        JP0.setComponentZOrder(But_xz_view, 0);
                        JP0.setComponentZOrder(But_zy_view, 0);
                        JP0.setVisible(true);
                        JP0.repaint();

                        But_save_tables.setEnabled(true);
                    }

                } catch (Exception w) {

                    set("<html><font size = '4' color = 'red'>Error</font><br>Некорректное число. Ожидалось значение типа Integer</html>");
               }
            }
        });
        But_save_tables.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                if(current_type==6){
                    isHermitreWasPrevious=true;
                }
                else {
                    isHermitreWasPrevious=false;
                }
                current_number++;
                curves.add(current_curve);
                vision(true, (byte)-1);
                JF.setTitle("Построение кривых");
                But_construct_tables.setText("<html><center>Подготовить таблицы для ввода значений</html>");
                JP0.setVisible(false);
                But_draw.setVisible(true);
                JF.setSize(WI,HI);

            }
        });
        But_periodic_BS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                current_type =0;
                JF.setTitle("Периодический B-сплайн");
                vision(false, (byte)0);
                JP.getComponent(5).setVisible(false);
                content_values.setVisible(false);


            }
        });
        But_composite_Bezier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                current_type =4;
                JF.setTitle("Составная кривая Безье");
                vision(false, (byte)1);
                JP.getComponent(5).setVisible(false);



            }
        });
        But_CatmullRom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                current_type =9;
                JF.setTitle("Кривая CatmullRom");
                vision(false, (byte)5);

            }
        });
        But_NURBS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                current_type =8;
                JF.setTitle("NURBS кривая ");
                vision(false, (byte)4);

            }
        });
        But_weight_Bezier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                current_type =5;
                JF.setTitle("Рациональная кривая Безье");
                vision(false, (byte)1);

            }
        });
        But_Hermite_curve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                current_type =6;
                JF.setTitle("Кривая Эрмита");
                vision(false, (byte)2);
                JP.getComponent(5).setVisible(true);



            }
        });
        But_Beta_spline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                current_type =7;
                JF.setTitle("Beta-сплайн");
                vision(false, (byte)3);
                JP.getComponent(5).setVisible(false);



            }
        });
        But_Bezier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                JF.setTitle("Кривая Безье");
                current_type =1;
                vision(false, (byte)1);
                JP.getComponent(5).setVisible(false);



            }
        });
        But_opened_BS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                JF.setTitle("Открытый B-сплайн");
                current_type =2;
                vision(false, (byte)0);

            }
        });
        But_closed_BS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                JF.setTitle("Закрытый B-сплайн");
                current_type =3;

               vision(false, (byte)0);
                JP.getComponent(5).setVisible(false);
                content_values.setVisible(false);

            }
        });
        But_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                vision(true, (byte)-1);
                JF.setTitle("Построение кривых");
                But_construct_tables.setText("<html><center>Подготовить таблицы для ввода значений</html>");
                JP0.setVisible(false);
                JF.setSize(WI,HI);
                if(current_number!=0){
                        But_draw.setVisible(true);
                }
             }
        });
        But_draw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                set("");
                vision(true, (byte)-1);
                JP0.setVisible(false);
                JF.setSize(WI,HI);
                F = new JFrame();
                Screen ScreenObject = new Screen();
                try {
                    Robot r = new Robot();
                    r.mouseMove((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
                } catch (AWTException exp) {
                    exp.printStackTrace();
                }
                F.add(ScreenObject);
                F.setUndecorated(true);
                F.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                F.setVisible(true);
                But_draw.setVisible(true);
               

            }
        });
        But_take_previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                But_save_tables.setEnabled(false);
                double[] temp_x_cords;
                double[] temp_y_cords;
                double[] temp_z_cords;
                if (isHermitreWasPrevious) {
                    temp_x_cords = Arrays.copyOfRange(curves.get(current_number - 1).x, 0, curves.get(current_number - 1).x.length / 2);
                    temp_y_cords = Arrays.copyOfRange(curves.get(current_number - 1).y, 0, curves.get(current_number - 1).x.length / 2);
                    temp_z_cords = Arrays.copyOfRange(curves.get(current_number - 1).z, 0, curves.get(current_number - 1).x.length / 2);
                } else {
                    temp_x_cords = curves.get(current_number - 1).x;
                    temp_y_cords = curves.get(current_number - 1).y;
                    temp_z_cords = curves.get(current_number - 1).z;
                }

                set("");
                Integer count_of_points = temp_x_cords.length;
                Integer power = 0;
                try {
                    try {

                        switch (current_type) {
                            case 9 : {
                                But_construct_tables.setText("<html><center>Изменить число точек кривой CatmullRom</html>");
                                f = "CatmullRom";
                                if (4 > count_of_points) {
                                    But_save_tables.setEnabled(true);
                                    throw new NullPointerException(" Количество точек не подходит для " + f);
                                }
                            }
                            case 8 : {
                                But_construct_tables.setText("<html><center>Изменить число точек NURBS кривой</html>");
                                f = "NURBS кривой";
                                power = Integer.parseInt(Tx2.getText());
                                if (power >= count_of_points) {

                                    throw new NullPointerException(" Количество точек не подходит для текущей степени " + f);
                                }
                            }
                            case 7 : {
                                But_construct_tables.setText("<html><center>Изменить число точек beta-сплайна  </html>");
                                f = "Beta-сплайна";
                                if (4 > count_of_points) {
                                    But_save_tables.setEnabled(true);
                                    throw new NullPointerException(" Количество точек не подходит для " + f);
                                }
                            }
                            case 6: {
                                But_construct_tables.setText("<html><center>Изменить число точек кривой  Эрмита </html>");
                                f = "кривой Эрмита";
                                if (2 > count_of_points) {
                                    But_save_tables.setEnabled(true);
                                    throw new NullPointerException(" Количество точек не подходит для " + f);
                                }
                            }
                            case 5:
                            case 1:
                            case -1:
                            case 4: {
                                But_construct_tables.setText("<html><center>Изменить размеры таблицы и степень кривой Безье</html>");
                                f = "кривой Безье";
                                power = Integer.parseInt(Tx2.getText());
                                if (power >= count_of_points) {

                                    throw new NullPointerException(" Количество точек не подходит для текущей степени " + f);
                                }
                            }
                            case 0:
                            case 2:
                            case 3: {
                                f = "B-сплайна";
                                But_construct_tables.setText("<html><center>Изменить размеры таблицы и степень B-сплайна</html>");
                                power = Integer.parseInt(Tx2.getText());
                                if (power >= count_of_points) {
                                    throw new NullPointerException(" Количество точек не подходит для текущей степени " + f);

                                }
                            }
                            default:
                                f = "Not defined";
                        }

                        Tx1.setText(count_of_points.toString());
                        JF.remove(JP);

                        DefaultTableModel temp_weight_model = new DefaultTableModel(new String[][]{}, Weight_table_model);
                        DefaultTableModel temp_values_model = new DefaultTableModel(new String[][]{}, Values_table_model);
                        DefaultTableModel temp_points_model = new DefaultTableModel(new String[][]{}, Points_table_model);
                        DefaultTableModel temp_points_model_2 = new DefaultTableModel(new String[][]{}, Points_table_model);


                        for (int i = 0; i < count_of_points; i++) {
                            temp_points_model.addRow(new Object[]{i + 1, String.valueOf(temp_x_cords[i]), String.valueOf(temp_y_cords[i]), String.valueOf(temp_z_cords[i])});
                        }


                        temp_points = new JTable(temp_points_model);
                        JP.remove(content_points);
                        content_points = new JScrollPane(temp_points);
                        JP.add(content_points);
                        content_points.setLocation(20, 180);
                        content_values.setLocation(250, 180);
                        content_points.setSize(200, (int) ((18.5 * count_of_points) + (20 * (((29 - count_of_points) / 10) - 1))));
                        content_points.setLocation(20, 180);


                        if (content_values.isVisible()) {
                            if (curves.get(current_number - 1).parametrize.length == 0) {
                                for (int i = 0; i < count_of_points + power + 1; i++) {
                                    temp_values_model.addRow(new Object[]{i + 1, ""});
                                }
                            } else {
                                double[] temp_t_params = curves.get(current_number - 1).parametrize;
                                for (int i = 0; i < count_of_points + power + 1; i++) {
                                    temp_values_model.addRow(new Object[]{i + 1, String.valueOf(temp_t_params[i])});
                                }
                            }


                            temp_values = new JTable(temp_values_model);
                            JP.remove(content_values);
                            content_values = new JScrollPane(temp_values);
                            JP.add(content_values);
                            content_values.setLocation(250, 180);
                            if ((count_of_points + power + 1) < 24) {
                                content_values.setSize(100, (int) (20 * (count_of_points + power)) + (35 * (((29 - count_of_points - power) / 10) - 1)));
                            } else {
                                content_values.setSize(100, 400);
                            }
                        }

                        if (content_weight.isVisible()) {
                            if (curves.get(current_number - 1).weight.length == 0) {
                                for (int i = 0; i < count_of_points; i++) {
                                    temp_weight_model.addRow(new Object[]{i + 1, ""});
                                }
                            } else {
                                double[] temp_t_weight = curves.get(current_number - 1).weight;
                                for (int i = 0; i < count_of_points; i++) {
                                    temp_weight_model.addRow(new Object[]{i + 1, String.valueOf(temp_t_weight[i])});
                                }
                            }
                            Point temp_point = content_weight.getLocation();
                            JP.remove(content_weight);
                            temp_weight = new JTable(temp_weight_model);
                            content_weight = new JScrollPane(temp_weight);
                            JP.add(content_weight);
                            content_weight.setLocation(temp_point);
                            if ((count_of_points + power + 1) < 24) {
                                content_weight.setSize(100, (int) (20 * (count_of_points)) + (35 * (((29 - count_of_points) / 10) - 1)));
                            } else {
                                content_weight.setSize(100, 400);
                            }
                        }

                        temp_weight = new JTable(temp_weight_model);


                        if(isHermitreWasPrevious&&content_points_2.isVisible()){

                            double[] temp_x_cords_2 = Arrays.copyOfRange(curves.get(current_number - 1).x, curves.get(current_number - 1).x.length / 2, curves.get(current_number - 1).x.length);
                            double[] temp_y_cords_2 = Arrays.copyOfRange(curves.get(current_number - 1).y, curves.get(current_number - 1).x.length / 2, curves.get(current_number - 1).x.length);
                            double[] temp_z_cords_2 = Arrays.copyOfRange(curves.get(current_number - 1).z, curves.get(current_number - 1).x.length / 2, curves.get(current_number - 1).x.length);
                                for (int i = 0; i < count_of_points; i++) {
                                    temp_points_model_2.addRow(new Object[]{i + 1, String.valueOf(temp_x_cords_2[i]), String.valueOf(temp_y_cords_2[i]), String.valueOf(temp_z_cords_2[i])});
                                }


                        }else {
                            for (int i = 0; i < count_of_points; i++) {
                                temp_points_model_2.addRow(new Object[]{i + 1, String.valueOf(0), String.valueOf(0), String.valueOf(0)});
                            }
                        }

                        Point temp_point = content_points_2.getLocation();
                        JP.remove(content_points_2);
                        temp_points_2 = new JTable(temp_points_model_2);
                        content_points_2 = new JScrollPane(temp_points_2);
                        JP.add(content_points_2);
                        content_points_2.setLocation(temp_point);
                        if ((count_of_points + power + 1) < 24) {
                            content_points_2.setSize(200, (int) (20 * (count_of_points)) + (35 * (((29 - count_of_points) / 10) - 1)));
                        } else {
                            content_points_2.setSize(200, 400);
                        }

                        if (Txbeta1.isVisible()) {
                            Txbeta1.setText(String.valueOf(curves.get(current_number - 1).beta1));
                            Txbeta2.setText(String.valueOf(curves.get(current_number - 1).beta2));

                        }

                        But_save_tables.setVisible(true);
                        But_preview.setVisible(true);
                        JF.add(JP);
                        JF.repaint();
                    } catch (NullPointerException a) {
                        er.setText("<html><font size = '4' color = 'red'>Error</font><br>" + a.getMessage());

                    }
                } catch (Exception w2) {

                    er.setText("<html><font size = '4' color = 'red'>Error</font><br>Некорректная степень" + f + ". <br>Ожидалось значение типа Integer </html>");
                }

            }


        });
    }

    public void set(String s) {
        er.setText(s);
    }
    public void vision(Boolean b, byte a){

        //----------------------виды--------------------------

        But_xy_view.setEnabled(false);
        But_zy_view.setEnabled(true);
        But_xz_view.setEnabled(true);


        JLabel Lx_info;
        Lx_info = new JLabel("<html><center>Вектор<br>параметризации</html>");
        JP.remove(5);
        JP.add(Lx_info,5);
       JP.getComponent(10).setVisible(false);

        //--------отчистка старой информации-------------

        if(!(temp_points.getModel().getRowCount()==0)){
            temp_points = new JTable(new String[][]{}, Points_table_model);
            JP.remove(content_points);
            content_points = new JScrollPane(temp_points);
            JP.add(content_points);
            content_points.setSize(200,100);
            content_points.setLocation(20,180);

            if(content_values.isVisible()){
                temp_values = new JTable(new String[][]{}, Values_table_model);
                JP.remove(content_values);
                content_values = new JScrollPane(temp_values);
                JP.add(content_values);
                content_values.setSize(100,100);
                content_values.setLocation(250,180);
            }
            if(content_weight.isVisible()){
                temp_weight = new JTable(new String[][]{}, Weight_table_model);
                JP.remove(content_weight);
                content_weight = new JScrollPane(temp_weight);
                JP.add(content_weight);
                content_values.setSize(100,100);
                content_values.setLocation(250,180);

            }
            if(content_points_2.isVisible()){
                temp_points_2 = new JTable(new String[][]{}, Points_table_model);
                JP.remove(content_points_2);
                content_points_2 = new JScrollPane(temp_points_2);
                JP.add(content_points_2);
                content_points_2.setSize(200,100);
                content_points_2.setLocation(230,180);

            }
        }

        //----------------------------------таблицы-----------------------------------------
        content_points.setVisible(!b);
        content_values.setVisible(false);
        content_weight.setVisible(false);
        content_points_2.setVisible(false);





        switch (a){
            case 0:{
                Lx_info = new JLabel("<html>Введите число опорных точек B-сплайна </html>");
                JP.remove(0);
                JP.add(Lx_info,0);
                JP.getComponent(0).setVisible(!b);
                JP.getComponent(0).setLocation(34, 20);
                JP.getComponent(0).setSize(300, 20);
                Lx_info = new JLabel("<html>Введите степень B-сплайна </html>");
                JP.remove(1);
                JP.add(Lx_info,1);
                JP.getComponent(1).setVisible(!b);
                JP.getComponent(1).setLocation(34, 40);
                JP.getComponent(1).setSize(300, 40);
                Tx1.setVisible(!b);
                Tx2.setVisible(!b);
                content_values.setVisible(!b);
                break;
            }
            case 1:{
                if(current_type==5){
                    Lx_info = new JLabel("<html><center>Весовая<br>параметризация</html>");
                    JP.remove(5);
                    JP.add(Lx_info,5);
                    content_weight.setVisible(!b);
                }
                Lx_info = new JLabel("<html>Введите число опорных точек Безье </html>");
                JP.remove(0);
                JP.add(Lx_info,0);
                JP.getComponent(0).setVisible(!b);
                JP.getComponent(0).setLocation(34, 20);
                JP.getComponent(0).setSize(300, 20);
                Lx_info = new JLabel("<html>Введите степень кривой Безье </html>");
                JP.remove(1);
                JP.add(Lx_info,1);
                JP.getComponent(1).setVisible(!b);
                JP.getComponent(1).setLocation(34, 40);
                JP.getComponent(1).setSize(300, 40);
                Tx1.setVisible(!b);
                Tx2.setVisible(!b);
                break;

            }
            case 2: {
                Lx_info = new JLabel("<html><center>Значения касательных</html>");
                JP.remove(5);
                JP.add(Lx_info,5);
                content_points_2.setVisible(!b);
                JP.remove(0);
                Lx_info = new JLabel("<html>Введите число опорных точек кривой Эрмита </html>");
                JP.add(Lx_info,0);
                JP.getComponent(0).setVisible(!b);
                JP.getComponent(0).setLocation(34, 20);
                JP.getComponent(0).setSize(300, 20);
                JP.getComponent(1).setVisible(b);
                Tx1.setVisible(!b);
                Tx2.setVisible(b);
                break;
            }
            case 3:{
                Lx_info = new JLabel("<html>Введите число опорных точек Beta-кривой </html>");
                JP.remove(0);
                JP.add(Lx_info,0);
                JP.getComponent(0).setVisible(!b);
                JP.getComponent(0).setLocation(34, 20);
                JP.getComponent(0).setSize(300, 20);
                JP.getComponent(1).setVisible(b);
                Tx1.setVisible(!b);
                Tx2.setVisible(b);
                Txbeta1.setVisible(!b);
                Txbeta2.setVisible(!b);
                Lx_info = new JLabel("<html>Beta1</html>");
                JP.remove(1);
                JP.add(Lx_info,1);
                JP.getComponent(1).setVisible(!b);
                JP.getComponent(1).setLocation(44, 35);
                JP.getComponent(1).setSize(80, 50);
                JP.getComponent(6).setVisible(!b);
                    break;

            }
            case 4:{
                Lx_info = new JLabel("<html>Введите число опорных точек NURBS </html>");
                JP.remove(0);
                JP.add(Lx_info,0);
                JP.getComponent(0).setVisible(!b);
                JP.getComponent(0).setLocation(34, 20);
                JP.getComponent(0).setSize(300, 20);
                Lx_info = new JLabel("<html>Введите степень NURBS кривой </html>");
                JP.remove(1);
                JP.add(Lx_info,1);
                JP.getComponent(1).setVisible(!b);
                JP.getComponent(1).setLocation(34, 40);
                JP.getComponent(1).setSize(300, 40);
                Tx1.setVisible(!b);
                Tx2.setVisible(!b);
                content_values.setVisible(!b);
                content_weight.setVisible(!b);
                content_weight.setLocation(400,180);
                JP.getComponent(10).setVisible(true);
                content_weight.setVisible(!b);
                break;

            }
            case 5:{
                Lx_info = new JLabel("<html>Введите число опорных точек CatmullRom кривой </html>");
                JP.remove(0);
                JP.add(Lx_info,0);
                JP.getComponent(0).setVisible(!b);
                JP.getComponent(0).setLocation(34, 20);
                JP.getComponent(0).setSize(400, 20);
                Tx1.setVisible(!b);

                break;

            }
            default:
                JP.getComponent(0).setVisible(!b);
                JP.getComponent(1).setVisible(!b);
                JP.getComponent(6).setVisible(!b);
                Tx1.setVisible(!b);
                Tx2.setVisible(!b);
                Txbeta1.setVisible(!b);
                Txbeta2.setVisible(!b);
                break;
        }

 //------------------------кнопки осей-----------------

        current_view=0;
        But_xz_view.setEnabled(true);
        But_xy_view.setEnabled(false);
        But_zy_view.setEnabled(true);
       
       //---------------кнопки-----------------
        But_periodic_BS.setVisible(b);
        But_Beta_spline.setVisible(b);
        But_Hermite_curve.setVisible(b);
        But_composite_Bezier.setVisible(b);
        But_weight_Bezier.setVisible(b);
        But_opened_BS.setVisible(b);
        But_closed_BS.setVisible(b);
        But_Bezier.setVisible(b);
        But_NURBS.setVisible(b);
        But_CatmullRom.setVisible(b);
        But_draw.setVisible(false);
        if(b) {

            But_save_tables.setVisible(!b);
        }
        But_construct_tables.setVisible(!b);

        if(current_number==0){
            But_take_previous.setEnabled(false);
        }
        else {
            But_take_previous.setEnabled(true);
        }

        But_construct_tables.setVisible(!b);
        But_take_previous.setVisible(!b);
        But_cancel.setVisible(!b);
        But_preview.setVisible(!b);
        But_save_tables.setVisible(!b);
        But_save_tables.setEnabled(false);


        //---------------область для предварительной отрисовки-----------------
        JP0.setVisible(false);
        int WI = 550;
        int HI;
        if(Toolkit.getDefaultToolkit().getScreenSize().height>800){
            HI = 800;
        }else{
            HI =Toolkit.getDefaultToolkit().getScreenSize().height;
        }
        JF.setSize(WI,HI);

        //---------------подписи-----------------
        JP.getComponent(4).setVisible(!b);
        JP.getComponent(5).setVisible(!b);
        if(current_type==6){
            JP.getComponent(5).setLocation(280, 130);
        }
        else {
            JP.getComponent(5).setLocation(250, 130);
        }
        JP.getComponent(5).setSize(100, 50);
        if(!(current_number==0)) {
            Lx_info = new JLabel("<html><center>Текущее значение количества кривых готовых к отрисовке = " + current_number + "</html>");
            Lx_info.setLocation(WI/2-175, HI-200);
            Lx_info.setSize(350, 50);
            JP.remove(6);
            JP.add(Lx_info, 6);
            JP.getComponent(6).setVisible(b);
        }
        
        //--------------------------------таблицы-------------------------

        
        Tx1.setText("");
        Tx2.setText("");

        //end of cleaner
       JF.repaint();
    }
    public static void main(String args[]) {
        new My_in();
    }
}