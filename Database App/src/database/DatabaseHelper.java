package database;

import data.AkRok;
import data.BlokovaVyuka;
import data.Fakulta;
import data.FormaVyuky;
import data.Katedra;
import data.Kategorie;
import data.Mistnost;
import data.Predmet;
import data.PridaniAkce;
import data.Role;
import data.Semestr;
import data.StudijniObor;
import data.ViewPredmet;
import data.ViewKatedry;
import data.ViewObor;
import data.ViewPlan;
import data.ViewRozvrh;
import data.ViewVyucujici;
import data.Vyucujici;
import data.VyucujiciRole;
import data.Zakonceni;
import data.ZpusobVyuky;
import data.ViewPrihlaseni;
import java.awt.image.RenderedImage;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author st52550
 */
public class DatabaseHelper {
    
    public DatabaseHelper(String login, String pswd) throws SQLException {
        myInit(login, pswd);
    }

    public static void myInit(String login, String pswd) throws SQLException {
        OracleConnector.setUpConnection("fei-sql1.upceucebny.cz", 1521, "IDAS12", login, pswd);
    } 
    
    public ArrayList<ViewVyucujici> getViewVyucujici(Fakulta fakulta) throws SQLException {
        ArrayList<ViewVyucujici> vyucujici = new ArrayList<>();

        if(fakulta != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT vyucujici_id, titul_pred, prijmeni, jmeno, titul_za, katedra, fakulta, telefon, mobil, email "
            + "FROM C##ST52550.VYUCUJICI_POHLED WHERE fakulta LIKE '" + fakulta.getNazevF() + "' ORDER BY prijmeni");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                vyucujici.add(new ViewVyucujici(
                        rset.getInt("vyucujici_id"),
                        rset.getString("titul_pred"),
                        rset.getString("prijmeni"),
                        rset.getString("jmeno"),
                        rset.getString("titul_za"),
                        rset.getString("katedra"),
                        rset.getString("fakulta"),
                        rset.getInt("telefon"),
                        rset.getInt("mobil"),
                        rset.getString("email")));
            }
        }
        
        for(ViewVyucujici v : vyucujici){
            if (v.getTitulPred() == null) {
                v.setTitulPred("");
            } 
            if (v.getTitulZa() == null) {
                v.setTitulZa("");
            } 
        }
        
        return vyucujici;
    }
    
    public ArrayList<ViewVyucujici> getViewVyucujici() throws SQLException {
        ArrayList<ViewVyucujici> vyucujici = new ArrayList<>();

        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT vyucujici_id, titul_pred, prijmeni, jmeno, titul_za, katedra, fakulta, telefon, mobil, email "
        + "FROM C##ST52550.VYUCUJICI_POHLED ORDER BY prijmeni");
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            vyucujici.add(new ViewVyucujici(
                    rset.getInt("vyucujici_id"),
                    rset.getString("titul_pred"),
                    rset.getString("prijmeni"),
                    rset.getString("jmeno"),
                    rset.getString("titul_za"),
                    rset.getString("katedra"),
                    rset.getString("fakulta"),
                    rset.getInt("telefon"),
                    rset.getInt("mobil"),
                    rset.getString("email")));
        }
            
        for(ViewVyucujici v : vyucujici){
            if (v.getTitulPred() == null) {
                v.setTitulPred("");
            } 
            if (v.getTitulZa() == null) {
                v.setTitulZa("");
            } 
        }
        return vyucujici;
    }
    
    public ArrayList<Katedra> getKatedry() throws SQLException {
        ArrayList<Katedra> kat = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT katedra_id, nazev FROM C##ST52550.KATEDRY_NAZVY");
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            kat.add(new Katedra( 
                    rset.getInt("katedra_id"),
                    rset.getString("nazev")));
        }
        return kat;
    }
    
    public ArrayList<Katedra> getKatedry(String fakulta) throws SQLException {
        ArrayList<Katedra> kat = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT katedra_id, katedra FROM C##ST52550.KATEDRY_POHLED WHERE fakulta LIKE '" + fakulta + "'");
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            kat.add(new Katedra( 
                    rset.getInt("katedra_id"),
                    rset.getString("katedra")));
        }
        return kat;
    }
    
    public int getDay(String datum) throws SQLException{
        Connection conn = OracleConnector.getConnection();
        
        int d = 0;
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT TO_CHAR(TO_DATE('" + datum + "'), 'D') AS datum from dual");
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            d = Integer.parseInt(rset.getString("datum"));
        }
        
        return d;
    }
    
    public ArrayList<StudijniObor> getObory() throws SQLException {
        ArrayList<StudijniObor> obor = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT obor_id, nazev FROM C##ST52550.OBORY_POHLED");
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            obor.add(new StudijniObor( 
                    rset.getInt("obor_id"),
                    rset.getString("nazev")));
        }
        return obor;
    }
    
    public ArrayList<StudijniObor> getObory(String fakulta) throws SQLException {
        ArrayList<StudijniObor> obor = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT obor_id, nazev FROM C##ST52550.OBORY_POHLED WHERE fakulta LIKE '" + fakulta + "'");
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            obor.add(new StudijniObor( 
                    rset.getInt("obor_id"),
                    rset.getString("nazev")));
        }
        return obor;
    }
    
    public ArrayList<Fakulta> getFakulty() throws SQLException {
        ArrayList<Fakulta> fakulta = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT fakulta_id, zkratka, nazev, telefon, mobil, email " +
        "FROM C##ST52550.FAKULTY_NAZVY");
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            fakulta.add(new Fakulta( 
                    rset.getInt("fakulta_id"),
                    rset.getString("zkratka"),
                    rset.getString("nazev"),
                    rset.getInt("telefon"),
                    rset.getInt("mobil"),
                    rset.getString("email")
            ));
        }
        return fakulta;
    }
    
    public ArrayList<Mistnost> getMistnosti(String fakulta) throws SQLException {
        ArrayList<Mistnost> mistnost = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT mistnost_id, oznaceni, kapacita, katedra, fakulta FROM C##ST52550.MISTNOSTI_NAZVY WHERE fakulta LIKE '" + fakulta + "'");
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            mistnost.add(new Mistnost( 
                    rset.getInt("mistnost_id"),
                    rset.getString("oznaceni"),
                    rset.getInt("kapacita"),
                    rset.getString("katedra"),
                    rset.getString("fakulta")
            ));
        }
        return mistnost;
    }
    
    public ArrayList<Mistnost> getMistnosti(String fakulta, int max_obsazenost) throws SQLException {
        ArrayList<Mistnost> mistnost = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT mistnost_id, oznaceni, kapacita, katedra, fakulta FROM C##ST52550.MISTNOSTI_NAZVY WHERE fakulta LIKE '" + fakulta + "' AND kapacita >= " + max_obsazenost);
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            mistnost.add(new Mistnost( 
                    rset.getInt("mistnost_id"),
                    rset.getString("oznaceni"),
                    rset.getInt("kapacita"),
                    rset.getString("katedra"),
                    rset.getString("fakulta")
            ));
        }
        return mistnost;
    }
    
    public ArrayList<ViewKatedry> getViewKatedry(Fakulta fakulta) throws SQLException {
        ArrayList<ViewKatedry> katedry = new ArrayList<>();

        if(fakulta != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT katedra_id, zkratkaKatedry, katedra, zkratkaFakulty, fakulta, telefon, mobil, email " +
            "FROM C##ST52550.KATEDRY_POHLED WHERE fakulta LIKE '" + fakulta.getNazevF() + "'");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                katedry.add(new ViewKatedry(
                        rset.getInt("katedra_id"),
                        rset.getString("zkratkaKatedry"),
                        rset.getString("katedra"),
                        rset.getString("zkratkaFakulty"),
                        rset.getString("fakulta"),
                        rset.getInt("telefon"),
                        rset.getInt("mobil"),
                        rset.getString("email")));
            }
        }
        return katedry;
    }
    
    public ArrayList<ViewPredmet> getViewPredmety(Fakulta fakulta) throws SQLException {
        ArrayList<ViewPredmet> predmety = new ArrayList<>();

        if(fakulta != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT predmet_id, predmet, zkratka, pocet_kreditu, rocnik_vyuky, pocet_pr, pocet_cv, pocet_sem, obor, katedra, fakulta " +
            "FROM C##ST52550.PREDMETY_POHLED WHERE fakulta LIKE '" + fakulta.getNazevF() + "'");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                predmety.add(new ViewPredmet(
                        rset.getInt("predmet_id"),
                        rset.getString("predmet"),
                        rset.getString("zkratka"),
                        rset.getInt("pocet_kreditu"),
                        rset.getInt("rocnik_vyuky"),
                        rset.getInt("pocet_pr"),
                        rset.getInt("pocet_cv"),
                        rset.getInt("pocet_sem"),
                        rset.getString("obor"),
                        rset.getString("katedra"),
                        rset.getString("fakulta")
                ));
            }
        }
        return predmety;
    }
    
    public ArrayList<ViewObor> getViewObory(Fakulta fakulta) throws SQLException {
        ArrayList<ViewObor> obory = new ArrayList<>();

        if(fakulta != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT obor_id, oznaceni, nazev, katedra, fakulta " +
            "FROM C##ST52550.OBORY_POHLED WHERE fakulta LIKE '" + fakulta.getNazevF() + "'");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                obory.add(new ViewObor(
                        rset.getInt("obor_id"),
                        rset.getString("oznaceni"),
                        rset.getString("nazev"),
                        rset.getString("katedra"),
                        rset.getString("fakulta")
                ));
            }
        }
        return obory;
    }
    
    public ArrayList<ViewPlan> getViewPlan(AkRok rok, ViewObor obor) throws SQLException {
        ArrayList<ViewPlan> plan = new ArrayList<>();
        
        
        if(rok != null && obor != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT ak_rok_id, kategorie_id, kategorie, predmet_id, predmet, rocnik_vyuky, zkratka, delka, " +
            "odhad_studentu, zpusob_vyuky_id, vyuka, semestr_id, semestr, zakonceni_id, zakonceni, forma_vyuky_id, forma " +
            "FROM C##ST52550.ROKY_POHLED " +
            "WHERE rok LIKE '" + rok.getRok() + "' AND obor LIKE '" + obor.getNazev() + 
            "' ORDER BY predmet");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                plan.add(new ViewPlan(
                        rset.getInt("ak_rok_id"),
                        rset.getInt("kategorie_id"),
                        rset.getString("kategorie"),
                        rset.getInt("predmet_id"),
                        rset.getString("predmet"),
                        rset.getString("zkratka"),
                        rset.getInt("delka"),
                        rset.getInt("odhad_studentu"),
                        rset.getInt("rocnik_vyuky"),
                        rset.getInt("zpusob_vyuky_id"),
                        rset.getString("vyuka"),
                        rset.getInt("semestr_id"),
                        rset.getString("semestr"),
                        rset.getInt("zakonceni_id"),
                        rset.getString("zakonceni"),
                        rset.getInt("forma_vyuky_id"),
                        rset.getString("forma")
                ));
            }
        }
        return plan;
    }
    
    public ArrayList<ViewPlan> getViewPlan(AkRok rok, Fakulta fakulta, ViewObor obor, Predmet predmet) throws SQLException {
        ArrayList<ViewPlan> plan = new ArrayList<>();
        
        
        if(rok != null && fakulta != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT ak_rok_id, kategorie_id, kategorie, predmet_id, predmet, rocnik_vyuky, zkratka, delka, " +
            "odhad_studentu, zpusob_vyuky_id, vyuka, semestr_id, semestr, zakonceni_id, zakonceni, forma_vyuky_id, forma " +
            "FROM C##ST52550.ROKY_POHLED " +
            "WHERE rok LIKE '" + rok.getRok() + "' AND fakulta LIKE '" + fakulta.getNazevF() + "' AND obor LIKE '" + obor.getNazev() +
            "' AND predmet LIKE '" + predmet.getNazev() + "' ORDER BY zpusob_vyuky_id");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                plan.add(new ViewPlan(
                        rset.getInt("ak_rok_id"),
                        rset.getInt("kategorie_id"),
                        rset.getString("kategorie"),
                        rset.getInt("predmet_id"),
                        rset.getString("predmet"),
                        rset.getString("zkratka"),
                        rset.getInt("delka"),
                        rset.getInt("odhad_studentu"),
                        rset.getInt("rocnik_vyuky"),
                        rset.getInt("zpusob_vyuky_id"),
                        rset.getString("vyuka"),
                        rset.getInt("semestr_id"),
                        rset.getString("semestr"),
                        rset.getInt("zakonceni_id"),
                        rset.getString("zakonceni"),
                        rset.getInt("forma_vyuky_id"),
                        rset.getString("forma")
                ));
            }
        }
        return plan;
    }
    
    public ArrayList<ViewPlan> getViewPlan(String rok, String fakulta, String obor, String predmet) throws SQLException {
        ArrayList<ViewPlan> plan = new ArrayList<>();
        
        
        if(rok != null && fakulta != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT ak_rok_id, kategorie_id, kategorie, predmet_id, predmet, rocnik_vyuky, zkratka, delka, " +
            "odhad_studentu, zpusob_vyuky_id, vyuka, semestr_id, semestr, zakonceni_id, zakonceni, forma_vyuky_id, forma " +
            "FROM C##ST52550.ROKY_POHLED " +
            "WHERE rok LIKE '" + rok + "' AND fakulta LIKE '" + fakulta + "' AND obor LIKE '" + obor +
            "' AND predmet LIKE '" + predmet + "' ORDER BY zpusob_vyuky_id");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                plan.add(new ViewPlan(
                        rset.getInt("ak_rok_id"),
                        rset.getInt("kategorie_id"),
                        rset.getString("kategorie"),
                        rset.getInt("predmet_id"),
                        rset.getString("predmet"),
                        rset.getString("zkratka"),
                        rset.getInt("delka"),
                        rset.getInt("odhad_studentu"),
                        rset.getInt("rocnik_vyuky"),
                        rset.getInt("zpusob_vyuky_id"),
                        rset.getString("vyuka"),
                        rset.getInt("semestr_id"),
                        rset.getString("semestr"),
                        rset.getInt("zakonceni_id"),
                        rset.getString("zakonceni"),
                        rset.getInt("forma_vyuky_id"),
                        rset.getString("forma")
                ));
            }
        }
        return plan;
    }
    
    public ArrayList<ViewRozvrh> getViewRozvrh(AkRok rok, ViewObor obor, Predmet predmet) throws SQLException {
        ArrayList<ViewRozvrh> rozvrh = new ArrayList<>();
        
        
        if(rok != null && obor != null && predmet != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT vyuc_rok_id, zkratka, predmet, rozsah, den, casod, casdo, mistnost, titul_pred, prijmeni, " +
            "jmeno, titul_za, role, max_obsazenost, platnost, zpusob, blokova_vyuka, TO_CHAR(datum, 'DD.MM.YYYY') AS datum " +
            "FROM C##ST52550.ROZVRHY_POHLED " +
            "WHERE rok LIKE '" + rok.getRok() + "' AND obor LIKE '" + obor.getNazev() +
            "' AND predmet LIKE '" + predmet.getNazev() + "' ORDER BY den, casOd");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                rozvrh.add(new ViewRozvrh(
                        rset.getInt("vyuc_rok_id"),
                        rset.getString("zkratka"),
                        rset.getString("predmet"),
                        rset.getString("zpusob"),
                        rset.getInt("rozsah"),
                        rset.getString("den"),
                        rset.getString("casod"),
                        rset.getString("casdo"),
                        " ",
                        rset.getString("mistnost"),
                        rset.getString("titul_pred") + " " + rset.getString("prijmeni") + 
                                " " + rset.getString("jmeno") + " " + rset.getString("titul_za"),
                        rset.getString("role"),
                        rset.getInt("max_obsazenost"),
                        rset.getInt("platnost"),
                        " ",
                        rset.getInt("blokova_vyuka"),
                        "",
                        rset.getString("datum")
                ));
            }
        }
        
        rozvrh.forEach((r) -> {
            r.setCas(r.getCasOd() + ":00 – " + r.getCasDo() + ":00");
            if (r.getPlatnost() == 1) {
                r.setPlatnostSlovy("platná");
            } else {
                r.setPlatnostSlovy("neplatná");
            }
            
            if (r.getBlokovaVyuka() == 1){
                r.setBlokovaVyukaSlovy("ANO – " + r.getDatum());
            }
                       
            r.setVyucujici(r.getVyucujici().replace("null",""));
            if (r.getDen() == null) {
                r.setDen("");
            }
        });
        
        return rozvrh;
    } 
    
    public ArrayList<ViewRozvrh> getViewRozvrhPlatneA(AkRok rok, ViewObor obor, Predmet predmet) throws SQLException {
        ArrayList<ViewRozvrh> rozvrh = new ArrayList<>();
        
        
        if(rok != null && obor != null && predmet != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT vyuc_rok_id, zkratka, predmet, rozsah, den, casod, casdo, mistnost, titul_pred, prijmeni, " +
            "jmeno, titul_za, role, max_obsazenost, platnost, zpusob, blokova_vyuka, TO_CHAR(datum, 'DD.MM.YYYY') AS datum " +
            "FROM C##ST52550.ROZVRHY_POHLED " +
            "WHERE rok LIKE '" + rok.getRok() + "' AND obor LIKE '" + obor.getNazev() +
            "' AND predmet LIKE '" + predmet.getNazev() + "' AND platnost = 1 AND blokova_vyuka = 0 ORDER BY den, casOd");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                rozvrh.add(new ViewRozvrh(
                        rset.getInt("vyuc_rok_id"),
                        rset.getString("zkratka"),
                        rset.getString("predmet"),
                        rset.getString("zpusob"),
                        rset.getInt("rozsah"),
                        rset.getString("den"),
                        rset.getString("casod"),
                        rset.getString("casdo"),
                        " ",
                        rset.getString("mistnost"),
                        rset.getString("titul_pred") + " " + rset.getString("prijmeni") + 
                                " " + rset.getString("jmeno") + " " + rset.getString("titul_za"),
                        rset.getString("role"),
                        rset.getInt("max_obsazenost"),
                        rset.getInt("platnost"),
                        " ",
                        rset.getInt("blokova_vyuka"),
                        "",
                        rset.getString("datum")
                ));
            }
        }
        
        rozvrh.forEach((r) -> {
            r.setCas(r.getCasOd() + ":00 – " + r.getCasDo() + ":00");
            if (r.getPlatnost() == 1) {
                r.setPlatnostSlovy("platná");
            } else {
                r.setPlatnostSlovy("neplatná");
            }
            
            if (r.getBlokovaVyuka() == 1){
                r.setBlokovaVyukaSlovy("ANO – " + r.getDatum());
            }
                       
            r.setVyucujici(r.getVyucujici().replace("null",""));
            if (r.getDen() == null) {
                r.setDen("");
            }
        });
        
        return rozvrh;
    }
    
    public ArrayList<ViewRozvrh> getViewRozvrhNeplatneA(AkRok rok) throws SQLException {
        ArrayList<ViewRozvrh> rozvrh = new ArrayList<>();
        
        
        if(rok != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT vyuc_rok_id, zkratka, predmet, rozsah, den, casod, casdo, mistnost, titul_pred, prijmeni, " +
            "jmeno, titul_za, role, max_obsazenost, platnost, zpusob, blokova_vyuka, TO_CHAR(datum, 'DD.MM.YYYY') AS datum " +
            "FROM C##ST52550.ROZVRHY_POHLED " +
            "WHERE rok LIKE '" + rok.getRok() + "' AND platnost = 0 ORDER BY blokova_vyuka, den, casOd");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                rozvrh.add(new ViewRozvrh(
                        rset.getInt("vyuc_rok_id"),
                        rset.getString("zkratka"),
                        rset.getString("predmet"),
                        rset.getString("zpusob"),
                        rset.getInt("rozsah"),
                        rset.getString("den"),
                        rset.getString("casod"),
                        rset.getString("casdo"),
                        " ",
                        rset.getString("mistnost"),
                        rset.getString("titul_pred") + " " + rset.getString("prijmeni") + 
                                " " + rset.getString("jmeno") + " " + rset.getString("titul_za"),
                        rset.getString("role"),
                        rset.getInt("max_obsazenost"),
                        rset.getInt("platnost"),
                        " ",
                        rset.getInt("blokova_vyuka"),
                        "",
                        rset.getString("datum")
                ));
            }
        }
        
        rozvrh.forEach((r) -> {
            r.setCas(r.getCasOd() + ":00 – " + r.getCasDo() + ":00");
            if (r.getPlatnost() == 1) {
                r.setPlatnostSlovy("platná");
            } else {
                r.setPlatnostSlovy("neplatná");
            }
            
            if (r.getBlokovaVyuka() == 1){
                r.setBlokovaVyukaSlovy("ANO – " + r.getDatum());
            }
                       
            r.setVyucujici(r.getVyucujici().replace("null",""));
            if (r.getDen() == null) {
                r.setDen("");
            }
        });
        
        return rozvrh;
    }
    
    public ArrayList<ViewRozvrh> getViewRozvrh(AkRok rok, String email) throws SQLException {
        ArrayList<ViewRozvrh> rozvrh = new ArrayList<>();
        
        
        if(rok != null && email != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT vyuc_rok_id, zkratka, predmet, rozsah, den, casod, casdo, mistnost, titul_pred, prijmeni, " +
            "jmeno, titul_za, role, max_obsazenost, platnost, zpusob, blokova_vyuka, TO_CHAR(datum, 'DD.MM.YYYY') AS datum " +
            "FROM C##ST52550.ROZVRHY_POHLED " +
            "WHERE rok LIKE '" + rok.getRok() + "' AND email LIKE '" + email + "' AND blokova_vyuka = 0 ORDER BY den, casOd");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                rozvrh.add(new ViewRozvrh(
                        rset.getInt("vyuc_rok_id"),
                        rset.getString("zkratka"),
                        rset.getString("predmet"),
                        rset.getString("zpusob"),
                        rset.getInt("rozsah"),
                        rset.getString("den"),
                        rset.getString("casod"),
                        rset.getString("casdo"),
                        " ",
                        rset.getString("mistnost"),
                        rset.getString("titul_pred") + " " + rset.getString("prijmeni") + 
                                " " + rset.getString("jmeno") + " " + rset.getString("titul_za"),
                        rset.getString("role"),
                        rset.getInt("max_obsazenost"),
                        rset.getInt("platnost"),
                        " ",
                        rset.getInt("blokova_vyuka"),
                        "",
                        rset.getString("datum")
                ));
            }
        }
        
        rozvrh.forEach((r) -> {
            r.setCas(r.getCasOd() + ":00 – " + r.getCasDo() + ":00");
            if (r.getPlatnost() == 1) {
                r.setPlatnostSlovy("platná");
            } else {
                r.setPlatnostSlovy("neplatná");
            }
            
            if (r.getBlokovaVyuka() == 1){
                r.setBlokovaVyukaSlovy("ANO – " + r.getDatum());
            }
            
            r.setVyucujici(r.getVyucujici().replace("null",""));
            if (r.getDen() == null) {
                r.setDen("");
            }
        });
        
        return rozvrh;
    } 
    
    public ArrayList<ViewRozvrh> getViewRozvrhPlatneA(AkRok rok, String email) throws SQLException {
        ArrayList<ViewRozvrh> rozvrh = new ArrayList<>();
        
        
        if(rok != null && email != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT vyuc_rok_id, zkratka, predmet, rozsah, den, casod, casdo, mistnost, titul_pred, prijmeni, " +
            "jmeno, titul_za, role, max_obsazenost, platnost, zpusob, blokova_vyuka, TO_CHAR(datum, 'DD.MM.YYYY') AS datum " +
            "FROM C##ST52550.ROZVRHY_POHLED " +
            "WHERE rok LIKE '" + rok.getRok() + "' AND email LIKE '" + email + "' AND platnost = 1 AND blokova_vyuka = 0 ORDER BY den, casOd");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                rozvrh.add(new ViewRozvrh(
                        rset.getInt("vyuc_rok_id"),
                        rset.getString("zkratka"),
                        rset.getString("predmet"),
                        rset.getString("zpusob"),
                        rset.getInt("rozsah"),
                        rset.getString("den"),
                        rset.getString("casod"),
                        rset.getString("casdo"),
                        " ",
                        rset.getString("mistnost"),
                        rset.getString("titul_pred") + " " + rset.getString("prijmeni") + 
                                " " + rset.getString("jmeno") + " " + rset.getString("titul_za"),
                        rset.getString("role"),
                        rset.getInt("max_obsazenost"),
                        rset.getInt("platnost"),
                        " ",
                        rset.getInt("blokova_vyuka"),
                        "",
                        rset.getString("datum")
                ));
            }
        }
        
        rozvrh.forEach((r) -> {
            r.setCas(r.getCasOd() + ":00 – " + r.getCasDo() + ":00");
            if (r.getPlatnost() == 1) {
                r.setPlatnostSlovy("platná");
            } else {
                r.setPlatnostSlovy("neplatná");
            }
            
            if (r.getBlokovaVyuka() == 1){
                r.setBlokovaVyukaSlovy("ANO – " + r.getDatum());
            }
            
            r.setVyucujici(r.getVyucujici().replace("null",""));
            if (r.getDen() == null) {
                r.setDen("");
            }
        });
        
        return rozvrh;
    } 
    
    public ArrayList<ViewRozvrh> getViewRozvrhBlokoveA(AkRok rok, String email) throws SQLException {
        ArrayList<ViewRozvrh> rozvrh = new ArrayList<>();
        
        
        if(rok != null && email != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT vyuc_rok_id, zkratka, predmet, rozsah, den, casod, casdo, mistnost, titul_pred, prijmeni, " +
            "jmeno, titul_za, role, max_obsazenost, platnost, zpusob, blokova_vyuka, TO_CHAR(datum, 'DD.MM.YYYY') AS datum " +
            "FROM C##ST52550.ROZVRHY_POHLED " +
            "WHERE rok LIKE '" + rok.getRok() + "' AND email LIKE '" + email + "' AND platnost = 1 AND blokova_vyuka = 1 ORDER BY datum");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                rozvrh.add(new ViewRozvrh(
                        rset.getInt("vyuc_rok_id"),
                        rset.getString("zkratka"),
                        rset.getString("predmet"),
                        rset.getString("zpusob"),
                        rset.getInt("rozsah"),
                        rset.getString("den"),
                        rset.getString("casod"),
                        rset.getString("casdo"),
                        " ",
                        rset.getString("mistnost"),
                        rset.getString("titul_pred") + " " + rset.getString("prijmeni") + 
                                " " + rset.getString("jmeno") + " " + rset.getString("titul_za"),
                        rset.getString("role"),
                        rset.getInt("max_obsazenost"),
                        rset.getInt("platnost"),
                        " ",
                        rset.getInt("blokova_vyuka"),
                        "",
                        rset.getString("datum")
                ));
            }
        }
        
        rozvrh.forEach((r) -> {
            r.setCas(r.getCasOd() + ":00 – " + r.getCasDo() + ":00");
            if (r.getPlatnost() == 1) {
                r.setPlatnostSlovy("platná");
            } else {
                r.setPlatnostSlovy("neplatná");
            }
            
            if (r.getBlokovaVyuka() == 1){
                r.setBlokovaVyukaSlovy("ANO – " + r.getDatum());
            }
            
            r.setVyucujici(r.getVyucujici().replace("null",""));
            if (r.getDen() == null) {
                r.setDen("");
            }
        });
        
        return rozvrh;
    } 
    
    public ArrayList<AkRok> getRoky() throws SQLException {
        ArrayList<AkRok> roky = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT MIN(ak_rok_id) AS ak_rok_id, rok " +
        "FROM C##ST52550.ROKY_POHLED GROUP BY rok");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            roky.add(new AkRok(
                    rset.getInt("ak_rok_id"),
                    rset.getString("rok")
            ));
        }
        return roky;
    }
    
    public VyucujiciRole getRole(int idPredmet) throws SQLException {
        VyucujiciRole vyucujici = null;
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT titul_pred, prijmeni, jmeno, titul_za, role " +
        "FROM C##ST52550.ROLE_POHLED WHERE ak_rok_id =" + idPredmet); 
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            vyucujici = new VyucujiciRole(
                    idPredmet,
                    rset.getString("titul_pred"),
                    rset.getString("prijmeni"),
                    rset.getString("jmeno"),
                    rset.getString("titul_za"),
                    rset.getString("role"));           
        }
        
        if(vyucujici.getTitulPred() == null){
                vyucujici.setTitulPred("");
        }
        if(vyucujici.getTitulPred() == null){
                vyucujici.setTitulZa("");
        }
        
        return vyucujici;
    }
    
    public ArrayList<Role> getRoleNazvy() throws SQLException {
        ArrayList<Role> role = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT role_id, role FROM C##ST52550.ROLE_NAZVY");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            role.add(new Role(
                    rset.getInt("role_id"),
                    rset.getString("role")
            ));
        }
        return role;
    }
    
    public ArrayList<Kategorie> getKategorie() throws SQLException {
        ArrayList<Kategorie> kategorie = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT kategorie_id, nazev FROM C##ST52550.KATEGORIE_NAZVY");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            kategorie.add(new Kategorie(
                    rset.getInt("kategorie_id"),
                    rset.getString("nazev")
            ));
        }
        return kategorie;
    }
    
    public ArrayList<Vyucujici> getVyucujici() throws SQLException {
        ArrayList<Vyucujici> vyucujici = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT vyucujici_id, titul_pred, prijmeni, jmeno, titul_za " +
        "FROM C##ST52550.VYUCUJICI_NAZVY ORDER BY prijmeni");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            vyucujici.add(new Vyucujici(
                    rset.getInt("vyucujici_id"),
                    rset.getString("titul_pred"),
                    rset.getString("prijmeni"),
                    rset.getString("jmeno"),
                    rset.getString("titul_za")
            ));
        }
        
        for(Vyucujici v : vyucujici){
            if (v.getTitulPred() == null) {
                v.setTitulPred("");
            } 
            if (v.getTitulZa() == null) {
                v.setTitulZa("");
            } 
        }
        
        return vyucujici;
    }
    
    public ArrayList<Predmet> getPredmet(String fakulta) throws SQLException {
        ArrayList<Predmet> predmety = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT predmet_id, predmet " +
        "FROM C##ST52550.PREDMETY_POHLED WHERE fakulta LIKE '" + fakulta + "'");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            predmety.add(new Predmet(
                    rset.getInt("predmet_id"),
                    rset.getString("predmet")
            ));
        }
        return predmety;
    }
    
    public ArrayList<Predmet> getPredmetObor(String obor) throws SQLException {
        ArrayList<Predmet> predmety = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT predmet_id, predmet " +
        "FROM C##ST52550.PREDMETY_POHLED WHERE obor LIKE '" + obor + "'");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            predmety.add(new Predmet(
                    rset.getInt("predmet_id"),
                    rset.getString("predmet")
            ));
        }
        return predmety;
    }
    
    public ArrayList<ZpusobVyuky> getZpusobVyuky() throws SQLException {
        ArrayList<ZpusobVyuky> vyuky = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT zpusob_vyuky_id, zpusob FROM C##ST52550.VYUKY_NAZVY");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            vyuky.add(new ZpusobVyuky(
                    rset.getInt("zpusob_vyuky_id"),
                    rset.getString("zpusob")
            ));
        }
        return vyuky;
    }
    
    public ArrayList<Zakonceni> getZakonceni() throws SQLException {
        ArrayList<Zakonceni> zakonceni = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT zakonceni_id, zpusob FROM C##ST52550.ZAKONCENI_NAZVY");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            zakonceni.add(new Zakonceni(
                    rset.getInt("zakonceni_id"),
                    rset.getString("zpusob")
            ));
        }
        return zakonceni;
    }
    
    public ArrayList<FormaVyuky> getFormaVyuky() throws SQLException {
        ArrayList<FormaVyuky> forma = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT forma_vyuky_id, forma FROM C##ST52550.FORMY_NAZVY");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            forma.add(new FormaVyuky(
                    rset.getInt("forma_vyuky_id"),
                    rset.getString("forma")
            ));
        }
        return forma;
    }
    
    public ArrayList<Semestr> getSemestr() throws SQLException {
        ArrayList<Semestr> semestry = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT semestr_id, nazev FROM C##ST52550.SEMESTRY_NAZVY");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            semestry.add(new Semestr(
                    rset.getInt("semestr_id"),
                    rset.getString("nazev")
            ));
        }
        return semestry;
    }
    
    public ArrayList<BlokovaVyuka> getBlokovaVyuka() throws SQLException {
        ArrayList<BlokovaVyuka> vyuky = new ArrayList<>();
        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT blokova_vyuka_id, TO_CHAR(datum, 'DD.MM.YYYY') AS datum FROM C##ST52550.BLOKOVE_POHLED");  
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            vyuky.add(new BlokovaVyuka(
                    rset.getInt("blokova_vyuka_id"),
                    rset.getString("datum")
            ));
        }
        return vyuky;
    }
    
    public void insertVyucujici(String titPred, String prijmeni, String jmeno, String titZa,
            String katedra, String telefon, String mobil, String email) throws SQLException, IOException{
        int idKatedry = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        PreparedStatement stmt1 = conn.prepareStatement(
        "INSERT INTO SEM_KONTAKTY(kontakt_id, telefon, email, mobil) " +
        "VALUES (kontakty.NEXTVAL, ?, ?, ?)");
        
        stmt1.setString(1, telefon);
        stmt1.setString(2, email);
        stmt1.setString(3, mobil);
        stmt1.executeUpdate();
        conn.commit();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "SELECT katedra_id FROM KATEDRY_NAZVY WHERE nazev LIKE '" + katedra + "'");
        
        ResultSet rset = stmt2.executeQuery();
        while (rset.next()) {
            idKatedry = rset.getInt("katedra_id");
        }
        
        PreparedStatement stmt3 = conn.prepareStatement(
        "INSERT INTO SEM_VYUCUJICI(vyucujici_id, titul_pred,prijmeni,jmeno,titul_za,kontakty_kontakt_id, katedry_katedra_id) " +
        "VALUES (vyucujici.NEXTVAL, ?, ?, ?, ?, kontakty.CURRVAL, ?)");
        
        stmt3.setString(1, titPred);
        stmt3.setString(2, prijmeni);
        stmt3.setString(3, jmeno);
        stmt3.setString(4, titZa);
        stmt3.setInt(5, idKatedry);
        stmt3.executeUpdate();
        conn.commit();
        
        InputStream isi = new BufferedInputStream(new FileInputStream("./src/img/blank.jpg"));           
        Image img = ImageIO.read(isi);
        InputStream is;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
       
        ImageIO.write((RenderedImage) img, "JPG", os);       
        
        is = new ByteArrayInputStream(os.toByteArray());
        
        CallableStatement cstmt = conn.prepareCall(" BEGIN inser_update_image(?, ?, ?, vyucujici.CURRVAL); END;");
        cstmt.setBinaryStream(1, is);
        cstmt.setString( 2, "noimage" );
        cstmt.setString( 3, ".jpg" );
        cstmt.execute();
        conn.commit();
    }
    
    public void insertKatedra(String zkK, String katedra, String fakulta) throws SQLException {
        int idFakulty = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT fakulta_id FROM FAKULTY_NAZVY WHERE nazev LIKE '" + fakulta + "'");
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            idFakulty = rset.getInt("fakulta_id");
        }
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "INSERT INTO sem_katedry(katedra_id, zkratka, nazev, fakulty_fakulta_id) " +
        "VALUES (katedry.NEXTVAL, ?, ?, ?)");
        
        stmt1.setString(1, zkK);
        stmt1.setString(2, katedra);
        stmt1.setInt(3, idFakulty);
        stmt1.executeUpdate();
        conn.commit();
    }
    
    public void insertFakulta(String zkF, String fakulta, String telefon, 
            String mobil, String email) throws SQLException { 
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "INSERT INTO SEM_KONTAKTY(kontakt_id, telefon, email, mobil) " +
        "VALUES (kontakty.NEXTVAL, ?, ?, ?)");
        
        stmt1.setString(1, telefon);
        stmt1.setString(2, email);
        stmt1.setString(3, mobil);
        stmt1.executeUpdate();
        conn.commit();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "INSERT INTO sem_fakulty(fakulta_id, zkratka, nazev, kontakty_kontakt_id) " +
        "VALUES (fakulty.NEXTVAL, ?, ?, kontakty.CURRVAL)");
        
        stmt2.setString(1, zkF);
        stmt2.setString(2, fakulta);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void insertPredmet(String zkPred, String nazPred, String kredity, String pocPr, String pocCv,
            String pocSem, String rocnik, String obor) throws SQLException{
        int idObor = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT obor_id FROM OBORY_POHLED WHERE nazev LIKE '" + obor + "'");
        
        ResultSet rset = stmt1.executeQuery();
        while (rset.next()) {
            idObor = rset.getInt("obor_id");
        }
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "INSERT INTO sem_predmety(predmet_id, nazev, zkratka, pocet_kreditu, rocnik_vyuky, studijni_obory_obor_id, pocet_pr, pocet_cv, pocet_sem) " +
        "VALUES (predmety.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)");
        
        stmt2.setString(1, nazPred);
        stmt2.setString(2, zkPred);
        stmt2.setString(3, kredity);
        stmt2.setString(4, rocnik);
        stmt2.setInt(5, idObor);
        stmt2.setString(6, pocPr);
        stmt2.setString(7, pocCv);
        stmt2.setString(8, pocSem);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void insertMistnost(String oznaceni, int kapacita, String katedra) throws SQLException{
        int idKatedra = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT katedra_id FROM KATEDRY_POHLED WHERE katedra LIKE '" + katedra + "'");
        
        ResultSet rset = stmt1.executeQuery();
        while (rset.next()) {
            idKatedra = rset.getInt("katedra_id");
        }
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "INSERT INTO sem_mistnosti(mistnost_id, oznaceni, kapacita, katedry_katedra_id) " +
        "VALUES (mistnosti.NEXTVAL, ?, ?, ?)");
        
        stmt2.setString(1, oznaceni);
        stmt2.setInt(2, kapacita);
        stmt2.setInt(3, idKatedra);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void insertObor(String oznaceni, String nazO, String katedra) throws SQLException{
        int idKatedra = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT katedra_id FROM KATEDRY_NAZVY WHERE nazev LIKE '" + katedra + "'");
        
        ResultSet rset = stmt1.executeQuery();
        while (rset.next()) {
            idKatedra = rset.getInt("katedra_id");
        }
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "INSERT INTO sem_studijni_obory(obor_id, oznaceni, nazev, katedry_katedra_id) " +
        "VALUES (studijni_obory.NEXTVAL, ?, ?, ?)");
        
        stmt2.setString(1, oznaceni);
        stmt2.setString(2, nazO);
        stmt2.setInt(3, idKatedra);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void pridatRole(int idPredmet, int idVyucujici, int idRole) throws SQLException{
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement(
        "INSERT INTO SEM_VYUCUJICI_ROKY (vyuc_rok_id, ak_roky_ak_rok_id, vyucujici_vyucujici_id, role_role_id) " +
        "VALUES (vyucujici_roky.NEXTVAL, ?, ?, ?)");

        stmt.setInt(1, idPredmet);
        stmt.setInt(2, idVyucujici);
        stmt.setInt(3, idRole);
        stmt.executeUpdate();
        conn.commit();   
    }
    
    public void insertBlokovaVyuka(String datum) throws SQLException {
        //Date date = new SimpleDateFormat("mm/dd/yyyy").parse(myDate);

        
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "INSERT INTO C##ST52550.SEM_BLOKOVE_VYUKY (blokova_vyuka_id, datum) VALUES (blokove_vyuky.NEXTVAL, ?)");  
        
        stmt.setString(1, datum);
        stmt.executeUpdate();
        conn.commit();  
    }
    
    public void insertPredmetPlan(String akRok, int idKategorie, int idPredmet, String delka,
            String odhad, int idVyuka, int idZakonceni, int idForma, int idSemestr) throws SQLException{
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "INSERT INTO sem_ak_roky(ak_rok_id,nazev, kategorie_kategorie_id, predmety_predmet_id, zpusoby_vyuky_zpusob_vyuky_id," +
          "zakonceni_zakonceni_id, formy_vyuky_forma_vyuky_id, semestry_semestr_id, delka, odhad_studentu) " +
        "VALUES (ak_roky.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        
        stmt2.setString(1, akRok);
        stmt2.setInt(2, idKategorie);
        stmt2.setInt(3, idPredmet);
        stmt2.setInt(4, idVyuka);
        stmt2.setInt(5, idZakonceni);
        stmt2.setInt(6, idForma);
        stmt2.setInt(7, idSemestr);
        stmt2.setString(8, delka);
        stmt2.setString(9, odhad);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void insertRozvrhovaAkce(int akRokId, int vyucujiciId, int roleId, int mistnostId,
            int den, int casOd, int casDo, int max_obsazenost, int platnost, int blokovaVyuka, int blokovaVyukaId) throws SQLException{
        Connection conn;
        conn = OracleConnector.getConnection();
        
        if(blokovaVyuka == 0){           
            PreparedStatement stmt2 = conn.prepareStatement(
            "INSERT INTO sem_vyucujici_roky(vyuc_rok_id,ak_roky_ak_rok_id, vyucujici_vyucujici_id, role_role_id, mistnosti_mistnost_id," +
              "den, casod, casdo, max_obsazenost, platnost, blokova_vyuka) " +
            "VALUES (ak_roky.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt2.setInt(1, akRokId);
            stmt2.setInt(2, vyucujiciId);
            stmt2.setInt(3, roleId);
            stmt2.setInt(4, mistnostId);
            stmt2.setInt(5, den);
            stmt2.setInt(6, casOd);
            stmt2.setInt(7, casDo);
            stmt2.setInt(8, max_obsazenost);
            stmt2.setInt(9, platnost);
            stmt2.setInt(10, blokovaVyuka);
            stmt2.executeUpdate();
            conn.commit();
        } else {
            String datum = null;
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT TO_CHAR(datum, 'DD.MM.YYYY') AS datum FROM SEM_BLOKOVE_VYUKY WHERE blokova_vyuka_id = " + blokovaVyukaId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                datum = rset.getString("datum");
            }
            
            int d = 0;
            PreparedStatement stmt1 = conn.prepareStatement(
            "SELECT TO_CHAR(TO_DATE('" + datum + "'), 'D') AS datum from dual");
            ResultSet rset1 = stmt1.executeQuery();
            while (rset1.next()) {
                d = rset1.getInt("datum");
            }
            
            PreparedStatement stmt2 = conn.prepareStatement(
            "INSERT INTO sem_vyucujici_roky(vyuc_rok_id,ak_roky_ak_rok_id, vyucujici_vyucujici_id, role_role_id, mistnosti_mistnost_id," +
              "den, casod, casdo, max_obsazenost, platnost, blokova_vyuka, blokove_vyuky_vyuka_id) " +
            "VALUES (ak_roky.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt2.setInt(1, akRokId);
            stmt2.setInt(2, vyucujiciId);
            stmt2.setInt(3, roleId);
            stmt2.setInt(4, mistnostId);
            stmt2.setInt(5, d);
            stmt2.setInt(6, casOd);
            stmt2.setInt(7, casDo);
            stmt2.setInt(8, max_obsazenost);
            stmt2.setInt(9, platnost);
            stmt2.setInt(10, blokovaVyuka);
            stmt2.setInt(11, blokovaVyukaId);
            stmt2.executeUpdate();
            conn.commit();
        }
    }
    
    public void insertRozvrhovaAkce(int rozvrhovaAkceId, int akRokId, int vyucujiciId, int roleId, int mistnostId,
            int den, int casOd, int casDo, int max_obsazenost, int platnost, int blokovaVyuka, int blokovaVyukaId) throws SQLException{
        Connection conn;
        conn = OracleConnector.getConnection();
        
        if(blokovaVyuka == 0){
            PreparedStatement stmt2 = conn.prepareStatement(
            "INSERT INTO sem_vyucujici_roky(vyuc_rok_id,ak_roky_ak_rok_id, vyucujici_vyucujici_id, role_role_id, mistnosti_mistnost_id," +
              "den, casod, casdo, max_obsazenost, platnost, blokova_vyuka) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt2.setInt(1, rozvrhovaAkceId);
            stmt2.setInt(2, akRokId);
            stmt2.setInt(3, vyucujiciId);
            stmt2.setInt(4, roleId);
            stmt2.setInt(5, mistnostId);
            stmt2.setInt(6, den);
            stmt2.setInt(7, casOd);
            stmt2.setInt(8, casDo);
            stmt2.setInt(9, max_obsazenost);
            stmt2.setInt(10, platnost);
            stmt2.setInt(11, blokovaVyuka);
            stmt2.executeUpdate();
            conn.commit();
        } else {
            String datum = null;
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT TO_CHAR(datum, 'DD.MM.YYYY') AS datum FROM SEM_BLOKOVE_VYUKY WHERE blokova_vyuka_id = " + blokovaVyukaId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                datum = rset.getString("datum");
            }
            
            int d = 0;
            PreparedStatement stmt1 = conn.prepareStatement(
            "SELECT TO_CHAR(TO_DATE('" + datum + "'), 'D') AS datum from dual");
            ResultSet rset1 = stmt1.executeQuery();
            while (rset1.next()) {
                d = rset1.getInt("datum");
            }
            
            PreparedStatement stmt2 = conn.prepareStatement(
            "INSERT INTO sem_vyucujici_roky(vyuc_rok_id,ak_roky_ak_rok_id, vyucujici_vyucujici_id, role_role_id, mistnosti_mistnost_id," +
              "den, casod, casdo, max_obsazenost, platnost, blokova_vyuka, blokove_vyuky_vyuka_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt2.setInt(1, rozvrhovaAkceId);
            stmt2.setInt(2, akRokId);
            stmt2.setInt(3, vyucujiciId);
            stmt2.setInt(4, roleId);
            stmt2.setInt(5, mistnostId);
            stmt2.setInt(6, d);
            stmt2.setInt(7, casOd);
            stmt2.setInt(8, casDo);
            stmt2.setInt(9, max_obsazenost);
            stmt2.setInt(10, platnost);
            stmt2.setInt(11, blokovaVyuka);
            stmt2.setInt(12, blokovaVyukaId);
            stmt2.executeUpdate();
            conn.commit();
        }
    }
    
    
    public void updateVyucujici(int idVyucujici, String titPred, String prijmeni, String jmeno, String titZa,
            String katedra, String telefon, String mobil, String email) throws SQLException {
        int idKatedry = 0;
        int idKontakt = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT katedra_id FROM KATEDRY_NAZVY WHERE nazev LIKE '" + katedra + "'");
        
        ResultSet rset = stmt1.executeQuery();
        while (rset.next()) {
            idKatedry = rset.getInt("katedra_id");
        }
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "SELECT kontakt_id FROM VYUCUJICI_POHLED WHERE kontakt_id = " + idVyucujici);
        
        ResultSet rset1 = stmt2.executeQuery();
        while (rset1.next()) {
            idKontakt = rset1.getInt("kontakt_id");
        }
        
        PreparedStatement stmt3 = conn.prepareStatement(
        "UPDATE SEM_KONTAKTY SET telefon = ?, email = ?, mobil = ? WHERE kontakt_id = ?");
        
        stmt3.setString(1, telefon);
        stmt3.setString(2, email);
        stmt3.setString(3, mobil);
        stmt3.setInt(4, idKontakt);
        stmt3.executeUpdate();
        conn.commit();
        
        PreparedStatement stmt4 = conn.prepareStatement(
        "UPDATE SEM_VYUCUJICI SET titul_pred = ?, prijmeni = ?, jmeno = ?, titul_za = ?, " +
        "katedry_katedra_id = ? WHERE vyucujici_id = ?");
        
        stmt4.setString(1, titPred);
        stmt4.setString(2, prijmeni);
        stmt4.setString(3, jmeno);
        stmt4.setString(4, titZa);
        stmt4.setInt(5, idKatedry);
        stmt4.setInt(6, idVyucujici);
        stmt4.executeUpdate();
        conn.commit();       
    }
    
    public void updateKatedra(int idKatedra, String zkK, String katedra, String fakulta) throws SQLException{
        int idFakulty = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT fakulta_id FROM FAKULTY_NAZVY WHERE nazev LIKE '" + fakulta + "'");
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            idFakulty = rset.getInt("fakulta_id");
        }             
        
        PreparedStatement stmt4 = conn.prepareStatement(
        "UPDATE SEM_KATEDRY SET zkratka = ?, nazev = ?, fakulty_fakulta_id = ? WHERE katedra_id = ?");
        
        stmt4.setString(1, zkK);
        stmt4.setString(2, katedra);
        stmt4.setInt(3, idFakulty);
        stmt4.setInt(4, idKatedra);
        stmt4.executeUpdate();
        conn.commit();   
    }
    
    public void updateFakulta(int idFakulta, String zkF, String fakulta, String telefon, 
            String mobil, String email) throws SQLException{
        int idKontakt = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();  
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT kontakt_id FROM FAKULTY_NAZVY WHERE fakulta_id =" + idFakulta);
        
        ResultSet rset1 = stmt1.executeQuery();
        while (rset1.next()) {
            idKontakt = rset1.getInt("kontakt_id");
        }
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "UPDATE SEM_KONTAKTY SET telefon = ?, email = ?, mobil = ? WHERE kontakt_id = ?");
        
        stmt2.setString(1, telefon);
        stmt2.setString(2, email);
        stmt2.setString(3, mobil);
        stmt2.setInt(4, idKontakt);
        stmt2.executeUpdate();
        conn.commit();
        
        PreparedStatement stmt3 = conn.prepareStatement(
        "UPDATE SEM_FAKULTY SET zkratka = ?, nazev = ? WHERE fakulta_id = ?");
        
        stmt3.setString(1, zkF);
        stmt3.setString(2, fakulta);
        stmt3.setInt(3, idFakulta);
        stmt3.executeUpdate();
        conn.commit();   
    }
    
    public void updatePredmet(int idPredmet, String zkPred, String nazPred, String kredity, String pocPr, String pocCv,
            String pocSem, String rocnik, String obor) throws SQLException{
        int idObor = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT obor_id FROM OBORY_POHLED WHERE nazev LIKE '" + obor + "'");
        
        ResultSet rset = stmt1.executeQuery();
        while (rset.next()) {
            idObor = rset.getInt("obor_id");
        }
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "UPDATE SEM_PREDMETY SET nazev = ?, zkratka = ?, pocet_kreditu = ?, rocnik_vyuky = ?," +
        "studijni_obory_obor_id = ?, pocet_pr = ?, pocet_cv = ?, pocet_sem = ? WHERE predmet_id = ?");
        
        stmt2.setString(1, nazPred);
        stmt2.setString(2, zkPred);      
        stmt2.setString(3, kredity);
        stmt2.setString(4, rocnik);
        stmt2.setInt(5, idObor);
        stmt2.setString(6, pocPr);
        stmt2.setString(7, pocCv);
        stmt2.setString(8, pocSem);
        stmt2.setInt(9, idPredmet);
        stmt2.executeUpdate();
        conn.commit();   
    }
    
    public void updateMistnost(int idMistnost, String oznaceni, int kapacita, String katedra) throws SQLException{
        int idKatedra = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT katedra_id FROM KATEDRY_POHLED WHERE katedra LIKE '" + katedra + "'");
        
        ResultSet rset = stmt1.executeQuery();
        while (rset.next()) {
            idKatedra = rset.getInt("katedra_id");
        }
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "UPDATE sem_mistnosti SET oznaceni = ?, kapacita = ?, katedry_katedra_id = ? WHERE mistnost_id = ?");
        
        stmt2.setString(1, oznaceni);
        stmt2.setInt(2, kapacita);
        stmt2.setInt(3, idKatedra);
        stmt2.setInt(4, idMistnost);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void updateObor(int idObor, String oznaceni, String nazO, String katedra) throws SQLException{
        int idKatedra = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT katedra_id FROM KATEDRY_NAZVY WHERE nazev LIKE '" + katedra + "'");
        
        ResultSet rset = stmt1.executeQuery();
        while (rset.next()) {
            idKatedra = rset.getInt("katedra_id");
        }
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "UPDATE SEM_STUDIJNI_OBORY SET oznaceni = ?, nazev = ?, katedry_katedra_id = ? WHERE obor_id = ?");
        
        stmt2.setString(1, oznaceni);
        stmt2.setString(2, nazO);
        stmt2.setInt(3, idKatedra);
        stmt2.setInt(4, idObor);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void updateRole(int idPredmet, int idVyucujici, int idRole) throws SQLException{
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement(
        "UPDATE SEM_VYUCUJICI_ROKY SET vyucujici_vyucujici_id = ?, role_role_id = ? WHERE ak_roky_ak_rok_id = ?");

        stmt.setInt(1, idVyucujici);
        stmt.setInt(2, idRole);
        stmt.setInt(3, idPredmet);
        stmt.executeUpdate();
        conn.commit();   
    }
    
    public void updatePredmetPlan(int idPredmetPlan, String akRok, int idKategorie, int idPredmet, String delka,
            String odhad, int idVyuka, int idZakonceni, int idForma, int idSemestr) throws SQLException{
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "UPDATE SEM_AK_ROKY SET nazev = ?, kategorie_kategorie_id = ?, predmety_predmet_id = ?, zpusoby_vyuky_zpusob_vyuky_id = ?," +
          "zakonceni_zakonceni_id = ?, formy_vyuky_forma_vyuky_id = ?, semestry_semestr_id = ?, delka = ?, " +
          "odhad_studentu = ? WHERE ak_rok_id = ?");
        
        stmt2.setString(1, akRok);
        stmt2.setInt(2, idKategorie);
        stmt2.setInt(3, idPredmet);
        stmt2.setInt(4, idVyuka);
        stmt2.setInt(5, idZakonceni);
        stmt2.setInt(6, idForma);
        stmt2.setInt(7, idSemestr);
        stmt2.setString(8, delka);
        stmt2.setString(9, odhad);
        stmt2.setInt(10, idPredmetPlan);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    
    
    public void updateRozvrhovaAkce(int idRozvrhovaAkce, int akRokId, int vyucujiciId, int roleId, int mistnostId,
            int den, int casOd, int casDo, int max_obsazenost, int platnost, int blokovaVyuka, int blokovaVyukaId) throws SQLException{
        Connection conn;
        conn = OracleConnector.getConnection();              
        
        if (blokovaVyuka == 0) {
        
            PreparedStatement stmt2 = conn.prepareStatement(
            "UPDATE SEM_VYUCUJICI_ROKY SET ak_roky_ak_rok_id = ?, vyucujici_vyucujici_id = ?, role_role_id = ?, mistnosti_mistnost_id = ?," +
              "den = ?, casOd = ?, casDo = ?, max_obsazenost = ?, platnost = ?, blokova_vyuka = ? WHERE vyuc_rok_id = ?");

            stmt2.setInt(1, akRokId);
            stmt2.setInt(2, vyucujiciId);
            stmt2.setInt(3, roleId);
            stmt2.setInt(4, mistnostId);
            stmt2.setInt(5, den);
            stmt2.setInt(6, casOd);
            stmt2.setInt(7, casDo);
            stmt2.setInt(8, max_obsazenost);
            stmt2.setInt(9, platnost);
            stmt2.setInt(10, blokovaVyuka);
            stmt2.setInt(11, idRozvrhovaAkce);
            stmt2.executeUpdate();
            conn.commit();
        } else {
            String datum = null;
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT TO_CHAR(datum, 'DD.MM.YYYY') AS datum FROM SEM_BLOKOVE_VYUKY WHERE blokova_vyuka_id = " + blokovaVyukaId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                datum = rset.getString("datum");
            }
            
            int d = 0;
            PreparedStatement stmt1 = conn.prepareStatement(
            "SELECT TO_CHAR(TO_DATE('" + datum + "'), 'D') AS datum from dual");
            ResultSet rset1 = stmt1.executeQuery();
            while (rset1.next()) {
                d = rset1.getInt("datum");
            }
            
            PreparedStatement stmt2 = conn.prepareStatement(
            "UPDATE SEM_VYUCUJICI_ROKY SET ak_roky_ak_rok_id = ?, vyucujici_vyucujici_id = ?, role_role_id = ?, mistnosti_mistnost_id = ?," +
              "den = ?, casOd = ?, casDo = ?, max_obsazenost = ?, platnost = ?, blokova_vyuka = ?, blokove_vyuky_vyuka_id = ? WHERE vyuc_rok_id = ?");

            stmt2.setInt(1, akRokId);
            stmt2.setInt(2, vyucujiciId);
            stmt2.setInt(3, roleId);
            stmt2.setInt(4, mistnostId);
            stmt2.setInt(5, d);
            stmt2.setInt(6, casOd);
            stmt2.setInt(7, casDo);
            stmt2.setInt(8, max_obsazenost);
            stmt2.setInt(9, platnost);
            stmt2.setInt(10, blokovaVyuka);
            stmt2.setInt(11, blokovaVyukaId);
            stmt2.setInt(12, idRozvrhovaAkce);
            stmt2.executeUpdate();
            conn.commit();
        }
    }   
    
    public void deleteVyucujici(int idVyucujici, String email) throws SQLException{
        int idKontakt = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT kontakt_id FROM KONTAKTY_POHLED WHERE email LIKE '" + email + "'");
        
        ResultSet rset1 = stmt1.executeQuery();
        while (rset1.next()) {
            idKontakt = rset1.getInt("kontakt_id");
        }
        
        PreparedStatement stmt4 = conn.prepareStatement(
        "DELETE FROM SEM_OBRAZKY WHERE id_vyucujici = ?");
        
        stmt4.setInt(1, idVyucujici);
        stmt4.executeUpdate();
        conn.commit();
        
        PreparedStatement stmt3 = conn.prepareStatement(
        "DELETE FROM SEM_VYUCUJICI WHERE vyucujici_id = ?");
        
        stmt3.setInt(1, idVyucujici);
        stmt3.executeUpdate();
        conn.commit();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_KONTAKTY WHERE kontakt_id = ?");
        
        stmt2.setInt(1, idKontakt);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void deleteKatedra(int idKatedra) throws SQLException{    
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_KATEDRY WHERE katedra_id = ?");
        
        stmt2.setInt(1, idKatedra);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void deleteFakulta(String fakulta, String email) throws SQLException{ 
        int idKontakt = 0;
        int idFakulty = 0;
        
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT fakulta_id FROM FAKULTY_NAZVY WHERE nazev LIKE '" + fakulta + "'");
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            idFakulty = rset.getInt("fakulta_id");
        }    
        
        PreparedStatement stmt1 = conn.prepareStatement(
        "SELECT kontakt_id FROM KONTAKTY_POHLED WHERE email LIKE '" + email + "'");
        
        ResultSet rset1 = stmt1.executeQuery();
        while (rset1.next()) {
            idKontakt = rset1.getInt("kontakt_id");
        }
        
        PreparedStatement stmt3 = conn.prepareStatement(
        "DELETE FROM SEM_FAKULTY WHERE fakulta_id = ?");
        
        stmt3.setInt(1, idFakulty);
        stmt3.executeUpdate();
        conn.commit();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_KONTAKTY WHERE kontakt_id = ?");
        
        stmt2.setInt(1, idKontakt);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void deletePredmet(int idPredmet) throws SQLException {
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_PREDMETY WHERE predmet_id = ?");
        
        stmt2.setInt(1, idPredmet);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void deleteMistnost(int idMistnost) throws SQLException {
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_MISTNOSTI WHERE mistnost_id = ?");
        
        stmt2.setInt(1, idMistnost);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void deleteObor(int idObor) throws SQLException {
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_STUDIJNI_OBORY WHERE obor_id = ?");
        
        stmt2.setInt(1, idObor);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void deletePredmetPlan(int idPredmetPlan) throws SQLException {
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_AK_ROKY WHERE ak_rok_id = ?");
        
        stmt2.setInt(1, idPredmetPlan);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void deleteRozvrhovaAkce(int idRozvrhovaAkce) throws SQLException {
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_VYUCUJICI_ROKY WHERE vyuc_rok_id = ?");
        
        stmt2.setInt(1, idRozvrhovaAkce);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public void deleteBlokovaVyuka(int idBlokovaVyuka) throws SQLException {
        Connection conn;
        conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_BLOKOVE_VYUKY WHERE blokova_vyuka_id = ?");
        
        stmt2.setInt(1, idBlokovaVyuka);
        stmt2.executeUpdate();
        conn.commit();
    }
    
    public PridaniAkce rozvrhovaAkce(String rok, String semestr, String obor, String predmet, String prijmeni,
            String jmeno, int den, int casOd, int casDo, String mistnost, int max_obsazenost) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call rozvrhova_akce(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, rok);
        cstmt.setString(2, semestr);
        cstmt.setString(3, obor);
        cstmt.setString(4, predmet);
        cstmt.setString(5, prijmeni);
        cstmt.setString(6, jmeno);
        cstmt.setInt(7, den);
        cstmt.setInt(8, casOd);
        cstmt.setInt(9, casDo);
        cstmt.setString(10, mistnost);
        cstmt.setInt(11, max_obsazenost);
        
        cstmt.registerOutParameter(12, Types.VARCHAR);
        cstmt.registerOutParameter(13, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(12);
        String h = cstmt.getString(13);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    }
    
    public PridaniAkce rozvrhovaAkceUcitel(String rok, String semestr, String obor, String predmet, String prijmeni,
            String jmeno, int den, int casOd, int casDo, String mistnost, int max_obsazenost) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call rozvrhova_akce_ucitel(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, rok);
        cstmt.setString(2, semestr);
        cstmt.setString(3, obor);
        cstmt.setString(4, predmet);
        cstmt.setString(5, prijmeni);
        cstmt.setString(6, jmeno);
        cstmt.setInt(7, den);
        cstmt.setInt(8, casOd);
        cstmt.setInt(9, casDo);
        cstmt.setString(10, mistnost);
        cstmt.setInt(11, max_obsazenost);
        
        cstmt.registerOutParameter(12, Types.VARCHAR);
        cstmt.registerOutParameter(13, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(12);
        String h = cstmt.getString(13);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    }
    
    ////////////////////////Lukas
    public int prihlaseniUzivatele(String jmeno, String heslo) throws SQLException {
        
        Connection conn = OracleConnector.getConnection();
       // PreparedStatement stmt = conn.prepareStatement("DECLARE c number(2); BEGIN c := PRIHLASENI('" + jmeno + "','" + heslo + "'); dbms_output.put_line('nalezeno ' || c); END;");
               
        CallableStatement cstmt = conn.prepareCall(" BEGIN ? := PRIHLASENIFUNCTION(?,MD5HASH(?)); END;");
        cstmt.registerOutParameter(1, Types.INTEGER);
        cstmt.setString( 2, jmeno );
        cstmt.setString( 3, heslo );
        cstmt.execute();
        int opravneni = cstmt.getInt(1); 
        //System.out.print("Cancellation is "+opravneni);
         if (opravneni == 99) {
             return opravneni;
         }else{
             return opravneni;
         }
    }
    
    public void ulozObrazek(InputStream isi, String nazev, String pripona, int idU) throws SQLException, IOException {
        Image img = ImageIO.read(isi);
        InputStream is;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (pripona.compareToIgnoreCase(".png")==0) {
            ImageIO.write((RenderedImage) img, "png", os);
        }else{
            ImageIO.write((RenderedImage) img, "JPG", os);
        }
        
        is = new ByteArrayInputStream(os.toByteArray());

        Connection conn = OracleConnector.getConnection();
        
        CallableStatement cstmt = conn.prepareCall(" BEGIN inser_update_image(?, ?, ?, ?); END;");
        cstmt.setBinaryStream(1, is);
        cstmt.setString( 2, nazev );
        cstmt.setString( 3, pripona );
        cstmt.setInt( 4, idU );
        cstmt.execute();
        
        /*PreparedStatement stmt = conn.prepareStatement("SELECT nazev,pripona FROM sem_obrazky WHERE id = ?");
        stmt.setInt(1, idU);
        ResultSet rset = stmt.executeQuery();
        if (!rset.next()) {
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO sem_obrazky (id, obrazek, nazev,pripona) VALUES (?,?, ?,?)");
            stmt1.setInt(1, idU);
            stmt1.setBinaryStream(2, is);
            stmt1.setString(3, nazev);
            stmt1.setString(4, pripona);
            stmt1.executeUpdate();
        }else{
            PreparedStatement stmt1 = conn.prepareStatement("UPDATE sem_obrazky SET obrazek = ?, nazev = ?, pripona = ? WHERE id = ?");
            stmt1.setBinaryStream(1, is);
            stmt1.setString(2, nazev);
            stmt1.setString(3, pripona);
            stmt1.setInt(4, idU);
            stmt1.executeUpdate();
        }*/

        
        conn.commit();
    }
    
    public InputStream nactiObrazek(int id) throws SQLException, FileNotFoundException, IOException {
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT nazev, pripona, obrazek FROM sem_obrazky WHERE id_vyucujici = ?");
        stmt.setInt(1, id);
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            Blob blob = conn.createBlob();
            blob = rset.getBlob("obrazek");
            if (blob == null) {
                break;
            }
            InputStream is = blob.getBinaryStream();
            return is;
        }
        return null;
    }
    
    //////////////////////lukas
    public ArrayList<ViewPrihlaseni> getViewPrihlaseni(Fakulta fakulta) throws SQLException {
        ArrayList<ViewPrihlaseni> vyucujici = new ArrayList<>();

        if(fakulta != null){
            Connection conn = OracleConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            "SELECT id_user, username, opravneni, id_vyucujici, prijmeni, jmeno, fakulta" +
            " FROM C##ST52550.PRIHLASENI_POHLED  WHERE fakulta LIKE '" + fakulta.getNazevF() + "' ORDER BY prijmeni");        
                    
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                vyucujici.add(new ViewPrihlaseni(
                        rset.getInt("id_user"),
                        rset.getInt("id_vyucujici"),
                        rset.getString("username"),
                        rset.getString("prijmeni"),
                        rset.getString("jmeno"),
                       rset.getInt("opravneni")));
            }
        }
        return vyucujici;
    }
    public void insertLogin(String username, String heslo, int idU) throws SQLException {
              
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt1 = conn.prepareStatement(
        "INSERT INTO SEM_PRIHLASENI(id_user, username, heslo, opravneni,vyucujici_vyucujici_id) " +
        "VALUES (prihlaseni.NEXTVAL, ?, MD5HASH(?), 1, ?)");
        
        stmt1.setString(1, username);
        stmt1.setString(2, heslo);
        stmt1.setInt(3, idU);
        stmt1.executeUpdate();
        conn.commit();
        
    }
     public void updateLogin(int id_user,String username) throws SQLException {
       Connection conn = OracleConnector.getConnection();
        
        PreparedStatement stmt4 = conn.prepareStatement(
        "UPDATE SEM_PRIHLASENI SET username = ? WHERE id_user = ?");
        
        stmt4.setString(1, username);
        stmt4.setInt(2, id_user);
        stmt4.executeUpdate();
        conn.commit(); 
    }
    public void updateLoginHeslo(int id_user, String heslo) throws SQLException {
       Connection conn = OracleConnector.getConnection();
        
        PreparedStatement stmt4 = conn.prepareStatement(
        "UPDATE SEM_PRIHLASENI SET heslo = MD5HASH(?) WHERE id_user = ?");
        
        stmt4.setString(1, heslo);
        stmt4.setInt(2, id_user);
        stmt4.executeUpdate();
        conn.commit();   
    }
     public void deleteLogin(int id) throws SQLException{
        
        Connection conn = OracleConnector.getConnection();
        
        PreparedStatement stmt2 = conn.prepareStatement(
        "DELETE FROM SEM_PRIHLASENI WHERE id_user = ?");        
        stmt2.setInt(1, id);
        stmt2.executeUpdate();
        conn.commit();
    }
     
    public PridaniAkce formularVyucujici(String titulPred, String jmeno, String prijmeni, String titulZa, 
            int telefon, int mobil, String email) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call formular_vyucujici(?,?,?,?,?,?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, titulPred);
        cstmt.setString(2, jmeno);
        cstmt.setString(3, prijmeni);
        cstmt.setString(4, titulZa);
        cstmt.setInt(5, telefon);
        cstmt.setInt(6, mobil);
        cstmt.setString(7, email);
        
        cstmt.registerOutParameter(8, Types.VARCHAR);
        cstmt.registerOutParameter(9, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(8);
        String h = cstmt.getString(9);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    } 
     
      public PridaniAkce formularPredmet(String zkratka, String nazev, int pocetKreditu, int pocetPr, 
            int pocetCv, int pocetSem, int dopRoc) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call formular_predmet(?,?,?,?,?,?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, zkratka);
        cstmt.setString(2, nazev);
        cstmt.setInt(3, pocetKreditu);
        cstmt.setInt(4, pocetPr);
        cstmt.setInt(5, pocetCv);
        cstmt.setInt(6, pocetSem);
        cstmt.setInt(7, dopRoc);
        
        
        cstmt.registerOutParameter(8, Types.VARCHAR);
        cstmt.registerOutParameter(9, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(8);
        String h = cstmt.getString(9);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    } 
      
    public PridaniAkce formularFakulta(String zkratka, String nazev, int telefon, int mobil, String email) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call formular_fakulta(?,?,?,?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, zkratka);
        cstmt.setString(2, nazev);
        cstmt.setInt(3, telefon);
        cstmt.setInt(4, mobil);
        cstmt.setString(5, email);
        
        
        cstmt.registerOutParameter(6, Types.VARCHAR);
        cstmt.registerOutParameter(7, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(6);
        String h = cstmt.getString(7);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    } 
    
    public PridaniAkce formularKatedra(String zkratka, String nazev) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call formular_katedra(?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, zkratka);
        cstmt.setString(2, nazev);
              
        
        cstmt.registerOutParameter(3, Types.VARCHAR);
        cstmt.registerOutParameter(4, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(3);
        String h = cstmt.getString(4);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    } 
    
    public PridaniAkce formularMistnost(String oznaceni, int kapacita) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call formular_mistnost(?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, oznaceni);
        cstmt.setInt(2, kapacita); 
        
        cstmt.registerOutParameter(3, Types.VARCHAR);
        cstmt.registerOutParameter(4, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(3);
        String h = cstmt.getString(4);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    } 
    
    public PridaniAkce formularObor(String oznaceni, String nazev) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call formular_obor(?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, oznaceni);
        cstmt.setString(2, nazev);
        
        cstmt.registerOutParameter(3, Types.VARCHAR);
        cstmt.registerOutParameter(4, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(3);
        String h = cstmt.getString(4);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    } 
    
     public PridaniAkce formularPrih(String username) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call formular_prihlaseni(?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, username);
                
        
        cstmt.registerOutParameter(2, Types.VARCHAR);
        cstmt.registerOutParameter(3, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(2);
        String h = cstmt.getString(3);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    } 
     
    public PridaniAkce formularPlan(String akRok, int delka, int odhad) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call formular_stud_plan(?,?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setString(1, akRok);
        cstmt.setInt(2, delka);
        cstmt.setInt(3, odhad);     
        
        cstmt.registerOutParameter(4, Types.VARCHAR);
        cstmt.registerOutParameter(5, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(4);
        String h = cstmt.getString(5);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    }
    
    public PridaniAkce formularRozvrh(int casOd, int casDo, int obsazenost, int rozsah) throws SQLException {        
        
        Connection conn = OracleConnector.getConnection();
               
        String statement = "{call formular_rozvrh(?,?,?,?,?,?)}";
        CallableStatement cstmt = conn.prepareCall(statement);
        
        cstmt.setInt(1, casOd);
        cstmt.setInt(2, casDo);
        cstmt.setInt(3, obsazenost);     
        cstmt.setInt(4, rozsah);
        
        cstmt.registerOutParameter(5, Types.VARCHAR);
        cstmt.registerOutParameter(6, Types.VARCHAR);
        
        cstmt.execute();
        
        String b = cstmt.getString(5);
        String h = cstmt.getString(6);
        
        //System.out.println(b + " " + h);
        
        PridaniAkce podminky = new PridaniAkce(b, h);
        
        return podminky;
    }  
             
    //------------ HELP ----
    public boolean controllDB() throws SQLException {
        Connection conn = OracleConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
        "SELECT count(table_name) pocet FROM user_tables WHERE table_name like 'SEM_%'");
        
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            if (rset.getInt("pocet") == 13) {
                return true;
            }
        }
        
        return false;
    }
}
