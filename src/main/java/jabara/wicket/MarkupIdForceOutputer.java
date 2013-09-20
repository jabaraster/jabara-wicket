/**
 * 
 */
package jabara.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.internal.Enclosure;

/**
 * @author jabaraster
 */
public class MarkupIdForceOutputer implements IComponentInstantiationListener {

    private static final Class<?>[] EXCUSION_TYPES = { TransparentWebMarkupContainer.class, Enclosure.class };

    /**
     * @see org.apache.wicket.application.IComponentInstantiationListener#onInstantiation(org.apache.wicket.Component)
     */
    @Override
    public void onInstantiation(final Component pComponent) {
        final Class<? extends Component> componentType = pComponent.getClass();
        for (final Class<?> type : EXCUSION_TYPES) {
            if (type.isAssignableFrom(componentType)) {
                return;
            }
        }
        pComponent.setOutputMarkupId(true);
    }

}
