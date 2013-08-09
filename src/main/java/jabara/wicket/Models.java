/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jabaraster
 */
public final class Models {

    private Models() {
        // 処理なし
    }

    /**
     * @param pCollection -
     * @return -
     * @see Model#of(Collection)
     */
    public static <C> IModel<Collection<? extends C>> of(final Collection<? extends C> pCollection) {
        return Model.of(pCollection);
    }

    /**
     * @param pValue -
     * @return -
     * @see Model#of(Serializable)
     */
    public static <T extends Serializable> IModel<T> of(final T pValue) {
        return Model.<T> of(pValue);
    }

    /**
     * @param pList -
     * @return -
     * @see Model#ofList(List)
     */
    public static <C> IModel<List<? extends C>> ofList(final List<? extends C> pList) {
        return Model.ofList(pList);
    }

    /**
     * @param pMap -
     * @return -
     * @see Model#ofMap(Map)
     */
    public static <K, V> IModel<Map<K, V>> ofMap(final Map<K, V> pMap) {
        return Model.ofMap(pMap);
    }

    /**
     * @param pSet -
     * @return -
     * @see Model#ofSet(Set)
     */
    public static <C> IModel<Set<? extends C>> ofSet(final Set<? extends C> pSet) {
        return Model.ofSet(pSet);
    }

    /**
     * @param pValue -
     * @return -
     */
    public static <T> IModel<T> readOnly(final T pValue) {
        return new AbstractReadOnlyModel<T>() {
            private static final long serialVersionUID = 4041207690549375772L;

            /**
             * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
             */
            @Override
            public T getObject() {
                return pValue;
            }
        };
    }

    /**
     * @param pFormat -
     * @param pTime -
     * @return -
     */
    public static IModel<String> readOnlyDate(final String pFormat, final Date pTime) {
        ArgUtil.checkNullOrEmpty(pFormat, "pFormat"); //$NON-NLS-1$
        ArgUtil.checkNull(pTime, "pTime"); //$NON-NLS-1$
        return Models.readOnly(new SimpleDateFormat(pFormat).format(pTime));
    }
}
