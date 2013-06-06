/**
 * 
 */
package jabara.wicket.beaneditor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.ServiceLoader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

/**
 * @author jabaraster
 */
@RunWith(Enclosed.class)
public class PropertyEditorTest {

    /**
     * @author jabaraster
     */
    public static class DefaultProviderOnly {
        /**
         * 
         */
        @Rule
        public final FileEvacuater evacuator = new FileEvacuater("target/test-classes/META-INF/services/" //$NON-NLS-1$
                                                     + IPropertyEditorComponentProvider.class.getName());

        /**
         * Test method for {@link jabara.wicket.beaneditor.PropertyEditor#getProvider()}.
         */
        @SuppressWarnings("static-method")
        @Test
        public void _デフォルトのプロバイダが取得できること() {
            assertEquals(DefaultPropertyEditorComponentProvider.class, PropertyEditor.getProvider().getClass());
        }
    }

    /**
     * @author jabaraster
     */
    public static class MultiConcreteProvider {
        /**
         * 
         */
        @SuppressWarnings({ "boxing", "static-method" })
        @Test
        public void _デフォルトのプロバイダでないプロバイダが取得できること() {
            final IPropertyEditorComponentProvider provider = PropertyEditor.getProvider();
            assertThat(provider instanceof DefaultPropertyEditorComponentProvider, is(false));
        }
    }

    /**
     * @author jabaraster
     */
    public static class NoProvider {
        private final ExpectedException exs  = ExpectedException.none();
        /**
         * 
         */
        @Rule
        public final RuleChain          rule = RuleChain.outerRule(new FileEvacuater("target/classes/META-INF/services/" //$NON-NLS-1$
                                                     + IPropertyEditorComponentProvider.class.getName())) //
                                                     .around(new FileEvacuater("target/test-classes/META-INF/services/" //$NON-NLS-1$
                                                             + IPropertyEditorComponentProvider.class.getName())) //
                                                     .around(this.exs);

        /**
         * 
         */
        @Test
        public void _プロバイダが１つもないと例外がスローされること() {
            this.exs.expect(IllegalStateException.class);
            this.exs.expectMessage(IPropertyEditorComponentProvider.class.getSimpleName() + "の実装クラスが見付かりませんでした. " + ServiceLoader.class.getName() //$NON-NLS-1$
                    + "の仕様に従って実装クラスと設定ファイルを配置して下さい."); //$NON-NLS-1$
            PropertyEditor.getProvider();
        }
    }

    private static class FileEvacuater extends ExternalResource {
        private static int _index = 0;

        private final File before;
        private final File after;

        FileEvacuater(final String pPath) {
            this.before = new File(pPath);
            this.after = new File(this.before.getParent(), "evacuation_" + _index++); //$NON-NLS-1$
        }

        @Override
        protected void after() {
            this.after.renameTo(this.before);
            super.after();
        }

        @Override
        protected void before() throws Throwable {
            super.before();
            this.before.renameTo(this.after);
        }
    }
}
