/**
 * 
 */
package jabara.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;

/**
 * @author jabaraster
 */
public class MarkupIdForceOutputer implements IComponentInstantiationListener {

    /**
     * @see org.apache.wicket.application.IComponentInstantiationListener#onInstantiation(org.apache.wicket.Component)
     */
    @Override
    public void onInstantiation(final Component pComponent) {
        pComponent.setOutputMarkupId(true);
    }

}
