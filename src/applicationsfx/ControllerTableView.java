package applicationsfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import static applicationsfx.Controller.Fx1;


public class ControllerTableView implements Initializable {


    private static final Logger logger = Logger.getLogger(ControllerTableView.class.getSimpleName());


    public static ExplorerFx Fx2;
    public ObservableList<FileInfo> list;
    String coppyTxtRead = null;
    File source = null;
    File x = null;
    File xurl = null;
    File dest = null;
    String a;
    String a1;
    String b;

    @FXML
    private TableView<FileInfo> tableview;
    @FXML
    private TableColumn<FileInfo, ImageView> image;
    @FXML
    private TableColumn<FileInfo, String> date;
    @FXML
    private TableColumn<FileInfo, String> name;
    @FXML
    private TableColumn<FileInfo, String> size;

    private Desktop desktop;


    public ControllerTableView() throws IOException {
    }


    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    private static String getFileExtension(String mystr) {
        int index = mystr.indexOf('.');
        return index == -1 ? null : mystr.substring(index);
    }

    @Override
        public void initialize(URL location, ResourceBundle resources) {


        Fx2 = new ClassTableView();
        Fx2.setValues(tableview, image, date, name, size);
        if (Fx2.CurrDirFile == null) {
            Fx2.CurrDirFile = new File("./");
            Fx2.CurrDirStr = Fx2.CurrDirFile.getAbsolutePath();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        File[] fl;
        ObservableList<FileInfo> list;
        if (Fx2.CurrDirFile == null) {

            Fx2.CurrDirFile = new File("./");

        }


        fl = Fx2.CurrDirFile.listFiles();//мб краш


        FileInfo st[] = new FileInfo[fl.length];
        for (int i = 0; i < fl.length; i++) {
            String s1 = null;
            String s2 = null;
            String s3 = null;
            ImageView img = null;
            try {
                if (Fx2.IsDrive(fl[i])) {
                    img = new ImageView(Fx2.getIconImageFX(fl[i]));
                    s1 = fl[i].getAbsolutePath();
                } else {
                    img = new ImageView(Fx2.getIconImageFX(fl[i]));
                    s1 = fl[i].getName();
                }
                s2 = Fx2.calculateSize(fl[i]);
                s3 = sdf.format(fl[i].lastModified());
            } catch (Exception x) {
                logger.info("Проходим по: " + x.getMessage());
            }
            st[i] = new FileInfo(img, s1, s2, s3);
        }

        list = FXCollections.observableArrayList(st);

        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableview.setItems(list);


    }

    @FXML
    private void handleTableMouseClicked(MouseEvent mouseEvent) {

        if (mouseEvent.getClickCount() == 2) {

            String str = tableview.getSelectionModel().getSelectedItem().getName();

            String s = Fx2.CurrDirStr + "\\" + str;
            System.out.println(s);
            File file = new File(s);
            if (file.isDirectory()) {
                try {
                    Fx2.CurrDirFile = file;
                    Fx2.CurrDirStr = Fx2.CurrDirFile.getAbsolutePath();
                    Fx2.setLabelTxt();
                    tableview.getItems().clear();
                    Fx2.CreateTableView();
                } catch (Exception x) {
                    System.out.println(x.getMessage());
                }
            } else if (file.isFile()) {
                desktop = Desktop.getDesktop();
                try {
                    desktop.open(file);
                } catch (IOException x) {
                    System.out.println(x.getMessage());
                }
            }
        }
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent) throws IOException {
        String str = null;
        String s;
        try {
            str = tableview.getSelectionModel().getSelectedItem().getName();
            s = Fx2.CurrDirStr + "\\" + str;
        } catch (Exception e) {
            logger.info(e);

            s = Fx1.CurrDirStr + "\\" + str;
        }


        if(keyEvent.getCode() == KeyCode.F5){ //формачка для поиска по регуляркам

            Stage newWindow = new Stage();
            Button button = new Button("Поиск");
            javafx.scene.control.TextField textArea = new javafx.scene.control.TextField();
            Label secondLabel = new Label("Найти по Регулярному выражению");
            FlowPane root = new FlowPane();
            root.setPadding(new Insets(10));
            root.setHgap(10);
            root.setVgap(10);

            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    String put = textArea.getText();

                    String s;
                    try {
                        s = Fx1.CurrDirStr + "\\";
                    } catch (Exception e) {
                        s = Fx2.CurrDirStr + "\\";
                    }


                    logger.info("Поиск :" + s);


                    Fx2.setLabelTxt();
                    tableview.getItems().clear();
                    Fx2.CreateTableView();

                    newWindow.close();
                }
            });


