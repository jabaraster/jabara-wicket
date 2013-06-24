/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jabaraster
 */
public final class JavaScriptUtil {
    private static final Logger                      _logger                = LoggerFactory.getLogger(JavaScriptUtil.class);

    private static final JavaScriptResourceReference JQUERY_1_9_1_REFERENCE = new JavaScriptResourceReference(JavaScriptUtil.class, "jquery-1.9.1.js"); //$NON-NLS-1$

    private JavaScriptUtil() {
        // 処理なし
    }

    /**
     * コンポーネントクラスと同名のjsファイルへの参照をheadタグに追加します.
     * 
     * @param pResponse -
     * @param pResourceBase このクラスと同じ場所にあり、同名のjsファイルへの参照がheadタグに追加されます.
     */
    public static void addComponentJavaScriptReference(final IHeaderResponse pResponse, final Class<? extends Component> pResourceBase) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        ArgUtil.checkNull(pResourceBase, "pResourceBase"); //$NON-NLS-1$
        final JavaScriptResourceReference ref = new JavaScriptResourceReference(pResourceBase, pResourceBase.getSimpleName() + ".js"); //$NON-NLS-1$
        pResponse.render(JavaScriptHeaderItem.forReference(ref));
    }

    /**
     * 任意のhtmlタグにフォーカスを当てるJavaScriptコードをheadタグに追加します.
     * 
     * @param pResponse -
     * @param pTag フォーカスを当てる対象のタグ. <br>
     *            {@link Component#setOutputMarkupId(boolean)}にtrueをセットしていることが前提.
     * @see #getFocusScript(Component)
     */
    @SuppressWarnings("nls")
    public static void addFocusScript(final IHeaderResponse pResponse, final Component pTag) {
        ArgUtil.checkNull(pTag, "pTag"); //$NON-NLS-1$
        pResponse.render(OnDomReadyHeaderItem.forScript(getFocusScript(pTag))); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * jQuery 1.9.1の参照をheadタグに追加します.
     * 
     * @param pResponse -
     */
    public static void addJQuery1_9_1Reference(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        pResponse.render(JavaScriptHeaderItem.forReference(JQUERY_1_9_1_REFERENCE));
    }

    /**
     * @param pTag フォーカスを当てる対象のタグ. <br>
     *            {@link Component#setOutputMarkupId(boolean)}にtrueをセットしていることが前提.
     * @return 任意のhtmlタグにフォーカスを当てるJavaScriptコード.
     */
    public static String getFocusScript(final Component pTag) {
        ArgUtil.checkNull(pTag, "pTag"); //$NON-NLS-1$

        if (!pTag.getOutputMarkupId()) {
            _logger.warn(pTag.getId() + "(型：" + pTag.getClass().getName() + ") のoutputMarkupIdプロパティがfalseであるため、" //$NON-NLS-1$ //$NON-NLS-2$
                    + JavaScriptUtil.class.getSimpleName() + "#getFocusScript()は正常に動作しません."); //$NON-NLS-1$
        }
        return "App.focus('" + pTag.getMarkupId() + "');"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
