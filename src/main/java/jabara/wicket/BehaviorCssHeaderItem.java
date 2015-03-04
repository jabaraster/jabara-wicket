/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author jabaraster
 */
public class BehaviorCssHeaderItem extends CssReferenceHeaderItem {

    /**
     * @param pBehaviorType -
     */
    public BehaviorCssHeaderItem(final Class<? extends Behavior> pBehaviorType) {
        this(pBehaviorType, false);
    }

    /**
     * @param pBehaviorType -
     * @param pMinimized 圧縮されたCSSを使う場合はtrueを指定.
     */
    public BehaviorCssHeaderItem(final Class<? extends Behavior> pBehaviorType, final boolean pMinimized) {
        super(new CssResourceReference(pBehaviorType, pBehaviorType.getSimpleName() + (pMinimized ? ".min" : "") + ".css") // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                , null //
                , null //
                , null);
    }

    /**
     * @param pBehaviorType -
     * @return ビヘイビアクラスと同じ場所にある"ビヘイビア名.css"を参照するヘッダアイテム.
     */
    public static BehaviorCssHeaderItem forType(final Class<? extends Behavior> pBehaviorType) {
        ArgUtil.checkNull(pBehaviorType, "pBehaviorType"); //$NON-NLS-1$
        return new BehaviorCssHeaderItem(pBehaviorType, false);
    }

    /**
     * @param pBehaviorType -
     * @param pConfiguration -
     * @return ビヘイビアクラスと同じ場所にある"ビヘイビア名.css"を参照するヘッダアイテム.
     */
    public static BehaviorCssHeaderItem forType(final Class<? extends Behavior> pBehaviorType, final RuntimeConfigurationType pConfiguration) {
        ArgUtil.checkNull(pBehaviorType, "pBehaviorType"); //$NON-NLS-1$
        ArgUtil.checkNull(pConfiguration, "pConfiguration"); //$NON-NLS-1$
        return new BehaviorCssHeaderItem(pBehaviorType, pConfiguration == RuntimeConfigurationType.DEPLOYMENT);
    }

    /**
     * @param pBehaviorType -
     * @return ビヘイビアクラスと同じ場所にある"ビヘイビア名.min.css"を参照するヘッダアイテム.
     */
    public static BehaviorCssHeaderItem minimizedForType(final Class<? extends Behavior> pBehaviorType) {
        ArgUtil.checkNull(pBehaviorType, "pBehaviorType"); //$NON-NLS-1$
        return new BehaviorCssHeaderItem(pBehaviorType, true);
    }
}
