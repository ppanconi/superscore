package domainapp.dom.superscore;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Nature;

@DomainObject(nature=Nature.VIEW_MODEL)
public class TeamScoreResult  implements Comparable<TeamScoreResult> {
	
	public static class Comparator implements java.util.Comparator<TeamScoreResult> {

		@Override
		public int compare(TeamScoreResult o1, TeamScoreResult o2) {
			if (o1 == o2) return 0;
			if (o2 == null) return 1;
			if (o1 == null) return -1;
			
			return -1 * o1.getResult().compareTo(o2.getResult());
			
		}
	}

	public TeamScoreResult() {
		super();
	}
	
	public TeamScoreResult(Team team, Long result) {
		super();
		this.team = team;
		this.result = result;
	}

	// {{ Team (property)
	private Team team;

	@MemberOrder(sequence = "1")
	public Team getTeam() {
		return team;
	}

	public void setTeam(final Team team) {
		this.team = team;
	}
	// }}

	// {{ result (property)
	private Long result;

	@MemberOrder(sequence = "2")
	public Long getResult() {
		return result;
	}

	public void setResult(final Long result) {
		this.result = result;
	}
	// }}

	@Override
	public int compareTo(TeamScoreResult o) {
		if (getResult() == null) return -1;
		if (o == null || o.getResult() == null) return 1;
		return -1 * getResult().compareTo(o.getResult());
	}

}
