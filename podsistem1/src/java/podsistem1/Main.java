/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entiteti.Filijala;
import entiteti.Komitent;
import entiteti.Mesto;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import wraper.FilijalaResource;
import wraper.KomitentResource;
import wraper.MestoResource;

/**
 *
 * @author korisnik
 */ 

public class Main {
    @Resource(lookup = "Queue11")
    public static Queue queue11;
    @Resource(lookup = "Queue21")
    public static Queue queue21;
    @Resource(lookup = "Queue12")
    public static Queue queue12;
    @Resource(lookup = "Queue31")
    public static Queue queue31;
    @Resource(lookup = "Queue2_1")
    public static Queue queue2_1;
    @Resource(lookup = "Queue35")
    public static Queue queue35;
    @Resource(lookup = "myConnFactory")
    public static ConnectionFactory connFactory;
    /**
     * @param args the command line arguments
     */
    private static String kreirajMesto(EntityManager em, String naziv, int pbr){
        Mesto m=new Mesto();
        m.setNaziv(naziv);
        m.setPostBr(pbr);
        em.persist(m);
        return "ok";
    }
    
    private static String kreirajFilijalu(EntityManager em, String naziv, String adresa, int idMes){
        Mesto mesto=em.find(Mesto.class, idMes);
        if(mesto == null)
            return "Ne postoji mesto za uneti Id!";
        Filijala f=new Filijala();
        f.setAdresa(adresa);
        f.setNaziv(naziv);
        f.setIdMes(mesto);
        em.persist(f);
        return "ok";
    }
    
    private static String kreirajKomitenta(EntityManager em, String naziv, String adresa, int idMes){
        Mesto mesto=em.find(Mesto.class, idMes);
        Komitent k=new Komitent();
        k.setAdresa(adresa);
        k.setNaziv(naziv);
        if(mesto == null)
            return "Ne postoji mesto za uneti Id!";
        k.setIdMes(mesto);
        em.persist(k);
        return "ok";
    }
    
    private static Vector<Mesto> dohvatiSvaMesta(EntityManager em){
        TypedQuery<Mesto> tq=em.createQuery("select m from Mesto m", Mesto.class);
        Vector<Mesto> lista=(Vector<Mesto>)(tq.getResultList());
        return lista;
    }
    
    private static Vector<Komitent> dohvatiSveKomitente(EntityManager em){
        TypedQuery<Komitent> tq=em.createQuery("select k from Komitent k", Komitent.class);
        Vector<Komitent> lista=(Vector<Komitent>)(tq.getResultList());
        return lista;
    }
    
    private static Vector<Filijala> dohvatiSveFilijale(EntityManager em){
        TypedQuery<Filijala> tq=em.createQuery("select f from Filijala f", Filijala.class);
        Vector<Filijala> lista=(Vector<Filijala>)(tq.getResultList());
        return lista;
    }
    
    private static String promeniSediste(EntityManager em, int idKom, int idMes){
        Komitent k=em.find(Komitent.class, idKom);
        if(k==null)
            return "Ne postoji komitent za uneti Id.";
        Mesto m=em.find(Mesto.class, idMes);
        if(m==null)
            return "Ne postoji mesto za uneti Id.";
        k.setIdMes(m);
        em.persist(k);
        return "ok";
    }
    
    private static Pair<Vector<Mesto>, Pair<Vector<Filijala>, Vector<Komitent>>> dohvatiSve(EntityManager em){
        Vector<Mesto> lista1=(Vector<Mesto>)em.createQuery("select f from Mesto f", Mesto.class).getResultList();
        Vector<Filijala> lista2=(Vector<Filijala>)em.createQuery("select f from Filijala f", Filijala.class).getResultList();
        Vector<Komitent> lista3=(Vector<Komitent>)em.createQuery("select f from Komitent f", Komitent.class).getResultList();
        Pair<Vector<Mesto>, Pair<Vector<Filijala>, Vector<Komitent>>> par=new Pair<Vector<Mesto>, Pair<Vector<Filijala>, Vector<Komitent>>>(lista1, new Pair<Vector<Filijala>, Vector<Komitent>>(lista2, lista3));
        
        return par;
    }
    
