package domainapp.dom.superscore;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import domainapp.dom.simple.SimpleObject;
import domainapp.dom.simple.SimpleObjects;
import domainapp.dom.simple.SimpleObjects.CreateDomainEvent;

@DomainService(
        nature = NatureOfService.VIEW,
        repositoryFor = Team.class
)
@DomainServiceLayout(
        menuOrder = "8"
)
public class Teams {
	
	//region > title
    public TranslatableString title() {
        return TranslatableString.tr("Teams");
    }
    //endregion

    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Team> listAll() {
        return container.allInstances(Team.class);
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<Teams> {
        public CreateDomainEvent(final Teams source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "3")
    public Team create(
            final @ParameterLayout(named="Player 1") ApplicationUser player1,
            final @ParameterLayout(named="Player 2") ApplicationUser player2
            ) {
        final Team obj = container.newTransientInstance(Team.class);
        obj.setPlayer1(player1);
        obj.setPlayer2(player2);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion

    //region > injected services

    @javax.inject.Inject 
    DomainObjectContainer container;

    //endregion

}
