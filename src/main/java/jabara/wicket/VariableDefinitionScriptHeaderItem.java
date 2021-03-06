/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;
import jabara.general.NameValue;

import java.util.Collections;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.Response;

/**
 * @author jabaraster
 */
public class VariableDefinitionScriptHeaderItem extends HeaderItem {

    private final Class<?>       pageType;
    private final NameValue<?>[] placeholders;

    private VariableDefinitionScriptHeaderItem(final Class<?> pPageType, final NameValue<?>[] pPlaceholders) {
        this.pageType = pPageType;
        this.placeholders = pPlaceholders.clone();
    }

    /**
     * @see org.apache.wicket.markup.head.HeaderItem#getRenderTokens()
     */
    @Override
    public Iterable<?> getRenderTokens() {
        return Collections.emptyList();
    }

    /**
     * @see org.apache.wicket.markup.head.HeaderItem#render(org.apache.wicket.request.Response)
     */
    @SuppressWarnings("nls")
    @Override
    public void render(final Response pResponse) {
        final StringBuilder script = new StringBuilder();

        script.append("<script type=\"text/javascript\">(function(App) {\n");
        script.append("    App.vars = {};\n");
        for (final NameValue<?> placeholder : this.placeholders) {
            script.append("    App.vars.").append(placeholder.getName()).append(" = ");
            final Object value = placeholder.getValue();

            if (value == null) {
                script.append("null");

            } else if (value.getClass().isPrimitive()) {
                script.append(value);

            } else if (isWrapper(value.getClass())) {
                script.append(value);

            } else {
                script.append("'").append(value).append("'");
            }
            script.append(";\n");
        }
        script.append("})(").append(this.pageType.getName()).append(");");
        script.append("</script>");

        pResponse.write(new String(script));
    }

    /**
     * @param pComponentType -
     * @param pPlaceholders -
     * @return -
     */
    public static VariableDefinitionScriptHeaderItem forComponent(final Class<? extends Component> pComponentType,
            final NameValue<?>... pPlaceholders) {
        ArgUtil.checkNull(pComponentType, "pComponentType"); //$NON-NLS-1$
        ArgUtil.checkNull(pPlaceholders, "pPlaceholders"); //$NON-NLS-1$
        return new VariableDefinitionScriptHeaderItem(pComponentType, pPlaceholders);
    }

    private static boolean isWrapper(final Class<? extends Object> pClass) {
        if (Character.class.isAssignableFrom(pClass)) {
            return true;
        }
        if (Number.class.isAssignableFrom(pClass)) {
            return true;
        }
        if (Boolean.class.isAssignableFrom(pClass)) {
            return true;
        }
        return false;
    }
}
