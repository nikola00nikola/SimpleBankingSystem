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
public class Nit extends Thread{

    public  JMSContext context;
    public  Queue queue35;
    public  Queue queue32;
    public  Queue queue33;
    public  Queue queue3_1;
    public  Queue queue34;
    public  Queue queue11;
    public  Queue queue21;
    public EntityManager em;

    public Queue getQueue11() {
        return queue11;
    }

    public void setQueue11(Queue queue11) {
        this.queue11 = queue11;
    }

    public Queue getQueue21() {
        return queue21;
    }

    public void setQueue21(Queue queue21) {
        this.queue21 = queue21;
    }

    
    
    public Queue getQueue34() {
        return queue34;
    }

    public void setQueue34(Queue queue34) {
        this.queue34 = queue34;
    }

    
    
    public Queue getQueue3_1() {
        return queue3_1;
    }

    public void setQueue3_1(Queue queue3_1) {
        this.queue3_1 = queue3_1;
    }

    
    
    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
    
    

    public JMSContext getContext() {
        return context;
    }

    public void setContext(JMSContext context) {
        this.context = context;
    }

    
    public Queue getQueue35() {
        return queue35;
    }

    public void setQueue35(Queue queue35) {
        this.queue35 = queue35;
    }

    public  Queue getQueue32() {
        return queue32;
    }

    public  void setQueue32(Queue queue32) {
        this.queue32 = queue32;
    }

    public  Queue getQueue33() {
        return queue33;
    }

    public  void setQueue33(Queue queue33) {
        this.queue33 = queue33;
    }
    
    private String dohvatiSve(EntityManager em){
        Vector<Mesto> lista1=(Vector<Mesto>)em.createQuery("select f from Mesto f", Mesto.class).getResultList();
        Vector<Filijala> lista2=(Vector<Filijala>)em.createQuery("select f from Filijala f", Filijala.class).getResultList();
        Vector<Komitent> lista3=(Vector<Komitent>)em.createQuery("select f from Komitent f", Komitent.class).getResultList();
        Vector<Racun> lista4=(Vector<Racun>)em.createQuery("select f from Racun f", Racun.class).getResultList();
        Vector<Transakcija> lista5=(Vector<Transakcija>)em.createQuery("select f from Transakcija f", Transakcija.class).getResultList();
        Vector<Ucestvuje> lista6=(Vector<Ucestvuje>)em.createQuery("select f from Ucestvuje f", Ucestvuje.class).getResultList();
        
        StringBuilder sb=new StringBuilder("Mesto: idMes Naziv Postanski_broj\n");
        for(Mesto m:lista1)
            sb.append(m.getIdMes()).append("   ").append(m.getNaziv()).append("   ").append(m.getPostBr()).append('\n');
        sb.append("Filijala: idFil Naziv Adresa Mesto\n");
        for(Filijala f:lista2)
            sb.append(f.getIdFil()).append("   ").append(f.getNaziv()).append("   ").append(f.getAdresa())
                .append("   ").append(f.getIdMes().getIdMes()).append('\n');
        sb.append("Komitent: idKom Naziv Adresa Sediste\n");
        for(Komitent k: lista3)
             sb.append(k.getIdKom()).append("   ").append(k.getNaziv()).append("   ").append(k.getAdresa()).append("   ")
                 .append(k.getIdMes().getIdMes()).append('\n');
        sb.append("Racun: idRac Stanje Status DozvMinus DatumVreme BrojTrans Kom Fil\n");
        for(Racun r:lista4)
            sb.append(r.getIdRac()).append("   ").append(r.getStanje()).append("   ").append(r.getStatus())
                        .append("   ").append(r.getDozvMinus()).append("   ").append(r.getDatumVreme()).append("   ")
                        .append(r.getBrojTrans()).append("   ").append(r.getIdKom()).append("   ").append(r.getIdFil()).append("\n");
        sb.append("Transakcije: idTra DatumVreme Iznos Svrha Vrsta idFil\n");
        for(Transakcija t:lista5)
            sb.append(t.getIdTra()).append("   ").append(t.getDatumVreme()).append("   ").append(t.getIznos())
                    .append("   ").append(t.getSvrha()).append("   ").append(t.getVrsta())
                        .append("   ").append(t.getIdFil()).append("\n");
        sb.append("Ucestvuje: idTra idRac RedBroj Prvi\n");
        for(Ucestvuje u: lista6)
            sb.append(u.getUcestvujePK().getIdTra()).append("   ").append(u.getUcestvujePK().getIdRac()).append("   ").append(u.getRedBroj())
                    .append("   ").append(u.getPrvi()).append("\n");
        String izlaz=sb.toString();
        return izlaz;
    }
    
