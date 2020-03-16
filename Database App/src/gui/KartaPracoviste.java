package gui;

import data.Fakulta;
import data.Katedra;
import data.ViewKatedry;
import dialogs.Alerts;
import dialogs.DialogPracoviste;
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
public class KartaPracoviste {
    private TableView<ViewKatedry> tableKat = new TableView();
    private TableColumn<ViewKatedry, String> zkratkaK = new TableColumn<>("Zkrakta katedry");
    private TableColumn<ViewKatedry, String> nazevK = new TableColumn<>("Název katedry");
    private TableColumn<ViewKatedry, String> zkratkaF = new TableColumn<>("Zkratka fakulty");
    private TableColumn<ViewKatedry, String> nazevF = new TableColumn<>("Název fakulty");
    private TableColumn<ViewKatedry, Integer> telefonK = new TableColumn<>("Telefon");
    private TableColumn<ViewKatedry, Integer> mobilK = new TableColumn<>("Mobil");
    private TableColumn<ViewKatedry, String> emailK = new TableColumn<>("E-mail");
    
    private ComboBox<String> katedraS1 = new ComboBox<>();
    private ComboBox<Fakulta> vyberF = new ComboBox<>();
    private ObservableList<Fakulta> listFakult;
    private ObservableList<String> katedryList = FXCollections.observableArrayList();
    private ComboBox<String> katedraS = new ComboBox<>(katedryList);
    private DialogPracoviste dialogPracoviste;
    private Alerts alerts = new Alerts();
    
    public VBox getKartaPracovist(Controller c, int opravneni) throws SQLException{
//Tvroba GUI        
        VBox pracoviste = new VBox(10);
            pracoviste.setId("layout");
            pracoviste.setPadding(new Insets(10, 10, 10, 10));
            
        Label l3 = new Label("Evidence pracovišť");
            l3.setStyle("-fx-font-size: 20px");
            
        HBox vyberFakultu = new HBox(10);
            vyberFakultu.setAlignment(Pos.CENTER_LEFT);
            Label nazevFakulty = new Label("Fakulta: ");
        vyberFakultu.getChildren().addAll(nazevFakulty, vyberF);
        
        updateFakulty(c);
        vyberF.getSelectionModel().selectFirst();
            
        vyberF.setOnAction(e -> {
            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
        });    

//Konstrukce tabulky pracovišť        
        zkratkaK.setCellValueFactory(new PropertyValueFactory<>("zkratkaK"));
            zkratkaK.prefWidthProperty().setValue(150);
        nazevK.setCellValueFactory(new PropertyValueFactory<>("nazevK"));
            nazevK.prefWidthProperty().setValue(300);
        zkratkaF.setCellValueFactory(new PropertyValueFactory<>("zkratkaF"));
            zkratkaF.prefWidthProperty().setValue(150);
        nazevF.setCellValueFactory(new PropertyValueFactory<>("nazevF"));
            nazevF.prefWidthProperty().setValue(350);
        telefonK.setCellValueFactory(new PropertyValueFactory<>("telefon"));
            telefonK.prefWidthProperty().setValue(125);
        mobilK.setCellValueFactory(new PropertyValueFactory<>("mobil"));
            mobilK.prefWidthProperty().setValue(125);
        emailK.setCellValueFactory(new PropertyValueFactory<>("email"));
            emailK.prefWidthProperty().setValue(300);

        tableKat.getColumns().addAll(zkratkaK, nazevK, zkratkaF, nazevF, 
                telefonK, mobilK, emailK);
        updateTable(c, vyberF.getSelectionModel().getSelectedItem());
        
        katedraS.setPromptText("Vyberte katedru");
        updateKatedry(c);

        Label l4 = new Label("Přidat, editovat či odebrat katedru");
            l4.setStyle("-fx-font-size: 20px");

//Tlačítko pro přidání, editaci či smazání katedry
//Přidej a Edituj je realizováno pomocí samostatných dialogů            
        HBox buttons2 = new HBox(10);
        
            Button pridejK = new Button("Přidej", new ImageView("/img/add.png")); 
                pridejK.setOnAction(e -> {    
                    dialogPracoviste = new DialogPracoviste();
                    dialogPracoviste.addKatedra(c);
                    updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                    updateKatedry(c);
                });
                
            Button editujK = new Button("Edituj", new ImageView("/img/edit.png"));
                editujK.setOnAction(e -> {
                    ViewKatedry vybranaKatedra = tableKat.getSelectionModel().getSelectedItem();
                    if (vybranaKatedra != null) {
                        dialogPracoviste = new DialogPracoviste();
                        dialogPracoviste.editKatedra(c, vybranaKatedra);
                        updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                        updateKatedry(c);
                    } else {
                        alerts.alertInfo("Nelze editovat", "Nejdříve vyberte katedru, kterého chcete editovat.");
                    }
                });
                
            Button smazatK = new Button("Smaž", new ImageView("/img/delete.png"));
                smazatK.setOnAction(e -> {
                    try {
                        if (tableKat.getSelectionModel().getSelectedItem() != null) {                                                    
                            int idKatedry = tableKat.getSelectionModel().getSelectedItem().getId();
                            c.deleteKatedra(idKatedry);
                            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                            updateKatedry(c);
                        } else {
                            alerts.alertInfo("Nelze smazat", "Nejdříve vyberte katedru, kterou chcete smazat.");
                        }    
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Nepodařilo se smazat katedru. Ujistěte se, jestli"
                                + "nemá přiřazeny nějaké předměty.");
                    }
                });
            
