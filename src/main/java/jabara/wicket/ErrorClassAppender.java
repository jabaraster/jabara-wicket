/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * 
 * @author jabaraster
 */
public final class ErrorClassAppender implements Serializable {
    private static final long   serialVersionUID = 6998584909786818613L;

    private final InnerAppender classAppender;

    /**
     * @param pErrorClassModel エラー時にclass属性に追加する値.
     */
    public ErrorClassAppender(final IModel<?> pErrorClassModel) {
        this.classAppender = new InnerAppender(pErrorClassModel);
    }

    /**
     * エラーのある入力項目にclass属性値を追加します. <br>
     * 
     * @param pForm このフォーム以下の入力項目が処理対象になります. <br>
     */
    public void addErrorClass(final Form<?> pForm) {
        ArgUtil.checkNull(pForm, "pForm"); //$NON-NLS-1$

        pForm.visitChildren(FormComponent.class, new IVisitor<FormComponent<?>, Object>() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void component(final FormComponent<?> pComponent, @SuppressWarnings("unused") final IVisit<Object> pVisit) {
                if (pComponent.isValid()) {
                    removeErrorClassAppender(pComponent);
                } else {
                    addErrorClassAppenderIfNot(pComponent);
                }
            }
        });
    }

    private void addErrorClassAppenderIfNot(final FormComponent<?> pComponent) {
        final List<InnerAppender> behaviors = pComponent.getBehaviors(InnerAppender.class);
        if (behaviors.isEmpty()) {
            pComponent.add(this.classAppender);
        }
    }

    private static void removeErrorClassAppender(final FormComponent<?> pComponent) {
        final List<InnerAppender> behaviors = pComponent.getBehaviors(InnerAppender.class);
        pComponent.remove(behaviors.toArray(new Behavior[behaviors.size()]));
    }

    private static class InnerAppender extends AttributeAppender {
        private static final long   serialVersionUID = 7305442479769301008L;

        private static final String CLASS_ATTRIBUTE  = "class";             //$NON-NLS-1$
        private static final String VALUE_SEPARATOR  = " ";                 //$NON-NLS-1$

        InnerAppender(final IModel<?> pReplaceModel) {
            super(CLASS_ATTRIBUTE, pReplaceModel, VALUE_SEPARATOR);
        }
    }

}
