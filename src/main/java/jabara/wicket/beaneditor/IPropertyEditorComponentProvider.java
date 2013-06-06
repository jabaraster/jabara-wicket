/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;

import org.apache.wicket.Component;

/**
 * @author jabaraster
 */
public interface IPropertyEditorComponentProvider {

    /**
     * @param pId
     * @param pBean
     * @param pProperty
     * @return -
     */
    Component create(String pId, Object pBean, BeanProperty pProperty);
}
