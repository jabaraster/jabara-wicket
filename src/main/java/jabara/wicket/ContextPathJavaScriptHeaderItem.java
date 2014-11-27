/**
 * 
 */
package jabara.wicket;

import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * @author jabaraster
 */
public final class ContextPathJavaScriptHeaderItem {

    private ContextPathJavaScriptHeaderItem() {
        //
    }

    /**
     * @return -
     */
    public static JavaScriptHeaderItem get() {
        final String script = "var contextPath = '" + RequestCycle.get().getRequest().getContextPath() + "';"; //$NON-NLS-1$ //$NON-NLS-2$
        return JavaScriptHeaderItem.forScript(script, null);
    }
}
