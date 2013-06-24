/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;
import jabara.general.ArgUtil;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jabaraster
 */
public class BooleanEditor extends Panel {
    private static final long  serialVersionUID = -2973107421144330537L;

    private final BeanProperty property;
    private CheckBox           editor;

    /**
     * @param pId -
     * @param pProperty -
     * @param pModel -
     */
    public BooleanEditor(final String pId, final BeanProperty pProperty, final IModel<Boolean> pModel) {
        super(pId, pModel);
        this.property = ArgUtil.checkNull(pProperty, "pProperty"); //$NON-NLS-1$
        this.add(getEditor());
    }

    @SuppressWarnings("unchecked")
    private CheckBox getEditor() {
        if (this.editor == null) {
            this.editor = new CheckBox("editor", (IModel<Boolean>) getDefaultModel()); //$NON-NLS-1$
            this.editor.setLabel(Model.of(this.property.getLocalizedName()));
        }
        return this.editor;
    }
}
