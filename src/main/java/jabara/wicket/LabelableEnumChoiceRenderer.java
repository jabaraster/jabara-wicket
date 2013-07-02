/**
 * 
 */
package jabara.wicket;

import jabara.general.ILabelable;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * @param <E> 描画対象の列挙型.
 * @author jabaraster
 */
public class LabelableEnumChoiceRenderer<E extends Enum<E>> implements IChoiceRenderer<E> {
    private static final long serialVersionUID = 3383267595764448170L;

    /**
     * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
     */
    @Override
    public Object getDisplayValue(final E pObject) {
        if (pObject instanceof ILabelable) {
            return ((ILabelable) pObject).getLabel(Session.get().getLocale());
        }
        return pObject;
    }

    /**
     * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
     */
    @Override
    public String getIdValue(final E pObject, @SuppressWarnings("unused") final int pIndex) {
        return pObject == null ? String.valueOf(pObject) : String.valueOf(pObject.ordinal());
    }

}
