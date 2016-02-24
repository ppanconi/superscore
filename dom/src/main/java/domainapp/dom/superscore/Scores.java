package domainapp.dom.superscore;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.joda.time.LocalDate;

@DomainService(
        nature = NatureOfService.VIEW
)
@DomainServiceLayout(
        menuOrder = "4"
)
public class Scores {

	// {{ worstPlayers (action)
	@MemberOrder(sequence = "1")
	@ActionLayout(named="List Worst Players")
	public WorstPlayers listWorstPlayers() {

		 WorstPlayers model = container.injectServicesInto(new WorstPlayers());
		 model.setStartDate(new LocalDate().withDayOfMonth(1));
		 model.setStopDate(new LocalDate().plusDays(1));
		 return model;
	}
	// }}
	
	// {{ bestPlayers (action)
	@MemberOrder(sequence = "2")
	@ActionLayout(named="List Best Players")
	public BestPlayers listBestPlayers() {

		 BestPlayers model = container.injectServicesInto(new BestPlayers());
		 model.setStartDate(new LocalDate().withDayOfMonth(1));
		 model.setStopDate(new LocalDate().plusDays(1));
		 return model;
	}
	// }}
	
	// {{ worstTeams (action)
	@MemberOrder(sequence = "3")
	@ActionLayout(named="List Worst Teams")
	public WorstTeams listWorstTeams() {
		
		WorstTeams model = container.injectServicesInto(new WorstTeams());
		model.setStartDate(new LocalDate().withDayOfMonth(1));
		model.setStopDate(new LocalDate().plusDays(1));
		return model;
	}
	// }}

	// {{ BestTeams (action)
	@MemberOrder(sequence = "4")
	@ActionLayout(named="List Best Teams")
	public BestTeams listBestTeams() {

		 BestTeams model = container.injectServicesInto(new BestTeams());
		 model.setStartDate(new LocalDate().withDayOfMonth(1));
		 model.setStopDate(new LocalDate().plusDays(1));
		 return model;
	}
	// }}

	// {{ injected: ServiceType
	@javax.inject.Inject 
    DomainObjectContainer container;
    

	// }}

}
