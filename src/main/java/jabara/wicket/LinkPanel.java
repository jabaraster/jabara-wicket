package jabara.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
public class LinkPanel extends Panel {
    private static final long           serialVersionUID = -870163225127196393L;

    private final IModel<String>        linkLabelModel;
    private final PageParameters        destinationParameter;
    private final Class<? extends Page> destination;

    private Link<?>                     link;
    private WebMarkupContainer          icon;
    private Label                       linkLabel;

    /**
     * @param pId -
     * @param pLinkLabelModel -
     * @param pDestinationParameter -
     * @param pDestination -
     */
    public LinkPanel( //
            final String pId //
            , final IModel<String> pLinkLabelModel //
            , final PageParameters pDestinationParameter //
            , final Class<? extends Page> pDestination //
    ) {
        super(pId);
        this.destinationParameter = pDestinationParameter;
        this.destination = pDestination;
        this.linkLabelModel = pLinkLabelModel;
        this.add(getLink());
    }

    /**
     * @return -
     */
    public WebMarkupContainer getIcon() {
        if (this.icon == null) {
            this.icon = new WebMarkupContainer("icon"); //$NON-NLS-1$
        }
        return this.icon;
    }

    /**
     * @return -
     */
    public Link<?> getLink() {
        if (this.link == null) {
            this.link = new BookmarkablePageLink<Object>("go", this.destination, this.destinationParameter); //$NON-NLS-1$
            this.link.add(getIcon());
            this.link.add(getLinkLabel());
        }
        return this.link;
    }

    private Label getLinkLabel() {
        if (this.linkLabel == null) {
            this.linkLabel = new Label("linkLabel", this.linkLabelModel); //$NON-NLS-1$
        }
        return this.linkLabel;
    }
}