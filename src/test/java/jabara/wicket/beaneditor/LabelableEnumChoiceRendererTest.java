/**
 * 
 */
package jabara.wicket.beaneditor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jabara.general.SortRule;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jabaraster
 */
public class LabelableEnumChoiceRendererTest {

    private LabelableEnumChoiceRenderer<SortRule> sut;

    /**
     * 
     */
    @Test
    public void _getDisplayValue() {
        final Object ex = SortRule.ASC.getLabel();
        assertThat(this.sut.getDisplayValue(SortRule.ASC), is(ex));
    }

    /**
     * 
     */
    @Test
    public void _getIdValue() {
        assertThat(this.sut.getIdValue(null, 0), is(String.valueOf((Object) null)));
        assertThat(this.sut.getIdValue(SortRule.ASC, 0), is(String.valueOf(SortRule.ASC.ordinal())));
        assertThat(this.sut.getIdValue(SortRule.DESC, 0), is(String.valueOf(SortRule.DESC.ordinal())));
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
