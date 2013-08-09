/**
 * 
 */
package jabara.wicket;

import jabara.general.Empty;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class LongTextLabel extends Panel {
    private static final long   serialVersionUID            = -7797703660557474364L;

    /**
     * 
     */
    public static final int     DEFAULT_PRE_TEXT_CHAR_COUNT = 50;

    private final Handler       handler                     = new Handler();

    private final AtomicBoolean showAll                     = new AtomicBoolean(false);

    private int                 preTextCharCount            = DEFAULT_PRE_TEXT_CHAR_COUNT;

    private Label               pre;
    private Label               post;
    private AjaxLink<?>         moreReader;

    /**
     * @param pId -
     * @param pModel -
     */
    public LongTextLabel(final String pId, final IModel<String> pModel) {
        super(pId, pModel);
        this.add(getPre());
        this.add(getPost());
        this.add(getMoreReader());
    }

    /**
     * @param pId -
     * @param pText -
     */
    @SuppressWarnings("serial")
    public LongTextLabel(final String pId, final String pText) {
        this(pId, new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return pText;
            }
        });
    }

    /**
     * @return 初期表示するテキストの文字数.
     */
    public int getPreTextCharCount() {
        return this.preTextCharCount;
    }

    /**
     * @param pPreTextCharCount 初期表示するテキストの文字数.
     * @return このオブジェクト.
     */
    public LongTextLabel setPreTextCharCount(final int pPreTextCharCount) {
        this.preTextCharCount = pPreTextCharCount;
        return this;
    }

    private TextContext getContext() {
        final String s = getModelString(getDefaultModel());
        return TextContext.get(s, this.preTextCharCount);
    }

    @SuppressWarnings("serial")
    private AjaxLink<?> getMoreReader() {
        if (this.moreReader == null) {
            this.moreReader = new IndicatingAjaxLink<Object>("moreReader") { //$NON-NLS-1$
                @Override
                public boolean isVisible() {
                    return getContext().isShorten();
                }

                @Override
                public void onClick(final AjaxRequestTarget pTarget) {
                    LongTextLabel.this.handler.onMoreReaderOnClick(pTarget);
                }
            };
        }
        return this.moreReader;
    }

    @SuppressWarnings({ "serial", "nls" })
    private Label getPost() {
        if (this.post == null) {
            this.post = new BrLabel("post", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    final TextContext tc = getContext();
                    return LongTextLabel.this.showAll.get() ? tc.getPost() : Empty.STRING;
                }
            });
            this.post.setOutputMarkupId(true);
        }
        return this.post;
    }

    @SuppressWarnings({ "nls", "serial" })
    private Label getPre() {
        if (this.pre == null) {
            this.pre = new BrLabel("pre", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return getContext().getPre();
                }
            });
        }
        return this.pre;
    }

    private static String getModelString(final IModel<?> pModel) {
        final Object o = pModel.getObject();
        return o == null ? Empty.STRING : o.toString();
    }

    private static class BrLabel extends Label {
        private static final long serialVersionUID = 8841904249230821972L;

        BrLabel(final String pId, final IModel<?> pModel) {
            super(pId, pModel);
        }

        @Override
        public void onComponentTagBody(final MarkupStream pMarkupStream, final ComponentTag pOpenTag) {
            final String s = Strings.escapeMarkup(getModelString(getDefaultModel())).toString();
            final String body = s.replaceAll("\r\n", "<br/>"); //$NON-NLS-1$ //$NON-NLS-2$
            replaceComponentTagBody(pMarkupStream, pOpenTag, body);
        }
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = 429466173120921956L;

        void onMoreReaderOnClick(final AjaxRequestTarget pTarget) {
            LongTextLabel.this.showAll.set(!LongTextLabel.this.showAll.get());
            pTarget.add(getPost());
        }
    }

    private static class TextContext implements Serializable {
        private static final long serialVersionUID = -8976576849099958317L;

        private final boolean     shorten;
        private final String      pre;
        private final String      post;

        TextContext(final boolean pShorten, final String pPre, final String pPost) {
            this.shorten = pShorten;
            this.pre = pPre;
            this.post = pPost;
        }

        String getPost() {
            return this.post;
        }

        String getPre() {
            return this.pre;
        }

        boolean isShorten() {
            return this.shorten;
        }

        public static TextContext get(final String s, final int pPreCharCount) {
            if (s == null) {
                return new TextContext(false, Empty.STRING, Empty.STRING);
            }
            if (s.length() < pPreCharCount - 1) {
                return new TextContext(false, s, Empty.STRING);
            }
            return new TextContext(true, s.substring(0, pPreCharCount), s.substring(pPreCharCount));
        }
    }
}
