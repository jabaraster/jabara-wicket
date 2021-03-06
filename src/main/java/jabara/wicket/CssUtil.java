/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author jabaraster
 */
public class CssUtil {

    private static final Set<Class<? extends Component>> _added = new CopyOnWriteArraySet<Class<? extends Component>>();

    private CssUtil() {
        // 処理なし
    }

    /**
     * コンポーネントクラスと同名のcssファイルへの参照をheadタグに追加します. <br>
     * またWicketの共有リソースにcssファイルを同名で登録します. <br>
     * 
     * @param pResponse -
     * @param pResourceBase このクラスと同じ場所にあり、同名のcssファイルへの参照がheadタグに追加されます.
     */
    public static void addComponentCssReference(final IHeaderResponse pResponse, final Class<? extends Component> pResourceBase) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        ArgUtil.checkNull(pResourceBase, "pResourceBase"); //$NON-NLS-1$

        final String cssFileName = pResourceBase.getSimpleName() + ".css"; //$NON-NLS-1$
        final CssResourceReference ref = new CssResourceReference(pResourceBase, cssFileName);
        pResponse.render(CssHeaderItem.forReference(ref));

        if (!_added.contains(pResourceBase)) {
            _added.add(pResourceBase); // このコードだと同じ型のpResourceBaseに対してaddが複数回動く可能性が残るのだが、多少遅くなることはあっても実害はない.
            WebApplication.get().mountResource(cssFileName, ref);
        }
    }

    /**
     * @param pComponentType -
     * @return -
     */
    public static CssHeaderItem forComponentCssHeaderItem(final Class<? extends Component> pComponentType) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        return ComponentCssHeaderItem.forType(pComponentType);
    }
}
