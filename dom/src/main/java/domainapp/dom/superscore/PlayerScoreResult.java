package domainapp.dom.superscore;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Nature;
import org.isisaddons.module.security.dom.user.ApplicationUser;

@DomainObject(nature=Nature.VIEW_MODEL)
public class PlayerScoreResult  implements Comparable<PlayerScoreResult> {
	
	public static class Comparator implements java.util.Comparator<PlayerScoreResult> {

		@Override
		public int compare(PlayerScoreResult o1, PlayerScoreResult o2) {
			if (o1 == o2) return 0;
			if (o2 == null) return 1;
			if (o1 == null) return -1;
			
			return -1 * o1.getResult().compareTo(o2.getResult());
			
		}
		
	}

	public PlayerScoreResult() {
		super();
	}
	
	public PlayerScoreResult(ApplicationUser player, Long result) {
		super();
		this.player = player;
		this.result = result;
	}

	// {{ Player (property)
	private ApplicationUser player;

	@MemberOrder(sequence = "1")
	public ApplicationUser getPlayer() {
		return player;
	}

	public void setPlayer(final ApplicationUser player) {
		this.player = player;
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
	public int compareTo(PlayerScoreResult o) {
//		if (getPlayer() == null) return -1;
//		if (o == null || o.getPlayer() == null) return +1;
//		return getPlayer().compareTo(o.getPlayer());
		if (getResult() == null) return -1;
		if (o == null || o.getResult() == null) return 1;
		return -1 * getResult().compareTo(o.getResult());
	}

}
