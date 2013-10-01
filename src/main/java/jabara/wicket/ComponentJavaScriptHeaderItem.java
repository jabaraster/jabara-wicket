/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * @author jabaraster
 */
public class ComponentJavaScriptHeaderItem extends JavaScriptReferenceHeaderItem {

    /**
     * @param pComponentType -
     */
    public ComponentJavaScriptHeaderItem(final Class<? extends Component> pComponentType) {
        super(new JavaScriptResourceReference(pComponentType, pComponentType.getSimpleName() + ".js") // //$NON-NLS-1$
                , null // PageParameters
                , null // id
                , false // defer
                , "UTF-8" // //$NON-NLS-1$
                , null // condition
        );
    }

    /**
     * @param pComponentType -
     * @return -
     */
    public static ComponentJavaScriptHeaderItem forType(final Class<? extends Component> pComponentType) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        return new ComponentJavaScriptHeaderItem(pComponentType);
    }
}
