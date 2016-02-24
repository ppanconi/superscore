/**
 * 
 */
package domainapp.dom.superscore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.joda.time.LocalDate;

/**
 * @author panks
 *
 */
@DomainObject(nature=Nature.VIEW_MODEL)
public class WorstTeams {
	
	public String title() {
		return "Worst Teams in the period";
	}

	// {{ StartDate (property)
	private LocalDate startDate;

	@MemberOrder(sequence = "1")
	@Property
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(final LocalDate startDate) {
		this.startDate = startDate;
	}
	// }}
	
	// {{ StopDate (property)
	private LocalDate stopDate;

	@MemberOrder(sequence = "2")
	@Property
	@PropertyLayout(renderedAsDayBefore=true)
	public LocalDate getStopDate() {
		return stopDate;
	}

	public void setStopDate(final LocalDate stopDate) {
		this.stopDate = stopDate;
	}
	// }}
	
	// {{ updaate (action)
	@MemberOrder(sequence = "3")
	public WorstTeams updatePerdiod(final @ParameterLayout(named="Start Date") LocalDate startDate,
						final @ParameterLayout(named="Stop Date", renderedAsDayBefore=true) LocalDate stopDate) {
		
		WorstTeams model = container.injectServicesInto(new WorstTeams());
		model.setStartDate(startDate);
		 
		if (! stopDate.isAfter(startDate)) {
			model.setStopDate(startDate.plusDays(1));
		} else {
			model.setStopDate(stopDate);
		}
		return model;
	}
	
	public LocalDate default0UpdatePerdiod() {
		return getStartDate();
	}
	
	public LocalDate default1UpdatePerdiod() {
		return getStopDate();
	}
	// }}


	// {{ WorstTeams (Collection)

	@MemberOrder(sequence = "4")
	@CollectionLayout(sortedBy=TeamScoreResult.Comparator.class, render=RenderType.EAGERLY)
	public List<TeamScoreResult> getWorstTeams() {
		PersistenceManager pm = jdoSupport.getJdoPersistenceManager();
		
		String query = "SELECT losers as team, count(losers) as result INTO domainapp.dom.superscore.TeamScoreResult "
        + "FROM domainapp.dom.superscore.Match "
        + "WHERE when >= :startDate && when <= :stopDate "
        + "GROUP BY losers "
        + "ORDER BY count(losers) DESC "
        ;
		
		@SuppressWarnings("unchecked")
		List<TeamScoreResult> data = (List<TeamScoreResult>) pm.newQuery(query)
			.executeWithArray(getStartDate().toDate(), getStopDate().toDate());
		
		return data;
	}
	
	
	@MemberOrder(sequence = "5")
	@CollectionLayout(sortedBy=TeamScoreResult.Comparator.class, render=RenderType.EAGERLY)
	public List<TeamScoreResult> getUndersTeams() {
		PersistenceManager pm = jdoSupport.getJdoPersistenceManager();
		
		String query = "SELECT losers as team, count(losers) as result INTO domainapp.dom.superscore.TeamScoreResult "
        + "FROM domainapp.dom.superscore.Match "
        + "WHERE when >= :startDate && when <= :stopDate "
        + " && underTable == true "
        + "GROUP BY losers "
        + "ORDER BY count(losers) DESC "
        ;
		
		@SuppressWarnings("unchecked")
		List<TeamScoreResult> data = (List<TeamScoreResult>) pm.newQuery(query)
			.executeWithArray(getStartDate().toDate(), getStopDate().toDate());
		
		return data;
	}

	@javax.inject.Inject 
	IsisJdoSupport jdoSupport;
	
	@javax.inject.Inject 
    DomainObjectContainer container;
}
