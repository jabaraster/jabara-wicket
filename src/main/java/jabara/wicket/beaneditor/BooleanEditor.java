/**
 * 
 */
package jabara.wicket.beaneditor;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class BooleanEditor extends Panel {
    private static final long serialVersionUID = -2973107421144330537L;

    private CheckBox          editor;

    /**
     * @param pId -
     * @param pModel -
     */
    public BooleanEditor(final String pId, final IModel<Boolean> pModel) {
        super(pId, pModel);
        this.add(getEditor());
    }

    @SuppressWarnings("unchecked")
    private CheckBox getEditor() {
        if (this.editor == null) {
            this.editor = new CheckBox("editor", (IModel<Boolean>) getDefaultModel()); //$NON-NLS-1$
        }
        return this.editor;
    }
}
