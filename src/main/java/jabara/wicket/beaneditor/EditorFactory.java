/**
 * 
 */
package jabara.wicket.beaneditor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jabaraster
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EditorFactory {

    /**
     * @return プロパティを編集するためのエディタクラス. <br>
     *         ここに指定されるクラスはpublicな引数なしコンストラクタが定義されていなければいけません. <br>
     */
    Class<? extends IEditorFactory> value();
}
