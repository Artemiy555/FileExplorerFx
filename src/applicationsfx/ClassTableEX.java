package applicationsfx;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "table")
public class ClassTableEX {

    private List<Map> persons;

    @XmlElement(name = "table")
    public List<Map> getPersons() {
        return persons;
    }

    public void setPersons(List<Map> persons) {
        this.persons = persons;
    }
}
