package gui;

import dialogs.Alerts;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author st52550
 */
public class Run extends Application {
    
//Inicializace
    private Controller c = new Controller();
    private KartaVyucujici kartaVyucujici;
    private KartaPracoviste kartaPracoviste;
    private KartaPredmety kartaPredmety;
    private KartaStudijniObory kartaStudijniObory;
    private KartaStudijniPlany kartaStudijniPlany;
    private KartaRozvrhy kartaRozvrhy;
    private KartaRozvrhyUcitel kartaRozvrhyUcitel;
    private KartaPrihlaseni kartaPrihlaseni;
    private KartaMistnosti kartaMistnosti;
    private Alerts alerts = new Alerts();
    
    //////////////////Lukas
    private int opravneni;
    private String username;
    
    @Override
    public void start(Stage loginStage) {
        loginStage.getIcons().add(new Image(Run.class.getResourceAsStream("/img/logo.png")));
        
// Okénko pro přihlášení do databáze
        GridPane grid = new GridPane();
            grid.setId("login-grid");
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text title = new Text("Přihlášení do");
            title.setId("title");
        Text title1 = new Text("fei-sql1.upceucebny.cz");
         
        grid.add(title, 0, 0, 2, 1);
        grid.add(title1, 0, 1, 2, 2);

        Label uzivatel = new Label("Jméno:");
        grid.add(uzivatel, 0, 4);

        TextField uzivatelText = new TextField();
            uzivatelText.setText("C##ST52550");
        grid.add(uzivatelText, 1, 4);

        Label heslo = new Label("Heslo:");           
        grid.add(heslo, 0, 5);

        PasswordField hesloBox = new PasswordField();
            hesloBox.setText("semestralkaPL");
        grid.add(hesloBox, 1, 5);
        
        Button btn = new Button("Přihlásit se");
            
            btn.setOnAction(e -> {
                try {
                    if (c.login(uzivatelText.getText(), hesloBox.getText())) {                                          
                        loginStage.close();
                        loginUser();                                    
                    } else {
                        title.setText("Přihlšení selhalo.");
                    }
                } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Přihlašovací údaje nejsou správné.");
                }    
            });
        
        HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 7);
        
        Scene scene = new Scene(grid, 350, 275);
        scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
        
        loginStage.setTitle("Je vyžadováno přihlášení");
        loginStage.setScene(scene);
        loginStage.show();
    }
    
    public void loginUser(){
        Stage loginUserStage = new Stage();
            loginUserStage.getIcons().add(new Image(Run.class.getResourceAsStream("/img/logo.png")));
            
        GridPane grid = new GridPane();
            grid.setId("login-grid");
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));    
        
        Text title = new Text("Přihlášení do");
            title.setId("title");
        Text title1 = new Text("systému vysoké školy");
         
        grid.add(title, 0, 0, 2, 1);
        grid.add(title1, 0, 1, 2, 2);

        Label uzivatel = new Label("Uživatel:");
        grid.add(uzivatel, 0, 4);

        TextField uzivatelText = new TextField();
        uzivatelText.setText("tomas.hudec@upce.cz");
        grid.add(uzivatelText, 1, 4);

        Label heslo = new Label("Heslo:");
        grid.add(heslo, 0, 5);

        PasswordField hesloBox = new PasswordField();
        hesloBox.setText("123456");
        grid.add(hesloBox, 1, 5);
        
        Button btn = new Button("Přihlásit se");
            
        ///////////Lukas
        Button btn2 = new Button("Pokračovat jako nepřihlášený uživatel");
            btn2.setId("pokracovat");
        btn.setOnAction(e -> {
            try {
                // System.out.println(uzivatelText.getText()+" "+ hesloBox.getText());                
                opravneni = c.prihlaseniUzivatele(uzivatelText.getText(), hesloBox.getText());
                //System.out.println(opravneni);
                if (opravneni != 99) {
                    username = uzivatelText.getText();
                    loginUserStage.close();
                    mainApp();
                } else {
                    title.setText("Přihlšení selhalo.");
                }
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Přihlašovací údaje nejsou správné.");
            }

        });

        btn2.setOnAction(e -> {
            try {
               
                opravneni = 3;
                loginUserStage.close();
                mainApp();

            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Přihlašovací údaje nejsou správné.");
            }

        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        ///////////////Lukas
        //Label neprihlaseny = new Label("Pokračovat jako nepřihlášený uživatel");
        VBox hbBtn2 = new VBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_LEFT);
        hbBtn2.getChildren().addAll(btn2);
        grid.add(hbBtn, 1, 7);
        grid.add(hbBtn2, 0, 8, 2, 1);
        
        Scene scene = new Scene(grid, 1100, 800);
        scene.getStylesheets().add(getClass().getResource("MainStyle.css")
               .toExternalForm());

        loginUserStage.setTitle("Přihlášení do systému");                      
        loginUserStage.setScene(scene);
        loginUserStage.show();
    }
    
    private HBox getUzivatel(){
        HBox uzivatel = new HBox();
            uzivatel.setId("uzivatel");
            Label user = new Label("");
                user.setStyle("-fx-text-fill: #ffffff");
                
            switch(opravneni){
                case 0: user.setText("Administrátor – " + username); break;
                case 1: user.setText("Učitel – " + username); break;
                default: user.setText("Neregistrovaný uživatel");
            }
        uzivatel.getChildren().add(user);
        
        return uzivatel;
    }
    
    public void mainApp() throws SQLException{
        Stage mainStage = new Stage();
            mainStage.getIcons().add(new Image(Run.class.getResourceAsStream("/img/logo.png")));
    
        TabPane tabs = new TabPane();
            tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);  
        
