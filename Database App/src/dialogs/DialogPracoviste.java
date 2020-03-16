package dialogs;

import data.Fakulta;
import data.Katedra;
import data.PridaniAkce;
import data.ViewKatedry;
import gui.Controller;
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
public class DialogPracoviste {
    private Alerts alerts = new Alerts();
    
    private Label zkK = new Label("Zkratka katedry:");
    private TextField zkKTxt = new TextField();
    private Label nazK = new Label("Název katedry:");
    private TextField nazKTxt = new TextField();
    private ObservableList<String> fakultyList1 = FXCollections.observableArrayList();
    private Label fakL1 = new Label("Fakulta:");
    private ComboBox<String> fakultaS1 = new ComboBox<>(fakultyList1);
    
    private Label zkF = new Label("Zkratka fakulty:");
    private TextField zkFTxt = new TextField();
    private Label nazF = new Label("Název fakulty:");
    private TextField nazFTxt = new TextField();
    private Label tel1 = new Label("Telefon:");
    private TextField telefonTxt1 = new TextField();
    private Label mob1 = new Label("Mobil:");
    private TextField mobilTxt1 = new TextField();
    private Label mail1 = new Label("E-mail:");    
    private TextField emailTxt1 = new TextField();
    
    private Button pridejK = new Button("Přidej katedru");
    private Button editujK = new Button("Edituj katedru");
    private Button pridejF = new Button("Přidej fakultu");
    private Button editujF = new Button("Edituj fakultu");

//Dialog pro přidání katedry    
    public void addKatedra(Controller c){
//Tvorba GUI         
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Přidat katedru");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/add.png")));
            window.setMinWidth(450);
            window.setHeight(350);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridKatedra(grid);
            GridPane.setConstraints(pridejK, 1, 7);
            GridPane.setHalignment(pridejK, HPos.RIGHT);
            grid.getChildren().add(pridejK);
        root.getChildren().add(grid); 
        
        getFakultaList(c);                  

//Tlačítko pro přidání katedry        
        pridejK.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularKatedra(zkKTxt.getText(), nazKTxt.getText());
                
                if(fakultaS1.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.vlozKatedru(zkKTxt.getText(), nazKTxt.getText(), fakultaS1.getSelectionModel().getSelectedItem());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    } 
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také fakultu");
                }     
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při vkládání katedry do databáze nastala chyba.");
            }
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearDataKatedra();
    }

//Dialog pro editaci katedry     
    public void editKatedra(Controller c, ViewKatedry katedra){
//Tvorba GUI 
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Editovat katedru");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/edit.png")));
            window.setMinWidth(450);
            window.setHeight(350);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridKatedra(grid);
            GridPane.setConstraints(editujK, 1, 7);
            GridPane.setHalignment(editujK, HPos.RIGHT);
            grid.getChildren().add(editujK);
        root.getChildren().add(grid); 
        
        getFakultaList(c);

//Vyplňení formulářů daty         
        zkKTxt.setText(katedra.getZkratkaK());
        nazKTxt.setText(katedra.getNazevK());
        fakultaS1.getSelectionModel().select(katedra.getNazevF());

//Tlačítko pro editaci katedry         
        editujK.setOnAction(e -> {
            PridaniAkce validniData;
            try {              
                validniData = c.formularKatedra(zkKTxt.getText(), nazKTxt.getText());
                
                if(fakultaS1.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.editKatedra(katedra.getId(), zkKTxt.getText(), nazKTxt.getText(),
                                fakultaS1.getSelectionModel().getSelectedItem());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    } 
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také fakultu");
                }        
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při editaci katedry nastala chyba.");
            }            
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearDataKatedra();
    }

//Dialog pro přidání katedry    
    public void addFakulta(Controller c){
//Tvorba GUI
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Přidat fakultu");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/add.png")));
            window.setMinWidth(450);
            window.setHeight(420);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridFakulta(grid);
            GridPane.setConstraints(pridejF, 1, 9);
            GridPane.setHalignment(pridejF, HPos.RIGHT);
            grid.getChildren().add(pridejF);
        root.getChildren().add(grid);

