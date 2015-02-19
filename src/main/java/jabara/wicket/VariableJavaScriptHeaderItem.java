/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;
import jabara.general.NameValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.util.string.interpolator.MapVariableInterpolator;
import org.apache.wicket.util.template.PackageTextTemplate;

/**
 * @author jabaraster
 */
public class VariableJavaScriptHeaderItem extends JavaScriptContentHeaderItem {

    /**
     * @param pScriptLocataionBase -
     * @param pScriptPath -
     * @param pVariables -
     */
    public VariableJavaScriptHeaderItem( //
            final Class<?> pScriptLocataionBase //
            , final String pScriptPath //
            , final NameValue<?>... pVariables) {
        super(bulidScript(pScriptLocataionBase, pScriptPath, pVariables), null, null);
    }

    /**
     * {@link MapVariableInterpolator}を使って変数を解決したJavaScriptコードを、headタグに埋め込める形式で返します.
     * 
     * @param pScriptLocataionBase -
     * @param pScriptPath -
     * @param pVariables -
     * @return -
     */
    public static VariableJavaScriptHeaderItem forVariables( //
            final Class<?> pScriptLocataionBase //
            , final String pScriptPath //
            , final NameValue<?>... pVariables) {

        ArgUtil.checkNull(pScriptLocataionBase, "pScriptLocataionBase"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pScriptPath, "pScriptPath"); //$NON-NLS-1$
        return new VariableJavaScriptHeaderItem(pScriptLocataionBase, pScriptPath, pVariables);
    }

    private static CharSequence bulidScript(final Class<?> pScriptLocataionBase, final String pScriptPath, final NameValue<?>[] pVariables) {
        ArgUtil.checkNull(pScriptLocataionBase, "pScriptLocataionBase"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pScriptPath, "pScriptPath"); //$NON-NLS-1$

        final PackageTextTemplate text = new PackageTextTemplate(pScriptLocataionBase, pScriptPath);
        try {
            final Map<String, Object> variables = new HashMap<String, Object>();
            if (pVariables != null) {
                for (final NameValue<?> nv : pVariables) {
                    if (nv != null) {
                        variables.put(nv.getName(), nv.getValue());
                    }
                }
            }
            return text.asString(variables);

        } finally {
            try {
                text.close();
            } catch (final IOException e) {
                // ignore.
            }
        }
    }
}
