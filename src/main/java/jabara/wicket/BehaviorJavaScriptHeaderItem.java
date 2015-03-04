/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * @author jabaraster
 */
public class BehaviorJavaScriptHeaderItem extends JavaScriptReferenceHeaderItem {

    /**
     * @param pBehaviorType -
     */
    public BehaviorJavaScriptHeaderItem(final Class<? extends Behavior> pBehaviorType) {
        this(pBehaviorType, false);
    }

    /**
     * @param pBehaviorType -
     * @param pMinimized 圧縮されたJSを使う場合はtrueを指定.
     */
    public BehaviorJavaScriptHeaderItem(final Class<? extends Behavior> pBehaviorType, final boolean pMinimized) {
        super(new JavaScriptResourceReference(pBehaviorType, pBehaviorType.getSimpleName() + (pMinimized ? ".min" : "") + ".js") // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                , null // PageParameters
                , null // id
                , false // defer
                , "UTF-8" // //$NON-NLS-1$
                , null // condition
        );
    }

    /**
     * @param pBehaviorType -
     * @return ビヘイビアクラスと同じ場所にある"ビヘイビア名.js"を参照するヘッダアイテム.
     */
    public static BehaviorJavaScriptHeaderItem forType(final Class<? extends Behavior> pBehaviorType) {
        ArgUtil.checkNull(pBehaviorType, "pBehaviorType"); //$NON-NLS-1$
        return new BehaviorJavaScriptHeaderItem(pBehaviorType, false);
    }

    /**
     * @param pBehaviorType -
     * @param pConfiguration -
     * @return ビヘイビアクラスと同じ場所にある"ビヘイビア名.js"を参照するヘッダアイテム.
     */
    public static BehaviorJavaScriptHeaderItem forType(final Class<? extends Behavior> pBehaviorType, final RuntimeConfigurationType pConfiguration) {
        ArgUtil.checkNull(pBehaviorType, "pBehaviorType"); //$NON-NLS-1$
        ArgUtil.checkNull(pConfiguration, "pConfiguration"); //$NON-NLS-1$
        return new BehaviorJavaScriptHeaderItem(pBehaviorType, pConfiguration == RuntimeConfigurationType.DEPLOYMENT);
    }

    /**
     * @param pBehaviorType -
     * @return ビヘイビアクラスと同じ場所にある"ビヘイビア名.min.js"を参照するヘッダアイテム.
     */
    public static BehaviorJavaScriptHeaderItem minimizedForType(final Class<? extends Behavior> pBehaviorType) {
        ArgUtil.checkNull(pBehaviorType, "pBehaviorType"); //$NON-NLS-1$
        return new BehaviorJavaScriptHeaderItem(pBehaviorType, true);
    }
}
