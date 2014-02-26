/**
 * 
 */
package jabara.wicket;

import jabara.general.Empty;
import jabara.general.IProducer2;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @param <E> -
 * @author jabaraster
 */
public class BootstrapLinkColumn<E> extends LinkColumn<E> {
    private static final long serialVersionUID = 1392006843959295743L;

    /**
     * @param pLinkLabelModel -
     * @param pDestination -
     * @param pParametersProducer -
     * @param pIconType -
     * @param pButtonType -
     */
    public BootstrapLinkColumn( //
            final IModel<String> pLinkLabelModel //
            , final Class<? extends Page> pDestination //
            , final IProducer2<E, PageParameters> pParametersProducer //
            , final IconType pIconType //
            , final ButtonType pButtonType //
    ) {
        super( //
                pLinkLabelModel //
                , pDestination //
                , pParametersProducer //
                , AttributeModifier.append("class", getClassValue(pIconType)) // //$NON-NLS-1$
                , AttributeModifier.append("class", getClassValue(pButtonType)) // //$NON-NLS-1$
        );
    }

    private static String getClassValue(final ButtonType type) {
        if (type == null) {
            throw new IllegalArgumentException("null not allowed."); //$NON-NLS-1$
        }
        if (type == ButtonType.DEFAULT) {
            return Empty.STRING;
        }
        return "btn-" + type.name().toLowerCase(); //$NON-NLS-1$
    }

    private static String getClassValue(final IconType pType) {
        if (pType == null) {
            throw new IllegalArgumentException("null not allowed."); //$NON-NLS-1$
        }
        return "glyphicon-" + pType.name().toLowerCase(); //$NON-NLS-1$
    }

    /**
     * @author jabaraster
     */
    public enum ButtonType {
        /**
         * 
         */
        DEFAULT,
        /**
         * 
         */
        PRIMARY,
        /**
         * 
         */
        INFO,
        /**
         * 
         */
        SUCCESS,
        /**
         * 
         */
        WARNING,
        /**
         * 
         */
        DANGER,
        /**
         * 
         */
        INVERSE, ;
    }

    /**
     * @author jabaraster
     */
    public enum IconType {
        /**
         * 
         */
        PLUS,
        /**
         * 
         */
        EDIT,
        /**
         * 
         */
        TRASH, ;
    }
}
