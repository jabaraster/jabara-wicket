/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperties;
import jabara.bean.BeanProperty;
import jabara.general.ArgUtil;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @param <E> 編集対象オブジェクトの型.
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
    protected final BeanProperties propertiesValue;

    private ListView<BeanProperty> properties;

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
     * @return 編集中のオブジェクト.
     */
    public E getBean() {
        return this.bean;
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        final CssResourceReference css = new CssResourceReference(getClass(), getClass().getSimpleName() + ".css"); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(css));
    }

    private ListView<BeanProperty> getProperties() {
        if (this.properties == null) {
            this.properties = new ListView<BeanProperty>("properties", this.propertiesValue.toList()) { //$NON-NLS-1$
                private static final long serialVersionUID = 8600810849579410319L;

                @Override
                protected void populateItem(final ListItem<BeanProperty> pItem) {
                    final BeanProperty property = BeanEditor.this.propertiesValue.get(pItem.getIndex());
                    pItem.add(new Label("label", property.getLocalizedName())); //$NON-NLS-1$

                    final PropertyEditor editor = new PropertyEditor("property", BeanEditor.this.bean, property); //$NON-NLS-1$
                    pItem.add(editor);
                    pItem.add(new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(editor))); //$NON-NLS-1$
                }
            };
            this.properties.setReuseItems(true);
        }
        return this.properties;
    }
}
