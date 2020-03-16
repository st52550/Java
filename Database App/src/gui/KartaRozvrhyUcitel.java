package gui;

import data.AkRok;
import data.ViewRozvrh;
import data.ViewVyucujici;
import data.Vyucujici;
import dialogs.Alerts;
import dialogs.DialogRozvrhyUcitel;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author st52550
 */
public class KartaRozvrhyUcitel {
    private Alerts alerts = new Alerts();
    
    Label rokL = new Label("Ak. rok");
    private ComboBox<AkRok> vyberR = new ComboBox<>();
    
    private Label vyucujiciL = new Label("Vyučující");   
    private ComboBox<Vyucujici> vyberVyucujici = new ComboBox<>();
    ListView<ViewRozvrh> list = new ListView<>();
    
    private DialogRozvrhyUcitel dialogRozvrhyUcitel;
    
    private TableView<ViewRozvrh> tableRoz = new TableView();
    private TableColumn<ViewRozvrh, String> zkratka = new TableColumn<>("Zkrakta");
    private TableColumn<ViewRozvrh, String> nazev = new TableColumn<>("Název předmětu");
    private TableColumn<ViewRozvrh, String> zpusob = new TableColumn<>("Způsob výuky");
    private TableColumn<ViewRozvrh, String> rozsah = new TableColumn<>("Rozsah hodin");
    private TableColumn<ViewRozvrh, String> den = new TableColumn<>("Den");
    private TableColumn<ViewRozvrh, String> cas = new TableColumn<>("Čas");
    private TableColumn<ViewRozvrh, String> mistnost = new TableColumn<>("Místnost");
    private TableColumn<ViewRozvrh, String> vyucujici = new TableColumn<>("Vyučující");
    private TableColumn<ViewRozvrh, String> role = new TableColumn<>("Role");   
    private TableColumn<ViewRozvrh, String> obsazenost = new TableColumn<>("Max. obsazenost");
    private TableColumn<ViewRozvrh, String> platnostSlovy = new TableColumn<>("Platnost");
    
    private TableView<ViewRozvrh> tableRoz1 = new TableView();
    private TableColumn<ViewRozvrh, String> blok = new TableColumn<>("Bloková výuka");
    private TableColumn<ViewRozvrh, String> zkratka1 = new TableColumn<>("Zkrakta");
    private TableColumn<ViewRozvrh, String> nazev1 = new TableColumn<>("Název předmětu");
    private TableColumn<ViewRozvrh, String> zpusob1 = new TableColumn<>("Způsob výuky");
    private TableColumn<ViewRozvrh, String> rozsah1 = new TableColumn<>("Rozsah hodin");
    private TableColumn<ViewRozvrh, String> den1 = new TableColumn<>("Den");
    private TableColumn<ViewRozvrh, String> cas1 = new TableColumn<>("Čas");
    private TableColumn<ViewRozvrh, String> mistnost1 = new TableColumn<>("Místnost");
    private TableColumn<ViewRozvrh, String> vyucujici1 = new TableColumn<>("Vyučující");
    private TableColumn<ViewRozvrh, String> role1 = new TableColumn<>("Role");   
    private TableColumn<ViewRozvrh, String> obsazenost1 = new TableColumn<>("Max. obsazenost");
    private TableColumn<ViewRozvrh, String> platnostSlovy1 = new TableColumn<>("Platnost");
    private Label pozadavkyL = new Label("Požadavky od vyučujících");
    
    private int pondeliRow = 1;
    private int uteryRow = 0;
    private int stredaRow = 0;
    private int ctvrtekRow = 0;
    private int patekRow = 0;
    
