/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;
import jabara.general.NotFound;
import jabara.general.ReflectionUtil;
import jabara.wicket.ValidatorUtil;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author jabaraster
 */
public class DefaultPropertyEditorComponentProvider implements IPropertyEditorComponentProvider {

    /**
     * @see jabara.wicket.beaneditor.IPropertyEditorComponentProvider#create(java.lang.String, java.lang.Object, jabara.bean.BeanProperty)
     */
    @SuppressWarnings({ "rawtypes" })
    @Override
    public Component create(final String pId, final Object pBean, final BeanProperty pProperty) {
        try {
            final EditorFactory factoryAnno = pProperty.getAnnocation(EditorFactory.class);
            final IEditorFactory factory = ReflectionUtil.newInstance(factoryAnno.value());
            return factory.create(pId, pBean, pProperty);
        } catch (final NotFound e) {
            // 無視して次の処理へ.
        }

        if (pProperty.isReadOnly()) {
            return new LabelInPanel(pId, new PropertyModel(pBean, pProperty.getName()));
        }
        if (pProperty.isMultiLine()) {
            return new TextAreaInPanel(pId, pProperty, new PropertyModel<String>(pBean, pProperty.getName()));
        }

        final Class<?> type = pProperty.getType();
        if (type.equals(boolean.class)) {
            return new PrimitiveBooleanEditor(pId, pProperty, new PropertyModel<Boolean>(pBean, pProperty.getName()));
        }
        if (type.equals(Boolean.class)) {
            if (pProperty.isNullable()) {
                return new BooleanEditor(pId, pProperty, new PropertyModel<Boolean>(pBean, pProperty.getName()));
            }
            return new PrimitiveBooleanEditor(pId, pProperty, new PropertyModel<Boolean>(pBean, pProperty.getName()));
        }
        if (Enum.class.isAssignableFrom(type)) {
            return new EnumEditor(pId, pProperty, new PropertyModel<Enum<?>>(pBean, pProperty.getName()));
        }

        return new TextFieldInPanel(pId, pProperty, new PropertyModel(pBean, pProperty.getName()));
    }

    /**
     * @author jabaraster
     */
    public static class LabelInPanel extends Panel {
        private static final long serialVersionUID = -6482487723458468208L;

        LabelInPanel(final String pId, final IModel<?> pModel) {
            super(pId, pModel);
            this.add(new Label("text", pModel)); //$NON-NLS-1$
        }

    }

    /**
     * @author jabaraster
     */
    public static class TextFieldInPanel extends Panel {
        private static final long serialVersionUID = 8973259496268254876L;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        TextFieldInPanel(final String pId, final BeanProperty pProperty, final IModel<?> pModel) {
            super(pId);

            final Class<?> propertyType = pProperty.getType();
            final TextField text = new TextField("text", pModel, propertyType); //$NON-NLS-1$
            text.setLabel(Model.of(pProperty.getLocalizedName()));

            if (propertyType.equals(String.class)) {
                ValidatorUtil.setSimpleStringValidator(text, pProperty.getBeanType(), pProperty.getName());

            } else if (propertyType.isPrimitive()) {
                text.setRequired(true);
            }

            this.add(text);
        }
    }

    private static class TextAreaInPanel extends Panel {
        private static final long serialVersionUID = 517013022794807327L;

        TextAreaInPanel(final String pId, final BeanProperty pProperty, final IModel<String> pModel) {
            super(pId, pModel);

            final TextArea<String> text = new TextArea<String>("text", pModel); //$NON-NLS-1$
            text.setLabel(Model.of(pProperty.getLocalizedName()));
            ValidatorUtil.setSimpleStringValidator(text, pProperty.getBeanType(), pProperty.getName());

            this.add(text);
        }
    }

}
