/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;
import jabara.general.ArgUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jabaraster
 */
public class EnumEditor extends Panel {
    private static final long       serialVersionUID = -2851340524786041369L;

    private final Class<Enum<?>>    enumType;
    private final BeanProperty      property;

    private DropDownChoice<Enum<?>> choice;

    /**
     * @param pId -
     * @param pProperty -
     * @param pModel -
     */
    @SuppressWarnings("unchecked")
    public EnumEditor( //
            final String pId //
            , final BeanProperty pProperty //
            , final IModel<Enum<?>> pModel //
    ) {
        super(pId, pModel);
        this.property = ArgUtil.checkNull(pProperty, "pProperty"); //$NON-NLS-1$
        if (!Enum.class.isAssignableFrom(pProperty.getType())) {
            throw new IllegalArgumentException("'" + pProperty.getType().getName() + "' is not enum."); //$NON-NLS-1$//$NON-NLS-2$
        }
        this.enumType = (Class<Enum<?>>) pProperty.getType();

        this.add(getChoice());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private DropDownChoice<Enum<?>> getChoice() {
        if (this.choice == null) {
            final List<Enum<?>> values = new ArrayList<Enum<?>>(Arrays.asList(this.enumType.getEnumConstants()));
            if (this.property.isNullable()) {
                values.add(null);
            }
            this.choice = new DropDownChoice<Enum<?>>( //
                    "choice" // //$NON-NLS-1$
                    , (IModel<Enum<?>>) getDefaultModel() //
                    , Model.ofList(values) //
                    , new LabelableEnumChoiceRenderer());
            this.choice.setRequired(!this.property.isNullable());
            this.choice.setLabel(Model.of(this.property.getLocalizedName()));
        }
        return this.choice;
    }
}
