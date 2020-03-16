package gui;

import data.Fakulta;
import data.Mistnost;
import data.ViewPredmet;
import dialogs.Alerts;
import dialogs.DialogMistnosti;
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
public class KartaMistnosti {
    private TableView<Mistnost> tableM = new TableView();
    private TableColumn<Mistnost, String> oznaceni = new TableColumn<>("Označení");
    private TableColumn<Mistnost, Integer> kapacita = new TableColumn<>("Kapacita");
    private TableColumn<Mistnost, String> katedra = new TableColumn<>("Katedra");
    private TableColumn<Mistnost, String> fakulta = new TableColumn<>("Fakulta");
    
    private ComboBox<Fakulta> vyberF = new ComboBox<>();
    private ObservableList<Fakulta> listFakult;
    private Alerts alerts = new Alerts();
    private DialogMistnosti dialogMistnosti;
    
    public VBox getKartaMistnosti(Controller c, int opravneni) throws SQLException{
//Tvorba GUI        
        VBox mistnosti = new VBox(10);
            mistnosti.setId("layout");
            mistnosti.setPadding(new Insets(10, 10, 10, 10));
            
        Label l3 = new Label("Evidence místností");
            l3.setStyle("-fx-font-size: 20px");
            
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

//Konstrukce tabulky místností        
        oznaceni.setCellValueFactory(new PropertyValueFactory<>("oznaceni"));
            oznaceni.prefWidthProperty().setValue(125);
        kapacita.setCellValueFactory(new PropertyValueFactory<>("kapacita"));
            kapacita.prefWidthProperty().setValue(125);
        katedra.setCellValueFactory(new PropertyValueFactory<>("katedra"));
            katedra.prefWidthProperty().setValue(300);
        fakulta.setCellValueFactory(new PropertyValueFactory<>("fakulta"));
            fakulta.prefWidthProperty().setValue(350);
        
        tableM.getColumns().addAll(oznaceni, kapacita, katedra, fakulta);
        updateTable(c, vyberF.getSelectionModel().getSelectedItem());
        
        Label l6 = new Label("Přidat, editovat či odebrat místnost");
            l6.setStyle("-fx-font-size: 20px"); 
 
//Tlačítko pro přidání, editaci či smazání místnosti            
        HBox buttons3 = new HBox(10); 
        
            Button pridejPred = new Button("Přidej", new ImageView("/img/add.png")); 
                pridejPred.setOnAction(e -> { 
                    dialogMistnosti = new DialogMistnosti();
                    dialogMistnosti.add(c, vyberF.getSelectionModel().getSelectedItem().getNazevF());
                    updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                });
                
            Button editujPred = new Button("Edituj", new ImageView("/img/edit.png"));
                editujPred.setOnAction(e -> {
                    Mistnost vybranaMistnost = tableM.getSelectionModel().getSelectedItem();
                    if (vybranaMistnost != null) {
                        dialogMistnosti = new DialogMistnosti();
                        dialogMistnosti.edit(c, vybranaMistnost, vyberF.getSelectionModel().getSelectedItem().getNazevF());
                        updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                    } else {
                        alerts.alertInfo("Nelze editovat", "Nejdříve vyberte předmět, který chcete editovat.");
                    }
                });
                
            Button smazatPred = new Button("Smaž", new ImageView("/img/delete.png"));
                smazatPred.setOnAction(e -> {
                    try {
                        if (tableM.getSelectionModel().getSelectedItem() != null) {
                            int idMistnost = tableM.getSelectionModel().getSelectedItem().getId();
                            c.deleteMistnost(idMistnost);
                            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                        } else {
                            alerts.alertInfo("Nelze smazat", "Nejdříve vyberte předmět, který chcete smazat.");
                        }  
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Nepodařilo se smazat předmět. Ujistěte se, jestli "
                                + "není součástí některého studijního plánu.");
                    }
                });
        
        buttons3.getChildren().addAll(pridejPred, editujPred, smazatPred);

//Přiřazení viditelnosti objektů podle práva uživatele        
        switch (opravneni) {
            case 0:
                mistnosti.getChildren().addAll(l3, vyberFakultu, tableM, l6, buttons3);
                break;
            case 1:
                mistnosti.getChildren().addAll(l3, vyberFakultu, tableM);
                break;
            default:
                mistnosti.getChildren().addAll(l3, vyberFakultu, tableM);
                break;
        } 
        
        return mistnosti;    
    }

//Aktualizace dat v tabulce     
    private void updateTable(Controller c, Fakulta f){
        try {
            if (f != null){
                ObservableList<Mistnost> listM = 
                    FXCollections.observableArrayList(c.getMistnosti(f.getNazevF()));
                tableM.setItems(listM);
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
