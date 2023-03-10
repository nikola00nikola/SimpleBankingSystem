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
import wraper.MestoResource;

/**
 *
 * @author korisnik
 */
@Path("mesta")
public class Mesta {
   
    @Resource(lookup = "Queue11")
    public Queue queue11;
    @Resource(lookup = "Queue12")
    public Queue queue12;
    @Resource(lookup = "myConnFactory")
    public ConnectionFactory connFactory;
    
    @GET
    public Response dohvatiSvaMesta(){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue12);
            
            Message msg=context.createMessage();
            msg.setIntProperty("zahtev", 10);
            
            producer.send(queue11, msg);
            System.out.println("Poslata ka podsis1");
            ObjectMessage objMsg=(ObjectMessage)consumer.receive();
            //System.out.println("Primljena mesta");
            StringBuilder sb=new StringBuilder("Mesto: IdMes Naziv Postanski_broj\n");
            Vector<MestoResource> vektor=(Vector<MestoResource>)objMsg.getObject();
            for(MestoResource m:vektor)
                sb.append(m.getIdMes()).append("   ").append(m.getNaziv()).append("   ").append(m.getPostBr()).append('\n');
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
    
    @POST
    @Path("{naziv}/{pbr}")
    public Response kreirajMesto(@PathParam("naziv") String naziv, @PathParam("pbr") int pbr){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue12);
            
            Message msg=context.createMessage();
            msg.setStringProperty("naziv", naziv);
            msg.setIntProperty("pbr", pbr);
            msg.setIntProperty("zahtev", 1);
            
            producer.send(queue11, msg);
            
            TextMessage txtMsg= (TextMessage) consumer.receive();
            String text=txtMsg.getText();
            consumer.close();
            context.close();
            if(text.equals("ok"))
                return Response.ok("Mesto je uspesno kreirano.").build();
            return Response.ok(new String (text)).build();
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
}
