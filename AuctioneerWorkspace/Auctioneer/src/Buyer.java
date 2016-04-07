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
	    
	    try {  
	        DFService.register(this, dfd );  
	    }
	    catch (FIPAException fe) { fe.printStackTrace(); }
	    
	    addBehaviour(new CyclicBehaviour(this) 
        {
             public void action() 
             {
                ACLMessage msg= receive();
                if (msg != null)
                    System.out.println( " - " +
                       getLocalName() + " <- " +
                       msg.getContent() );
                block();
             }
        });
	}
}
