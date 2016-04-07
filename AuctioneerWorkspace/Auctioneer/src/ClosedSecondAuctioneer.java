import java.util.HashMap;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.domain.FIPAException;


public class ClosedSecondAuctioneer extends Auctioneer{
	private DFAgentDescription[] buyers;
	private HashMap<String, Integer> bids;
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
		bids = new HashMap<String, Integer>(numAgents);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent( "Startsecond" );
        for(DFAgentDescription b : buyers)
        {
        	msg.addReceiver(b.getName());
        }
        send(msg);
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
                	bids.put(msg.getSender().getName(), Integer.parseInt(msg.getContent()));
                	if (bids.size() == numAgents)
                		getWinner();
                }
                block();
             }
        });
	}
	
	protected void getWinner()
	{
		bids.
	}
}
