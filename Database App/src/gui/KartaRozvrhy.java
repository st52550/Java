package gui;

import data.AkRok;
import data.Fakulta;
import data.Predmet;
import data.ViewObor;
import data.ViewPlan;
import data.ViewRozvrh;
import dialogs.Alerts;
import dialogs.DialogRozvrhy;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
public class KartaRozvrhy {
    private Alerts alerts = new Alerts();
    
    private ComboBox<Fakulta> vyberF = new ComboBox<>();
    private ObservableList<Fakulta> listFakult;
    private ComboBox<AkRok> vyberR = new ComboBox<>();
    private ComboBox<Predmet> vyberPredmet = new ComboBox<>();
    private ObservableList<Predmet> listPredmet;  
    private ComboBox<ViewObor> vyberObor = new ComboBox<>();  
    private ObservableList<ViewObor> listObor;
    
    private ObservableList<ViewPlan> listPlan; 
    private DialogRozvrhy dialogRozvrhy;
    
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
    
    private int pondeliRow = 1;
    private int uteryRow = 0;
    private int stredaRow = 0;
    private int ctvrtekRow = 0;
    private int patekRow = 0;
    
    private Label pocetP = new Label("Přednášky: ");
    private Label pocetCS = new Label("Cvičení/Semináře: ");
    
    private int pocetPrednasek;
    private int pocetCviceni;
    private int odhadStudentu;
    
