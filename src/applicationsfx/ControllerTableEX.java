package applicationsfx;


import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;
import java.util.prefs.Preferences;

import static applicationsfx.Calculator.calculate;

public class ControllerTableEX {

    Map<String, String> dataRow;
    ObservableList<Map> allData;
    String key1;
    int sizeMap;
    boolean main = false;
    boolean line = false;
    @FXML
    private TableView table = new TableView<>();
    private char nextChar = 'A';

    @FXML
    private void refresh() {
        table.refresh();
    }

    private void main() {
        table.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells = table.getSelectionModel().getSelectedCells();


        selectedCells.addListener(new ListChangeListener() {//пол выделеного элемента
            @Override
            public void onChanged(Change c) {
                try {
                    TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                    Object val = tablePosition.getTableColumn().getCellData(tablePosition.getRow());
                    System.out.println("Selected Value " + val);
                } catch (Exception e) {
                    System.out.println("Ну бывает..");
                }

            }
        });
        main = true;
    }

    @FXML
    private void addLine() {

        allData = table.getItems();
        int offset = allData.size();
        dataRow = new HashMap<>();
        for (int j = 0; j < table.getColumns().size(); j++) {
            String mapKey = Character.toString((char) ('A' + j));
            String value1 = mapKey + (offset + 1);
            dataRow.put(mapKey, value1);
        }
        allData.add(dataRow);
        String string = allData.toString();
        System.out.println(string);
        line = true;
    }

    private String parser(String old) {
        int parserNumder;
        String parserCode;

        parserNumder = Integer.parseInt(old.substring(1));
        parserNumder--;
        System.out.println("ParserNumber " + parserNumder);

        parserCode = old.replaceAll("[^,A-Z]", "");//работает!!!!
        System.out.println("ParserCode " + parserCode);

        Map map = allData.get(parserNumder);
        return map.get(parserCode).toString();
        //добавляем пробелы и отправим(через пробелы) лист{=-/ и все такое}, каждую часть отправим в парсер каждый элемент и засетим его и отправим в калькулятор.
    }


    private void ketMap(String oldMining) {
        int size = allData.size();

        for (int i = 0; size > i; i++) {
            Map map = allData.get(i);

            Collection<String> collection = map.keySet();
            for (String key : collection) {
                Object obj = map.get(key);
                if (key != null) {
                    if (oldMining.equals(obj)) {

                        key1 = key;// нашли наше значение и возвращаем  ключ
                        sizeMap = i;//координата в ObservableList

                        System.out.println("ключ полученый из метода " + key1);
                        System.out.println("координата в ObservableList " + sizeMap);
                    }
                }
            }


        }

    }


    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(ControllerTableEX.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(ControllerTableEX.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Обновление заглавия сцены.
         //   primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Обновление заглавия сцены.
           // primaryStage.setTitle("AddressApp");
        }
    }

    public void savePersonDataToFile(File file) {


            System.out.println("работает");
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ClassTableEX.class);

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            System.out.println("Обёртываем наши данные об адресатах.");
            ClassTableEX wrapper = new ClassTableEX();
            wrapper.setPersons(allData);

            System.out.println(" Маршаллируем и сохраняем XML в файл.");
            m.marshal(wrapper, file);

            // Сохраняем путь к файлу в реестре.
            setPersonFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }

    }

    @FXML
    private void save() {



        File file = new File("C:\\new\\");
        setPersonFilePath(file);


    }

    @FXML
    private void saveas() {
//        FileChooser fileChooser = new FileChooser();
//
//        // Задаём фильтр расширений
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
//                "XML files (*.xml)", "*.xml");
//        fileChooser.getExtensionFilters().add(extFilter);

        // Показываем диалог сохранения файла
        File file = new File("C:\\new\\trt");

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }

                savePersonDataToFile(file);



        }
    }

    @FXML
    private void open() {
//
        File file = new File("C://new//какой-то_файл.xml");


        try {
        JAXBContext context = JAXBContext
                .newInstance(ClassTableEX.class);  //тут и не работает

        Unmarshaller um = context.createUnmarshaller();

        // Чтение XML из файла и демаршализация.
            ClassTableEX wrapper = (ClassTableEX) um.unmarshal(file);

        allData.clear();
            allData.addAll(wrapper.getPersons());

        // Сохраняем путь к файлу в реестре.
     //   setPersonFilePath(file);

    } catch (Exception e) { // catches ANY exception
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Could not load data");
        alert.setContentText("Could not load data from file:\n" + file.getPath());

        alert.showAndWait();
    }
    }


    @FXML
    public void addColum() {
        if (main == false) main();

        if (line == false) {
            String mapChar = String.valueOf(nextChar++);
            TableColumn<Map, String> column = new TableColumn<>("Class " + mapChar);
            column.setCellValueFactory(new MapValueFactory(mapChar));
            column.setMinWidth(130);

            Callback<TableColumn<Map, String>, TableCell<Map, String>>
                    cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
                @Override
                public TableCell call(TableColumn p) {
                    return new TextFieldTableCell(new StringConverter() {

                        @Override
                        public String toString(Object t) {
                            return t.toString();
                        }

                        @Override
                        public Object fromString(String string) {
                            return string;
                        }
                    });
                }
            };


            column.setCellFactory(cellFactoryForMap);
            column.setOnEditCommit((TableColumn.CellEditEvent<Map, String> t) -> {
                //System.out.println("работаю");


                String oldMining = t.getOldValue();
                System.out.println("Старое значение " + oldMining);

                ketMap(oldMining);

                String test = t.getNewValue();
                String arg = test;
                System.out.println("Новое значение " + test);

                test = calculate(test);
                if (test == "null") {

                    test = t.getNewValue();

                    test = test.replaceAll("\\+", " + ");
                    test = test.replaceAll("-", " - ");
                    test = test.replaceAll("/", " / ");
                    test = test.replaceAll("\\*", " * ");
                    test = test.replaceAll("%", " % ");
                    System.out.println("что-то " + test);

                    List<String> paraList = new ArrayList<String>();
                    paraList = Arrays.asList(test.split(" "));
                    System.out.println(paraList);
                    String tr = null;
                    for (int i = 0; paraList.size() > i; i++) {
                        try {
                            tr = parser(paraList.get(i));
                            System.out.println("tst" + tr);
                            if (tr != null) {
                                paraList.set(i, tr);
                            }
                        } catch (Exception e) {
                            System.out.println("неудачно");
                        }


                        System.out.println("измененная " + paraList);
                        String calcul = getStringRepresentation(paraList);
                        System.out.println("фулл " + calcul);
                        test = calculate(calcul);
                        if (test == "null") {
                            test = "";

                        }
                        System.out.println("Посчитаный" + calcul);
                        if (test != "null"){


                        }
                    }

                    // "\\(|\\)|\\+|-|\\*|/|<|%",


                    System.out.println("работаю");
                }

                //arg = arg + "=";//добавить костыл, чтоб если в строке есть ровно, оно не добавлялась

                System.out.println("работаю322" +arg);
                Map map = allData.get(sizeMap);
                table.refresh();
                System.out.println("Изменяем " + map.put(key1, test));
                //System.out.println("Изменяем " + map.put(key1, (arg + test)));

                System.out.println("test: " + map.toString());

                System.out.println(allData);


            });


            table.getColumns().add(column);

        } else System.out.println("Попробуй крашни");
    }

    String getStringRepresentation(List<String> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (String ch : list) {
            builder.append(ch);
        }
        return builder.toString();
    }
}
