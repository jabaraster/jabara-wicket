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
        this(pComponentType, false);
    }

    /**
     * @param pComponentType -
     * @param pMinimized 圧縮されたJSを使う場合はtrueを指定.
     */
    public ComponentJavaScriptHeaderItem(final Class<? extends Component> pComponentType, final boolean pMinimized) {
        super(new JavaScriptResourceReference(pComponentType, pComponentType.getSimpleName() + (pMinimized ? ".min" : "") + ".js") // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                , null // PageParameters
                , null // id
                , false // defer
                , "UTF-8" // //$NON-NLS-1$
                , null // condition
        );
    }

    /**
     * @param pComponentType -
     * @return コンポーネントクラスと同じ場所にある"コンポーネント名.js"を参照するヘッダアイテム.
     */
    public static ComponentJavaScriptHeaderItem forType(final Class<? extends Component> pComponentType) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        return new ComponentJavaScriptHeaderItem(pComponentType, false);
    }

    /**
     * @param pComponentType -
     * @return コンポーネントクラスと同じ場所にある"コンポーネント名.min.js"を参照するヘッダアイテム.
     */
    public static ComponentJavaScriptHeaderItem minimizedForType(final Class<? extends Component> pComponentType) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        return new ComponentJavaScriptHeaderItem(pComponentType, true);
    }
}
