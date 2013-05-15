/**
 * 
 */
package jabara.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;

/**
 * @author jabaraster
 */
public class MarkupIdForceOutputer implements IComponentInstantiationListener {

    /**
     * @see org.apache.wicket.application.IComponentInstantiationListener#onInstantiation(org.apache.wicket.Component)
     */
    @Override
    public void onInstantiation(final Component pComponent) {
        if (pComponent.getClass().equals(TransparentWebMarkupContainer.class)) {
            // TransparentWebMarkupContainerのid属性を出力すると警告が出るので、これは除外.
            return;
        }
        pComponent.setOutputMarkupId(true);
    }

}
