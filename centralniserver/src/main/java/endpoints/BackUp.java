/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author korisnik
 */
@Path("backup")
public class BackUp {
   
    @Resource(lookup = "Queue32")
    public Queue queue32;
    @Resource(lookup = "Queue34")
    public Queue queue34;
    @Resource(lookup = "myConnFactory3")
    public ConnectionFactory connFactory;
    
   
    
    @GET
    public Response dohvatiSve(){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue34);
            
            Message msg=context.createMessage();
            msg.setIntProperty("zahtev", 15);
            
            producer.send(queue32, msg);
            //System.out.println("Poslata ka podsis1");
            ObjectMessage objMsg=(ObjectMessage)consumer.receive();
            //System.out.println("Primljena mesta");
            String izlaz = (String)objMsg.getObject();
            //System.out.println(izlaz);
            consumer.close();
            context.close();
            return Response.ok(new String(izlaz)).build();
        
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
    
    @GET
    @Path("razlika")
    public Response dohvatiRazliku(){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue34);
            
            Message msg=context.createMessage();
            msg.setIntProperty("zahtev", 16);
            
            producer.send(queue32, msg);
            ObjectMessage objMsg=(ObjectMessage)consumer.receive();
            String izlaz = (String)objMsg.getObject();
            consumer.close();
            context.close();
            return Response.ok(new String(izlaz)).build();
        
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
}

