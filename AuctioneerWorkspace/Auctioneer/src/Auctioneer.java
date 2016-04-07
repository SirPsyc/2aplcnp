import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class Auctioneer extends Agent {
	/*
	 * Register with the DFA.
	 */
	protected void register()
	{
	 	DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() ); 
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "seller" );
        sd.setName( getLocalName() );
        dfd.addServices(sd);
        
        try {  
            DFService.register(this, dfd );  
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
	}
	/*
	 * Get all agents with type buyer.
	 */
	protected DFAgentDescription[] getBuyers(){
		DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "buyer" );
        dfd.addServices(sd);
        
        DFAgentDescription[] result;
		try {
			result = DFService.search(this, dfd);
		} catch (FIPAException e) {
			result = null;
			e.printStackTrace();
		}
        return result;
	}
}
