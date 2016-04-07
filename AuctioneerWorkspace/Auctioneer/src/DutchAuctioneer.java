import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.domain.FIPAException;


public class DutchAuctioneer extends Auctioneer{
	private DFAgentDescription[] buyers;
	private Integer numAgents, startPrice, curPrice, ready;
	private boolean winnerFound = false;
	
	//get all messages that buyers are ready.
	private SimpleBehaviour getReady = new SimpleBehaviour(this)
			{
				public void action()
				{
					System.out.println("receiving" + ready);
					ACLMessage msg = receive();
	                if (msg != null)
	                {
	                	ready += 1;
	                }
	                else
	                	block();
				}
				
				public boolean done()
				{
					if (ready == numAgents)
					{
						startBidding();
						return true;
					}
					else
						return false;
				}
			};
		
	//get when a buyer stops the bidding.
	private SimpleBehaviour getBid = new SimpleBehaviour(this)
			{
				private AID winner;
				public void action()
				{
					System.out.println("receiving" + ready);
					ACLMessage msg = receive();
		            if (msg != null)
		            {
		            	winnerFound = true;
		            	winner = msg.getSender();
		            }
		            else
		            	block();
				}
				
				public boolean done()
				{
					if (winnerFound)
					{
						getWinner(winner);
						return true;
					}
					else
						return false;
				}
			};
	
	protected void setup(){
		//register with DFService
		register();
		ready = 0;
		
		//get amount of agents participating in the auction and the starting price.
		Object[] args = getArguments();
		numAgents = Integer.parseInt(args[0].toString());
		startPrice = Integer.parseInt(args[1].toString());
		curPrice = startPrice;
        
		//get all buyers AID for messaging
        buyers = getBuyers();
        for(DFAgentDescription b : buyers)
        {
        	System.out.println(b.getName());
        }
        startAuction();
	}
	
	protected void startAuction()
	{
		//see if buyers are ready.
		addBehaviour(getReady);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent( "Startdutch" );
        for(DFAgentDescription b : buyers)
        {
        	msg.addReceiver(b.getName());
        }
        send(msg);
        System.out.println("ready?");
	}
	
	protected void sendCurPrice()
	{
		//send the current price.
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent( "Currentdutch" + curPrice);
        for(DFAgentDescription b : buyers)
        {
        	msg.addReceiver(b.getName());
        }
        send(msg);
        System.out.println("Current price: " + curPrice);
	}
	
	protected void startBidding()
	{
		//start the bidding and keep lowering the price.
		System.out.println("start");
		
		Behaviour lowerPrice = new TickerBehaviour(this, 100){
			protected void onTick()
			{
				if (!winnerFound)
				{
					curPrice -= 10;
					sendCurPrice();
				}
				else
				{
					System.out.println("ticker stopped");
					stop();
				}
			}
		};
		
		//keep lowering the price and get bids.	
		addBehaviour(getBid);
		sendCurPrice();
		addBehaviour(lowerPrice);

	}
	
	//message the winner.
	protected void getWinner(AID winner)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent( "Auctionwon"+curPrice );
        msg.addReceiver(winner);
        send(msg);
	}
}
	