            root.getChildren().addAll(secondLabel, textArea, button);
            Scene secondScene = new Scene(root, 230, 100);

            newWindow.setTitle("Поиск");
            newWindow.setScene(secondScene);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.show();


        }


            if (keyEvent.getCode() == KeyCode.DELETE){
                javafx.scene.control.TextField textArea = new javafx.scene.control.TextField();
                 str = tableview.getSelectionModel().getSelectedItem().getName();

               // String pakName = textArea.getText();



                    s = Fx1.CurrDirStr + "\\" + str;

                    //s = Fx2.CurrDirStr + "\\" + pakName;
                try {
                    //FileUtils.deleteDirectory(new File(s));//пидорское удаление папок в одну строку
                    logger.info("Удаление папки:" + s);
                    System.out.println("Удаление папки:" + s);
                }catch (Exception e) {

                    File file = new File(s);
                    file.delete();
                    logger.info("Удаление файла:" + s);
                    System.out.println("Удаление файла:" + s);
                }


                Fx2.setLabelTxt();
                tableview.getItems().clear();
                Fx2.CreateTableView();
                s=null;
            }



        if (keyEvent.getCode() == KeyCode.F12) {


            Stage newWindow = new Stage();
            Button button = new Button("Cоздать");
            javafx.scene.control.TextField textArea = new javafx.scene.control.TextField();
            Label secondLabel = new Label("Создать папку?");
            FlowPane root = new FlowPane();
            root.setPadding(new Insets(10));
            root.setHgap(10);
            root.setVgap(10);

            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    String pakName = textArea.getText();

                    String s;
                    try {
                        s = Fx1.CurrDirStr + "\\" + pakName;
                    } catch (Exception e) {
                        s = Fx2.CurrDirStr + "\\" + pakName;
                    }

                    File f = new File(s);
                    try {
                        if (f.mkdir()) {
                            logger.info("Дериктория создана");
                        } else {
                            logger.info("Дерик");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    logger.info("Cоздание папки по:" + s);
                    Fx2.setLabelTxt();
                    tableview.getItems().clear();
                    Fx2.CreateTableView();

                    newWindow.close();
                }
            });


            root.getChildren().addAll(secondLabel, textArea, button);
            Scene secondScene = new Scene(root, 230, 100);

            newWindow.setTitle("Добавление папки");
            newWindow.setScene(secondScene);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.show();

        }



        if (keyEvent.getCode() == KeyCode.F1) {//чистка HTML

            Stage newWindow = new Stage();
            Button button = new Button("Да");
            Button button1 = new Button("Нет");
            Label secondLabel = new Label("Вы уверены?");

            FlowPane root = new FlowPane();

            root.setHgap(10);
            root.setVgap(10);

            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    String str = tableview.getSelectionModel().getSelectedItem().getName();
                    String s = Fx2.CurrDirStr + "\\" + str;
                    logger.info("Чистим Html" + s);

                    String html = "";
                    try {
                        FileReader fis = new FileReader(s);
                        int i;
                        if (fis.read() != 0) {
                            while ((i = fis.read()) != -1) {
                                html += (char) i;
                            }
                        }
                       // html = Jsoup.parse(html).text();
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        logger.info("краш");
                    }
                    try {
                        FileWriter save = new FileWriter(s);
                        save.write(html);
                        save.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    newWindow.close();
                }
            });


            button1.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    newWindow.close();
                }
            });

            root.getChildren().addAll(secondLabel, button, button1);
            Scene secondScene = new Scene(root, 400, 500);

            newWindow.setTitle("Second Stage");
            newWindow.setScene(secondScene);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.show();

        }

        if (keyEvent.getCode() == KeyCode.F2) { //подсчет

            logger.info("Считаем слова" + s); //наш юрл

            String stringlist = "";
            try {
                FileReader list = new FileReader(s);
                int i;
                if (list.read() != 0) {
                    while ((i = list.read()) != -1) {
                        stringlist += (char) i;
                    }
                }
                try {
                    list.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                logger.info("краш при подсчете");
            }
            stringlist = stringlist.replaceAll("\\n", "");
            stringlist = stringlist.replaceAll("\\t", "");
            stringlist = stringlist.replaceAll("\\r", "");
            stringlist = stringlist.replaceAll("&","");
            stringlist = stringlist.replaceAll("\\.","");
            stringlist = stringlist.replaceAll(",","");
            stringlist = stringlist.replaceAll("\\?","");
            stringlist = stringlist.replaceAll("!","");
            stringlist = stringlist.replaceAll("%","");
            stringlist = stringlist.replaceAll("\\$","");
            stringlist = stringlist.replaceAll(":","");
            stringlist = stringlist.replaceAll(";","");
            stringlist = stringlist.replaceAll("\\\\","");
            stringlist = stringlist.replaceAll("\\)","");
            stringlist = stringlist.replaceAll("\\(","");
            //stringlist = stringlist.replaceAll("[^(\\w)]","");
            System.out.println(stringlist);


            List<String> paraList = new ArrayList<String>();
            paraList = Arrays.asList(stringlist.split(" "));
            
            System.out.println(paraList);
            int size = paraList.size();

            int i = 0;
            Map<String, Integer> duplicatCountMap = new HashMap<String, Integer>();
            for (int j = 0; size > j; j++) {
                int count = 0;
                for (i = 0; size > i; i++) {
                    if (paraList.get(j).equals(paraList.get(i))) {
                        count++;
                        duplicatCountMap.put(paraList.get(j), count);
                    }
                }
            }
            String a = duplicatCountMap.toString();
            for (Map.Entry entry : duplicatCountMap.entrySet()) {
                System.out.println(entry.getKey() + ", " + entry.getValue());
            }

            try {
                FileWriter save = new FileWriter("C:\\log\\log.txt");
                save.write("new: " + a);
                save.close();
                File file = new File("C:\\log\\log.txt");
                desktop = Desktop.getDesktop();
                try {
                    desktop.open(file);
                } catch (IOException x) {
                    logger.info("краш");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (keyEvent.getCode() == KeyCode.F3) {//Копирование с изменением
            b = str;
            String stringlist = "";
            try {
                FileReader list = new FileReader(s);
                int i;
                if (list.read() != 0) {
                    while ((i = list.read()) != -1) {
                        stringlist += (char) i;
                    }
                }
                try {
                    list.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                logger.info("краш");
            }

            List<String> paraList = new ArrayList<String>();

            stringlist = stringlist.replaceAll("\\n", "");
            stringlist = stringlist.replaceAll("\\t", "");
            stringlist = stringlist.replaceAll("\\r", "");

            paraList = Arrays.asList(stringlist.split("\\n"));
            int size = paraList.size();

            int i = 0;
            Map<String, Integer> duplicatCountMap = new HashMap<String, Integer>();
            for (int j = 0; size > j; j++) {
                int count = 0;
                for (i = 0; size > i; i++) {
                    if (paraList.get(j).equals(paraList.get(i))) {
                        count++;
                        duplicatCountMap.put((paraList.get(j)), count);
                    }
                }
            }

            coppyTxtRead = duplicatCountMap.toString();
            coppyTxtRead = stringlist.replaceAll("new:","");
            coppyTxtRead = stringlist.replaceAll("\\{","");
            coppyTxtRead = stringlist.replaceAll("}","\\\n");
            System.out.println(coppyTxtRead);
            logger.info("Копирование с изменением" + s);

        }
        String s2 = null;
        if (keyEvent.getCode() == KeyCode.F4) {//Вставляем с изменением
            if (coppyTxtRead != null) {

                try {
                    FileWriter save = new FileWriter(Fx2.CurrDirStr + "\\" + b);
                    save.write(coppyTxtRead);
                    save.close();

                    Fx2.setLabelTxt();
                    tableview.getItems().clear();
                    Fx2.CreateTableView();
                    logger.info("Вставляем с изменением" + b + "в" + Fx2.CurrDirStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else
                logger.info("Pls copy file wizz F3");
        }


        if (keyEvent.getCode() == KeyCode.C) {
            a = str;
            source = new File(s);
            logger.info("Сoppy: " + s);

        }


        if (keyEvent.getCode() == KeyCode.X) {
            a1 = str;
            xurl = new File(s);


            logger.info("Сoppy and delete: " + s);
        }


        if (keyEvent.getCode() == KeyCode.V) {
            if (source != null & x == null) {
                try {
                    dest = new File(Fx2.CurrDirStr + "\\" + a);
                } catch (Exception e) {

                }
                try {
                    copyFileUsingStream(source, dest);
                } catch (Exception e) {
                    logger.info("не копирую папки");
                }


                Fx2.setLabelTxt();
                tableview.getItems().clear();
                Fx2.CreateTableView();
                logger.info("Копирую в: " + source);
                source = null;
            }


            if (xurl != null & source == null) {
                try {
                    dest = new File(Fx2.CurrDirStr + "\\" + a1);
                } catch (Exception e) {
                    logger.info("не копирую папки");
                }

                try {
                    copyFileUsingStream(xurl, dest);
                } catch (Exception e) {
                    logger.info("не копирую папки");
                }

                xurl.delete();
                Fx2.setLabelTxt();
                tableview.getItems().clear();
                Fx2.CreateTableView();
                logger.info("Save and delete: " + x);
                x = null;
            }

        }

        s = null;
    }


}

