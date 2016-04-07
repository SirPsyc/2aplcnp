import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.domain.FIPAException;


public class ClosedSecondAuctioneer extends Auctioneer{
	private DFAgentDescription[] buyers;
	private SortedMap<Integer, AID> bids;
	private Integer numAgents;
	
	protected void setup(){
		//TODO
		//discover agents
		//let them bid
		//give highest bidder the second highest price.
		register();
		
		Object[] args = getArguments();
		numAgents = Integer.parseInt(args[0].toString());
        
        buyers = getBuyers();
        for(DFAgentDescription b : buyers)
        {
        	System.out.println(b.getName());
        }
        startAuction();
	}
	
	protected void startAuction()
	{
		bids = new TreeMap<Integer,AID>(Collections.reverseOrder());
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent( "Startsecond" );
        for(DFAgentDescription b : buyers)
        {
        	msg.addReceiver(b.getName());
        }
        send(msg);
        receiveBids();
	}
	
	protected void receiveBids()
	{
		addBehaviour(new CyclicBehaviour(this) 
        {
             public void action() 
             {
                ACLMessage msg = receive();
                if (msg != null)
                {
                	System.out.println( " - " +
                            getLocalName() + 
                            " <- Bid received by " + 
                			msg.getSender().getLocalName() +
                			" = " +
                            msg.getContent() );
                	
                	bids.put(Integer.parseInt(msg.getContent()),msg.getSender());
                	if (bids.size() == numAgents)
                		getWinner();
                }
                block();
             }
        });
	}
	
	protected void getWinner()
	{
		AID winner = bids.remove(bids.firstKey());
		Integer price = bids.firstKey();
		
		System.out.println("Winner is " + winner.getLocalName().toString() + " for $" + price.toString());
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(price.toString());
        msg.addReceiver(winner);
        send(msg);
	}
}
