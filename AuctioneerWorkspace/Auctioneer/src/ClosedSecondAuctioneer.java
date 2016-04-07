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
		//register with DFService
		register();
		
		//get amount of agents participating in the auction
		Object[] args = getArguments();
		numAgents = Integer.parseInt(args[0].toString());
        
		//get all buyers AID for messaging
        buyers = getBuyers();
        for(DFAgentDescription b : buyers)
        {
        	System.out.println(b.getName());
        }
        startAuction();
	}
	
	/*
	 * Start the auction, by messaging all agents to give their bid.
	 */
	protected void startAuction()
	{
		bids = new TreeMap<Integer,AID>(Collections.reverseOrder());
        receiveBids();
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent( "Startsecond" );
        for(DFAgentDescription b : buyers)
        {
        	msg.addReceiver(b.getName());
        }
        send(msg);
	}
	
	/*
	 * Receive the bids
	 * If all bids are in getWinner()
	 */
	protected void receiveBids()
	{
		addBehaviour(new CyclicBehaviour(this) 
        {
             public void action() 
             {
                ACLMessage msg = receive();
                if (msg != null)
                {
                	bids.put(Integer.parseInt(msg.getContent()),msg.getSender());
                	if (bids.size() == numAgents)
                		getWinner();
                }
                else
                	block();
             }
        });
	}
	
	/*
	 * Get the highest bidder and the second highest price
	 * Message the winner with the amount.
	 */
	protected void getWinner()
	{
		AID winner = bids.remove(bids.firstKey());
		Integer price = bids.firstKey();
		
		//System.out.println("Winner is " + winner.getLocalName().toString() + " for $" + price.toString());
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("Auctionwon" + price);
        msg.addReceiver(winner);
        send(msg);
	}
}
