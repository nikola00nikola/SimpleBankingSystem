/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;


import entiteti.Racun;
import entiteti.Transakcija;
import entiteti.Ucestvuje;
import java.util.Date;
import java.util.List;
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
import wraper.RacunResource;
import wraper.TransakcijaResource;

/**
 *
 * @author korisnik
 */ 

public class Main {
    @Resource(lookup = "Queue21")
    public static Queue queue21;
    @Resource(lookup = "Queue11")
    public static Queue queue11;
    @Resource(lookup = "Queue2_1")
    public static Queue queue2_1;
    @Resource(lookup = "Queue22")
    public static Queue queue22;
    @Resource(lookup = "Queue31")
    public static Queue queue31;
    @Resource(lookup = "Queue35")
    public static Queue queue35;
    @Resource(lookup = "myConnFactory")
    public static ConnectionFactory connFactory;
    /**
     * @param args the command line arguments
     */
    private static String otvoriRacun(EntityManager em, JMSContext context, JMSProducer producer, int idKom, int idFil, int dozvMinus){
        try {
            JMSConsumer consumer = context.createConsumer(queue2_1);
            Message msgKom=context.createMessage();
            msgKom.setIntProperty("idKom", idKom);
            msgKom.setIntProperty("idFil", idFil);
            msgKom.setIntProperty("zahtev", 50);
            producer.send(queue11, msgKom);
            
            Message prijem = consumer.receive();
            consumer.close();
            if(!prijem.getBooleanProperty("Kom"))
                return "Ne postoji Komitent za zadati Id.";
            if(!prijem.getBooleanProperty("Fil"))
                return "Ne postoji Filijala za zadati Id. ";
            Date curentDate=new Date();
            Racun racun=new Racun();
            racun.setBrojTrans(0);
            racun.setDatumVreme(curentDate);
            racun.setDozvMinus(dozvMinus);
            racun.setIdFil(idFil);
            racun.setIdKom(idKom);
            racun.setStanje(0);
            racun.setStatus("A");
            em.persist(racun);
            return "ok";
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "greska";
    }
    
    private static String zatvoriRacun(EntityManager em, int idRac){
        Racun racun=em.find(Racun.class, idRac);
        if(racun == null)
            return "Ne postoji racun za uneti Id.";
        if(racun.getStatus().equals("Z"))
            return "Racun je vec ugasen.";
       racun.setStatus("Z");
       em.persist(racun);
       return "ok";
    }
  
    private static String kreirajUplatu(EntityManager em, int idRac, double iznos, String svrha, int idFil){
        Racun racun=em.find(Racun.class, idRac);
        if(racun == null)
            return "Ne postoji racun za uneti Id." + idRac;
        if(racun.getStatus().equals("Z"))
            return "Racun je ugasen";
        List<Integer> lista=em.createQuery("select max( t.idTra) from Transakcija t", Integer.class).getResultList();
        int idTra;
        if(lista!=null && !lista.isEmpty() && lista.get(0) != null)
            idTra = (int)(lista.get(0)) + 1;
        else
            idTra=1;
        Transakcija tr=new Transakcija(idTra, new Date(), iznos, svrha, "U", idFil);
        racun.setBrojTrans(racun.getBrojTrans()+1);
        racun.setStanje(racun.getStanje()+iznos);
        if(racun.getStatus().equals("B") && racun.getStanje() + racun.getDozvMinus() > 0)
            racun.setStatus("A");
        Ucestvuje uc=new Ucestvuje(idTra, racun.getIdRac());
        uc.setRedBroj(racun.getBrojTrans());
        uc.setTransakcija(tr);
        uc.setRacun(racun);
        em.persist(racun);
        em.persist(tr);
       // em.getTransaction().commit();
       // em.getTransaction().begin();
        em.persist(uc);
        
        return "ok";
    }
    
    private static String kreirajIsplatu(EntityManager em, int idRac, double iznos, String svrha, int idFil){
        Racun racun=em.find(Racun.class, idRac);
        if(racun == null)
            return "Ne postoji racun za uneti Id." + idRac;
        if(racun.getStatus().equals("Z"))
            return "Racun je ugasen.";
        if(racun.getStatus().equals("B"))
            return "Racun je blokiran.";
        List<Integer> lista=em.createQuery("select max( t.idTra) from Transakcija t", Integer.class).getResultList();
        int idTra;
        if(lista!=null && !lista.isEmpty() && lista.get(0) != null)
            idTra = (int)(lista.get(0)) + 1;
        else
            idTra=1;
        Transakcija tr=new Transakcija(idTra, new Date(), iznos, svrha, "I", idFil);
        racun.setBrojTrans(racun.getBrojTrans()+1);
        racun.setStanje(racun.getStanje()-iznos);
        if(racun.getStanje() + racun.getDozvMinus() < 0)
            racun.setStatus("B");
        Ucestvuje uc=new Ucestvuje(idTra, racun.getIdRac());
        uc.setRedBroj(racun.getBrojTrans());
        uc.setTransakcija(tr);
        uc.setRacun(racun);
        em.persist(racun);
        em.persist(tr);
       // em.getTransaction().commit();
       // em.getTransaction().begin();
        em.persist(uc);
        
        return "ok";
    }
    
    private static String kreirajTransakcijuPrenosa(EntityManager em, int idRac1, int idRac2, double iznos, String svrha){
        Racun racun1=em.find(Racun.class, idRac1);
        Racun racun2=em.find(Racun.class, idRac2);
        if(racun1 == racun2)
            return "Uneti su jednaki identifikatori za racune.";
        if(racun1 == null)
            return "Ne postoji racun " + idRac1;
        if(racun2 == null)
            return "Ne postoji racun " + idRac2;
        if(racun1.getStatus().equals("Z"))
            return "Racun "+ idRac1+"je ugasen.";
        if(racun2.getStatus().equals("Z"))
            return "Racun "+ idRac2+"je ugasen.";
        if(racun1.getStatus().equals("B"))
            return "Racun " + idRac1+ " je blokiran.";
        
        List<Integer> lista=em.createQuery("select max( t.idTra) from Transakcija t", Integer.class).getResultList();
        int idTra;
        if(lista!=null && !lista.isEmpty() && lista.get(0) != null)
            idTra = (int)(lista.get(0)) + 1;
        else
            idTra=1;
        Transakcija tr=new Transakcija(idTra, new Date(), iznos, svrha, "P");
        tr.setIdTra(idTra);
        racun1.setBrojTrans(racun1.getBrojTrans()+1);
        racun1.setStanje(racun1.getStanje()-iznos);
        racun2.setBrojTrans(racun2.getBrojTrans()+1);
        racun2.setStanje(racun2.getStanje()+iznos);
        if(racun1.getStanje() + racun1.getDozvMinus() < 0)
            racun1.setStatus("B");
        if(racun2.getStatus().equals("B") && racun2.getStanje() + racun2.getDozvMinus() > 0)
            racun2.setStatus("A");
        Ucestvuje uc1=new Ucestvuje(tr.getIdTra(), racun1.getIdRac());
        Ucestvuje uc2=new Ucestvuje(tr.getIdTra(), racun2.getIdRac());
        uc1.setTransakcija(tr);
        uc2.setTransakcija(tr);
        uc1.setRacun(racun1);
        uc2.setRacun(racun2);
        uc2.setPrvi(Boolean.FALSE);
        uc1.setRedBroj(racun1.getBrojTrans());
        uc2.setRedBroj(racun2.getBrojTrans());
        em.persist(racun1);
        em.persist(racun2);
        em.persist(tr);
        //em.getTransaction().commit();
        //em.getTransaction().begin();
        em.persist(uc1);
        em.persist(uc2);
        
        return "ok";
    }
    
    
    private static Vector<Racun> dohvatiSveRacune(EntityManager em, int idKom){
        List<Racun> racuni=em.createQuery("select r from Racun r where r.idKom = :idKom", Racun.class).setParameter("idKom", idKom).getResultList();
        if(racuni == null || racuni.isEmpty())
            return null;
        Vector<Racun> lista=(Vector<Racun>)(racuni);
        if(lista.isEmpty())
            return null;
        return lista;
    }
    
    private static Vector<Ucestvuje> dohvatiSveTransakcije(EntityManager em, int idRac){
        Racun racun=em.find(Racun.class, idRac);
        if(racun == null)
            return null;
        List<Ucestvuje> transakcije=em.createQuery("select u from Ucestvuje u where u.racun.idRac = :idRac", Ucestvuje.class).setParameter("idRac", idRac).getResultList();
        if(transakcije == null || transakcije.isEmpty())
            return null;
        Vector<Ucestvuje> lista=(Vector<Ucestvuje>) transakcije;
        return lista;
    }
    
    private static Pair<Vector<Racun>, Pair<Vector<Transakcija>, Vector<Ucestvuje>>> dohvatiSve(EntityManager em){
        Vector<Racun> lista1=(Vector<Racun>)em.createQuery("select f from Racun f", Racun.class).getResultList();
        Vector<Transakcija> lista2=(Vector<Transakcija>)em.createQuery("select f from Transakcija f", Transakcija.class).getResultList();
        Vector<Ucestvuje> lista3=(Vector<Ucestvuje>)em.createQuery("select f from Ucestvuje f", Ucestvuje.class).getResultList();
        Pair<Vector<Racun>, Pair<Vector<Transakcija>, Vector<Ucestvuje>>> par=new Pair<>(lista1, new Pair<Vector<Transakcija>, Vector<Ucestvuje>>(lista2, lista3));
        
        return par;
    }
    
    public static void main(String[] args) {
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("podsistem2PU");
        EntityManager em=emf.createEntityManager();
        JMSContext context=connFactory.createContext();
        JMSConsumer consumer=context.createConsumer(queue21);
        JMSProducer producer=context.createProducer();
        
        while(true){
            try {
                System.out.println("podsistem2: cekanje...");
                Message msg=consumer.receive();
                System.out.println("podsistem2: primljena poruka");
                em.getTransaction().begin();
                String poruka="";
                TextMessage txtMsg=context.createTextMessage();
                int zahtev=msg.getIntProperty("zahtev");
                switch(zahtev){
                    case 5:
                        poruka=otvoriRacun(em, context, producer, msg.getIntProperty("idKom"), msg.getIntProperty("idFil"), msg.getIntProperty("DMinus"));
                        break;
                    case 6:
                        poruka=zatvoriRacun(em, msg.getIntProperty("idRac"));
                        break;
                    case 7:
                        poruka=kreirajTransakcijuPrenosa(em, msg.getIntProperty("idRac1"), msg.getIntProperty("idRac2"), msg.getDoubleProperty("iznos"), msg.getStringProperty("svrha"));
                        break;
                    case 8:
                        poruka=kreirajUplatu(em, msg.getIntProperty("idRac"), msg.getDoubleProperty("iznos"), msg.getStringProperty("svrha"), msg.getIntProperty("idFil"));
                        break;
                    case 9:
                        poruka=kreirajIsplatu(em, msg.getIntProperty("idRac"), msg.getDoubleProperty("iznos"), msg.getStringProperty("svrha"), msg.getIntProperty("idFil"));
                        break;
                    case 13:
                    {
                        Vector<Racun> lista=dohvatiSveRacune(em, msg.getIntProperty("idKom"));
                        ObjectMessage objMsg=context.createObjectMessage();
                        if(lista == null){
                            objMsg.setBooleanProperty("uspeh", false);
                        }else{
                            Vector<RacunResource> vektor=new Vector<>();
                            for(Racun r : lista){
                                vektor.add(new RacunResource(r.getIdRac(), r.getDozvMinus(), r.getBrojTrans(), r.getIdKom(), r.getIdFil(), r.getStanje(), r.getStatus(), r.getDatumVreme()));
                            }
                            objMsg.setObject(vektor);
                            objMsg.setBooleanProperty("uspeh", true);
                        }
                        
            
                        em.getTransaction().rollback();
                        producer.send(queue22, objMsg);
                        break;
                    }
                     case 14:
                    {
                        Vector<Ucestvuje> lista=dohvatiSveTransakcije(em, msg.getIntProperty("idRac"));
                        Vector<TransakcijaResource> vektor=new Vector<>();
                        ObjectMessage objMsg=context.createObjectMessage();
                        if(lista == null){
                            objMsg.setBooleanProperty("uspeh", false);
                        }else{
                            for(Ucestvuje u : lista){
                            String vrsta=u.getTransakcija().getVrsta();
                            if(vrsta.equals("P")){
                                if(u.getPrvi().equals(true))
                                    vrsta="P-";
                                else
                                    vrsta="P+";
                            }
                            vektor.add(new TransakcijaResource(u.getTransakcija().getIdTra(), u.getTransakcija().getIdFil(), u.getRedBroj()
                                    , u.getTransakcija().getDatumVreme(), u.getTransakcija().getIznos(), u.getTransakcija().getSvrha(), vrsta));
                        }
                            objMsg.setObject(vektor);
                            objMsg.setBooleanProperty("uspeh", true);
                        }
                        
                        
                        
                        em.getTransaction().rollback();
                        producer.send(queue22, objMsg);
                        break;
                    }
                    
                    case 32:
                    {
                        ObjectMessage objMsg=context.createObjectMessage(dohvatiSve(em));
                        producer.send(queue31, objMsg);
                        em.getTransaction().rollback();
                        break;
                    }
                    
                    case 34:
                    {
                        ObjectMessage objMsg=context.createObjectMessage(dohvatiSve(em));
                        producer.send(queue35, objMsg);
                        em.getTransaction().rollback();
                        break;
                    }
                   
                }
                
                if(zahtev != 13 && zahtev != 14 && zahtev != 12 && zahtev != 32 && zahtev!=34){
                    if(poruka.equals("ok"))
                        em.getTransaction().commit();
                    else
                        em.getTransaction().rollback();
                    txtMsg.setText(poruka);
                    //txtMsg.setIntProperty("aa", msg.getIntProperty("idMes"));
                
                    producer.send(queue22, txtMsg);
                    //System.out.println("Poslata poruka ka serveru sa idMes: "+msg.getIntProperty("idMes")+"   "+poruka);
                }
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
