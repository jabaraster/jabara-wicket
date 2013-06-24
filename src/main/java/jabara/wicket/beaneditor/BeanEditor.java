/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperties;
import jabara.bean.BeanProperty;
import jabara.general.ArgUtil;
import jabara.general.NotFound;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @param <E> 編集対象オブジェクトの型.
 * @author jabaraster
 */
public class BeanEditor<E> extends Panel {
    private static final long                 serialVersionUID    = 7179674106258203277L;

    /**
     * 
     */
    protected final E                         bean;

    /**
     * 
     */
    protected final BeanProperties            propertiesValue;

    private ListView<BeanProperty>            properties;

    private final Map<String, PropertyEditor> propertyName2Editor = new HashMap<String, PropertyEditor>();

    /**
     * @param pId -
     * @param pBean -
     */
    public BeanEditor(final String pId, final E pBean) {
        super(pId);
        this.bean = ArgUtil.checkNull(pBean, "pBean"); //$NON-NLS-1$
        this.propertiesValue = BeanProperties.getInstance(pBean.getClass()).toVisiblePropertiesOnly();
        this.add(getProperties());
    }

    /**
     * @param pPropertyName -
     * @return 指定の名前のプロパティを編集するためのエディタコンポーネント.
     * @throws NotFound -
     */
    public PropertyEditor findInputComponent(final String pPropertyName) throws NotFound {
        ArgUtil.checkNullOrEmpty(pPropertyName, "pPropertyName"); //$NON-NLS-1$

        final PropertyEditor editor = this.propertyName2Editor.get(pPropertyName);
        if (editor == null) {
            throw NotFound.GLOBAL;
        }
        return editor;
    }

    /**
     * @return 編集中のオブジェクト.
     */
    public E getBean() {
        return this.bean;
    }

    private ListView<BeanProperty> getProperties() {
        if (this.properties == null) {
            this.properties = new ListView<BeanProperty>("properties", this.propertiesValue.toList()) { //$NON-NLS-1$
                private static final long serialVersionUID = 8600810849579410319L;

                @SuppressWarnings("synthetic-access")
                @Override
                protected void populateItem(final ListItem<BeanProperty> pItem) {
                    final BeanProperty property = BeanEditor.this.propertiesValue.get(pItem.getIndex());
                    pItem.add(new Label("label", property.getLocalizedName())); //$NON-NLS-1$

                    final PropertyEditor editor = new PropertyEditor("property", BeanEditor.this.bean, property); //$NON-NLS-1$
                    pItem.add(editor);
                    pItem.add(new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(editor))); //$NON-NLS-1$

                    BeanEditor.this.propertyName2Editor.put(property.getName(), editor);
                }
            };
            this.properties.setReuseItems(true);
        }
        return this.properties;
    }
}
