/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author jabaraster
 */
public class CssUtil {

    private CssUtil() {
        // 処理なし
    }

    /**
     * コンポーネントクラスと同名のcssファイルへの参照をheadタグに追加します.
     * 
     * @param pResponse -
     * @param pResourceBase このクラスと同じ場所にあり、同名のcssファイルへの参照がheadタグに追加されます.
     */
    public static void addComponentCssReference(final IHeaderResponse pResponse, final Class<? extends Component> pResourceBase) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        ArgUtil.checkNull(pResourceBase, "pResourceBase"); //$NON-NLS-1$
        final CssResourceReference ref = new CssResourceReference(pResourceBase, pResourceBase.getSimpleName() + ".css"); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(ref));
    }
}
