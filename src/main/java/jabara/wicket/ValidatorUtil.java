/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.metamodel.Attribute;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * @author jabaraster
 */
public final class ValidatorUtil {

    private ValidatorUtil() {
        //
    }

    /**
     * 文字列入力用コンポーネントの基本的な入力チェックを追加します. <br>
     * 対象のフィールドにアノテーション{@link Size}が付与されている必要があります. <br>
     * アノテーション{@link NotNull}が付与されている場合、{@link FormComponent#setRequired(boolean)}にtrueがセットされます. <br>
     * <br>
     * 現在の仕様では、型Tの<strong>サブクラス</strong>のフィールドに対して処理出来ません. <br>
     * フィールドが定義されている型そのもののClassオブジェクトを指定するようにして下さい. <br>
     * 
     * @param pComponent Wicketの入力コンポーネント.
     * @param pCheckTargetObjectType チェック対象オブジェクトの型.
     * @param pPropertyName チェック対象プロパティ.
     * @return 追加したチェックに関する情報.
     */
    public static ValidatorInfo setSimpleStringValidator( //
            final FormComponent<String> pComponent //
            , final Class<?> pCheckTargetObjectType //
            , final String pPropertyName) {

        ArgUtil.checkNull(pComponent, "pComponent"); //$NON-NLS-1$
        ArgUtil.checkNull(pCheckTargetObjectType, "pCheckTargetObjectType"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pPropertyName, "pPropertyName"); //$NON-NLS-1$

        final boolean required = isRequired(pCheckTargetObjectType, pPropertyName);
        final Size size = getSizeAnnotation(pCheckTargetObjectType, pPropertyName);
        pComponent.setRequired(required);
        if (size != null) {
            pComponent.add(createStringValidator(size));
        }
        return new ValidatorInfo(required, size);
    }

    /**
     * {@link #setSimpleStringValidator(FormComponent, Class, String)}と同じ効果です.
     * 
     * @param <T> チェック対象オブジェクトの型.
     * @param pComponent Wicketの入力コンポーネント.
     * @param pCheckTargetObjectType チェック対象オブジェクトの型.
     * @param pPropertyName チェック対象プロパティ.
     * @return 追加したチェックに関する情報.
     */
    public static <T> ValidatorInfo setSimpleStringValidator( //
            final FormComponent<String> pComponent //
            , final Class<T> pCheckTargetObjectType //
            , final Attribute<T, String> pPropertyName) {

        ArgUtil.checkNull(pComponent, "pComponent"); //$NON-NLS-1$
        ArgUtil.checkNull(pCheckTargetObjectType, "pCheckTargetObjectType"); //$NON-NLS-1$
        ArgUtil.checkNull(pPropertyName, "pPropertyName"); //$NON-NLS-1$

        return setSimpleStringValidator(pComponent, pCheckTargetObjectType, pPropertyName.getName());
    }

    private static <T> IValidator<String> createStringValidator(final Size pSize) {
        return new StringValidator(Integer.valueOf(pSize.min()), Integer.valueOf(pSize.max()));
    }

    private static Field getField(final Class<?> pCheckTargetObjectType, final String pPropertyName) {
        try {
            return pCheckTargetObjectType.getDeclaredField(pPropertyName);
        } catch (final NoSuchFieldException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private static <T> Size getSizeAnnotation(final Class<T> pCheckTargetObjectType, final String pPropertyName) {
        final Field field = getField(pCheckTargetObjectType, pPropertyName);
        return field.getAnnotation(Size.class);
    }

    private static <T> boolean isRequired(final Class<T> pCheckTargetObjectType, final String pPropertyName) {
        final Field field = getField(pCheckTargetObjectType, pPropertyName);
        return field.isAnnotationPresent(NotNull.class);
    }

    /**
     * @author jabaraster
     */
    public static class ValidatorInfo implements Serializable {
        private static final long serialVersionUID = -5292946437870881954L;

        private final boolean     required;
        private final Size        size;

        /**
         * @param pRequired -
         * @param pSize -
         */
        public ValidatorInfo(final boolean pRequired, final Size pSize) {
            this.required = pRequired;
            this.size = pSize;
        }

        /**
         * @return sizeを返す.
         * @throws NotFound -
         */
        public Size getSize() throws NotFound {
            if (this.size == null) {
                throw NotFound.GLOBAL;
            }
            return this.size;
        }

        /**
         * @return requiredを返す.
         */
        public boolean isRequired() {
            return this.required;
        }
    }
}
