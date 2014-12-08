/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.IProducer2;
import jabara.general.NotFound;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class BootstrapTab extends Panel {
    private static final long                      serialVersionUID = 8082212651799681507L;

    private final List<? extends BootstrapTabData> tabDatas;
    private ListView<BootstrapTabData>             knobs;
    private ListView<BootstrapTabData>             contents;

    /**
     * @param pId -
     * @param pTabDataModel -
     */
    public BootstrapTab(final String pId, final IModel<List<? extends BootstrapTabData>> pTabDataModel) {
        super(pId, pTabDataModel);
        ArgUtil.checkNull(pTabDataModel, "pModel"); //$NON-NLS-1$
        this.tabDatas = pTabDataModel.getObject();
        this.add(getKnobs());
        this.add(getContents());
    }

    /**
     * @param pId -
     * @param pTabData -
     */
    public BootstrapTab(final String pId, final List<BootstrapTabData> pTabData) {
        this(pId, Models.ofList(pTabData));
    }

    /**
     * @see org.apache.wicket.Component#onBeforeRender()
     */
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        try {
            final List<ListItem<BootstrapTabData>> knobItems = collectItems(getKnobs());
            final List<ListItem<BootstrapTabData>> contentItems = collectItems(getContents());
            for (int i = 0; i < knobItems.size(); i++) {
                final ListItem<BootstrapTabData> knobItem = knobItems.get(i);
                final ListItem<BootstrapTabData> contentItem = contentItems.get(i);
                final WebMarkupContainer knob = Components.findFirst(knobItem, WebMarkupContainer.class);
                knob.add(AttributeModifier.append("href", "#" + contentItem.getMarkupId())); //$NON-NLS-1$//$NON-NLS-2$
            }
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private ListView<BootstrapTabData> getContents() {
        if (this.contents == null) {
            this.contents = new ListView<BootstrapTabData>("contents", this.tabDatas) { //$NON-NLS-1$
                private static final long serialVersionUID = -6728159173785858994L;

                @Override
                protected void populateItem(final ListItem<BootstrapTabData> pItem) {
                    pItem.setOutputMarkupId(true);
                    pItem.add(pItem.getModelObject().getTabContentProducer().produce("content")); //$NON-NLS-1$
                    if (pItem.getIndex() == 0) {
                        activate(pItem);
                    }
                }
            };
        }
        return this.contents;
    }

    private ListView<BootstrapTabData> getKnobs() {
        if (this.knobs == null) {
            this.knobs = new ListView<BootstrapTabData>("knobs", this.tabDatas) { //$NON-NLS-1$
                private static final long serialVersionUID = -6728159173785858994L;

                @Override
                protected void populateItem(final ListItem<BootstrapTabData> pItem) {
                    final WebMarkupContainer knobLink = new WebMarkupContainer("knobLink"); //$NON-NLS-1$
                    knobLink.add(new Label("label", pItem.getModelObject().getTabLabel())); //$NON-NLS-1$
                    pItem.add(knobLink);

                    if (pItem.getIndex() == 0) {
                        activate(pItem);
                    }
                }
            };
        }
        return this.knobs;
    }

    private static void activate(final Component pItem) {
        pItem.add(AttributeModifier.append("class", "active")); //$NON-NLS-1$//$NON-NLS-2$
    }

    private static List<ListItem<BootstrapTabData>> collectItems(final ListView<BootstrapTabData> v) {
        final List<ListItem<BootstrapTabData>> ret = new ArrayList<ListItem<BootstrapTabData>>();
        v.visitChildren(ListItem.class, new IVisitor<ListItem<BootstrapTabData>, Object>() {
            @Override
            public void component(final ListItem<BootstrapTabData> pObject, @SuppressWarnings("unused") final IVisit<Object> pVisit) {
                final Object o = pObject.getModelObject();
                if (o instanceof BootstrapTabData) {
                    ret.add(pObject);
                }
            }
        });
        return ret;
    }

    /**
     * @author jabaraster
     */
    public static class BootstrapTabData implements Serializable {
        private static final long               serialVersionUID = 1748211402752078563L;

        private final String                    tabLabel;
        private final IProducer2<String, Panel> tabContentProducer;

        /**
         * @param pTabText -
         * @param pTabContentProducer -
         */
        public BootstrapTabData(final String pTabText, final IProducer2<String, Panel> pTabContentProducer) {
            ArgUtil.checkNullOrEmpty(pTabText, "pTabText"); //$NON-NLS-1$
            ArgUtil.checkNull(pTabContentProducer, "pTabContentProducer"); //$NON-NLS-1$
            this.tabLabel = pTabText;
            this.tabContentProducer = pTabContentProducer;
        }

        /**
         * @return タブコンテンツとなる{@link Panel}を返すオブジェクト.
         */
        public IProducer2<String, Panel> getTabContentProducer() {
            return this.tabContentProducer;
        }

        /**
         * @return タブ部分のテキスト.
         */
        public String getTabLabel() {
            return this.tabLabel;
        }
    }
}
