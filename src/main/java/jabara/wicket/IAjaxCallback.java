/**
 * 
 */
package jabara.wicket;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author jabaraster
 */
public interface IAjaxCallback extends Serializable {

    /**
     * @param pTarget -
     */
    void call(AjaxRequestTarget pTarget);
}
