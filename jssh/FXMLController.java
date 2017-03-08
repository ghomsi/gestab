package jssh;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLController implements Initializable {

    public TabPane tabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tabPane.getTabs().add(new TerminalTab(tabPane));

    }


}