//Karta vyučujích
        Tab vyucujici = new Tab("Vyučující");
            kartaVyucujici = new KartaVyucujici();                       
        
            VBox tabV = new VBox();
                tabV.getChildren().addAll(getUzivatel(), kartaVyucujici.getKartaVyucujici(c, opravneni));

            ScrollPane scrollV = new ScrollPane();
                scrollV.setFitToWidth(true);
                scrollV.setFitToHeight(true);
                scrollV.setContent(tabV);
            vyucujici.setContent(scrollV);      
        
        vyucujici.setOnSelectionChanged(e -> {
            if (vyucujici.isSelected()) {
                kartaVyucujici.updateData(c);
            }
        });
//Karta pracovišť
        Tab katedry = new Tab("Pracoviště");
            kartaPracoviste = new KartaPracoviste();
            
            VBox tabKat = new VBox();
                tabKat.getChildren().addAll(getUzivatel(), kartaPracoviste.getKartaPracovist(c, opravneni));            
 
            ScrollPane scrollP = new ScrollPane();
                scrollP.setFitToWidth(true);
                scrollP.setFitToHeight(true);
                scrollP.setContent(tabKat);
            katedry.setContent(scrollP);
        
//Karta předmětů
        Tab predmety = new Tab("Předměty");
            kartaPredmety = new KartaPredmety();
            
            VBox tabP = new VBox();
                tabP.getChildren().addAll(getUzivatel(), kartaPredmety.getKartaPredmety(c, opravneni));

            ScrollPane scrollPred = new ScrollPane();
                scrollPred.setFitToWidth(true);
                scrollPred.setFitToHeight(true);
                scrollPred.setContent(tabP);
            predmety.setContent(scrollPred);
            
        predmety.setOnSelectionChanged(e -> {
            if (predmety.isSelected()) {
                kartaPredmety.updateData(c);
            }
        });
        
//Karta místností
        Tab mistnosti = new Tab("Místnosti");
            kartaMistnosti = new KartaMistnosti();
            
            VBox tabM = new VBox();
                tabM.getChildren().addAll(getUzivatel(), kartaMistnosti.getKartaMistnosti(c, opravneni));

            ScrollPane scrollM = new ScrollPane();
                scrollM.setFitToWidth(true);
                scrollM.setFitToHeight(true);
                scrollM.setContent(tabM);
            mistnosti.setContent(scrollM);
            
        mistnosti.setOnSelectionChanged(e -> {
            if (mistnosti.isSelected()) {
                kartaMistnosti.updateData(c);
            }
        });
            
