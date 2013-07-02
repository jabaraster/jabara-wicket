/**
 * 
 */
package jabara.wicket;

import jabara.general.SortRule;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

/**
 * @author jabaraster
 */
@RunWith(Enclosed.class)
public class LabelableEnumChoiceRendererTest {
    /**
     * @param pArgs 起動引数.
     */
    public static void main(final String[] pArgs) {
        System.out.println(String.valueOf(null));
    }

    /**
     * @author jabaraster
     */
    public static class ILabelableImplementedEnum {
        private LabelableEnumChoiceRenderer<SortRule> sut;

        /**
         * 
         */
        @Test
        public void _getDisplayValue() {
            final Object is = SortRule.ASC.getLabel();
            assertThat(this.sut.getDisplayValue(SortRule.ASC), is(is));
        }

        /**
         * 
         */
        @Test
        public void _getIdValue() {
            assertThat(this.sut.getIdValue(null, 0), is("null")); //$NON-NLS-1$
        }

        /**
         * 
         */
        @SuppressWarnings("unused")
        @Before
        public void setUp() {
            new WicketTester(); // Session#get()を有効にするためにnewする.
            this.sut = new LabelableEnumChoiceRenderer<SortRule>();
        }
    }

}
