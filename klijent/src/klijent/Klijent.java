/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klijent;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 *
 * @author korisnik
 */
public class Klijent {

    /**
     * @param args the command line arguments
     */
    public interface Izlaz{
        @POST("mesta/{naziv}/{pbr}")
        Call<String> kreirajMesto(@Path("naziv") String user, @Path("pbr") int pBroj);
        
        @POST("filijale/{naziv}/{adresa}/{idMes}")
        Call<String> kreirajFilijalu(@Path("naziv") String naziv, @Path("adresa") String adresa, @Path("idMes") int idMes);
        
        @POST("komitenti/{naziv}/{adresa}/{idMes}")
        Call<String> kreirajKomitenta(@Path("naziv") String naziv, @Path("adresa") String adresa, @Path("idMes") int idMes);
        
        @PUT("komitenti/{idKom}/{idMes}")
        Call<String> promeniSediste(@Path("idKom") int idKom, @Path("idMes") int idMes);
        
        @POST("racuni/{IdKom}/{IdFil}/{DMinus}")
        Call<String> otvoriRacun(@Path("IdKom") int idKom, @Path("IdFil") int idFil, @Path("DMinus") int dMinus);
        
        @PUT("racuni/{idRac}")
        Call<String> zatvoriRacun(@Path("idRac") int idRac);
        
        @POST("transakcije/{IdRac1}/{IdRac2}/{Iznos}/{Svrha}")
        Call<String> kreirajPrenos(@Path("IdRac1") int idRac1, @Path("IdRac2") int IdRac2, @Path("Iznos") double iznos, @Path("Svrha") String svrha);
        
        @POST("transakcije/u/{IdRac}/{Iznos}/{Svrha}/{IdFil}")
        Call<String> kreirajUplatu(@Path("IdRac") int IdRac, @Path("Iznos") double iznos, @Path("Svrha") String svrha, @Path("IdFil") int IdFil);
        
        @POST("transakcije/i/{IdRac}/{Iznos}/{Svrha}/{IdFil}")
        Call<String> kreirajIsplatu(@Path("IdRac") int IdRac, @Path("Iznos") double iznos, @Path("Svrha") String svrha, @Path("IdFil") int IdFil);
        
        @GET("mesta")
        Call<String> dohvatiSvaMesta();
        
        @GET("filijale")
        Call<String> dohvatiSveFilijale();
        
        @GET("komitenti")
        Call<String> dohvatiSveKomitente();
        
        @GET("racuni/{idKom}")
        Call<String> dohvatiRacuneZaIdKom(@Path("idKom") int idKom);
        
        @GET("transakcije/{idRac}")
        Call<String> dohvatiTransakcijeZaIdRac(@Path("idRac") int idRac);
        
        @GET("backup")
        Call<String> dohvatiSveBackup();
        
        @GET("backup/razlika")
        Call<String> dohvatiSveRazlike();
    }
    
