/**
 * 
 */
package jabara.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author jabaraster
 */
public class NullAjaxCallback implements IAjaxCallback {
    private static final long            serialVersionUID = -6658626435539881575L;

    /**
     * 
     */
    public static final NullAjaxCallback GLOBAL           = new NullAjaxCallback();

    /**
     * @see jabara.wicket.IAjaxCallback#call(org.apache.wicket.ajax.AjaxRequestTarget)
     */
    @Override
    public void call(@SuppressWarnings("unused") final AjaxRequestTarget pTarget) {
        // 処理なし
    }

}
