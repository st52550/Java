package dialogs;

import data.Katedra;
import data.PridaniAkce;
import data.ViewVyucujici;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author st52550
 */
public class DialogVyucujici {
    private Alerts alerts = new Alerts();
    
    private Label titul1 = new Label("Titul před:");
    private TextField titul1Txt = new TextField();
    private Label prijmeni = new Label("Příjmení:");
    private TextField prijmeniTxt = new TextField();
    private Label jmeno = new Label("Jméno:");    
    private TextField jmenoTxt = new TextField();
    private Label titul2 = new Label("Titul za:");    
    private TextField titul2Txt = new TextField();          

    private ObservableList<String> katedryList = FXCollections.observableArrayList();
    private Label katedra = new Label("Katedra:");
    private ComboBox<String> katedraS = new ComboBox<>(katedryList);                                                                    
    private Label telefon = new Label("Telefon:");
    private TextField telefonTxt = new TextField();
    private Label mobil = new Label("Mobil:");
    private TextField mobilTxt = new TextField();
    private Label email = new Label("E-mail:");    
    private TextField emailTxt = new TextField();  
            
    private Button pridej = new Button("Přidej vyučujícího");
    private Button edituj = new Button("Edituj vyučujícího");

//Dialog pro přidání vyučujícího    
    public void add(Controller c){
//Tvorba GUI        
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Přidat vyučujícího");
            window.getIcons().add(new Image(DialogVyucujici.class
                .getResourceAsStream("/img/add.png")));
            window.setMinWidth(450);
            window.setHeight(520);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGrid(grid);
            GridPane.setConstraints(pridej, 1, 14);
            GridPane.setHalignment(pridej, HPos.RIGHT);
            grid.getChildren().add(pridej);
        root.getChildren().add(grid); 
        
        getKatedryList(c);

//Tlačítko pro přidání vyučujícího         
        pridej.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularVyucujici(titul1Txt.getText(), jmenoTxt.getText(), prijmeniTxt.getText(),
                        titul2Txt.getText(), Integer.parseInt(telefonTxt.getText()), Integer.parseInt(mobilTxt.getText()), emailTxt.getText());

                if(katedraS.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.vlozVyucujiciho(titul1Txt.getText(), prijmeniTxt.getText(), jmenoTxt.getText(),
                                titul2Txt.getText(), katedraS.getSelectionModel().getSelectedItem(),
                                telefonTxt.getText(), mobilTxt.getText(), emailTxt.getText());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    } 
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také katedru");
                }     
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při vkládání vyučujícího do databáze nastala chyba.");
            } catch (IOException ex) {
                alerts.alertError("Chyba", "Při vkládání vyučujícího do databáze nastala chyba. Obr."+ex);
            } catch (NumberFormatException ex) {
                alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
            }
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearData();
    }

//Dialog pro editaci vyučujícího    
    public void edit(Controller c, ViewVyucujici vyucujici){
//Tvorba GUI
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Editovat vyučujícího");
            window.getIcons().add(new Image(DialogVyucujici.class
                .getResourceAsStream("/img/edit.png")));
            window.setMinWidth(450);
            window.setHeight(520);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGrid(grid);
            GridPane.setConstraints(edituj, 1, 14);
            GridPane.setHalignment(edituj, HPos.RIGHT);
            grid.getChildren().add(edituj);
        root.getChildren().add(grid);
        
        getKatedryList(c);

//Vyplňení formulářů daty        
        titul1Txt.setText(vyucujici.getTitulPred());
        prijmeniTxt.setText(vyucujici.getPrijmeni());
        jmenoTxt.setText(vyucujici.getJmeno());
        titul2Txt.setText(vyucujici.getTitulZa());
        katedraS.getSelectionModel().select(vyucujici.getKatedra());
        telefonTxt.setText(String.valueOf(vyucujici.getTelefon()));
        mobilTxt.setText(String.valueOf(vyucujici.getMobil()));
        emailTxt.setText(vyucujici.getEmail());

//Tlačítko pro editaci vyučujícího        
        edituj.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularVyucujici(titul1Txt.getText(), jmenoTxt.getText(), prijmeniTxt.getText(),
                        titul2Txt.getText(), Integer.parseInt(telefonTxt.getText()), Integer.parseInt(mobilTxt.getText()), emailTxt.getText());

                if(katedraS.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.editVyucujiciho(vyucujici.getIdVyucujici(), titul1Txt.getText(), prijmeniTxt.getText(), jmenoTxt.getText(),
                                    titul2Txt.getText(), katedraS.getSelectionModel().getSelectedItem(),
                                    telefonTxt.getText(), mobilTxt.getText(), emailTxt.getText());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    } 
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také katedru");
                }      
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při editaci vyučujícího nastala chyba");
            } catch (NumberFormatException ex) {
                alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
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
    private void setGrid(GridPane grid){
        grid.setPadding(new Insets(10, 10, 10, 10));        
        grid.setVgap(8);
        grid.setHgap(10);        
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");
        
        GridPane.setConstraints(grid, 0, 1);
        GridPane.setConstraints(titul1, 0, 2);
        GridPane.setConstraints(titul1Txt, 1, 2);
        GridPane.setConstraints(prijmeni, 0, 3);
        GridPane.setConstraints(prijmeniTxt, 1, 3);       
        GridPane.setConstraints(jmeno, 0, 4);
        GridPane.setConstraints(jmenoTxt, 1, 4);
        GridPane.setConstraints(titul2, 0, 5);
        GridPane.setConstraints(titul2Txt, 1, 5);
        GridPane.setConstraints(katedra, 0, 7);
        GridPane.setConstraints(katedraS, 1, 7);
        GridPane.setConstraints(telefon, 0, 9);
        GridPane.setConstraints(telefonTxt, 1, 9);
        GridPane.setConstraints(mobil, 0, 10);
        GridPane.setConstraints(mobilTxt, 1, 10);
        GridPane.setConstraints(email, 0, 11);
        GridPane.setConstraints(emailTxt, 1, 11);       
        
        grid.getChildren().clear();
        grid.getChildren().addAll(titul1, titul1Txt, prijmeni, prijmeniTxt,
                jmeno, jmenoTxt, titul2, titul2Txt, katedra, katedraS, telefon,
                telefonTxt, mobil, mobilTxt, email, emailTxt);
    }
 
//Aktualizace sezanmu kateder z databáze
    private void getKatedryList(Controller c){
        try {
            katedryList.clear();
            for(Katedra kat:c.getKatedry()){katedryList.add(kat.getNazevK());}
            katedraS.setPromptText("Vyberte katedru"); 
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Nepodařil se zobrazit seznam kateder.");
        }
    }
 
//Vyčištění formulářů
    private void clearData(){
        titul1Txt.clear();
        prijmeniTxt.clear();
        jmenoTxt.clear();
        titul2Txt.clear();
        telefonTxt.clear();
        mobilTxt.clear();
        emailTxt.clear();
    }
}
