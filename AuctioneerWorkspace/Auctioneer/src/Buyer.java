import java.util.Random;
import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class Buyer extends Agent{
	protected void setup(){
		DFAgentDescription dfd = new DFAgentDescription();
	    dfd.setName( getAID() ); 
	    ServiceDescription sd  = new ServiceDescription();
	    sd.setType( "buyer" );
	    sd.setName( getLocalName() );
	    dfd.addServices(sd);
	    
	    Object[] args = getArguments();
	    int maximumPrice = Integer.parseInt(args[0].toString());
	    
	    try {  
	        DFService.register(this, dfd );  
	    }
	    catch (FIPAException fe) { fe.printStackTrace(); }
	    
	    Random rand = new Random();
	    
	    addBehaviour(new CyclicBehaviour(this) 
        {
             public void action() 
             {
                ACLMessage msg= receive();
                if (msg != null)
                {
                    System.out.println( " - " +
                       getLocalName() + " <- " +
                       msg.getContent() );
                    
                    AID auctioneer = msg.getSender();
                    
                    if (msg.getContent().equals("Startsecond"))
                    {
                    	ACLMessage bid = new ACLMessage(ACLMessage.INFORM);
                        bid.setContent( "" + maximumPrice );
                        bid.addReceiver(auctioneer);
                        send(bid);
                    }
                    else if (msg.getContent() == "Startenglish")
                    {
                    	int firstBid = rand.nextInt(10) + 1;
                    	ACLMessage bid = new ACLMessage(ACLMessage.INFORM);
                        bid.setContent( "" + firstBid );
                        bid.addReceiver(auctioneer);
                        send(bid);
                    }
                    else if (msg.getContent() == "Startdutch")
                    {
                    	ACLMessage bid = new ACLMessage(ACLMessage.INFORM);
                        bid.setContent( "ready" );
                        bid.addReceiver(auctioneer);
                        send(bid);
                    }
                    else if (msg.getContent().startsWith("Currentenglish"))
                    {
                    	
                    }
                    else if (msg.getContent().startsWith("Currentdutch"))
                    {
                    	
                    }
                    else if (msg.getContent() == "Auctionwon")
                    {
                    	System.out.println( "NICE!, i won for " /*+ price*/ );
                    }
                    else if (msg.getContent() == "Auctionlost")
                    {
                    	System.out.println( "THOUSANDS OF BOMBS AND GRENADES!, i lost" );
                    }
                }
                block();
             }
        });
	}
}