    public Group getKartaRozvrhyUcitel(Controller c, int opravneni, String userEmail) throws SQLException{
//Tvorba GUI      
        Group group = new Group();

        VBox rozvrhy = new VBox(10);
            rozvrhy.setId("layout");
            rozvrhy.setPadding(new Insets(10, 10, 10, 10));
            rozvrhy.setPrefWidth(1900);
        
        group.getChildren().addAll(rozvrhy);
    
//Výběr parametrů pro rozvrhovou akci                 
        HBox rozvrhH = new HBox(10);
            rozvrhH.setAlignment(Pos.CENTER_LEFT);
            
            updateData(c);
            updateTable(c, userEmail);           
        
            rokL.setMinSize(50, Region.USE_PREF_SIZE);
            vyucujiciL.setMinSize(75, Region.USE_PREF_SIZE);
            
            Button zobrazRozvrh = new Button("Zobraz rozvrh");
            Button zpetRozvrh = new Button("Zpět k mému rozvrhu");        

//Konstrukce tabulky platných rozvrhových akcí            
        zkratka.setCellValueFactory(new PropertyValueFactory<>("zkratka"));
            zkratka.prefWidthProperty().setValue(90);
        nazev.setCellValueFactory(new PropertyValueFactory<>("nazev"));
            nazev.prefWidthProperty().setValue(250);
        zpusob.setCellValueFactory(new PropertyValueFactory<>("zpusob"));
            zpusob.prefWidthProperty().setValue(125); 
        rozsah.setCellValueFactory(new PropertyValueFactory<>("pocetH"));
            rozsah.prefWidthProperty().setValue(125);     
        den.setCellValueFactory(new PropertyValueFactory<>("den"));
            den.prefWidthProperty().setValue(100);
        cas.setCellValueFactory(new PropertyValueFactory<>("cas"));
            cas.prefWidthProperty().setValue(125);
        mistnost.setCellValueFactory(new PropertyValueFactory<>("mistnost"));
            mistnost.prefWidthProperty().setValue(90);
        vyucujici.setCellValueFactory(new PropertyValueFactory<>("vyucujici"));
            vyucujici.prefWidthProperty().setValue(250);        
        obsazenost.setCellValueFactory(new PropertyValueFactory<>("obsazenost"));
            obsazenost.prefWidthProperty().setValue(150);
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
            role.prefWidthProperty().setValue(125); 
        platnostSlovy.setCellValueFactory(new PropertyValueFactory<>("platnostSlovy")); 
            platnostSlovy.prefWidthProperty().setValue(100);

//Konstrukce tabulky neplatných rozvrhových akcí            
        blok.setCellValueFactory(new PropertyValueFactory<>("blokovaVyukaSlovy"));
            blok.prefWidthProperty().setValue(175);     
        zkratka1.setCellValueFactory(new PropertyValueFactory<>("zkratka"));
            zkratka1.prefWidthProperty().setValue(90);
        nazev1.setCellValueFactory(new PropertyValueFactory<>("nazev"));
            nazev1.prefWidthProperty().setValue(250);
        zpusob1.setCellValueFactory(new PropertyValueFactory<>("zpusob"));
            zpusob1.prefWidthProperty().setValue(125); 
        rozsah1.setCellValueFactory(new PropertyValueFactory<>("pocetH"));
            rozsah1.prefWidthProperty().setValue(125);     
        den1.setCellValueFactory(new PropertyValueFactory<>("den"));
            den1.prefWidthProperty().setValue(100);
        cas1.setCellValueFactory(new PropertyValueFactory<>("cas"));
            cas1.prefWidthProperty().setValue(125);
        mistnost1.setCellValueFactory(new PropertyValueFactory<>("mistnost"));
            mistnost1.prefWidthProperty().setValue(90);
        vyucujici1.setCellValueFactory(new PropertyValueFactory<>("vyucujici"));
            vyucujici1.prefWidthProperty().setValue(250);        
        obsazenost1.setCellValueFactory(new PropertyValueFactory<>("obsazenost"));
            obsazenost1.prefWidthProperty().setValue(150);
        role1.setCellValueFactory(new PropertyValueFactory<>("role"));
            role1.prefWidthProperty().setValue(125); 
        platnostSlovy1.setCellValueFactory(new PropertyValueFactory<>("platnostSlovy")); 
            platnostSlovy1.prefWidthProperty().setValue(100);    
            
        tableRoz.getColumns().addAll(zkratka, nazev, zpusob, rozsah, den, cas, mistnost, vyucujici,
                    role, obsazenost, platnostSlovy);
        tableRoz.setPrefHeight(250);
        
        pozadavkyL.setStyle("-fx-font-size: 20px"); 

//Přidání objektů na základě oprávnění        
        if (opravneni == 0) {
            tableRoz1.getColumns().addAll(blok, zkratka1, nazev1, zpusob1, rozsah1, den1, cas1, mistnost1, vyucujici1,
                    role1, obsazenost1, platnostSlovy1);
            tableRoz1.setPrefHeight(250);
            updateTable1(c);           
        }
        
        Label roz = new Label("Můj rozvrh");
            roz.setStyle("-fx-font-size: 20px");
            
            GridPane gridR = new GridPane();                              
                setGridRozvrh(gridR);
                
        updateGridPane(c, gridR, userEmail,0);        

//Tlačítko pro přidání, editaci či smazání rozvrhové akce         
        HBox rozH = new HBox(10); 
                
            Button pridatAkci = new Button("Přidat", new ImageView("/img/add.png"));  
                pridatAkci.setOnAction(e -> {
                    if (vyberR.getSelectionModel().getSelectedItem() != null &&
                        vyberVyucujici.getSelectionModel().getSelectedItem() != null) {
                            dialogRozvrhyUcitel = new DialogRozvrhyUcitel();
                            dialogRozvrhyUcitel.addRozvrh(c, vyberR.getSelectionModel().getSelectedItem().getRok(), opravneni, userEmail);
                            String email = getVyucujici(c);
                            updateTable(c, email);
                            updateGridPane(c, gridR, email,0);
                            updateBlok(c);
                    } else {
                        alerts.alertInfo("Oznámení", "Všechny parametry musí být vybrány.");
                    }        
                });                
            
            Button editAkce = new Button("Editovat", new ImageView("/img/edit.png"));               
                editAkce.setOnAction(e -> {                  
                        if (tableRoz.getSelectionModel().getSelectedItem() != null) {
                            ViewRozvrh rozvrhovaAkce = tableRoz.getSelectionModel().getSelectedItem();
                            if(opravneni == 1 && rozvrhovaAkce.getPlatnost() == 0){
                                dialogRozvrhyUcitel = new DialogRozvrhyUcitel();
                                dialogRozvrhyUcitel.editRozvrh(c, vyberR.getSelectionModel().getSelectedItem().getRok(), rozvrhovaAkce, opravneni, userEmail, 0);
                                String email = getVyucujici(c);
                                updateTable(c, email);
                                updateGridPane(c, gridR, email,0);
                                updateBlok(c);
                            } else if (opravneni == 0){
                                dialogRozvrhyUcitel = new DialogRozvrhyUcitel();
                                dialogRozvrhyUcitel.editRozvrh(c, vyberR.getSelectionModel().getSelectedItem().getRok(), rozvrhovaAkce, opravneni, userEmail, 0);
                                String email = getVyucujici(c);
                                updateTable(c, email);
                                updateGridPane(c, gridR, email,0);
                                updateBlok(c);
                            } else {
                                alerts.alertInfo("Oznámení", "Nemáte oprávnění editovat platné rozvrhové akce");
                            }    
                        } else {
                            alerts.alertInfo("Oznámení", "Nejdříve vyberte rozvrhovou akci z tabulky");
                        }                    
                });
            
            Button smazatAkci = new Button("Smazat", new ImageView("/img/delete.png"));
                smazatAkci.setOnAction(e -> {
                    if (tableRoz.getSelectionModel().getSelectedItem() != null) {
                        try {
                            int idRozvrhovaAkce = tableRoz.getSelectionModel().getSelectedItem().getId();
                            ViewRozvrh rozvrhovaAkce = tableRoz.getSelectionModel().getSelectedItem();
                            if(opravneni == 1 && rozvrhovaAkce.getPlatnost() == 0){
                                c.deleteRozvrhovaAkce(idRozvrhovaAkce);
                                String email = getVyucujici(c);
                                updateTable(c, email);
                                updateGridPane(c, gridR, email,0);
                                updateBlok(c);
                            } else if (opravneni == 0){
                                c.deleteRozvrhovaAkce(idRozvrhovaAkce);
                                String email = getVyucujici(c);
                                updateTable(c, email);
                                updateGridPane(c, gridR, email,0);
                                updateBlok(c);
                            } else {
                               alerts.alertInfo("Oznámení", "Nemáte oprávnění editovat platné rozvrhové akce"); 
                            }
                        } catch (SQLException ex) {
                            alerts.alertError("Chyba", "Nepodařilo se smazat rozvrhovou akci.");
                        }
                    }
                });
            
            rozH.getChildren().addAll(pridatAkci, editAkce, smazatAkci); 

//Tlačítko pro ověření či smazání rozvrhové akce             
        HBox rozH1 = new HBox(10);
        if(opravneni == 0){                                

            Button editAkce1 = new Button("Ověřit", new ImageView("/img/edit.png"));               
                editAkce1.setOnAction(e -> {                  
                        if (tableRoz1.getSelectionModel().getSelectedItem() != null) {
                            ViewRozvrh rozvrhovaAkce = tableRoz1.getSelectionModel().getSelectedItem();
                                dialogRozvrhyUcitel = new DialogRozvrhyUcitel();
                                dialogRozvrhyUcitel.editRozvrh(c, vyberR.getSelectionModel().getSelectedItem().getRok(), rozvrhovaAkce, opravneni, userEmail, 1);
                                
                                String email = getVyucujici(c);
                                    
                                updateTable(c, email);
                                updateTable1(c);
                                updateGridPane(c, gridR, email,0); 
                                updateBlok(c);
                        } else {
                            alerts.alertInfo("Oznámení", "Nejdříve vyberte rozvrhovou akci z tabulky");
                        }                    
                });

            Button smazatAkci1 = new Button("Smazat", new ImageView("/img/delete.png"));
                smazatAkci1.setOnAction(e -> {
                    if (tableRoz1.getSelectionModel().getSelectedItem() != null) {
                        try {
                            int idRozvrhovaAkce = tableRoz1.getSelectionModel().getSelectedItem().getId();
                            ViewRozvrh rozvrhovaAkce = tableRoz1.getSelectionModel().getSelectedItem();
                                c.deleteRozvrhovaAkce(idRozvrhovaAkce);
                                
                                String email = getVyucujici(c);
                                
                                updateTable(c, email);
                                updateTable1(c);
                                updateGridPane(c, gridR, email,0);
                                updateBlok(c);
                        } catch (SQLException ex) {
                            alerts.alertError("Chyba", "Nepodařilo se smazat rozvrhovou akci.");
                        }
                    }
                });

            rozH1.getChildren().addAll(editAkce1, smazatAkci1);
        }

//Tlačítko pro zobrazení rozvrhových akcí učitele        
        zobrazRozvrh.setOnAction(e -> {
            if (vyberR.getSelectionModel().getSelectedItem() != null &&
                vyberVyucujici.getSelectionModel().getSelectedItem() != null) {                   
                String email = getVyucujici(c);
                
                if(opravneni == 0 || opravneni == 1){
                    updateTable(c, email);
                    updateTable1(c);
                    updateGridPane(c, gridR, email,0);
                } else {
                    updateTablePlatneA(c, getVyucujici(c));
                    updateGridPane(c, gridR, getVyucujici(c), 1);
                }                   
                    updateBlok(c);
                    roz.setText("Rozvrh vyučujícího");               
            } else {
                alerts.alertInfo("Oznámení", "Všechny parametry musí být vybrány.");
            }
        }); 

//Tlačítko pro návrat na rozvrhové akce přihlášeného uživatele       
        zpetRozvrh.setOnAction(e -> {
            try {
                ObservableList<ViewVyucujici> listVyuc = FXCollections.observableArrayList(c.getViewVyucujici());
                Vyucujici vyuc = null;
                for(ViewVyucujici v : listVyuc){
                    if (v.getEmail().equals(userEmail)) {
                        vyuc = new Vyucujici(v.getIdVyucujici(), v.getTitulPred(), v.getPrijmeni(), v.getJmeno(), v.getTitulZa());
                    }
                }
                               
                vyberVyucujici.getSelectionModel().select(vyuc);
                
                updateTable(c, userEmail);
                updateTable1(c);
                updateGridPane(c, gridR, userEmail,0);
                updateBlok(c);
                roz.setText("Můj rozvrh");
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Chyba při návratu na rozvrh");
            }
        });
        
        Label blokovaL = new Label("Blokové výuky v daném roce");
            blokovaL.setStyle("-fx-font-size: 20px");
        
        if(opravneni != 0 && opravneni != 1){
            vyberVyucujici.getSelectionModel().selectFirst();
        }
        
        updateBlok(c);   

//Tlačítko pro přidání a smazání blokových výuk        
        Button nastaveni = new Button("Nastavení blokových výuk");
        nastaveni.setOnAction(e ->{
            dialogRozvrhyUcitel = new DialogRozvrhyUcitel();
            dialogRozvrhyUcitel.blokoveVyuky(c);
        });

//Tlačítko pro editaci a smazání blokové rozvrhové akce        
        HBox rozH2 = new HBox(10);
        if(opravneni == 0){                                

            Button editAkce2 = new Button("Editovat", new ImageView("/img/edit.png"));               
                editAkce2.setOnAction(e -> {                  
                        if (list.getSelectionModel().getSelectedItem() != null) {
                            ViewRozvrh rozvrhovaAkce = list.getSelectionModel().getSelectedItem();
                                dialogRozvrhyUcitel = new DialogRozvrhyUcitel();
                                dialogRozvrhyUcitel.editRozvrh(c, vyberR.getSelectionModel().getSelectedItem().getRok(), rozvrhovaAkce, opravneni, userEmail, 1);
                                
                                String email = getVyucujici(c);
                                    
                                updateTable(c, email);
                                updateTable1(c);
                                updateGridPane(c, gridR, email,0); 
                                updateBlok(c);
                        } else {
                            alerts.alertInfo("Oznámení", "Nejdříve vyberte blokovou výuku ze seznamu");
                        }                    
                });

            Button smazatAkci2 = new Button("Smazat", new ImageView("/img/delete.png"));
                smazatAkci2.setOnAction(e -> {
                    if (list.getSelectionModel().getSelectedItem() != null) {
                        try {
                            int idRozvrhovaAkce = list.getSelectionModel().getSelectedItem().getId();
                            ViewRozvrh rozvrhovaAkce = list.getSelectionModel().getSelectedItem();
                                c.deleteRozvrhovaAkce(idRozvrhovaAkce);
                                
                                String email = getVyucujici(c);
                                
                                updateTable(c, email);
                                updateTable1(c);
                                updateGridPane(c, gridR, email,0);
                                updateBlok(c);
                        } catch (SQLException ex) {
                            alerts.alertError("Chyba", "Nepodařilo se smazat rozvrhovou akci.");
                        }
                    }
                });

            rozH2.getChildren().addAll(editAkce2, smazatAkci2);
        }

//Přiřazení viditelnosti objektů podle práva uživatele        
        switch (opravneni) {
            case 0:
                rozvrhH.getChildren().addAll(rokL, vyberR, vyucujiciL, vyberVyucujici, zobrazRozvrh, zpetRozvrh);
                rozvrhy.getChildren().addAll(rozvrhH, tableRoz, rozH, pozadavkyL, tableRoz1, rozH1, roz, gridR, blokovaL, 
                        nastaveni, list, rozH2);
                break;
            case 1:
                rozvrhH.getChildren().addAll(rokL, vyberR, vyucujiciL, vyberVyucujici, zobrazRozvrh, zpetRozvrh);
                rozvrhy.getChildren().addAll(rozvrhH, tableRoz, roz, rozH, gridR, blokovaL, list);
                break;
            default:
                rozvrhH.getChildren().addAll(rokL, vyberR, vyucujiciL, vyberVyucujici, zobrazRozvrh);
                rozvrhy.getChildren().addAll(rozvrhH, tableRoz, roz, gridR, blokovaL, list);
                roz.setText("Rozvrh vyučujícího");
                vyberVyucujici.getSelectionModel().selectFirst();
                String email = getVyucujici(c);
                updateTablePlatneA(c, email);
                updateGridPane(c, gridR, email, 1);
                updateBlok(c);
                break;
        } 
                
        return group;
    }

//Získání emailu vybraného vyučujícího    
    private String getVyucujici(Controller c){
        ObservableList<ViewVyucujici> listVyuc;
        String email = null;
        try {
            listVyuc = FXCollections.observableArrayList(c.getViewVyucujici());

            for(ViewVyucujici v : listVyuc){
                if (v.getIdVyucujici() == vyberVyucujici.getSelectionModel().getSelectedItem().getId()) {
                    email = v.getEmail();
                }
            }
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při získání vyučujících.");
        }
        
        return email;
    }

//Tvroba grafického rozvrhu    
    private void setGridRozvrh(GridPane gridR){
        GridPane.setHgrow(gridR, Priority.ALWAYS);
        GridPane.setVgrow(gridR, Priority.ALWAYS);
        gridR.setStyle("-fx-background-color: #fcfcfc; -fx-padding: 2; -fx-hgap: 2; -fx-vgap: 2;");
        gridR.setSnapToPixel(false);
        gridR.setMinWidth(1640);
        gridR.setMaxWidth(1640); 
        gridR.setMinHeight(314);
        
        gridR.add(new StackPane(new Rectangle(50, 50, Color.rgb(252, 252, 252)), new Label("")), 0, 0);
        gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Út ")), 0, 2);
        gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" St ")), 0, 3);
        gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Čt ")), 0, 4);
        gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Pá ")), 0, 5); 
        
        setGridCol(gridR);
    }

