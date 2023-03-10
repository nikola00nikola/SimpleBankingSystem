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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import wraper.RacunResource;

/**
 *
 * @author korisnik
 */
@Path("racuni")
public class Racuni {
   
    @Resource(lookup = "Queue21")
    public Queue queue21;
    @Resource(lookup = "Queue22")
    public Queue queue22;
    @Resource(lookup = "myConnFactory")
    public ConnectionFactory connFactory;
    
    @GET
    @Path("{idKom}")
    public Response dohvatiSveRacune(@PathParam("idKom") int idKom){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue22);
            
            Message msg=context.createMessage();
            msg.setIntProperty("zahtev", 13);
            msg.setIntProperty("idKom", idKom);
            
            producer.send(queue21, msg);
            
            ObjectMessage objMsg=(ObjectMessage)consumer.receive();
            if(objMsg.getBooleanProperty("uspeh") == false)
                return Response.ok("Ne postoji racun za uneti IdKom.").build();
            //System.out.println("Primljena mesta");
            StringBuilder sb=new StringBuilder("Racun: IdRac Stanje Status DozvMinus DatumVreme BrojTrans Kom Fil\n");
            Vector<RacunResource> vektor=(Vector<RacunResource>)objMsg.getObject();
            for(RacunResource r:vektor)
                sb.append(r.getIdRac()).append("   ").append(r.getStanje()).append("   ").append(r.getStatus())
                        .append("   ").append(r.getDozvMinus()).append("   ").append(r.getDatumVreme()).append("   ")
                        .append(r.getBrojTrans()).append("   ").append(r.getIdKom()).append("   ").append(r.getIdFil()).append("\n");
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
    
    @PUT
    @Path("{idRac}")
    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response ugasiRacun(@PathParam("idRac") int idRac){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue22);
            
            Message msg=context.createMessage();
            msg.setIntProperty("idRac", idRac);
            msg.setIntProperty("zahtev", 6);
            
            producer.send(queue21, msg);
            //System.out.println("Poslata je poruka ka podsistemu1 sa IdMes: "+idMes);
            Message por = consumer.receive();
            //System.out.println("Primljena je poruka od podsistema1 "+((TextMessage)por).getText()+" id " +((TextMessage)por).getIntProperty("aa"));
            TextMessage txtMsg=(TextMessage) por;
            String text=txtMsg.getText();
            consumer.close();
            context.close();
            if(text.equals("ok"))
                return Response.ok("Racun je uspesno zatvoren.").build();
            return Response.ok(new String(text)).build();
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
    
    @POST
    @Path("{IdKom}/{IdFil}/{DMinus}")
    public Response otvoriRacun(@PathParam("IdKom") int idKom, @PathParam("IdFil") int idFil, @PathParam("DMinus") int dozvMinus){
        try {
            JMSContext context=connFactory.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(queue22);
            
            Message msg=context.createMessage();
            msg.setIntProperty("idKom", idKom);
            msg.setIntProperty("idFil", idFil);
            msg.setIntProperty("DMinus", dozvMinus);
            msg.setIntProperty("zahtev", 5);
            
            producer.send(queue21, msg);
            
            TextMessage txtMsg= (TextMessage) consumer.receive();
            String text=txtMsg.getText();
            consumer.close();
            context.close();
            if(text.equals("ok"))
                return Response.ok("Racun je uspesno otvoren.").build();
            return Response.ok(new String(text)).build();
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("Doslo je do greske").build();
    }
}
