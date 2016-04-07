import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.domain.FIPAException;


public class ClosedSecondAuctioneer extends Auctioneer{
	private DFAgentDescription[] buyers;
	
	protected void setup(){
		//TODO
		//discover agents
		//let them bid
		//give highest bidder the second highest price.
		register();
		
		Object[] args = getArguments();
		int numofagents = Integer.parseInt(args[0].toString());
        
        buyers = getBuyers(numofagents);
        for(DFAgentDescription b : buyers)
        {
        	System.out.println(b.getName());
        }
        startAuction();
	}
	
	protected void startAuction()
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent( "Startbid" );
        for(DFAgentDescription b : buyers)
        {
        	msg.addReceiver(b.getName());
        }
        send(msg);
	}
}