//Tvorba časů v grafickém rozvrhu     
    private void setGridCol(GridPane gridR){
        gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Po ")), 0, 1);
        
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 7:00–8:00 ")), 1, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 8:00–9:00 ")), 2, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 9:00–10:00 ")), 3, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 10:00–11:00 ")), 4, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 11:00–12:00 ")), 5, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 12:00–13:00 ")), 6, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 13:00–14:00 ")), 7, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 14:00–15:00 ")), 8, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 15:00–16:00 ")), 9, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 16:00–17:00 ")), 10, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 17:00–18:00 ")), 11, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 18:00–19:00 ")), 12, 0);
        gridR.add(new StackPane(new Rectangle(120, 50, Color.LIGHTGREY), new Label(" 19:00–20:00 ")), 13, 0);  
    }

//Překleslení grafického rozvrhu    
    private void resetGridRozvrh(GridPane gridR, int den){
        setGridCol(gridR);
        
        switch(den){
            case 1 : pondeliRow++; //2                    
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.rgb(252, 252, 252)), new Label(" ")), 0, pondeliRow); // 2
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Út ")), 0, pondeliRow + 1); //3
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" St ")), 0, pondeliRow + 2); //4
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Čt ")), 0, pondeliRow + 3); //5
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Pá ")), 0, pondeliRow + 4); //6
                     break;
            case 2 : uteryRow++; //1
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Út ")), 0, pondeliRow + 1); // 3
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.rgb(252, 252, 252)), new Label(" ")), 0, pondeliRow + uteryRow + 1); //4
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" St ")), 0, pondeliRow + uteryRow + 2); //5
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Čt ")), 0, pondeliRow + uteryRow + 3); //6
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Pá ")), 0, pondeliRow + uteryRow + 4); //7
                     break;
            case 3 : stredaRow++; //1
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" St ")), 0, pondeliRow + uteryRow + 1 + 1); //4
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.rgb(252, 252, 252)), new Label(" ")), 0, pondeliRow + uteryRow + 1 + stredaRow + 1); //5
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Čt ")), 0, pondeliRow + uteryRow + 1 + stredaRow + 2); //6 
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Pá ")), 0, pondeliRow + uteryRow + 1 + stredaRow + 3); //7 
                     break;
            case 4 : ctvrtekRow++; //1
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Čt ")), 0, pondeliRow + uteryRow + 1 + stredaRow + 1 + 1);//5
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.rgb(252, 252, 252)), new Label(" ")), 0, pondeliRow + uteryRow + 1 + stredaRow + 1 + ctvrtekRow + 1);//6
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Pá ")), 0, pondeliRow + uteryRow + 1 + stredaRow + 1 + ctvrtekRow + 2);//7
                     break;
            case 5 : patekRow++; //1
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.LIGHTGREY), new Label(" Pá ")), 0, pondeliRow + uteryRow + 1 + stredaRow + 1 + ctvrtekRow + 1 + 1); //6
                     gridR.add(new StackPane(new Rectangle(50, 50, Color.rgb(252, 252, 252)), new Label(" ")), 0, pondeliRow + uteryRow + 1 + stredaRow + 1 + ctvrtekRow + 1 + patekRow + 1);//7
                     break;
        }
    }

