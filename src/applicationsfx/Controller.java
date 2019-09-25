package applicationsfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static applicationsfx.ControllerTableView.Fx2;


public class Controller implements Initializable {

    static ClassTreeView Fx1;
    @FXML
    private Pane secPane;
    @FXML
    private TreeView<String> treeview;
    @FXML
    private Label label;

    private int count;

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ControllerTableView.class.getSimpleName());


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        count = 0;
        Fx1 = new ClassTreeView();
        Fx1.CurrDirFile = new File("C:/");
        Fx1.CurrDirStr = Fx1.CurrDirFile.getAbsolutePath();
        Fx1.lbl = label;
        Fx2.lbl = label;
        label.setText(Fx1.CurrDirStr);


        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("Scene2.fxml"));

            secPane.getChildren().add(newLoadedPane);//первая подкл и работает


        } catch (NullPointerException x) {
            x.printStackTrace();
        } catch (IOException x) {
            x.printStackTrace();
        }


        Fx1.CreateTreeView(treeview);


    }

    @FXML
    private void handleMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            try {

                TreeItem<String> item = treeview.getSelectionModel().getSelectedItem();
                Fx1.CurrDirName = item.getValue();  //первый итм
                System.out.println("Текст : " + item.getValue());
                Fx1.CurrDirFile = new File(Fx1.FindAbsolutePath(item, item.getValue()));

                Fx1.CurrDirStr = Fx1.CurrDirFile.getAbsolutePath();
                Fx2.tableview.getItems().clear();
                Fx2.CreateTableView();

            } catch (Exception x) {
                System.out.println(x.getMessage());
            }
        }


    }
    
    @FXML
    private void loadFxml(ActionEvent event) throws IOException {

        count = (count + 1) % 2;

        Pane newLoadedPane;
        secPane.getChildren().clear();
        newLoadedPane = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
        secPane.getChildren().add(newLoadedPane);


    }
    @FXML
    private void createTxt(){




        //else
            //textFille.setText("Еnter URL!\n");

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


                File file = new File(s);
                try {
                    file.createNewFile();
                    logger.info("Txt создан");
                    System.out.println("txt create");
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                File f = new File(s);
//                try {
//                    if (f.mkdir()) {
//                        logger.info("Txt создан");
//                    } else {
//                        logger.info("  ");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                logger.info("Cоздание txt по:" + s);
                Fx2.setLabelTxt();
                //   tableview.getItems().clear();
                Fx2.CreateTableView();

                newWindow.close();
            }
        });


        root.getChildren().addAll(secondLabel, textArea, button);
        Scene secondScene = new Scene(root, 230, 100);

        newWindow.setTitle("Добавление txt");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.show();


   }

    @FXML
    private void createPak(){

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
             //   tableview.getItems().clear();
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


}
