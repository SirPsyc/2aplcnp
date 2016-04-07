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
	    
	    // Keep looking for new messages
	    addBehaviour(new CyclicBehaviour(this) 
        {
             public void action() 
             {
                ACLMessage msg= receive();
                if (msg != null)
                {
                    /*System.out.println( " - " +
                       getLocalName() + " <- " +
                       msg.getContent() );*/
                    
                    AID auctioneer = msg.getSender();
                    
                    //In case of start second price sealed auction send back maximum price of bidder
                    if (msg.getContent().equals("Startsecond"))
                    {
                    	ACLMessage bid = new ACLMessage(ACLMessage.INFORM);
                        bid.setContent( "" + maximumPrice );
                        bid.addReceiver(auctioneer);
                        send(bid);
                        System.out.println( " - " +
                                getLocalName() + " <- " +
                        		maximumPrice +
                                " sent bid to " +
                                auctioneer.getLocalName());
                    }
                    //In case of start English auction send back a first bid between $1 and $10
                    else if (msg.getContent().equals("Startenglish"))
                    {
                    	int firstBid = rand.nextInt(10) + 1;
                    	ACLMessage bid = new ACLMessage(ACLMessage.INFORM);
                        bid.setContent( "" + firstBid );
                        bid.addReceiver(auctioneer);
                        send(bid);
                    }
                    //In case of start Dutch auction send back a message bidder is ready to start
                    else if (msg.getContent().equals("Startdutch"))
                    {
                    	ACLMessage bid = new ACLMessage(ACLMessage.INFORM);
                        bid.setContent( "ready" );
                        bid.addReceiver(auctioneer);
                        send(bid);
                    }
                    //Receiving the current bid of the English auction, 
                    //if current bid is lower than bidder maximum send a new bid which is
                    //the current bid with a random added value of $1 to $5,
                    //unless this new bid is higher than maximum in which case send maximum as bid
                    else if (msg.getContent().startsWith("Currentenglish"))
                    {
                    	int currentBid = Integer.parseInt(msg.getContent().substring(10));
                    	
                    	
                    }
                    //Receiving the current price of the Dutch auction,
                    //if current price lower than bidder maximum send "agreed"
                    else if (msg.getContent().startsWith("Currentdutch"))
                    {
                    	int currentPrice = Integer.parseInt(msg.getContent().substring(10));
                    	
                    	
                    }
                    //Receiving message of having won the auction for a certain price,
                    //print out a line saying I WON :D
                    else if (msg.getContent().startsWith("Auctionwon"))
                    {
                    	int price = Integer.parseInt(msg.getContent().substring(10));
                    	System.out.println( " - " + getLocalName() + " : NICE!, i won for $" + price );
                    }
                }
                else
                	block();
             }
        });
	}
}