//Aktualizace dat v grafickém rozvrhu    
    private void updateGridPane(Controller c, GridPane gridR, String email, int neregistrovany){
        pondeliRow = 1;
        uteryRow = 0;
        stredaRow = 0;
        ctvrtekRow = 0;
        patekRow = 0;
        gridR.getChildren().clear();
        setGridRozvrh(gridR);
         
        try {
            ObservableList<ViewRozvrh> listRozvrh;
            if (neregistrovany == 1) {
                listRozvrh = FXCollections.observableArrayList(
                    c.getViewRozvrhPlatneA(vyberR.getSelectionModel().getSelectedItem(), email));
            } else {
                listRozvrh = FXCollections.observableArrayList(
                    c.getViewRozvrh(vyberR.getSelectionModel().getSelectedItem(), email));
            }
           
            String predchoziUcitel = null;
            String testDen;
            int testCasOd = 0;
            int testCasDo = 0;            
            
            int i = 0;
            for (ViewRozvrh r : listRozvrh){                              
                testDen = r.getDen();
                testCasOd = Integer.parseInt(r.getCasOd());
                testCasDo = Integer.parseInt(r.getCasDo());
                String ucitel = r.getVyucujici(); 
                String vUcitel = null;
                
                int d1 = 0;
                switch(r.getDen()){
                    case "1" : d1 = pondeliRow; break;
                    case "2" : d1 = pondeliRow + 1; break;
                    case "3" :    
                               d1 = pondeliRow + uteryRow + 1 + 1; break;
                    case "4" : 
                               d1 = pondeliRow + uteryRow + 1 + stredaRow + 1 + 1; break;
                    case "5" :                      
                               d1 = pondeliRow + uteryRow + 1 + stredaRow + 1 + ctvrtekRow + 1 + 1; break;
                }
                
                int col1 = 0;
                switch(r.getCasOd()){
                    case "7": col1 = 1; break;
                    case "8": col1 = 2; break;
                    case "9": col1 = 3; break;
                    case "10": col1 = 4; break;
                    case "11": col1 = 5; break;
                    case "12": col1 = 6; break;
                    case "13": col1 = 7; break;
                    case "14": col1 = 8; break;
                    case "15": col1 = 9; break;
                    case "16": col1 = 10; break;
                    case "17": col1 = 11; break;
                    case "18": col1 = 12; break;
                    case "19": col1 = 13; break;
                }
                
                ViewRozvrh v = null;
                if (i + 1 < listRozvrh.size()) {
                    v = listRozvrh.get(i + 1);
                }                             
                
                if(v != null){
                    if(v.getDen().equals(testDen)){
                        if ((Integer.parseInt(v.getCasOd()) < testCasOd && Integer.parseInt(v.getCasDo()) > testCasDo) ||
                                (Integer.parseInt(v.getCasOd()) > testCasOd && Integer.parseInt(v.getCasOd()) < testCasDo) ||
                                (Integer.parseInt(v.getCasOd()) == testCasOd && Integer.parseInt(v.getCasDo()) == testCasDo ||
                                 Integer.parseInt(v.getCasOd()) == testCasOd && Integer.parseInt(v.getCasDo()) < testCasDo)) {
                            
                            vUcitel = v.getVyucujici();
                            if (!v.getVyucujici().equals(ucitel)) {
                                resetGridRozvrh(gridR, Integer.parseInt(v.getDen()));
                                
                                int d = 0;
                                switch(v.getDen()){
                                    case "1" : d = pondeliRow; break;
                                    case "2" : d = pondeliRow + uteryRow + 1; break;
                                    case "3" : d = pondeliRow + uteryRow + 1 + stredaRow + 1; break;
                                    case "4" : d = pondeliRow + uteryRow + 1 + stredaRow + 1 + ctvrtekRow + 1; break;
                                    case "5" : d = pondeliRow + uteryRow + 1 + stredaRow + 1 + ctvrtekRow + 1 + patekRow + 1; break;
                                }
                                
                                int col = 0;
                                switch(v.getCasOd()){
                                    case "7": col = 1; break;
                                    case "8": col = 2; break;
                                    case "9": col = 3; break;
                                    case "10": col = 4; break;
                                    case "11": col = 5; break;
                                    case "12": col = 6; break;
                                    case "13": col = 7; break;
                                    case "14": col = 8; break;
                                    case "15": col = 9; break;
                                    case "16": col = 10; break;
                                    case "17": col = 11; break;
                                    case "18": col = 12; break;
                                    case "19": col = 13; break;
                                }
                                
                                if (v.getPlatnost() == 1) {
                                    if (v.getZpusob().equals("přednáška")) {
                                        gridR.add(new StackPane(new Rectangle((120 * v.getPocetH()), 50, Color.LIGHTGREEN), new Label(v.getZkratka() + " – " + v.getMistnost() + "\n"
                                                + v.getVyucujici())), col, d, v.getPocetH(), 1);
                                    } else if (v.getZpusob().equals("cvičení")) {
                                        gridR.add(new StackPane(new Rectangle((120 * v.getPocetH()), 50, Color.LIGHTCORAL), new Label(v.getZkratka() + " – " + v.getMistnost() + "\n"
                                                + v.getVyucujici())), col, d, v.getPocetH(), 1);
                                    } else {
                                        gridR.add(new StackPane(new Rectangle((120 * v.getPocetH()), 50, Color.LIGHTCORAL), new Label(v.getZkratka() + " – " + v.getMistnost() + "\n"
                                                + v.getVyucujici())), col, d, v.getPocetH(), 1);
                                    }
                                } else {
                                    if (v.getZpusob().equals("přednáška")) {
                                        gridR.add(new StackPane(new Rectangle((120 * v.getPocetH()), 50, Color.LIGHTGRAY), new Label(v.getZkratka() + " – " + v.getMistnost() + "\n"
                                                + v.getVyucujici())), col, d, v.getPocetH(), 1);
                                    } else if (v.getZpusob().equals("cvičení")) {
                                        gridR.add(new StackPane(new Rectangle((120 * v.getPocetH()), 50, Color.LIGHTGRAY), new Label(v.getZkratka() + " – " + v.getMistnost() + "\n"
                                                + v.getVyucujici())), col, d, v.getPocetH(), 1);
                                    } else {
                                        gridR.add(new StackPane(new Rectangle((120 * v.getPocetH()), 50, Color.LIGHTGRAY), new Label(v.getZkratka() + " – " + v.getMistnost() + "\n"
                                                + v.getVyucujici())), col, d, v.getPocetH(), 1);
                                    }
                                }                                                              
                            }                         
                        }
                    }                                        
                }
                
                if(!r.getVyucujici().equals(predchoziUcitel)){
                    if (r.getPlatnost() == 1) {
                        if (r.getZpusob().equals("přednáška")) {
                            gridR.add(new StackPane(new Rectangle((120 * r.getPocetH()), 50, Color.LIGHTGREEN), new Label(r.getZkratka() + " – " + r.getMistnost() + "\n"
                                    + r.getVyucujici())), col1, d1, r.getPocetH(), 1);
                        } else if (r.getZpusob().equals("cvičení")) {
                            gridR.add(new StackPane(new Rectangle((120 * r.getPocetH()), 50, Color.LIGHTCORAL), new Label(r.getZkratka() + " – " + r.getMistnost() + "\n"
                                    + r.getVyucujici())), col1, d1, r.getPocetH(), 1);
                        } else {
                            gridR.add(new StackPane(new Rectangle((120 * r.getPocetH()), 50, Color.LIGHTCORAL), new Label(r.getZkratka() + " – " + r.getMistnost() + "\n"
                                    + r.getVyucujici())), col1, d1, r.getPocetH(), 1);
                        }
                    } else {
                        if (r.getZpusob().equals("přednáška")) {
                            gridR.add(new StackPane(new Rectangle((120 * r.getPocetH()), 50, Color.LIGHTGRAY), new Label(r.getZkratka() + " – " + r.getMistnost() + "\n"
                                    + r.getVyucujici())), col1, d1, r.getPocetH(), 1);
                        } else if (r.getZpusob().equals("cvičení")) {
                            gridR.add(new StackPane(new Rectangle((120 * r.getPocetH()), 50, Color.LIGHTGRAY), new Label(r.getZkratka() + " – " + r.getMistnost() + "\n"
                                    + r.getVyucujici())), col1, d1, r.getPocetH(), 1);
                        } else {
                            gridR.add(new StackPane(new Rectangle((120 * r.getPocetH()), 50, Color.LIGHTGRAY), new Label(r.getZkratka() + " – " + r.getMistnost() + "\n"
                                    + r.getVyucujici())), col1, d1, r.getPocetH(), 1);
                        }
                    }                   
                }  
                
                predchoziUcitel = vUcitel;
                i++;
            }
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat grafického rozvrhu.");
        }
    } 

