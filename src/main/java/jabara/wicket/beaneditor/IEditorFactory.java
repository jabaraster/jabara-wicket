/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jabaraster
 */
public interface IEditorFactory {

    /**
     * @param pId -
     * @param pBean -
     * @param pProperty -
     * @return -
     */
    Panel create(String pId, Object pBean, BeanProperty pProperty);
}
