package gui;

import data.Fakulta;
import data.ViewObor;
import dialogs.Alerts;
import dialogs.DialogObory;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author st52550
 */
public class KartaStudijniObory {
    private TableView<ViewObor> tableO = new TableView();
    private TableColumn<ViewObor, String> oznaceni = new TableColumn<>("Označení");
    private TableColumn<ViewObor, String> nazevO = new TableColumn<>("Název oboru");
    private TableColumn<ViewObor, String> katedraO = new TableColumn<>("Katedra");
    private TableColumn<ViewObor, String> fakultaO = new TableColumn<>("Fakulta");
    
    private ComboBox<Fakulta> vyberF = new ComboBox<>();
    private ObservableList<Fakulta> listFakult;
    
    private Alerts alerts = new Alerts();
    private DialogObory dialogObory;
    
    public VBox getKartaStudijniObory(Controller c, int opravneni) throws SQLException{
//Tvorba GUI          
        VBox obory = new VBox(10);
            obory.setId("layout");
            obory.setPadding(new Insets(10, 10, 10, 10));
            
        Label l5 = new Label("Seznam studijních oborů");
            l5.setStyle("-fx-font-size: 20px");
            
        HBox vyberFakultu = new HBox(10);
            vyberFakultu.setAlignment(Pos.CENTER_LEFT);
            Label nazevFakulty = new Label("Fakulta: ");
            listFakult = FXCollections.observableArrayList(c.getFakulty());
            vyberF.setItems(listFakult);
        vyberFakultu.getChildren().addAll(nazevFakulty, vyberF);
        
        vyberF.getSelectionModel().selectFirst();
            
        vyberF.setOnAction(e -> {
            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
        });  

//Konstrukce tabulky studijních oborů        
        oznaceni.setCellValueFactory(new PropertyValueFactory<>("oznaceni"));
            oznaceni.prefWidthProperty().setValue(150);
        nazevO.setCellValueFactory(new PropertyValueFactory<>("nazev")); 
            nazevO.prefWidthProperty().setValue(250);
        katedraO.setCellValueFactory(new PropertyValueFactory<>("katedra"));
            katedraO.prefWidthProperty().setValue(300);
        fakultaO.setCellValueFactory(new PropertyValueFactory<>("fakulta"));
            fakultaO.prefWidthProperty().setValue(350);
        
        tableO.getColumns().addAll(oznaceni, nazevO, katedraO, fakultaO);
        updateTable(c, vyberF.getSelectionModel().getSelectedItem());

        Label l6 = new Label("Přidat, editovat či odebrat studijní obor");
            l6.setStyle("-fx-font-size: 20px"); 
            
//Tlačítko pro přidání, editaci či smazání místnosti
        HBox buttons3 = new HBox(10); 
        
            Button pridejObor = new Button("Přidej", new ImageView("/img/add.png")); 
                pridejObor.setOnAction(e -> { 
                    dialogObory = new DialogObory();
                    dialogObory.add(c, vyberF.getSelectionModel().getSelectedItem().getNazevF());
                    updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                });
                
            Button editujObor = new Button("Edituj", new ImageView("/img/edit.png"));
                editujObor.setOnAction(e -> {
                    ViewObor vybranyObor = tableO.getSelectionModel().getSelectedItem();
                    if (vybranyObor != null) {
                        dialogObory = new DialogObory();
                        dialogObory.edit(c, vybranyObor, vyberF.getSelectionModel().getSelectedItem().getNazevF());
                        updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                    } else {
                        alerts.alertInfo("Nelze editovat", "Nejdříve vyberte obor, který chcete editovat.");
                    }
                });
                
            Button smazatObor = new Button("Smaž", new ImageView("/img/delete.png"));
                smazatObor.setOnAction(e -> {
                    try {
                        if (tableO.getSelectionModel().getSelectedItem() != null) {
                            int idObor = tableO.getSelectionModel().getSelectedItem().getId();
                            c.deleteObor(idObor);
                            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                        } else {
                            alerts.alertInfo("Nelze smazat", "Nejdříve vyberte obor, který chcete smazat.");
                        }  
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Nepodařilo se smazat obor. Ujistěte se, jestli "
                                + "není součástí některé katedry.");
                    }
                });
                
//////////Lukas
//Přiřazení viditelnosti objektů podle práva uživatele
        switch (opravneni) {
            case 0:
                buttons3.getChildren().addAll(pridejObor, editujObor, smazatObor);
                obory.getChildren().addAll(l5, vyberFakultu, tableO, l6, buttons3);
                break;
            case 1:
                obory.getChildren().addAll(l5, vyberFakultu, tableO);
                break;
            default:                
                obory.getChildren().addAll(l5, vyberFakultu, tableO);
                break;
        }           
        
        return obory;
    }

//Aktualizace dat v tabulce     
    private void updateTable(Controller c, Fakulta f){
        try {
            if (f != null){
                ObservableList<ViewObor> listOboru = 
                    FXCollections.observableArrayList(c.getViewObory(f));
                tableO.setItems(listOboru);
            }
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat do tabulky.");
        }
    }

//Aktualizace fakult    
    public void updateData(Controller c){
        try {
            listFakult = FXCollections.observableArrayList(c.getFakulty());
            vyberF.setItems(listFakult);
            vyberF.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při načítání dat do seznamů.");
        }
    }
}