//Aktualizace akademických let a vyučujících    
    public void updateData(Controller c){
        try {
            AkRok puvodni = null;
            if (vyberR.getSelectionModel().getSelectedItem() != null){
                puvodni = vyberR.getSelectionModel().getSelectedItem();
            }
            
            ObservableList<AkRok> listLet = FXCollections.observableArrayList(c.getRoky());
            vyberR.setItems(listLet);
            vyberR.getSelectionModel().select(puvodni);
            
            ObservableList<Vyucujici> listV = FXCollections.observableArrayList(c.getVyucujici());
            vyberVyucujici.setItems(listV);                       
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při načítání dat do seznamů.");
        }
    }

//Aktualizace dat v tabulce rozvrhových akcí    
    private void updateTable(Controller c, String email){
        try {
            ObservableList<ViewRozvrh> listRozvrh = FXCollections.observableArrayList(
                    c.getViewRozvrh(vyberR.getSelectionModel().getSelectedItem(), email));
            
            for (ViewRozvrh r : listRozvrh){
                switch(r.getDen()){
                    case "1": r.setDen("pondělí"); break;
                    case "2": r.setDen("úterý"); break;
                    case "3": r.setDen("středa"); break;
                    case "4": r.setDen("čtvrtek"); break;
                    case "5": r.setDen("pátek"); break;
                    default : r.setDen(""); break;
                }
            }
            
            if(vyberVyucujici.getSelectionModel().getSelectedItem() == null){
                ObservableList<ViewVyucujici> listVyuc = FXCollections.observableArrayList(c.getViewVyucujici());
                Vyucujici vyuc = null;
                for(ViewVyucujici v : listVyuc){
                    if (v.getEmail().equals(email)) {
                        vyuc = new Vyucujici(v.getIdVyucujici(), v.getTitulPred(), v.getPrijmeni(), v.getJmeno(), v.getTitulZa());
                    }
                }
                               
                vyberVyucujici.getSelectionModel().select(vyuc);
            }
            tableRoz.setItems(listRozvrh);
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat do tabulky.");
        }
    }

