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
import wraper.FilijalaResource;

/**
 *
 * @author korisnik
 */
@Path("filijale")
public class Filijale {
   
    @Resource(lookup = "Queue11")
    public Queue queue11;
    @Resource(lookup = "Queue12")
    public Queue queue12;
    @Resource(lookup = "myConnFactory")
    public ConnectionFactory connFactory;
    
    @POST
    @Path("{naziv}/{adresa}/{idMes}")
    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajFilijalu(@PathParam("naziv") String naziv, @PathParam("adresa") String adresa, @PathParam("idMes") int idMes){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue12);
            
            Message msg=context.createMessage();
            msg.setStringProperty("naziv", naziv);
            msg.setStringProperty("adresa", adresa);
            msg.setIntProperty("idMes", idMes);
            msg.setIntProperty("zahtev", 2);
            
            producer.send(queue11, msg);
            //System.out.println("Poslata je poruka ka podsistemu1 sa IdMes: "+idMes);
            Message por = consumer.receive();
            //System.out.println("Primljena je poruka od podsistema1 "+((TextMessage)por).getText()+" id " +((TextMessage)por).getIntProperty("aa"));
            TextMessage txtMsg=(TextMessage) por;
            String text=txtMsg.getText();
            consumer.close();
            context.close();
            if(text.equals("ok"))
                return Response.ok("Filijala je uspesno kreirana.").build();
            return Response.ok(new String(text)).build();
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
    
    @GET
    public Response dohvatiSveFilijale(){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue12);
            
            Message msg=context.createMessage();
            msg.setIntProperty("zahtev", 12);
            
            producer.send(queue11, msg);
            //System.out.println("Poslata ka podsis1");
            ObjectMessage objMsg=(ObjectMessage)consumer.receive();
            //System.out.println("Primljena mesta");
            StringBuilder sb=new StringBuilder("Filijala: IdFil Naziv Adresa Mesto\n");
            Vector<FilijalaResource> vektor=(Vector<FilijalaResource>)objMsg.getObject();
            for(FilijalaResource f:vektor)
                sb.append(f.getIdFil()).append("   ").append(f.getNaziv()).append("   ").append(f.getAdresa())
                        .append("   ").append(f.getIdMes()).append('\n');
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

