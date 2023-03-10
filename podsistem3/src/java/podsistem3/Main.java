/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entiteti.Filijala;
import entiteti.Komitent;
import entiteti.Mesto;
import entiteti.Racun;
import entiteti.Transakcija;
import entiteti.Ucestvuje;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author korisnik
 */
public class Main {
    @Resource(lookup = "myConnFactory3")
    public static ConnectionFactory connFactory;
    @Resource(lookup = "Queue31")
    public static Queue queue31;
    @Resource(lookup = "Queue32")
    public static Queue queue32;
    @Resource(lookup = "Queue33")
    public static Queue queue33;
    @Resource(lookup = "Queue34")
    public static Queue queue34;
    @Resource(lookup = "Queue21")
    public static Queue queue21;
    @Resource(lookup = "Queue11")
    public static Queue queue11;
    @Resource(lookup = "Queue3_1")
    public static Queue queue3_1;
    @Resource(lookup = "Queue35")
    public static Queue queue35;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            EntityManagerFactory emf=Persistence.createEntityManagerFactory("podsistem3PU");
            EntityManager em=emf.createEntityManager();
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer1= context.createConsumer(queue31);
            JMSConsumer consumer2= context.createConsumer(queue3_1);
            JMSProducer producer=context.createProducer();
            Nit nit=new Nit();
            nit.setContext(connFactory.createContext());
            nit.setQueue35(queue35);
            nit.setQueue32(queue32);
            nit.setQueue33(queue33);
            nit.setQueue34(queue34);
            nit.setQueue3_1(queue3_1);
            nit.setQueue11(queue11);
            nit.setQueue21(queue21);
            nit.setEm(em);
            nit.start();
            while(true){
                Thread.sleep(110000);
                Message msg=context.createMessage();
                msg.setIntProperty("zahtev", 404);
                producer.send(queue32, msg);
                Message receive = consumer2.receive();
                System.out.println("Backup je krenuo");
                
                em.getTransaction().begin();

                em.createQuery("delete from Komitent").executeUpdate();
                em.createQuery("delete from Filijala").executeUpdate();
                em.createQuery("delete from Mesto").executeUpdate();
                em.createQuery("delete from Ucestvuje").executeUpdate();
                em.createQuery("delete from Transakcija").executeUpdate();
                em.createQuery("delete from Racun").executeUpdate();
                Message poruka1=context.createMessage();
                poruka1.setIntProperty("zahtev", 31);
                producer.send(queue11, poruka1);

                ObjectMessage objmsg1=(ObjectMessage)consumer1.receive();
                System.out.println("backup1 primljena poruka");
                Pair<Vector<Mesto>, Pair<Vector<Filijala>, Vector<Komitent>>> par1=(Pair<Vector<Mesto>, Pair<Vector<Filijala>, Vector<Komitent>>>)objmsg1.getObject();
                for(Mesto m : par1.getKey())
                    em.createNativeQuery("insert into mesto (IdMes, PostBr, Naziv) values (?, ?, ?)").setParameter(1, m.getIdMes())
                              .setParameter(2, m.getPostBr()).setParameter(3, m.getNaziv()).executeUpdate();
                for(Filijala f : par1.getValue().getKey())
                    em.createNativeQuery("insert into filijala (IdFil, Adresa, Naziv, IdMes) values (?, ?, ?, ?)").setParameter(1, f.getIdFil())
                        .setParameter(2, f.getAdresa()).setParameter(3, f.getNaziv()).setParameter(4, f.getIdMes().getIdMes()).executeUpdate();
                for(Komitent k:par1.getValue().getValue())
                     em.createNativeQuery("insert into komitent (IdKom, Adresa, Naziv, IdMes) values (?, ?, ?, ?)").setParameter(1, k.getIdKom())
                        .setParameter(2, k.getAdresa()).setParameter(3, k.getNaziv()).setParameter(4, k.getIdMes().getIdMes()).executeUpdate();

                Message poruka2=context.createMessage();
                poruka2.setIntProperty("zahtev", 32);
                producer.send(queue21, poruka2);
                ObjectMessage objMsg2=(ObjectMessage)consumer1.receive();
                System.out.println("backup2 prijmljena poruka");
                Pair<Vector<Racun>, Pair<Vector<Transakcija>, Vector<Ucestvuje>>> par2=(Pair<Vector<Racun>, Pair<Vector<Transakcija>, Vector<Ucestvuje>>>)objMsg2.getObject();

                for(Racun r : par2.getKey())
                    em.createNativeQuery("insert into racun (IdRac, Stanje, Status, DozvMinus, DatumVreme, BrojTrans, IdKom, IdFil) values (?, ?, ?, ?, ?, ?, ?, ?)")
                        .setParameter(1, r.getIdRac()).setParameter(2, r.getStanje()).setParameter(3, r.getStatus()).setParameter(4, r.getDozvMinus())
                            .setParameter(5, r.getDatumVreme()).setParameter(6, r.getBrojTrans()).setParameter(7, r.getIdKom()).setParameter(8, r.getIdFil()).executeUpdate();
                for(Transakcija t : par2.getValue().getKey())
                    em.createNativeQuery("insert into transakcija (IdTra, DatumVreme, Iznos, Svrha, Vrsta, IdFil) values (?, ?, ?, ?, ?, ?)")
                        .setParameter(1, t.getIdTra()).setParameter(2, t.getDatumVreme()).setParameter(3, t.getIznos()).setParameter(4, t.getSvrha())
                            .setParameter(5, t.getVrsta()).setParameter(6, t.getIdFil()).executeUpdate();
                for(Ucestvuje u : par2.getValue().getValue())
                    em.createNativeQuery("insert into ucestvuje (IdTra, IdRac, RedBroj, Prvi) values (?, ?, ?, ?)").setParameter(1, u.getUcestvujePK().getIdTra())
                        .setParameter(2, u.getUcestvujePK().getIdRac()).setParameter(3, u.getRedBroj()).setParameter(4, u.getPrvi()).executeUpdate();

                em.getTransaction().commit();
                System.out.println("Backup odradjen");   
                Message por=context.createMessage();
                
                producer.send(queue33, msg);
                   
            }
            } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    
}