//Aktualizace dat v tabulce platných rozvrhových akcí    
    private void updateTablePlatneA(Controller c, String email){
        try {
            ObservableList<ViewRozvrh> listRozvrh = FXCollections.observableArrayList(
                    c.getViewRozvrhPlatneA(vyberR.getSelectionModel().getSelectedItem(), email));
            
            for (ViewRozvrh r : listRozvrh){
                switch(r.getDen()){
                    case "1": r.setDen("pondělí"); break;
                    case "2": r.setDen("úterý"); break;
                    case "3": r.setDen("středa"); break;
                    case "4": r.setDen("čtvrtek"); break;
                    case "5": r.setDen("pátek"); break;
                    default : r.setDen(""); break;
                }
            }
            
            if(vyberVyucujici.getSelectionModel().getSelectedItem() == null){
                ObservableList<ViewVyucujici> listVyuc = FXCollections.observableArrayList(c.getViewVyucujici());
                Vyucujici vyuc = null;
                for(ViewVyucujici v : listVyuc){
                    if (v.getEmail().equals(email)) {
                        vyuc = new Vyucujici(v.getIdVyucujici(), v.getTitulPred(), v.getPrijmeni(), v.getJmeno(), v.getTitulZa());
                    }
                }
                               
                vyberVyucujici.getSelectionModel().select(vyuc);
            }
            tableRoz.setItems(listRozvrh);
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat do tabulky.");
        }
    }

