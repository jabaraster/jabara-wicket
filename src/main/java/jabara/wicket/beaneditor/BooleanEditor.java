/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;
import jabara.general.ArgUtil;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jabaraster
 */
public class BooleanEditor extends Panel {
    private static final long       serialVersionUID = 4242215417910213738L;

    private final BeanProperty      property;
    private DropDownChoice<Boolean> choice;

    /**
     * @param pId -
     * @param pProperty -
     * @param pModel -
     */
    public BooleanEditor(final String pId, final BeanProperty pProperty, final IModel<Boolean> pModel) {
        super(pId, pModel);
        this.property = ArgUtil.checkNull(pProperty, "pProperty"); //$NON-NLS-1$
        this.add(getChoice());
    }

    @SuppressWarnings({ "synthetic-access", "unchecked" })
    private DropDownChoice<Boolean> getChoice() {
        if (this.choice == null) {
            this.choice = new DropDownChoice<Boolean>( //
                    "choice" // //$NON-NLS-1$
                    , (IModel<Boolean>) getDefaultModel() //
                    , Model.ofList(Arrays.asList(Boolean.TRUE, Boolean.FALSE, null)) //
                    , new Renderer());
            this.choice.setRequired(false);
            this.choice.setLabel(Model.of(this.property.getLocalizedName()));
        }
        return this.choice;
    }

    private class Renderer implements IChoiceRenderer<Boolean> {
        private static final long serialVersionUID = 446655471677870475L;

        @Override
        public Object getDisplayValue(final Boolean pObject) {
            return BooleanEditor.this.getString("choiceDisplay_" + pObject); //$NON-NLS-1$
        }

        @Override
        public String getIdValue(final Boolean pObject, @SuppressWarnings("unused") final int pIndex) {
            return String.valueOf(pObject);
        }

    }
}
