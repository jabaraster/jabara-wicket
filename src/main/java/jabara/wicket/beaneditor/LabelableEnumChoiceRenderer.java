/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.general.ILabelable;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * @param <E> 列挙型.
 * @author jabaraster
 */
public class LabelableEnumChoiceRenderer<E extends Enum<E>> implements IChoiceRenderer<E> {
    private static final long serialVersionUID = -3728108723963367130L;

    /**
     * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
     */
    @Override
    public Object getDisplayValue(final E pObject) {
        if (pObject == null) {
            return String.valueOf(pObject);
        }
        if (pObject instanceof ILabelable) {
            return ((ILabelable) pObject).getLabel(Session.get().getLocale());
        }
        return pObject.name();
    }

    /**
     * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
     */
    @Override
    public String getIdValue(final E pObject, @SuppressWarnings("unused") final int pIndex) {
        if (pObject == null) {
            return String.valueOf(pObject);
        }
        return String.valueOf(pObject.ordinal());
    }
}
