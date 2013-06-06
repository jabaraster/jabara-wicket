/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author jabaraster
 */
public class DefaultPropertyEditorComponentProvider implements IPropertyEditorComponentProvider {

    /**
     * @see jabara.wicket.beaneditor.IPropertyEditorComponentProvider#create(java.lang.String, java.lang.Object, jabara.bean.BeanProperty)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Component create(final String pId, final Object pBean, final BeanProperty pProperty) {
        if (pProperty.isReadOnly()) {
            return new LabelInPanel(pId, new PropertyModel(pBean, pProperty.getName()));
        }

        if (pProperty.getType().equals(Boolean.TYPE)) {
            return new BooleanEditor(pId, new PropertyModel<Boolean>(pBean, pProperty.getName()));
        }

        return new TextFieldInPanel(pId, new PropertyModel(pBean, pProperty.getName()), pProperty.getType());
    }

    /**
     * @author jabaraster
     */
    public static class LabelInPanel extends Panel {
        private static final long serialVersionUID = -6482487723458468208L;

        /**
         * @param pId
         * @param pModel
         */
        public LabelInPanel(final String pId, final IModel<?> pModel) {
            super(pId, pModel);
            this.add(new Label("text", pModel)); //$NON-NLS-1$
        }

    }

    /**
     * @author jabaraster
     */
    public static class TextFieldInPanel extends Panel {
        private static final long serialVersionUID = 8973259496268254876L;

        /**
         * @param pId
         * @param pModel
         * @param pValueType
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public TextFieldInPanel(final String pId, final IModel<?> pModel, final Class<?> pValueType) {
            super(pId);
            this.add(new TextField("text", pModel, pValueType)); //$NON-NLS-1$
        }
    }

}
