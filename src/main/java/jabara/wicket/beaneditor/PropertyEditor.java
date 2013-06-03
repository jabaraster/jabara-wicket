/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;
import jabara.general.ArgUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @param <B>
 * @author jabaraster
 */
public class PropertyEditor<B> extends Panel {
    private static final long serialVersionUID = -5504683462568384945L;

    /**
     * @param pId
     * @param pBean
     * @param pProperty
     */
    @SuppressWarnings({ "rawtypes" })
    public PropertyEditor(final String pId, final B pBean, final BeanProperty pProperty) {
        super(pId);

        ArgUtil.checkNull(pBean, "pBean"); //$NON-NLS-1$
        ArgUtil.checkNull(pProperty, "pPropertyValue"); //$NON-NLS-1$

        if (pProperty.isReadOnly()) {
            this.add(new ExLabel("value", new PropertyModel(pBean, pProperty.getName()))); //$NON-NLS-1$
        } else {
            this.add(new ExTextField("value", new PropertyModel(pBean, pProperty.getName()), pProperty.getType())); //$NON-NLS-1$
        }
    }

    /**
     * @author jabaraster
     */
    public static class ExLabel extends Panel {
        private static final long serialVersionUID = -6482487723458468208L;

        /**
         * @param pId
         * @param pModel
         */
        public ExLabel(final String pId, final IModel<?> pModel) {
            super(pId, pModel);
            this.add(new Label("text", pModel)); //$NON-NLS-1$
        }

    }

    /**
     * @author jabaraster
     */
    public static class ExTextField extends Panel {
        private static final long serialVersionUID = 8973259496268254876L;

        /**
         * @param pId
         * @param pModel
         * @param pValueType
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public ExTextField(final String pId, final IModel<?> pModel, final Class<?> pValueType) {
            super(pId);
            this.add(new TextField("text", pModel, pValueType)); //$NON-NLS-1$
        }
    }
}
