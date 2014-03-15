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
        this(pComponentType, false);
    }

    /**
     * @param pComponentType -
     * @param pMinimized 圧縮されたCSSを使う場合はtrueを指定.
     */
    public ComponentCssHeaderItem(final Class<? extends Component> pComponentType, final boolean pMinimized) {
        super(new CssResourceReference(pComponentType, pComponentType.getSimpleName() + (pMinimized ? ".min" : "") + ".css") // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                , null //
                , null //
                , null);
    }

    /**
     * @param pComponentType -
     * @return コンポーネントクラスと同じ場所にある"コンポーネント名.css"を参照するヘッダアイテム.
     */
    public static ComponentCssHeaderItem forType(final Class<? extends Component> pComponentType) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        return new ComponentCssHeaderItem(pComponentType, false);
    }

    /**
     * @param pComponentType -
     * @return コンポーネントクラスと同じ場所にある"コンポーネント名.min.css"を参照するヘッダアイテム.
     */
    public static ComponentCssHeaderItem minimizedForType(final Class<? extends Component> pComponentType) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        return new ComponentCssHeaderItem(pComponentType, true);
    }
}