    public Group getKartaRozvrhy(Controller c, int opravneni) throws SQLException{
//Tvorba GUI     
        Group group = new Group();

        VBox rozvrhy = new VBox(10);
            rozvrhy.setId("layout");
            rozvrhy.setPadding(new Insets(10, 10, 10, 10));
            rozvrhy.setPrefWidth(1900);
        
        group.getChildren().addAll(rozvrhy);
    
//Výběr parametrů pro rozvrhovou akci     
        Label l7 = new Label("Rozvrhové akce");
            l7.setStyle("-fx-font-size: 20px");           
              
        HBox rozvrhH = new HBox(10);
            rozvrhH.setAlignment(Pos.CENTER_LEFT);
            Label rokL = new Label("Ak. rok");
                rokL.setMinSize(50, Region.USE_PREF_SIZE);

            updateData(c);

            Label fakultaL = new Label("Fakulta"); 
                fakultaL.setMinSize(60, Region.USE_PREF_SIZE);
            listFakult = FXCollections.observableArrayList(c.getFakulty());
            vyberF.setItems(listFakult);                                          

//Při výběru fakulty se zobrazí seznam studijních oborů            
            vyberF.setOnAction(e -> {
                try {
                    if(vyberF.getSelectionModel().getSelectedItem() != null){
                        listObor = FXCollections.observableArrayList(c.getViewObory(
                                vyberF.getSelectionModel().getSelectedItem()));
                        vyberObor.setItems(listObor);
                    }
                } catch (SQLException ex) {
                    alerts.alertError("Chyba", "Chyba při načítání předmětů.");
                }                    
            });                                             
            
            Label oborL = new Label("Obor");
                oborL.setMinSize(40, Region.USE_PREF_SIZE);

//Při výběru studijního oboru se zobrazí seznam předmětů daného oboru                
            vyberObor.setOnAction(e -> {
                try {
                    if(vyberObor.getSelectionModel().getSelectedItem() != null){
                        listPredmet = FXCollections.observableArrayList(c.getPredmetObor(
                                vyberObor.getSelectionModel().getSelectedItem().getNazev()));
                        vyberPredmet.setItems(listPredmet);
                    }
                } catch (SQLException ex) {
                    alerts.alertError("Chyba", "Chyba při načítání předmětů.");
                }                    
            });
            
            Label predmetL = new Label("Předmět");  
                predmetL .setMinSize(70, Region.USE_PREF_SIZE);
            
            Button zobrazRozvrh = new Button("Zobraz rozvrh");
        rozvrhH.getChildren().addAll(rokL, vyberR, fakultaL, vyberF, oborL, vyberObor, 
                predmetL, vyberPredmet, zobrazRozvrh);         
        
        HBox kontrola = new HBox(75);
            kontrola.setAlignment(Pos.CENTER_LEFT);
            Label pocet = new Label("Odhadovaný počet studentů: ");  
                pocet.setStyle("-fx-font-size: 20px");
                pocetP.setStyle("-fx-font-size: 20px");
                pocetCS.setStyle("-fx-font-size: 20px");
        kontrola.getChildren().addAll(pocet, pocetP, pocetCS);

//Konstrukce tabulky rozvrhových akcí        
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
            
        tableRoz.getColumns().addAll(zkratka, nazev, zpusob, rozsah, den, cas, mistnost, vyucujici,
                    role, obsazenost, platnostSlovy);
        tableRoz.setPrefHeight(350);

            Label roz = new Label("Rozvrh předmětu");
            roz.setStyle("-fx-font-size: 20px");
            
            GridPane gridR = new GridPane();                              
                setGridRozvrh(gridR);

//Tlačítko pro přidání, editaci či smazání rozvrhové akce               
            HBox rozH = new HBox(10); 
                
            Button pridatAkci = new Button("Přidat", new ImageView("/img/add.png"));  
                pridatAkci.setOnAction(e -> {
                    if (vyberR.getSelectionModel().getSelectedItem() != null &&
                        vyberF.getSelectionModel().getSelectedItem() != null &&
                        vyberObor.getSelectionModel().getSelectedItem() != null &&
                        vyberPredmet.getSelectionModel().getSelectedItem() != null) {
                            dialogRozvrhy = new DialogRozvrhy();
                            dialogRozvrhy.addRozvrh(c, vyberR.getSelectionModel().getSelectedItem().getRok(), 
                                vyberObor.getSelectionModel().getSelectedItem().getNazev(), listPlan, "",
                                vyberF.getSelectionModel().getSelectedItem().getNazevF());
                            updateTable(c);
                            updateGridPane(c, gridR);
                    } else {
                        alerts.alertInfo("Oznámení", "Všechny parametry musí být vybrány.");
                    }        
                });                
            
            Button editAkce = new Button("Editovat", new ImageView("/img/edit.png"));
                editAkce.setOnAction(e -> {
                    if (tableRoz.getSelectionModel().getSelectedItem() != null) {
                        ViewRozvrh rozvrhovaAkce = tableRoz.getSelectionModel().getSelectedItem();
                        dialogRozvrhy = new DialogRozvrhy();
                        dialogRozvrhy.editRozvrh(c, vyberR.getSelectionModel().getSelectedItem().getRok()
                                ,vyberObor.getSelectionModel().getSelectedItem().getNazev(), listPlan,
                                rozvrhovaAkce, "", vyberF.getSelectionModel().getSelectedItem().getNazevF());
                        updateTable(c);
                        updateGridPane(c, gridR);
                    } else {
                        alerts.alertInfo("Oznámení", "Nejdříve vyberte rozvrhovou akci z tabulky");
                    }
                });
            
            Button smazatAkci = new Button("Smazat", new ImageView("/img/delete.png"));
                smazatAkci.setOnAction(e -> {
                    if (tableRoz.getSelectionModel().getSelectedItem() != null) {
                        try {
                            int idRozvrhovaAkce = tableRoz.getSelectionModel().getSelectedItem().getId();
                            c.deleteRozvrhovaAkce(idRozvrhovaAkce);
                            updateTable(c);
                            updateGridPane(c, gridR);
                        } catch (SQLException ex) {
                            alerts.alertError("Chyba", "Nepodařilo se smazat rozvrhovou akci.");
                        }
                    }
                });
            
            rozH.getChildren().addAll(pridatAkci, editAkce, smazatAkci);
            
//Tlačítko pro zobrazení rozvrhových akcí
            zobrazRozvrh.setOnAction(e -> {
                if (vyberR.getSelectionModel().getSelectedItem() != null &&
                    vyberF.getSelectionModel().getSelectedItem() != null &&
                    vyberObor.getSelectionModel().getSelectedItem() != null &&
                    vyberPredmet.getSelectionModel().getSelectedItem() != null) {
                    try {
                        listPlan = FXCollections.observableArrayList(c.getViewPlan(vyberR.getSelectionModel().getSelectedItem(), 
                                vyberF.getSelectionModel().getSelectedItem(), vyberObor.getSelectionModel().getSelectedItem(),
                                vyberPredmet.getSelectionModel().getSelectedItem()));
                        odhadStudentu = listPlan.get(0).getOdhadS();
                        pocet.setText("Odhadovaný počet studentů: " + odhadStudentu);
                       
                        updateTable(c);
                        updateGridPane(c, gridR);
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Předmět není ve studijním plánu.");
                    } catch (IndexOutOfBoundsException ex1) {
                        alerts.alertInfo("Upozornění", "Daný předmět není ve studijním plánu.");
                    }                
                } else {
                    alerts.alertInfo("Oznámení", "Všechny parametry musí být vybrány.");
                }
            }); 

//Přiřazení viditelnosti objektů podle práva uživatele            
            switch (opravneni) {
                case 0:
                    rozvrhy.getChildren().addAll(l7, rozvrhH, kontrola, tableRoz, rozH, roz, gridR);
                    break;
                case 1:
                    rozvrhy.getChildren().addAll(l7, rozvrhH, tableRoz, roz, gridR);
                    break;
                default:
                    rozvrhy.getChildren().addAll(l7, rozvrhH, tableRoz, roz, gridR);
                    break;
            } 

        return group;
    }
 
//Tvorba grafického rozvrhu    
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
    private void updateGridPane(Controller c, GridPane gridR){
        pondeliRow = 1;
        uteryRow = 0;
        stredaRow = 0;
        ctvrtekRow = 0;
        patekRow = 0;
        gridR.getChildren().clear();
        setGridRozvrh(gridR);  
         
        try {
            ObservableList<ViewRozvrh> listRozvrh = FXCollections.observableArrayList(
                    c.getViewRozvrhPlatneA(vyberR.getSelectionModel().getSelectedItem(), 
                            vyberObor.getSelectionModel().getSelectedItem(), 
                            vyberPredmet.getSelectionModel().getSelectedItem()));
           
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
                            }                         
                        }
                    }                                        
                }
                
                if(!r.getVyucujici().equals(predchoziUcitel)){
                    if (r.getZpusob().equals("přednáška")) {  
                        gridR.add(new StackPane(new Rectangle((120 * r.getPocetH()), 50, Color.LIGHTGREEN), new Label(r.getZkratka() + " – " + r.getMistnost() + "\n" + 
                                r.getVyucujici())), col1, d1, r.getPocetH(), 1);
                        } else if (r.getZpusob().equals("cvičení")) {
                        gridR.add(new StackPane(new Rectangle((120 * r.getPocetH()), 50, Color.LIGHTCORAL), new Label(r.getZkratka() + " – " + r.getMistnost() + "\n" + 
                                r.getVyucujici())), col1, d1, r.getPocetH(), 1);
                        } else {
                        gridR.add(new StackPane(new Rectangle((120 * r.getPocetH()), 50, Color.LIGHTCORAL), new Label(r.getZkratka() + " – " + r.getMistnost() + "\n" + 
                                r.getVyucujici())), col1, d1, r.getPocetH(), 1);
                        }
                    }  
                
                predchoziUcitel = vUcitel;
                i++;
            }
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat grafického rozvrhu.");
        }
    } 