//Karta studijních oborů
        Tab obory = new Tab("Studijní obory");
            kartaStudijniObory = new KartaStudijniObory();
            
            VBox tabO = new VBox();
                tabO.getChildren().addAll(getUzivatel(), kartaStudijniObory.getKartaStudijniObory(c, opravneni));

            ScrollPane scrollO = new ScrollPane();
                scrollO.setFitToWidth(true);
                scrollO.setFitToHeight(true);
                scrollO.setContent(tabO);
            obory.setContent(scrollO);
            
        obory.setOnSelectionChanged(e -> {
            if (obory.isSelected()) {
                kartaStudijniObory.updateData(c);
            }
        });    

//Karta studijního plánu        
        Tab roky = new Tab("Studijní plány");
            kartaStudijniPlany = new KartaStudijniPlany();
            
            VBox tabSP = new VBox();
                tabSP.getChildren().addAll(getUzivatel(), kartaStudijniPlany.getKartaStudijniPlany(c, opravneni));

            ScrollPane scrollR = new ScrollPane();
                scrollR.setFitToWidth(true);
                scrollR.setFitToHeight(true);
                scrollR.setContent(tabSP);
            roky.setContent(scrollR);
            
        roky.setOnSelectionChanged(e -> {
            if (roky.isSelected()) {
                kartaStudijniPlany.updateData(c);
            }
        });     
            
//Karta rozvrhů        
        Tab rozvrhy = new Tab("Rozvrhy");
            kartaRozvrhy = new KartaRozvrhy();
            
            VBox tabR = new VBox();
                tabR.getChildren().addAll(getUzivatel(), kartaRozvrhy.getKartaRozvrhy(c, opravneni));

            ScrollPane scrollRoz = new ScrollPane();
                scrollRoz.setFitToWidth(true);
                scrollRoz.setFitToHeight(true);
                scrollRoz.setContent(tabR);
            rozvrhy.setContent(scrollRoz);
            
        rozvrhy.setOnSelectionChanged(e -> {
            if (rozvrhy.isSelected()) {
                kartaRozvrhy.updateData(c);
            }
        });     
            
//Karta rozvrhů vyučujícího
        Tab rozvrhyV = new Tab("Můj rozvrh");
            if (opravneni != 0 && opravneni != 1) {
                rozvrhyV.setText("Roz. Akce");
            }
            kartaRozvrhyUcitel = new KartaRozvrhyUcitel();
            
            VBox tabRV = new VBox();
                tabRV.getChildren().addAll(getUzivatel(), kartaRozvrhyUcitel.getKartaRozvrhyUcitel(c, opravneni, username));

            ScrollPane scrollRozV = new ScrollPane();
                scrollRozV.setFitToWidth(true);
                scrollRozV.setFitToHeight(true);
                scrollRozV.setContent(tabRV);
            rozvrhyV.setContent(scrollRozV);
        
        rozvrhyV.setOnSelectionChanged(e -> {
            if (rozvrhyV.isSelected()) {
                kartaRozvrhyUcitel.updateData(c);
            }
        }); 
            
//Karta odhlášení    
        Tab odhlaseni = new Tab("Odejít");
            odhlaseni.setOnSelectionChanged(e -> {
                if (odhlaseni.isSelected()) {
                    mainStage.close();
                    loginUser();
                }
            });
            
//Karta Účtů
        Tab vyucujiciP = new Tab("Účty");
            kartaPrihlaseni = new KartaPrihlaseni();
            VBox tabVP = new VBox();
                    tabVP.getChildren().addAll(getUzivatel(), kartaPrihlaseni.getKartaPrihlaseni(c, opravneni));

            ScrollPane scrollVP = new ScrollPane();
                scrollVP.setFitToWidth(true);
                scrollVP.setFitToHeight(true);
                scrollVP.setContent(tabVP);
            vyucujiciP.setContent(scrollVP);      
        
        vyucujiciP.setOnSelectionChanged(e -> {
            if (vyucujiciP.isSelected()) {
                kartaPrihlaseni.updateData(c);
            }
        });             
        
        tabs.getTabs().addAll(vyucujici, katedry, predmety, mistnosti, obory, roky, rozvrhy, rozvrhyV, vyucujiciP, odhlaseni);      
        
        Scene scene = new Scene(tabs, 1100, 800);
        scene.getStylesheets().add(getClass().getResource("MainStyle.css")
               .toExternalForm());

        mainStage.setTitle("Portál vysoké školy");                      
        mainStage.setScene(scene);
        mainStage.show();
    }     

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