        Label l41 = new Label("Přidat, editovat či odebrat fakultu");
            l41.setStyle("-fx-font-size: 20px");
            
//Tlačítko pro přidání, editaci či smazání fakulty
//Přidej a Edituj je realizováno pomocí samostatných dialogů 
        HBox buttons2a = new HBox(10); 
        
            Button pridejF = new Button("Přidej", new ImageView("/img/add.png")); 
                pridejF.setOnAction(e -> {  
                    dialogPracoviste = new DialogPracoviste();
                    dialogPracoviste.addFakulta(c);
                    updateFakulty(c);
                });
                
            Button editujF = new Button("Edituj", new ImageView("/img/edit.png"));
                editujF.setOnAction(e -> {
                    Fakulta vybranaFakulta = vyberF.getSelectionModel().getSelectedItem();
                    if (vybranaFakulta != null) {
                        dialogPracoviste = new DialogPracoviste();
                        dialogPracoviste.editFakulta(c, vybranaFakulta);
                        updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                        updateFakulty(c);
                    } else {
                        alerts.alertInfo("Nelze editovat", "Nejdříve vyberte fakultu, kterou chcete editovat.");
                    }
                });
                
            Button smazatF = new Button("Smaž", new ImageView("/img/delete.png"));
                smazatF.setOnAction(e -> {
                    try {
                        if (vyberF.getSelectionModel().getSelectedItem() != null) {                                                    
                            Fakulta vybranaFakulta = vyberF.getSelectionModel().getSelectedItem();
                            c.deleteFakulta(vybranaFakulta.getNazevF(), vybranaFakulta.getEmail());
                            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                            updateKatedry(c);
                            updateFakulty(c);
                        } else {
                            alerts.alertInfo("Nelze smazat", "Nejdříve vyberte fakultu, kterou chcete smazat.");
                        }   
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Nepodařilo se smazat fakultu. Ujistěte se, jestli "
                                + "neobsahuje nějakou katedru.");
                    }
                });
                
//////////Lukas
//Přiřazení viditelnosti objektů podle práva uživatele
        switch (opravneni) {
            case 0:
                buttons2.getChildren().addAll(pridejK,editujK,smazatK);
               buttons2a.getChildren().addAll(pridejF,editujF,smazatF);            
               pracoviste.getChildren().addAll(l3, vyberFakultu, tableKat, l4, buttons2, l41, buttons2a);
                break;
            case 1:            
               pracoviste.getChildren().addAll(l3, vyberFakultu, tableKat);
                break;
            default:          
               pracoviste.getChildren().addAll(l3, vyberFakultu, tableKat);
                break;
        }                
        return pracoviste;
    }

//Aktualizace dat v tabulce    
    private void updateTable(Controller c, Fakulta f){
        try {
            if (f != null){
                ObservableList<ViewKatedry> listKateder =
                    FXCollections.observableArrayList(c.getViewKatedry(f));       
                tableKat.setItems(listKateder);
            }
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat do tabulky.");
        }
    }

//Aktualizace kateder    
    private void updateKatedry(Controller c){
        try {
            for(Katedra kat:c.getKatedry()){katedryList.add(kat.getNazevK());}
            katedraS.setItems(katedryList);
            katedraS1.setItems(katedryList);
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při načítání seznamu kateder.");
        }
    }
 
//Aktualizace fakult     
    private void updateFakulty(Controller c){
        try {
            listFakult = FXCollections.observableArrayList(c.getFakulty());
            vyberF.setItems(listFakult);
            vyberF.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při načítání seznamu fakult.");
        }
    }
}
