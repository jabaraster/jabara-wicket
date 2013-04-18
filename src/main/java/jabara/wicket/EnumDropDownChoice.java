/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.model.IModel;

/**
 * @param <E>
 * @author jabaraster
 */
public abstract class EnumDropDownChoice<E extends Enum<E>> extends DropDownChoice<E> {
    private static final long serialVersionUID = -4616569102818789301L;

    /**
     * @param pId
     * @param pInitialValueModel
     */
    public EnumDropDownChoice(final String pId, final IModel<E> pInitialValueModel) {
        super(pId);
        this.setChoices(getEnumValues());
        setModel(ArgUtil.checkNull(pInitialValueModel, "pInitialValueModel")); //$NON-NLS-1$
        setChoiceRenderer(new EnumChoiceRenderer<E>(this));
    }

    @SuppressWarnings("unchecked")
    private Class<E> getConcreteTypeForTypeArgumentOfSuperClass() {
        final ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<E>) genericSuperclass.getActualTypeArguments()[0];
    }

    private List<E> getEnumValues() {
        return Arrays.asList(getConcreteTypeForTypeArgumentOfSuperClass().getEnumConstants());
    }
}
