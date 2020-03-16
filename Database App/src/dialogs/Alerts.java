package dialogs;

import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author st52550
 */
public class Alerts {
    public void alertInfo(String title, String message){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(title);
        info.setContentText(message);
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass()
                      .getResource("/img/info.png").toString()));
        info.setHeaderText(null);
        info.showAndWait();      
    }
    
    public void alertError(String title, String message) {
        Alert chyba = new Alert(Alert.AlertType.ERROR);
        chyba.setTitle(title);
        chyba.setContentText(message);
        Stage stage = (Stage) chyba.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass()
                .getResource("/img/error.png").toString()));
        chyba.setHeaderText(null);
        chyba.showAndWait();
    }
    
    public void alertExit(String title, String message){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(title);
        confirm.setContentText(message);
        Stage stage = (Stage) confirm.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass()
                .getResource("/img/confirm.png").toString()));
        confirm.setHeaderText(null);

        Optional<ButtonType> optional = confirm.showAndWait();
        if (optional.get() == ButtonType.OK) {
            Platform.exit();
        }
    }
    
    public boolean alertConfirm(String title, String message){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(title);
        confirm.setContentText(message);
        Stage stage = (Stage) confirm.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass()
                .getResource("/img/confirm.png").toString()));
        confirm.setHeaderText(null);
        
        Optional<ButtonType> optional = confirm.showAndWait();
    return optional.get() == ButtonType.OK;    
    }
    
    public String textDialog(String title, String message) {
        TextInputDialog text = new TextInputDialog();
        text.setTitle(title);
        text.setHeaderText(null);
        text.setContentText(message);
        Stage stage = (Stage) text.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass()
                .getResource("/img/text.png").toString()));

        Optional<String> result = text.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
    return null;    
    }
}