    private  String dohvatiRazlike(EntityManager em){
        try {
            Message poruka1=context.createMessage();
            poruka1.setIntProperty("zahtev", 33);
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer1=context.createConsumer(queue35);
            producer.send(queue11, poruka1);
            ObjectMessage objmsg1=(ObjectMessage)consumer1.receive();
            Pair<Vector<Mesto>, Pair<Vector<Filijala>, Vector<Komitent>>> par1=(Pair<Vector<Mesto>, Pair<Vector<Filijala>, Vector<Komitent>>>)objmsg1.getObject();
            
            Message poruka2=context.createMessage();
            poruka2.setIntProperty("zahtev", 34);
            producer.send(queue21, poruka2);
            ObjectMessage objMsg2=(ObjectMessage)consumer1.receive();
            Pair<Vector<Racun>, Pair<Vector<Transakcija>, Vector<Ucestvuje>>> par2=(Pair<Vector<Racun>, Pair<Vector<Transakcija>, Vector<Ucestvuje>>>)objMsg2.getObject();
            
            Vector<Mesto> lista1=(Vector<Mesto>)em.createQuery("select f from Mesto f", Mesto.class).getResultList();
            Vector<Filijala> lista2=(Vector<Filijala>)em.createQuery("select f from Filijala f", Filijala.class).getResultList();
            Vector<Komitent> lista3=(Vector<Komitent>)em.createQuery("select f from Komitent f", Komitent.class).getResultList();
            Vector<Racun> lista4=(Vector<Racun>)em.createQuery("select f from Racun f", Racun.class).getResultList();
            Vector<Transakcija> lista5=(Vector<Transakcija>)em.createQuery("select f from Transakcija f", Transakcija.class).getResultList();
            Vector<Ucestvuje> lista6=(Vector<Ucestvuje>)em.createQuery("select f from Ucestvuje f", Ucestvuje.class).getResultList();
            
            StringBuilder sb=new StringBuilder("Mesto: idMes Naziv Postanski_broj\n");
            int l1=lista1.size();
            int l2=par1.getKey().size();
            for(int i=l1;i<l2;i++){
                Mesto m = par1.getKey().get(i);
                sb.append(m.getIdMes()).append("   ").append(m.getNaziv()).append("   ").append(m.getPostBr()).append('\n');
            }
            
            sb.append("Filijala: idFil Naziv Adresa Mesto\n");
            l1=lista2.size();
            l2=par1.getValue().getKey().size();
            for(int i=l1;i<l2;i++){
                Filijala f = par1.getValue().getKey().get(i);
                sb.append(f.getIdFil()).append("   ").append(f.getNaziv()).append("   ").append(f.getAdresa())
                    .append("   ").append(f.getIdMes()).append('\n');
            }
            
            sb.append("Komitent: idKom Naziv Adresa Sediste\n");
            l1=lista3.size();
            l2=par1.getValue().getValue().size();
            for(int i=0;i<l1;i++){
                Komitent k1=par1.getValue().getValue().get(i);
                Komitent k2=lista3.get(i);
                if(!k1.getIdMes().getIdMes().equals(k2.getIdMes().getIdMes()))
                    sb.append(k1.getIdKom()).append("   ").append(k1.getNaziv()).append("   ").append(k1.getAdresa()).append("   ")
                        .append(k1.getIdMes().getIdMes()).append('\n');
            }
            for(int i=l1;i<l2;i++){
                Komitent k = par1.getValue().getValue().get(i);
                sb.append(k.getIdKom()).append("   ").append(k.getNaziv()).append("   ").append(k.getAdresa()).append("   ")
                    .append(k.getIdMes().getIdMes()).append('\n');
            }
            
            sb.append("Racun: idRac Stanje Status DozvMinus DatumVreme BrojTrans Kom Fil\n");
            l1=lista4.size();
            l2=par2.getKey().size();
            for(int i=0;i<l1;i++){
                Racun r1=par2.getKey().get(i);
                Racun r2=lista4.get(i);
                if(!r1.getStatus().equals(r2.getStatus()) || r1.getBrojTrans() != r2.getBrojTrans())
                    sb.append(r1.getIdRac()).append("   ").append(r1.getStanje()).append("   ").append(r1.getStatus())
                        .append("   ").append(r1.getDozvMinus()).append("   ").append(r1.getDatumVreme()).append("   ")
                        .append(r1.getBrojTrans()).append("   ").append(r1.getIdKom()).append("   ").append(r1.getIdFil()).append("\n");
            }
            for(int i=l1;i<l2;i++){
                Racun r=par2.getKey().get(i);
                sb.append(r.getIdRac()).append("   ").append(r.getStanje()).append("   ").append(r.getStatus())
                        .append("   ").append(r.getDozvMinus()).append("   ").append(r.getDatumVreme()).append("   ")
                        .append(r.getBrojTrans()).append("   ").append(r.getIdKom()).append("   ").append(r.getIdFil()).append("\n");
            }
            
            sb.append("Transakcije: idTra DatumVreme Iznos Svrha Vrsta idFil\n");
            l1=lista5.size();
            l2=par2.getValue().getKey().size();
            for(int i=l1;i<l2;i++){
                Transakcija t=par2.getValue().getKey().get(i);
                sb.append(t.getIdTra()).append("   ").append(t.getDatumVreme()).append("   ").append(t.getIznos())
                    .append("   ").append(t.getSvrha()).append("   ").append(t.getVrsta())
                        .append("   ").append(t.getIdFil()).append("\n");
            }
            
            sb.append("Ucestvuje: idTra idRac RedBroj Prvi\n");
            l1=lista6.size();
            l2=par2.getValue().getValue().size();
            for(int i=l1;i<l2;i++){
                Ucestvuje u=par2.getValue().getValue().get(i);
                sb.append(u.getUcestvujePK().getIdTra()).append("   ").append(u.getUcestvujePK().getIdRac()).append("   ").append(u.getRedBroj())
                    .append("   ").append(u.getPrvi()).append("\n");
            }
            String izlaz = sb.toString();
            consumer1.close();
            return izlaz;
        } catch (JMSException ex) {
            Logger.getLogger(Nit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public void run() {
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("podsistem3PU");
        EntityManager em=emf.createEntityManager();
        JMSConsumer consumer = context.createConsumer(queue32);
        JMSConsumer consumer3 = context.createConsumer(queue33);
        JMSProducer producer = context.createProducer();
        while(true){
            try {
                System.out.println("cekanje..");
                Message msg=consumer.receive();
                System.out.println("podsistem3 nit primljena poruka..");
                switch(msg.getIntProperty("zahtev")){
                    case 404:
                    {
                        Message por=context.createMessage();
                        producer.send(queue3_1, por);
                        System.out.println("nit pauzirana");
                        Message receive = consumer3.receive();
                        System.out.println("nit nastavljena");
                        break;
                    }
                    case 15:
                    {
                        em.getTransaction().begin();
                        ObjectMessage objMsg=context.createObjectMessage(dohvatiSve(em));
                        producer.send(queue34, objMsg);
                        em.getTransaction().rollback();
                        break;
                    }
                    case 16:
                    {
                        em.getTransaction().begin();
                        ObjectMessage objMsg=context.createObjectMessage(dohvatiRazlike(em));
                        producer.send(queue34, objMsg);
                        em.getTransaction().rollback();
                        break;
                    }
                }
                
            } catch (JMSException ex) {
                Logger.getLogger(Nit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
