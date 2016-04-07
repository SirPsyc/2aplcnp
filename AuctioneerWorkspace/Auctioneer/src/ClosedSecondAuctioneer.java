import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;


public class ClosedSecondAuctioneer extends Auctioneer{
	protected void setup(){
		//TODO
		//discover agents
		//let them bid
		//give highest bidder the second highest price.
		register();
		
		Object[] args = getArguments();
		int numofagents = Integer.parseInt(args[0].toString());
        
        DFAgentDescription[] buyers = getBuyers(numofagents);
        for(DFAgentDescription b : buyers)
        {
        	System.out.println(b.getName());
        }
	}
}
