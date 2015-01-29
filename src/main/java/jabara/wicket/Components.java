/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * @author jabaraster
 */
public final class Components {

    private Components() {
        // nop
    }

    /**
     * @param pRoot -
     * @param pFindTarget -
     * @return -
     * @throws NotFound -
     */
    public static <C extends Component> C findFirst( //
            final MarkupContainer pRoot //
            , final Class<C> pFindTarget //
    ) throws NotFound {

        ArgUtil.checkNull(pRoot, "pRoot"); //$NON-NLS-1$
        ArgUtil.checkNull(pFindTarget, "pFindTarget"); //$NON-NLS-1$

        final C ret = pRoot.visitChildren(pFindTarget, new IVisitor<C, C>() {
            @Override
            public void component(final C pObject, final IVisit<C> pVisit) {
                pVisit.stop(pObject);
            }
        });
        if (ret == null) {
            throw NotFound.GLOBAL;
        }
        return ret;
    }

    /**
     * @param pRoot -
     * @param pFindTarget -
     * @return -
     */
    public static <C extends Component> C findFirstUnsafe( //
            final MarkupContainer pRoot //
            , final Class<C> pFindTarget //
    ) {
        ArgUtil.checkNull(pRoot, "pRoot"); //$NON-NLS-1$
        ArgUtil.checkNull(pFindTarget, "pFindTarget"); //$NON-NLS-1$

        try {
            return findFirst(pRoot, pFindTarget);
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

}
