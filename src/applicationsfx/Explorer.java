package applicationsfx;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.logging.Logger;


public interface Explorer {

    Image getIconImageFX(File f);

    TreeItem<String>[] TreeCreate(File dir);

    String calculateSize(File f);

    String FindAbsolutePath(TreeItem<String> item, String s);

    boolean IsDrive(File f);

    int FilesHiddensCount(File dir);

    void CreateTreeView(TreeView<String> treeview);

    void CreateTableView(TableView<FileInfo> tableview, TableColumn<FileInfo, ImageView> image, TableColumn<FileInfo, String> date,
                         TableColumn<FileInfo, String> name, TableColumn<FileInfo, String> size);

    void CreateTableView();

    void CreateTilesView();

    void setLabelTxt();

    void Initiate();

    void setValues(TableView<FileInfo> tableview, TableColumn<FileInfo, ImageView> image, TableColumn<FileInfo, String> date,
                   TableColumn<FileInfo, String> name, TableColumn<FileInfo, String> size);

    void CreateTiles();



}
