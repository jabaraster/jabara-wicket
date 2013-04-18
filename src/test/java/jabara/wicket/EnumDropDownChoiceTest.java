/**
 * 
 */
package jabara.wicket;

import java.lang.reflect.ParameterizedType;

import org.junit.Test;

/**
 * @author jabaraster
 */
public class EnumDropDownChoiceTest {

    /**
     * 
     */
    @SuppressWarnings({ "static-method", "unused" })
    @Test
    public void _test() {
        new Conc();
    }

    static class Conc extends Super<String> {
        Conc() {
            final ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
            jabara.Debug.write(genericSuperclass.getActualTypeArguments()[0]);
        }
    }

    static class Super<E> {
        //
    }
}
