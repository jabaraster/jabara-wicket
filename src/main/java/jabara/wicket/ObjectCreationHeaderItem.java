/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import java.util.Collections;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.Response;

/**
 * @author jabaraster
 */
public class ObjectCreationHeaderItem extends HeaderItem {
    private static final long serialVersionUID = -2989084388200520698L;

    private final Class<?>    type;

    private ObjectCreationHeaderItem(final Class<?> pComponentType) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        this.type = pComponentType;
    }

    /**
     * @see org.apache.wicket.markup.head.HeaderItem#getRenderTokens()
     */
    @Override
    public Iterable<?> getRenderTokens() {
        return Collections.emptyList();
    }

    /**
     * @see org.apache.wicket.markup.head.HeaderItem#render(org.apache.wicket.request.Response)
     */
    @SuppressWarnings("nls")
    @Override
    public void render(final Response pResponse) {
        pResponse.write("<script type=\"text/javascript\">(function (pName){" //
                + "var receiver = window;" //
                + "pName.split('.').map(function(pToken) {" //
                + "if (!(pToken in receiver)) {" //
                + "receiver[pToken] = {};" //
                + "}" //
                + "receiver = receiver[pToken];" //
                + "});" //
                + "})('" + this.type.getName() + "');</script>" //
        );
    }

    /**
     * @param pBehaviorType -
     * @return -
     */
    public static ObjectCreationHeaderItem forBehavior(final Class<? extends Behavior> pBehaviorType) {
        ArgUtil.checkNull(pBehaviorType, "pBehaviorType"); //$NON-NLS-1$
        return new ObjectCreationHeaderItem(pBehaviorType);
    }

    /**
     * @param pComponentType -
     * @return -
     */
    public static ObjectCreationHeaderItem forComponent(final Class<? extends Component> pComponentType) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        return new ObjectCreationHeaderItem(pComponentType);
    }
}