    public static void main(String[] args) {
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("podsistem1PU");
        EntityManager em=emf.createEntityManager();
        JMSContext context=connFactory.createContext();
        JMSConsumer consumer=context.createConsumer(queue11);
        JMSProducer producer=context.createProducer();
        while(true){
            try {
                System.out.println("podsistem1: cekanje...");
                Message msg=consumer.receive();
                System.out.println("podsistem1: primljena poruka");
                em.getTransaction().begin();
                String poruka="";
                TextMessage txtMsg=context.createTextMessage();
                int zahtev=msg.getIntProperty("zahtev");
                switch(zahtev){
                    case 1:
                        poruka = kreirajMesto(em, msg.getStringProperty("naziv"), msg.getIntProperty("pbr"));
                        break;
                    case 2:
                        poruka= kreirajFilijalu(em, msg.getStringProperty("naziv"), msg.getStringProperty("adresa"), msg.getIntProperty("idMes"));
                        break;
                    case 3:
                        poruka= kreirajKomitenta(em, msg.getStringProperty("naziv"), msg.getStringProperty("adresa"), msg.getIntProperty("idMes"));
                        break;
                    case 4:
                        poruka = promeniSediste(em, msg.getIntProperty("idKom"), msg.getIntProperty("idMes"));
                        break;
                    case 10:{
                        Vector<Mesto> lista=dohvatiSvaMesta(em);
                        Vector<MestoResource> vektor=new Vector<>();
                        for(Mesto m : lista){
                            vektor.add(new MestoResource(m.getIdMes(), m.getNaziv(), m.getPostBr()));
                        }
                        ObjectMessage objMsg=context.createObjectMessage();
                        objMsg.setObject(vektor);
                        em.getTransaction().rollback();
                        producer.send(queue12, objMsg);
                        break;
                    }
                    case 12:
                    {
                        Vector<Filijala> lista=dohvatiSveFilijale(em);
                        Vector<FilijalaResource> vektor=new Vector<>();
                        for(Filijala f : lista){
                            vektor.add(new FilijalaResource(f.getIdFil(), f.getNaziv(),f.getAdresa(),f.getIdMes().getIdMes()));
                        }
                        ObjectMessage objMsg=context.createObjectMessage();
                        objMsg.setObject(vektor);
                        em.getTransaction().rollback();
                        producer.send(queue12, objMsg);
                        break;
                    }
                    case 11:
                    {
                        Vector<Komitent> lista=dohvatiSveKomitente(em);
                        Vector<KomitentResource> vektor=new Vector<>();
                        for(Komitent k : lista){
                            vektor.add(new KomitentResource(k.getIdKom(), k.getNaziv(), k.getAdresa(), k.getIdMes().getIdMes()));
                        }
                        ObjectMessage objMsg=context.createObjectMessage();
                        objMsg.setObject(vektor);
                        em.getTransaction().rollback();
                        producer.send(queue12, objMsg);
                        break;
                    }
                    case 50:
                    {
                        Message info=context.createMessage();
                        info.setBooleanProperty("Kom", true);
                        info.setBooleanProperty("Fil", true);
                        Komitent k=em.find(Komitent.class, msg.getIntProperty("idKom"));
                        if(k == null)
                            info.setBooleanProperty("Kom", false);
                        Filijala f = em.find(Filijala.class, msg.getIntProperty("idFil"));
                        if(f == null)
                            info.setBooleanProperty("Fil", false);
                        producer.send(queue2_1, info);
                        em.getTransaction().rollback();
                        break;
                    }
                    case 31:
                    {
                        ObjectMessage objMsg=context.createObjectMessage(dohvatiSve(em));
                        producer.send(queue31, objMsg);
                        em.getTransaction().rollback();
                        break;
                    }
                    
                    case 33:
                    {
                        ObjectMessage objMsg=context.createObjectMessage(dohvatiSve(em));
                        producer.send(queue35, objMsg);
                        em.getTransaction().rollback();
                        break;
                    }
                }
                
                if(zahtev != 10 && zahtev != 11 && zahtev != 12 && zahtev != 50 && zahtev != 31 && zahtev != 33){
                    if(poruka.equals("ok"))
                        em.getTransaction().commit();
                    else
                        em.getTransaction().rollback();
                    txtMsg.setText(poruka);
                    //txtMsg.setIntProperty("aa", msg.getIntProperty("idMes"));
                
                    producer.send(queue12, txtMsg);
                    //System.out.println("Poslata poruka ka serveru sa idMes: "+msg.getIntProperty("idMes")+"   "+poruka);
                }
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