//Aktualizace dat v tabulce neplatných rozvrhových akcí    
    private void updateTable1(Controller c){
        try {
            ObservableList<ViewRozvrh> listRozvrh = FXCollections.observableArrayList(
                    c.getViewRozvrhNeplatneA(vyberR.getSelectionModel().getSelectedItem()));
            
            for (ViewRozvrh r : listRozvrh){
                switch(r.getDen()){
                    case "1": r.setDen("pondělí"); break;
                    case "2": r.setDen("úterý"); break;
                    case "3": r.setDen("středa"); break;
                    case "4": r.setDen("čtvrtek"); break;
                    case "5": r.setDen("pátek"); break;
                    default : r.setDen(""); break;
                }
            }
            
            tableRoz1.setItems(listRozvrh);
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat do tabulky.");
        }
    }

//Aktualizace dat v seznamu blokových výuk    
    private void updateBlok(Controller c){
        try {
            ObservableList<ViewRozvrh> listBlok = FXCollections.observableArrayList(
                    c.getViewRozvrhBlokoveA(vyberR.getSelectionModel().getSelectedItem(), getVyucujici(c)));
                     
            list.setItems(listBlok);
            list.setMinHeight(200);
            list.setMaxHeight(200);
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat do seznamu.");
        }
    }
}


