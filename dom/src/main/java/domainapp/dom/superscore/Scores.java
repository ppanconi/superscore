package domainapp.dom.superscore;

import org.apache.isis.applib.DomainObjectContainer;
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
	public WorstPlayers worstPlayers() {

		 WorstPlayers model = container.injectServicesInto(new WorstPlayers());
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
