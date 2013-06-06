/**
 * 
 */
package jabara.wicket.beaneditor;

import jabara.bean.BeanProperty;
import jabara.general.ArgUtil;

import java.util.ServiceLoader;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * @param <B>
 * @author jabaraster
 */
public class PropertyEditor<B> extends Panel {
    private static final long                             serialVersionUID = -5504683462568384945L;

    private static final IPropertyEditorComponentProvider _provider        = getProvider();

    /**
     * @param pId
     * @param pBean
     * @param pProperty
     */
    public PropertyEditor(final String pId, final B pBean, final BeanProperty pProperty) {
        super(pId);

        ArgUtil.checkNull(pBean, "pBean"); //$NON-NLS-1$
        ArgUtil.checkNull(pProperty, "pPropertyValue"); //$NON-NLS-1$

        this.add(_provider.create("value", pBean, pProperty)); //$NON-NLS-1$
    }

    static IPropertyEditorComponentProvider getProvider() {
        IPropertyEditorComponentProvider defaultProvider = null;
        for (final IPropertyEditorComponentProvider provider : ServiceLoader.load(IPropertyEditorComponentProvider.class)) {
            if (provider instanceof DefaultPropertyEditorComponentProvider) {
                defaultProvider = provider;
            } else {
                return provider;
            }
        }
        if (defaultProvider == null) {
            // 実装クラスが１つも配置されていない場合、ここに処理が移る.
            throw new IllegalStateException(IPropertyEditorComponentProvider.class.getSimpleName() + "の実装クラスが見付かりませんでした. " //$NON-NLS-1$
                    + ServiceLoader.class.getName() + "の仕様に従って実装クラスと設定ファイルを配置して下さい."); //$NON-NLS-1$
        }
        return defaultProvider;
    }
}
