/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperties;
import jabara.bean.BeanProperty;
import jabara.general.ArgUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @param <E>
 * @author jabaraster
 */
public class BeanEditor<E> extends Panel {
    private static final long      serialVersionUID = 7179674106258203277L;

    /**
     * 
     */
    protected final E              bean;

    /**
     * 
     */
    protected final BeanProperties propertiesModel;

    private Loop                   properties;

    /**
     * @param pId
     * @param pBean
     */
    public BeanEditor(final String pId, final E pBean) {
        super(pId);
        this.bean = ArgUtil.checkNull(pBean, "pBean"); //$NON-NLS-1$
        this.propertiesModel = BeanProperties.getInstance(pBean.getClass());
        this.add(getProperties());
    }

    /**
     * @return 編集中のオブジェクト.
     */
    public E getBean() {
        return this.bean;
    }

    private Loop getProperties() {
        if (this.properties == null) {
            this.properties = new Loop("properties", this.propertiesModel.size()) { //$NON-NLS-1$
                private static final long serialVersionUID = 8600810849579410319L;

                @Override
                protected void populateItem(final LoopItem pItem) {
                    final BeanProperty property = BeanEditor.this.propertiesModel.get(pItem.getIndex());
                    pItem.add(new Label("label", property.getLocalizedName())); //$NON-NLS-1$
                    pItem.add(new PropertyEditor<E>("property", BeanEditor.this.bean, property)); //$NON-NLS-1$
                }
            };

        }
        return this.properties;
    }
}