//Aktualizace akademických let a fakult    
    public void updateData(Controller c){
        try {
            AkRok puvodni = null;
            if (vyberR.getSelectionModel().getSelectedItem() != null){
                puvodni = vyberR.getSelectionModel().getSelectedItem();
            }
            ObservableList<AkRok> listLet = FXCollections.observableArrayList(c.getRoky());
            vyberR.setItems(listLet);
            vyberR.getSelectionModel().select(puvodni);
            
            Fakulta puvodniF = null;
            if (vyberF.getSelectionModel().getSelectedItem() != null){
                puvodniF = vyberF.getSelectionModel().getSelectedItem();
            }
            
            ViewObor puvodniO = null;
            if (vyberObor.getSelectionModel().getSelectedItem() != null) {
                puvodniO = vyberObor.getSelectionModel().getSelectedItem();               
            }
            
            Predmet puvodniP = null;
            if (vyberPredmet.getSelectionModel().getSelectedItem() != null) {
                puvodniP = vyberPredmet.getSelectionModel().getSelectedItem();               
            }
            
            listFakult = FXCollections.observableArrayList(c.getFakulty());
            vyberF.setItems(listFakult);
            vyberF.getSelectionModel().select(puvodniF);  
            
            vyberObor.getSelectionModel().select(puvodniO);
            vyberPredmet.getSelectionModel().select(puvodniP);
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při načítání dat do seznamů.");
        }
    }

//Aktualizace dat v tabulce    
    private void updateTable(Controller c){
        try {
            pocetPrednasek = 0;
            pocetCviceni = 0;
            
            ObservableList<ViewRozvrh> listRozvrh = FXCollections.observableArrayList(
                    c.getViewRozvrhPlatneA(vyberR.getSelectionModel().getSelectedItem(), 
                            vyberObor.getSelectionModel().getSelectedItem(), 
                            vyberPredmet.getSelectionModel().getSelectedItem()));
            
            for (ViewRozvrh r : listRozvrh){
                switch(r.getDen()){
                    case "1": r.setDen("pondělí"); break;
                    case "2": r.setDen("úterý"); break;
                    case "3": r.setDen("středa"); break;
                    case "4": r.setDen("čtvrtek"); break;
                    case "5": r.setDen("pátek"); break;
                    default : r.setDen(""); break;
                }
                
                if (r.getZpusob().equals("přednáška")) {
                    pocetPrednasek += r.getObsazenost();
                } else {
                    pocetCviceni += r.getObsazenost();
                }
            }
            
            tableRoz.setItems(listRozvrh);
            updateCount();
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat do tabulky.");
        }
    }

//Aktualizace počtu vytvořených přednášek, cvičení či seminářů    
    private void updateCount(){
        if (pocetPrednasek >= odhadStudentu) {
            pocetP.setText("Přednášky: " + pocetPrednasek);
            pocetP.setGraphic(new ImageView("/img/yes.png"));
        } else {
            pocetP.setText("Přednášky: " + pocetPrednasek + " (Zbývá: " + (odhadStudentu-pocetPrednasek) + ")");
            pocetP.setGraphic(new ImageView("/img/no.png"));
        }
        
        if (pocetCviceni >= odhadStudentu){
            pocetCS.setText("Cvičení/Semináře: " + pocetCviceni);
            pocetCS.setGraphic(new ImageView("/img/yes.png")); 
        } else {
            pocetCS.setText("Cvičení/Semináře: " + pocetCviceni + " (Zbývá: " + (odhadStudentu-pocetCviceni) + ")");
            pocetCS.setGraphic(new ImageView("/img/no.png"));
        }
    }
}
