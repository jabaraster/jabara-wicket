/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author jabaraster
 */
public class ComponentCssHeaderItem extends CssReferenceHeaderItem {

    /**
     * @param pComponentType -
     */
    public ComponentCssHeaderItem(final Class<? extends Component> pComponentType) {
        super(new CssResourceReference(pComponentType, pComponentType.getSimpleName() + ".css"), null, null, null); //$NON-NLS-1$
    }

    /**
     * @param pComponentType -
     * @return -
     */
    public static ComponentCssHeaderItem forType(final Class<? extends Component> pComponentType) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        return new ComponentCssHeaderItem(pComponentType);
    }
}