    public static void main(String[] args) {
        int opt;
        Scanner sc=new Scanner(System.in);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/centralniserver/api/").addConverterFactory(ScalarsConverterFactory.create()).build();
        Izlaz izlaz=retrofit.create(Izlaz.class);
        while(true){
            System.out.println("1.  Kreiranje mesta\n2.  Kreiranje filijale u mestu\n3.  Kreiranje komitenta\n4.  Promena sedišta za zadatog komitenta\n"+
                    "5.  Otvaranje racuna\n6.  Zatvaranje racuna\n7.  Kreiranje transakcije koja je prenos sume sa jednog racuna na drugi racun\n8.  Kreiranje transakcije koja je uplata novca na racun\n"+
                    "9.  Kreiranje transakcije koja je isplata novca sa racuna\n10. Dohvatanje svih mesta\n11. Dohvatanje svih filijala\n12. Dohvatanje svih komitenata\n"+
                    "13. Dohvatanje svih računa za komitenta\n14. Dohvatanje svih transakcija za racun\n15. Dohvatanje svih podataka iz rezervne kopije\n16. Dohvatanje razlike u podacima u originalnim podacima i u rezervnoj kopiji\n0. Kraj rada\n\n\n");
            opt=sc.nextInt();
            
            switch(opt){
                case 1:
                {
                    try {
                        System.out.println("Unesite naziv mesta: ");
                        String naziv=sc.next();
                        System.out.println("Unesite Postanski Broj: ");
                        int postanskiBroj=sc.nextInt();
                        Call<String> odgovor = izlaz.kreirajMesto(naziv, postanskiBroj);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case 2:
                {
                    try {
                        System.out.println("Unesite naziv filijale: ");
                        String naziv=sc.next();
                        System.out.println("Unesite adresu filijale: ");
                        String adresa=sc.next();
                        System.out.println("Unesite Id Mesta u kom se Filijala nalazi: ");
                        int idMes=sc.nextInt();
                        Call<String> odgovor =izlaz.kreirajFilijalu(naziv, adresa, idMes);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                 case 3:
                {
                    try {
                        System.out.println("Unesite naziv komitenta: ");
                        String naziv=sc.next();
                        System.out.println("Unesite adresu komitenta: ");
                        String adresa=sc.next();
                        System.out.println("Unesite Id Mesta komitenta: ");
                        int idMes=sc.nextInt();
                        Call<String> odgovor = izlaz.kreirajKomitenta(naziv, adresa, idMes);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                 case 4:
                 {
                   
                    try {
                        System.out.println("Unesite Id Komitenta: ");
                        int idKom = sc.nextInt();
                        System.out.println("Unesite Id Mesta : ");
                        int idMes=sc.nextInt();
                        Call<String> odgovor = izlaz.promeniSediste(idKom, idMes);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 5:
                 {
                   
                    try {
                        System.out.println("Unesite Id Komitenta: ");
                        int idKom = sc.nextInt();
                        System.out.println("Unesite Id Filijale : ");
                        int idFil=sc.nextInt();
                        System.out.println("Unesite koliko iznosi Dozvoljeni minus : ");
                        int dMinus=sc.nextInt();
                        Call<String> odgovor = izlaz.otvoriRacun(idKom, idFil, dMinus);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 6:
                 {
                   
                    try {
                        System.out.println("Unesite Id Racuna koji treba da se zatvori: ");
                        int idRac = sc.nextInt();
                        Call<String> odgovor = izlaz.zatvoriRacun(idRac);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 7:
                 {
                   
                    try {
                        System.out.println("Unesite Id Racuna posiljaoca: ");
                        int idRac1 = sc.nextInt();
                        System.out.println("Unesite Id Racuna primaoca: ");
                        int idRac2 = sc.nextInt();
                        System.out.println("Unesite iznos transakicje prenosa: ");
                        double iznos = sc.nextDouble();
                        System.out.println("Unesite svrhu transakicje prenosa: ");
                        String svrha = sc.next();
                        Call<String> odgovor = izlaz.kreirajPrenos(idRac1, idRac2, iznos, svrha);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 8:
                 {
                   
                    try {
                        System.out.println("Unesite Id Racuna na koji se vrsi uplata: ");
                        int idRac = sc.nextInt();
                        System.out.println("Unesite iznos transakicje uplate: ");
                        double iznos = sc.nextDouble();
                        System.out.println("Unesite svrhu transakicje uplate: ");
                        String svrha = sc.next();
                        System.out.println("Unesite Id Filijale u kojoj se vrsi uplata: ");
                        int idFil = sc.nextInt();
                        Call<String> odgovor = izlaz.kreirajUplatu(idRac, iznos, svrha, idFil);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 9:
                 {
                   
                    try {
                        System.out.println("Unesite Id Racuna na kom se vrsi isplata: ");
                        int idRac = sc.nextInt();
                        System.out.println("Unesite iznos transakicje isplate: ");
                        double iznos = sc.nextDouble();
                        System.out.println("Unesite svrhu transakicje isplate: ");
                        String svrha = sc.next();
                        System.out.println("Unesite Id Filijale u kojoj se vrsi isplata: ");
                        int idFil = sc.nextInt();
                        Call<String> odgovor = izlaz.kreirajIsplatu(idRac, iznos, svrha, idFil);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 10:
                 {
                   
                    try {
                        Call<String> odgovor = izlaz.dohvatiSvaMesta();
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 11:
                 {
                   
                    try {
                        Call<String> odgovor = izlaz.dohvatiSveFilijale();
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 12:
                 {
                   
                    try {
                        Call<String> odgovor = izlaz.dohvatiSveKomitente();
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 13:
                 {
                   
                    try {
                        System.out.println("Unesite Id Komitenta");
                        int idKom = sc.nextInt();
                        Call<String> odgovor = izlaz.dohvatiRacuneZaIdKom(idKom);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 14:
                 {
                   
                    try {
                        System.out.println("Unesite Id Racuna");
                        int idRac = sc.nextInt();
                        Call<String> odgovor = izlaz.dohvatiTransakcijeZaIdRac(idRac);
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 15:
                 {
                   
                    try {
                        Call<String> odgovor = izlaz.dohvatiSveBackup();
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 16:
                 {
                   
                    try {
                        Call<String> odgovor = izlaz.dohvatiSveRazlike();
                        String ispis = odgovor.execute().body();
                        System.out.println(ispis);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                 }
                 
                 case 0:
                 {
                     return;
                 }



            }
            
        }
    }
    
}
