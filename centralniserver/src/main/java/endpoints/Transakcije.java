/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import wraper.TransakcijaResource;

/**
 *
 * @author korisnik
 */
@Path("transakcije")
public class Transakcije {
   
    @Resource(lookup = "Queue21")
    public Queue queue21;
    @Resource(lookup = "Queue22")
    public Queue queue22;
    @Resource(lookup = "myConnFactory")
    public ConnectionFactory connFactory;
    
  
    
    @POST
    @Path("u/{IdRac}/{Iznos}/{Svrha}/{IdFil}")
    public Response kreirajUplatu(@PathParam("IdRac") int idRac, @PathParam("Iznos") double iznos, @PathParam("Svrha") String svrha, @PathParam("IdFil") int idFil){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue22);
            
            Message msg=context.createMessage();
            msg.setIntProperty("idRac", idRac);
            msg.setDoubleProperty("iznos", iznos);
            msg.setStringProperty("svrha", svrha);
            msg.setIntProperty("idFil", idFil);
            msg.setIntProperty("zahtev", 8);
            
            producer.send(queue21, msg);
            
            TextMessage txtMsg= (TextMessage) consumer.receive();
            String text=txtMsg.getText();
            consumer.close();
            context.close();
            if(text.equals("ok"))
                return Response.ok("Uplata je uspesno realizovana.").build();
            return Response.ok(new String(text)).build();
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
    
    @POST
    @Path("i/{IdRac}/{Iznos}/{Svrha}/{IdFil}")
    public Response kreirajIsplatu(@PathParam("IdRac") int idRac, @PathParam("Iznos") double iznos, @PathParam("Svrha") String svrha, @PathParam("IdFil") int idFil){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue22);
            
            Message msg=context.createMessage();
            msg.setIntProperty("idRac", idRac);
            msg.setDoubleProperty("iznos", iznos);
            msg.setStringProperty("svrha", svrha);
            msg.setIntProperty("idFil", idFil);
            msg.setIntProperty("zahtev", 9);
            
            producer.send(queue21, msg);
            
            TextMessage txtMsg= (TextMessage) consumer.receive();
            String text=txtMsg.getText();
            consumer.close();
            context.close();
            if(text.equals("ok"))
                return Response.ok("Isplata je uspesno realizovana.").build();
            return Response.ok(new String(text)).build();
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
    
    @POST
    @Path("{IdRac1}/{IdRac2}/{Iznos}/{Svrha}")
    public Response kreirajPrenos(@PathParam("IdRac1") int idRac1, @PathParam("IdRac2") int idRac2, @PathParam("Iznos") double iznos, @PathParam("Svrha") String svrha){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue22);
            
            Message msg=context.createMessage();
            msg.setIntProperty("idRac1", idRac1);
            msg.setIntProperty("idRac2", idRac2);
            msg.setDoubleProperty("iznos", iznos);
            msg.setStringProperty("svrha", svrha);
            msg.setIntProperty("zahtev", 7);
            
            producer.send(queue21, msg);
            
            TextMessage txtMsg= (TextMessage) consumer.receive();
            String text=txtMsg.getText();
            consumer.close();
            context.close();
            if(text.equals("ok"))
                return Response.ok("Transakcija prenosa je uspesno realizovana.").build();
            return Response.ok(new String(text)).build();
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
    
    @GET
    @Path("{idRac}")
    public Response dohvatiSveTransakcije(@PathParam("idRac") int idRac){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue22);
            
            Message msg=context.createMessage();
            msg.setIntProperty("zahtev", 14);
            msg.setIntProperty("idRac", idRac);
            
            producer.send(queue21, msg);
            
            ObjectMessage objMsg=(ObjectMessage)consumer.receive();
            if(objMsg.getBooleanProperty("uspeh") == false)
                return Response.ok("Nema transakcija za prikaz.").build();
            //System.out.println("Primljena mesta");
            StringBuilder sb=new StringBuilder("Transakcija: IdTra RedniBroj Iznos DatumVreme Svrha Vrsta Filijala\n");
            Vector<TransakcijaResource> vektor=(Vector<TransakcijaResource>)objMsg.getObject();
            for(TransakcijaResource t:vektor)
                sb.append(t.getIdTra()).append("   ").append(t.getRedniBroj()).append("   ").append(t.getIznos()).append("   ")
                        .append(t.getDatumVreme()).append("   ").append(t.getSvrha()).append("   ").append(t.getVrsta())
                        .append("   ").append(t.getIdFil()).append("\n");
            String izlaz=sb.toString();
            //System.out.println(izlaz);
            consumer.close();
            context.close();
            return Response.ok(new String(izlaz)).build();
        
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
}
