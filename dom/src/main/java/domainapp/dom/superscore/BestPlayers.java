/**
 * 
 */
package domainapp.dom.superscore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.util.TitleBuffer;
import org.joda.time.LocalDate;

/**
 * @author panks
 *
 */
@DomainObject(nature=Nature.VIEW_MODEL)
public class BestPlayers {
	
	public String title() {
		return "Best Players in the period";
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
	public BestPlayers updatePerdiod(final @ParameterLayout(named="Start Date") LocalDate startDate,
						final @ParameterLayout(named="Stop Date", renderedAsDayBefore=true) LocalDate stopDate) {
		
		BestPlayers model = container.injectServicesInto(new BestPlayers());
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


	// {{ LoserPlayers (Collection)
//	private List<Object[]> collectionName = new ArrayList<Object[]>();

	@MemberOrder(sequence = "4")
	@CollectionLayout(sortedBy=PlayerScoreResult.Comparator.class, render=RenderType.EAGERLY)
	public List<PlayerScoreResult> getBestPlayers() {
		PersistenceManager pm = jdoSupport.getJdoPersistenceManager();
		
		String query1 = "SELECT winners.player1 as player, count(winners.player1) as result INTO domainapp.dom.superscore.PlayerScoreResult "
        + "FROM domainapp.dom.superscore.Match "
        + "WHERE when >= :startDate && when <= :stopDate "
        + "GROUP BY winners.player1 "
        + "ORDER BY count(winners.player1) DESC "
        ;
		
		@SuppressWarnings("unchecked")
		List<PlayerScoreResult> set1 = (List<PlayerScoreResult>) pm.newQuery(query1)
			.executeWithArray(getStartDate().toDate(), getStopDate().toDate());
		
		String query2 = "SELECT winners.player2 as player, count(winners.player2) as result INTO domainapp.dom.superscore.PlayerScoreResult "
		        + "FROM domainapp.dom.superscore.Match "
		        + "WHERE when >= :startDate && when <= :stopDate "
		        + "GROUP BY winners.player2 "
		        + "ORDER BY count(winners.player2) DESC "
		        ;
		
		@SuppressWarnings("unchecked")
		List<PlayerScoreResult> set2 = (List<PlayerScoreResult>) pm.newQuery(query2)
				.executeWithArray(getStartDate().toDate(), getStopDate().toDate());
		
		List<PlayerScoreResult> l = new ArrayList<>();
		l.addAll(set1);
		l.addAll(set2);
		
		String query = "SELECT player as player, sum(result) as result INTO domainapp.dom.superscore.PlayerScoreResult "
				+ "FROM domainapp.dom.superscore.PlayerScoreResult "
				+ "GROUP BY player "
				+ "ORDER BY sum(result) DESC"
				;
		
		Query jdoQuery = pm.newQuery(query);
		jdoQuery.setCandidates(l);
		Object data = jdoQuery.execute();
		
		return (List<PlayerScoreResult>) data;
	}
	
	
	@MemberOrder(sequence = "5")
	@CollectionLayout(sortedBy=PlayerScoreResult.Comparator.class, render=RenderType.EAGERLY,
					  describedAs="Players who have passed several opponents under the table")
	public List<PlayerScoreResult> getStrikersPlayers() {
		PersistenceManager pm = jdoSupport.getJdoPersistenceManager();
		
		String query1 = "SELECT winners.player1 as player, count(winners.player1) as result INTO domainapp.dom.superscore.PlayerScoreResult "
        + "FROM domainapp.dom.superscore.Match "
        + "WHERE when >= :startDate && when <= :stopDate "
        + " && underTable == true "
        + "GROUP BY winners.player1 "
        + "ORDER BY count(winners.player1) DESC "
        ;
		
		@SuppressWarnings("unchecked")
		List<PlayerScoreResult> set1 = (List<PlayerScoreResult>) pm.newQuery(query1)
			.executeWithArray(getStartDate().toDate(), getStopDate().toDate());
		
		String query2 = "SELECT winners.player2 as player, count(winners.player2) as result INTO domainapp.dom.superscore.PlayerScoreResult "
		        + "FROM domainapp.dom.superscore.Match "
		        + "WHERE when >= :startDate && when <= :stopDate "
		        + " && underTable == true "
		        + "GROUP BY winners.player2 "
		        + "ORDER BY count(winners.player2) DESC "
		        ;
		
		@SuppressWarnings("unchecked")
		List<PlayerScoreResult> set2 = (List<PlayerScoreResult>) pm.newQuery(query2)
				.executeWithArray(getStartDate().toDate(), getStopDate().toDate());
		
		List<PlayerScoreResult> l = new ArrayList<>();
		l.addAll(set1);
		l.addAll(set2);
		
		String query = "SELECT player as player, sum(result) as result INTO domainapp.dom.superscore.PlayerScoreResult "
				+ "FROM domainapp.dom.superscore.PlayerScoreResult "
				+ "GROUP BY player "
				+ "ORDER BY sum(result) DESC"
				;
		
		Query jdoQuery = pm.newQuery(query);
		jdoQuery.setCandidates(l);
		Object data = jdoQuery.execute();
		
		return (List<PlayerScoreResult>) data;
	}

	@javax.inject.Inject 
	IsisJdoSupport jdoSupport;
	
	@javax.inject.Inject 
    DomainObjectContainer container;
}
