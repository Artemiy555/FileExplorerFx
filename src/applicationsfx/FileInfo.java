package applicationsfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;


public class FileInfo {


    private ImageView image;
    private SimpleStringProperty name;
    private SimpleStringProperty size;
    private SimpleStringProperty date;

    public FileInfo(ImageView image, String name, String size, String date) {
        super();
        this.image = image;
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleStringProperty(size);
        this.date = new SimpleStringProperty(date);
    }

    public FileInfo(String name, String size, String date) {
        super();


        this.name = new SimpleStringProperty(name);
        this.size = new SimpleStringProperty(size);
        this.date = new SimpleStringProperty(date);
    }


    public String getDate() {
        return date.get();
    }

    public String getSize() {
        return size.get();
    }

    public String getName() {
        return name.get();
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView value) {
        image = value;
    }


}
