/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;
import jabara.general.NameValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.util.string.interpolator.MapVariableInterpolator;
import org.apache.wicket.util.template.PackageTextTemplate;

/**
 * @author jabaraster
 */
public class VariableCssHeaderItem extends CssContentHeaderItem {

    /**
     * @param pCssLocataionBase -
     * @param pCssPath -
     * @param pVariables -
     */
    public VariableCssHeaderItem( //
            final Class<?> pCssLocataionBase //
            , final String pCssPath //
            , final NameValue<?>... pVariables) {
        super(bulidCss(pCssLocataionBase, pCssPath, pVariables), null, null);
    }

    /**
     * {@link MapVariableInterpolator}を使って変数を解決したCSSコードを、headタグに埋め込める形式で返します.
     * 
     * @param pCssLocataionBase -
     * @param pCssPath -
     * @param pVariables -
     * @return -
     */
    public static VariableCssHeaderItem forVariables( //
            final Class<?> pCssLocataionBase //
            , final String pCssPath //
            , final NameValue<?>... pVariables) {

        ArgUtil.checkNull(pCssLocataionBase, "pCssLocataionBase"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pCssPath, "pCssPath"); //$NON-NLS-1$
        return new VariableCssHeaderItem(pCssLocataionBase, pCssPath, pVariables);
    }

    private static CharSequence bulidCss(final Class<?> pCssLocataionBase, final String pCssPath, final NameValue<?>[] pVariables) {
        ArgUtil.checkNull(pCssLocataionBase, "pCssLocataionBase"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pCssPath, "pCssPath"); //$NON-NLS-1$

        final PackageTextTemplate text = new PackageTextTemplate(pCssLocataionBase, pCssPath);
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
