/**
 * 
 */
package jabara.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
public abstract class LoginPageInstantiationAuthorizer implements IAuthorizationStrategy {

    /**
     * @return 常にtrueを返します.
     * @see org.apache.wicket.authorization.IAuthorizationStrategy#isActionAuthorized(org.apache.wicket.Component,
     *      org.apache.wicket.authorization.Action)
     */
    @Override
    public boolean isActionAuthorized(@SuppressWarnings("unused") final Component pComponent, @SuppressWarnings("unused") final Action pAction) {
        return true;
    }

    /**
     * @see org.apache.wicket.authorization.IAuthorizationStrategy#isInstantiationAuthorized(java.lang.Class)
     */
    @Override
    public <T extends IRequestableComponent> boolean isInstantiationAuthorized(final Class<T> pComponentClass) {
        // Pageに載っているUI部品は常に表示OKにする.
        // どうせPage自体を表示NGにすれば部品も表示されないので、これでいい.
        if (!WebPage.class.isAssignableFrom(pComponentClass)) {
            return true;
        }

        // 認証済みなのにログインページを表示しようとした場合、メインページにリダイレクトさせる.
        if (getLoginPageType().equals(pComponentClass)) {
            if (!Session.get().isSessionInvalidated() && isAuthenticated()) {
                throw new RestartResponseAtInterceptPageException(getFirstPageType());
            }
            return true;
        }

        // 認証不要なページ（ログインページとか）は表示する.
        if (!getRestictedPageType().isAssignableFrom(pComponentClass)) {
            return true;
        }

        // 認証済みの場合は表示する.
        if (isAuthenticated()) {
            return true;
        }

        // ログインページにリダイレクトする.
        final Request request = RequestCycle.get().getRequest();
        final PageParameters parameters = new PageParameters();

        if (!request.getUrl().getPath().isEmpty()) {
            final String requestPath = request.getContextPath() + request.getFilterPath() + "/" + request.getUrl().getPath(); //$NON-NLS-1$
            parameters.add("u", requestPath); //$NON-NLS-1$
        }
        throw new RestartResponseAtInterceptPageException(getLoginPageType(), parameters);
    }

    /**
     * @return ログイン後に最初に表示されるページの型を返して下さい.
     */
    protected abstract Class<? extends Page> getFirstPageType();

    /**
     * @return ログインページの型を返して下さい.
     */
    protected abstract Class<? extends Page> getLoginPageType();

    /**
     * @return ログイン済みでないと表示してはいけないページの基底型を返して下さい.
     */
    protected abstract Class<? extends Page> getRestictedPageType();

    /**
     * @return 現在のセッションが認証済みかどうかを返して下さい.
     */
    protected abstract boolean isAuthenticated();

}
