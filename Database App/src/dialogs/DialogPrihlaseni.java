/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import data.Fakulta;
import data.PridaniAkce;
import data.ViewVyucujici;
import data.ViewPrihlaseni;
import gui.Controller;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Spektrom
 */
public class DialogPrihlaseni {

    private Alerts alerts = new Alerts();

    private Label usernameLabel = new Label("Username:");
    private TextField usernameTxt = new TextField();
    private Label hesloLabel = new Label("Heslo:");
    private PasswordField hesloTxt = new PasswordField();

    private ObservableList<ViewVyucujici> vyucujiciList = FXCollections.observableArrayList();
    private Label vyucLabel = new Label("Vyucujici:");
    private ComboBox<ViewVyucujici> vyucujiciS = new ComboBox<>(vyucujiciList);
    private Label info = new Label("Info:");
    
    private Button pridej = new Button("Přidej login");
    private Button edituj = new Button("Edituj login");

//Dialog pro přidání účtu    
    public void add(Controller c, Fakulta fak) {
//Tvorba GUI        
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Přidat vyučujícího");
        window.getIcons().add(new Image(DialogVyucujici.class
                .getResourceAsStream("/img/add.png")));
        window.setMinWidth(450);
        window.setHeight(300);

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setId("gridRoot");
        GridPane grid = new GridPane();
        setGrid(grid);
        GridPane.setConstraints(pridej, 1, 5);
        GridPane.setHalignment(pridej, HPos.RIGHT);
        grid.getChildren().add(pridej);
        root.getChildren().add(grid);

        nactiVyucujici(c, fak);

//Tlačítko pro přidání účtu        
        pridej.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularPrih(usernameTxt.getText());

                if(vyucujiciS.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.vlozLogin(usernameTxt.getText(), hesloTxt.getText(), vyucujiciS.getSelectionModel().getSelectedItem().getIdVyucujici());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    } 
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také vyučujícího");
                }     
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při vkládání vyučujícího do databáze nastala chyba." + ex);
            } catch (IOException ex) {
                alerts.alertError("Chyba", "Při vkládání vyučujícího do databáze nastala chyba. Obr.");
            }
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
                .toExternalForm());
        window.setScene(scene);
        window.showAndWait();

        clearData();
    }

//Dialog pro editaci účtu     
    public void editLogin(Controller c, ViewPrihlaseni vyucujici, Fakulta fak) {
//Tvorba GUI         
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Editovat login");
        window.getIcons().add(new Image(DialogVyucujici.class
                .getResourceAsStream("/img/edit.png")));
        window.setMinWidth(450);
        window.setHeight(300);

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setId("gridRoot");
        GridPane grid = new GridPane();
        setGrid(grid);
        GridPane.setConstraints(info, 0, 5);
        GridPane.setConstraints(edituj, 1, 6);
        GridPane.setHalignment(edituj, HPos.RIGHT);
        grid.getChildren().addAll(info,edituj);
        root.getChildren().add(grid);

        nactiVyucujici(c, fak);

//Vyplňení formulářů daty         
        usernameTxt.setText(vyucujici.getUsername());
        hesloTxt.setDisable(true);
        vyucujiciS.setDisable(true);
        vyucujiciS.getSelectionModel().select(vyucujici.getId_vyucujici());
        info.setText(vyucujici.getUsername() + " " + vyucujici.getJmeno()+ " " + vyucujici.getPrijmeni() + " ID:" + vyucujici.getId_vyucujici());

//Tlačítko pro editaci účtu        
        edituj.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularPrih(usernameTxt.getText());

                if (validniData != null) {
                    if (validniData.getMuzePridat().equals("true")) {
                        c.editLogin(vyucujici.getId_user(), usernameTxt.getText());
                        window.close();
                    } else {
                        alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                    }
                }    
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při editaci vyučujícího nastala chyba");
            }
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
                .toExternalForm());
        window.setScene(scene);
        window.showAndWait();

        clearData();
    }

//Dialog pro editaci hesla    
    public void editHeslo(Controller c, ViewPrihlaseni vyucujici, Fakulta fak) {
//Tvroba GUI        
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Editovat heslo");
        window.getIcons().add(new Image(DialogVyucujici.class
                .getResourceAsStream("/img/edit.png")));
        window.setMinWidth(450);
        window.setHeight(300);

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setId("gridRoot");
        GridPane grid = new GridPane();
        setGrid(grid);
        GridPane.setConstraints(info, 0, 5);
        GridPane.setConstraints(edituj, 1, 6);
        GridPane.setHalignment(edituj, HPos.RIGHT);
        grid.getChildren().addAll(info,edituj);
        root.getChildren().add(grid);

        nactiVyucujici(c, fak);
        usernameTxt.setDisable(true);
        vyucujiciS.setDisable(true);
       
        info.setText(vyucujici.getUsername() + " " + vyucujici.getJmeno()+ " " + vyucujici.getPrijmeni() + " ID:" + vyucujici.getId_vyucujici());

//Tlačítko pro editaci hesla        
        edituj.setOnAction(e -> {
            try {
                c.editLoginHeslo(vyucujici.getId_user(), hesloTxt.getText());
                window.close();
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při editaci vyučujícího nastala chyba"+ex);
            }
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
                .toExternalForm());
        window.setScene(scene);
        window.showAndWait();

        clearData();
    }

//Tvorba GUI    
    private void setGrid(GridPane grid) {
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");

        GridPane.setConstraints(grid, 0, 1);
        GridPane.setConstraints(usernameLabel, 0, 2);
        GridPane.setConstraints(usernameTxt, 1, 2);
        GridPane.setConstraints(hesloLabel, 0, 3);
        GridPane.setConstraints(hesloTxt, 1, 3);
        GridPane.setConstraints(vyucLabel, 0, 4);
        GridPane.setConstraints(vyucujiciS, 1, 4);

        grid.getChildren().clear();
        grid.getChildren().addAll(usernameLabel, usernameTxt, hesloLabel, hesloTxt,
                vyucLabel, vyucujiciS);
    }

//Aktualizace sezanmu vyučujících z databáze     
    private void nactiVyucujici(Controller c, Fakulta fak) {
        try {
            for (ViewVyucujici v : c.getViewVyucujici(fak)) {
                vyucujiciList.add(v);
            }
            vyucujiciS.setPromptText("Vyberte vyucujiciho");
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Nepodařil se zobrazit seznam vyucujicich.");
        }
    }

//Vyčištění formulářů    
    private void clearData() {
        usernameTxt.clear();
        hesloTxt.clear();
        usernameTxt.setDisable(false);
        hesloTxt.setDisable(false);
        vyucujiciS.setDisable(false);
    }
}