//Tlačítko pro přidání fakulty        
        pridejF.setOnAction(e -> {
            PridaniAkce validniAkce;
            try {
                validniAkce = c.formularFakulta(zkFTxt.getText(), nazFTxt.getText(), Integer.parseInt(telefonTxt1.getText()),
                        Integer.parseInt(mobilTxt1.getText()), emailTxt1.getText());
                
                if (validniAkce != null) {
                    if (validniAkce.getMuzePridat().equals("true")) {
                        c.vlozFakultu(zkFTxt.getText(), nazFTxt.getText(), telefonTxt1.getText(), 
                                mobilTxt1.getText(), emailTxt1.getText());
                        window.close();
                } else {
                        alerts.alertInfo("Nelze přidat", validniAkce.getHlaska());
                    }
                }        
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při vkládání fakulty do databáze nastala chyba.");
            } catch (NumberFormatException ex) {
                alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
            }
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearDataFakulta();
    }
    
//Dialog pro editaci fakulty    
    public void editFakulta(Controller c, Fakulta fakulta){
//Tvorba GUI        
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Editovat fakultu");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/edit.png")));
            window.setMinWidth(450);
            window.setHeight(420);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridFakulta(grid);
            GridPane.setConstraints(editujF, 1, 9);
            GridPane.setHalignment(editujF, HPos.RIGHT);
            grid.getChildren().add(editujF);
        root.getChildren().add(grid); 
 
//Vyplňení formulářů daty        
        zkFTxt.setText(fakulta.getZkratkaF());
        nazFTxt.setText(fakulta.getNazevF());
        telefonTxt1.setText(String.valueOf(fakulta.getTelefon()));
        mobilTxt1.setText(String.valueOf(fakulta.getMobil()));
        emailTxt1.setText(fakulta.getEmail());

//Tlačítko pro editaci fakulty        
        editujF.setOnAction(e -> {
            PridaniAkce validniAkce;
            try {
                validniAkce = c.formularFakulta(zkFTxt.getText(), nazFTxt.getText(), Integer.parseInt(telefonTxt1.getText()),
                        Integer.parseInt(mobilTxt1.getText()), emailTxt1.getText());
                
                if (validniAkce != null) {
                    if (validniAkce.getMuzePridat().equals("true")) {
                        c.editFakulta(fakulta.getId(), zkFTxt.getText(), nazFTxt.getText(), telefonTxt1.getText(), 
                                        mobilTxt1.getText(), emailTxt1.getText());
                        window.close();
                    } else {
                        alerts.alertInfo("Nelze přidat", validniAkce.getHlaska());
                    }
                }
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při editaci fakulty nastala chyba.");
            } catch (NumberFormatException ex) {
                alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
            }
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearDataFakulta();
    }    

//Tvorba GUI
    private void setGridKatedra(GridPane grid){
        grid.setPadding(new Insets(10, 10, 10, 10));        
        grid.setVgap(8);
        grid.setHgap(10);        
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");
        
        GridPane.setConstraints(grid, 0, 1);
        GridPane.setConstraints(zkK, 0, 2);
        GridPane.setConstraints(zkKTxt, 1, 2);
        GridPane.setConstraints(nazK, 0, 3);
        GridPane.setConstraints(nazKTxt, 1, 3);       
        GridPane.setConstraints(fakL1, 0, 4);
        GridPane.setConstraints(fakultaS1, 1, 4);       
        
        grid.getChildren().clear();
        grid.getChildren().addAll(zkK, zkKTxt, nazK, nazKTxt, fakL1, fakultaS1);
    }

//Tvorba GUI
    private void setGridFakulta(GridPane grid){
        grid.setPadding(new Insets(10, 10, 10, 10));        
        grid.setVgap(8);
        grid.setHgap(10);        
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");
        
        GridPane.setConstraints(grid, 0, 1);
        GridPane.setConstraints(zkF, 0, 2);
        GridPane.setConstraints(zkFTxt, 1, 2);
        GridPane.setConstraints(nazF, 0, 3);
        GridPane.setConstraints(nazFTxt, 1, 3);       
        GridPane.setConstraints(tel1, 0, 4);
        GridPane.setConstraints(telefonTxt1, 1, 4); 
        GridPane.setConstraints(mob1, 0, 5);
        GridPane.setConstraints(mobilTxt1, 1, 5);       
        GridPane.setConstraints(mail1, 0, 6);
        GridPane.setConstraints(emailTxt1, 1, 6); 
        
        grid.getChildren().clear();
        grid.getChildren().addAll(zkF, zkFTxt, nazF, nazFTxt, tel1, telefonTxt1,
                mob1, mobilTxt1, mail1, emailTxt1);
    }

//Aktualizace sezanmu fakult z databáze    
    private void getFakultaList(Controller c){
        try {
            for(Fakulta kat:c.getFakulty()){fakultyList1.add(kat.getNazevF());}
            fakultaS1.setPromptText("Vyberte fakultu");
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Nepodařil se zobrazit seznam fakult.");
        }                    
    }  

//Vyčištění formulářů    
    private void clearDataKatedra(){
        zkKTxt.clear();
        nazKTxt.clear();
    }

//Vyčištění formulářů    
    private void clearDataFakulta(){
        zkFTxt.clear();
        nazFTxt.clear();
        telefonTxt1.clear();
        mobilTxt1.clear();
        emailTxt1.clear();
    }
}
