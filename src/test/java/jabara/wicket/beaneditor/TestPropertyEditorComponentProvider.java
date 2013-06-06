/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;

import org.apache.wicket.Component;

/**
 * @author jabaraster
 */
public class TestPropertyEditorComponentProvider implements IPropertyEditorComponentProvider {

    /**
     * @see jabara.wicket.beaneditor.IPropertyEditorComponentProvider#create(java.lang.String, java.lang.Object, jabara.bean.BeanProperty)
     */
    @Override
    public Component create(final String pId, final Object pBean, final BeanProperty pProperty) {
        return new DefaultPropertyEditorComponentProvider().create(pId, pBean, pProperty);
    }

}
