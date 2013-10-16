/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;
import jabara.general.Empty;
import jabara.general.ExceptionUtil;
import jabara.general.IProducer;
import jabara.general.io.DataOperation;
import jabara.general.io.DataOperation.Operation;
import jabara.general.io.FileReadableData;
import jabara.general.io.IReadableData;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.string.interpolator.MapVariableInterpolator;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class FileUploadPanel extends Panel {
    private static final long  serialVersionUID     = -220850110042516428L;

    private IProducer<File>    temporaryFileCreator = DefaultTemporaryFileCreator.GLOBAL;

    private Operation          operation            = Operation.NOOP;
    private FileReadableData   uploadData;

    private IAjaxCallback      onUpload             = NullAjaxCallback.GLOBAL;
    private IAjaxCallback      onReset              = NullAjaxCallback.GLOBAL;
    private IAjaxCallback      onDelete             = NullAjaxCallback.GLOBAL;

    private final Handler      handler              = new Handler();

    private WebMarkupContainer container;
    private FileUploadField    file;
    private Label              fileValue;
    private AjaxButton         hiddenUploader;
    private AjaxButton         deleter;
    private AjaxButton         restorer;

    /**
     * @param pId -
     */
    public FileUploadPanel(final String pId) {
        super(pId);
        this.add(getContainer());
    }

    /**
     * @return -
     */
    public DataOperation getDataOperation() {
        return new DataOperation(this.operation, this.uploadData);
    }

    /**
     * @return -
     */
    public FileUploadField getFile() {
        if (this.file == null) {
            this.file = new FileUploadField("file"); //$NON-NLS-1$
            this.file.setOutputMarkupId(true);
        }
        return this.file;
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(ComponentCssHeaderItem.forType(FileUploadPanel.class));
        pResponse.render(ComponentJavaScriptHeaderItem.forType(FileUploadPanel.class));

        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("containerId", getContainer().getMarkupId()); //$NON-NLS-1$
        vars.put("url", getHiddenUploaderCallbackUrl()); //$NON-NLS-1$
        vars.put("hiddenUploaderId", getHiddenUploader().getMarkupId()); //$NON-NLS-1$
        vars.put("fileFieldId", getFile().getMarkupId()); //$NON-NLS-1$
        final String script = MapVariableInterpolator.interpolate( //
                "initializeFileUploadPanel('${containerId}', '${url}', '${hiddenUploaderId}', '${fileFieldId}')" // //$NON-NLS-1$
                , vars //
                );
        pResponse.render(OnDomReadyHeaderItem.forScript(script));
    }

    /**
     * @param pCallback -
     * @return -
     */
    public FileUploadPanel setOnDelete(final IAjaxCallback pCallback) {
        ArgUtil.checkNull(pCallback, "pCallback"); //$NON-NLS-1$
        this.onDelete = pCallback == null ? NullAjaxCallback.GLOBAL : pCallback;
        return this;
    }

    /**
     * @param pCallback -
     * @return -
     */
    public FileUploadPanel setOnReset(final IAjaxCallback pCallback) {
        ArgUtil.checkNull(pCallback, "pCallback"); //$NON-NLS-1$
        this.onReset = pCallback == null ? NullAjaxCallback.GLOBAL : pCallback;
        return this;
    }

    /**
     * @param pCallback -
     * @return -
     */
    public FileUploadPanel setOnUpload(final IAjaxCallback pCallback) {
        this.onUpload = pCallback == null ? NullAjaxCallback.GLOBAL : pCallback;
        return this;
    }

    /**
     * @param pCreator -
     * @return -
     */
    public FileUploadPanel setTemporaryFileCreator(final IProducer<File> pCreator) {
        this.temporaryFileCreator = pCreator == null ? DefaultTemporaryFileCreator.GLOBAL : pCreator;
        return this;
    }

    /**
     * @return -
     */
    protected WebMarkupContainer getContainer() {
        if (this.container == null) {
            this.container = new WebMarkupContainer("container"); //$NON-NLS-1$
            this.container.setOutputMarkupId(true);
            this.container.add(getFile());
            this.container.add(getFileValue());
            this.container.add(getHiddenUploader());
            this.container.add(getDeleter());
            this.container.add(getRestorer());

        }
        return this.container;
    }

    /**
     * @return -
     */
    protected AjaxButton getDeleter() {
        if (this.deleter == null) {
            this.deleter = new IndicatingAjaxButton("deleter") { //$NON-NLS-1$
                private static final long serialVersionUID = 9021528280576073380L;

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    FileUploadPanel.this.handler.onDelete(pTarget);
                }
            };
        }
        return this.deleter;
    }

    /**
     * @return -
     */
    @SuppressWarnings("nls")
    protected Label getFileValue() {
        if (this.fileValue == null) {
            this.fileValue = new Label("fileValue", new AbstractReadOnlyModel<String>() {
                private static final long serialVersionUID = -471390311954435759L;

                @Override
                public String getObject() {
                    if (FileUploadPanel.this.operation == Operation.UPDATE) {
                        return buildDataInformation(getString("uploaded"), FileUploadPanel.this.uploadData);
                    }
                    return getString("noUploadFile"); //$NON-NLS-1$
                }
            });
        }
        return this.fileValue;
    }

    /**
     * @return -
     */
    protected AjaxButton getHiddenUploader() {
        if (this.hiddenUploader == null) {
            this.hiddenUploader = new AjaxButton("hiddenUploader") { //$NON-NLS-1$
                private static final long serialVersionUID = 3637109892153262303L;

                @Override
                protected void onError(final AjaxRequestTarget pTarget, final Form<?> pForm) {
                    onUploadError(pTarget, pForm);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, final Form<?> pForm) {
                    onUploadSubmit(pTarget, pForm);
                    FileUploadPanel.this.handler.onUpload(pTarget);
                }
            };
            this.hiddenUploader.setOutputMarkupId(true);
        }
        return this.hiddenUploader;
    }

    /**
     * @return -
     */
    protected AjaxButton getRestorer() {
        if (this.restorer == null) {
            this.restorer = new IndicatingAjaxButton("restorer") { //$NON-NLS-1$
                private static final long serialVersionUID = 622729316081692586L;

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    FileUploadPanel.this.handler.onRestore(pTarget);
                }
            };
        }
        return this.restorer;
    }

    /**
     * @param pTarget -
     * @param pForm -
     */
    @SuppressWarnings("unused")
    protected void onUploadError(final AjaxRequestTarget pTarget, final Form<?> pForm) {
        // デフォルト処理なし
    }

    /**
     * @param pTarget -
     * @param pForm -
     */
    @SuppressWarnings("unused")
    protected void onUploadSubmit(final AjaxRequestTarget pTarget, final Form<?> pForm) {
        // デフォルト処理なし
    }

    private CharSequence getHiddenUploaderCallbackUrl() {
        final List<AjaxFormSubmitBehavior> bs = getHiddenUploader().getBehaviors(AjaxFormSubmitBehavior.class);
        return bs.isEmpty() ? Empty.STRING : bs.get(0).getCallbackUrl();
    }

    @SuppressWarnings("resource")
    private FileReadableData saveToTemporaryFile(final FileUpload pUpload) {
        final File temp = this.temporaryFileCreator.produce();
        OutputStream out = null;
        BufferedOutputStream bufOut = null;
        try {
            out = new FileOutputStream(temp);
            bufOut = new BufferedOutputStream(out);
            IOUtils.copy(pUpload.getInputStream(), bufOut);

            return new FileReadableData(pUpload.getClientFileName(), pUpload.getContentType(), temp);

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);

        } finally {
            pUpload.closeStreams();
            IOUtils.closeQuietly(bufOut);
            IOUtils.closeQuietly(out);
        }
    }

    @SuppressWarnings("boxing")
    private static String buildDataInformation(final String pPrefix, final IReadableData pData) {
        return pPrefix + MessageFormat.format("{0}({1} {2,number,#,##0}KB)", pData.getName(), pData.getContentType(), pData.getSize() / 1024); //$NON-NLS-1$
    }

    /**
     * @author jabaraster -
     */
    public static class DefaultTemporaryFileCreator implements IProducer<File> {
        private static final long                       serialVersionUID = 830320220166815993L;

        /**
         * 
         */
        public static final DefaultTemporaryFileCreator GLOBAL           = new DefaultTemporaryFileCreator();

        /**
         * @see jabara.general.IProducer#produce()
         */
        @Override
        public File produce() {
            try {
                return File.createTempFile(FileUploadPanel.class.getName(), ".dat"); //$NON-NLS-1$
            } catch (final IOException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = -5086355719055362617L;

        void onDelete(final AjaxRequestTarget pTarget) {
            FileUploadPanel.this.operation = Operation.DELETE;
            deleteUploadDataIfExists();

            pTarget.add(getFileValue());
            FileUploadPanel.this.onDelete.call(pTarget);
        }

        void onRestore(final AjaxRequestTarget pTarget) {
            FileUploadPanel.this.operation = Operation.NOOP;
            deleteUploadDataIfExists();

            pTarget.add(getFileValue());
            FileUploadPanel.this.onReset.call(pTarget);
        }

        void onUpload(final AjaxRequestTarget pTarget) {
            final FileUpload upload = getFile().getFileUpload();
            if (upload != null) {
                if (FileUploadPanel.this.uploadData != null) {
                    FileUploadPanel.this.uploadData.deleteFile();
                }
                FileUploadPanel.this.uploadData = saveToTemporaryFile(upload);
                FileUploadPanel.this.operation = Operation.UPDATE;
            }
            pTarget.add(getFileValue());
            FileUploadPanel.this.onUpload.call(pTarget);
        }

        private void deleteUploadDataIfExists() {
            if (FileUploadPanel.this.uploadData != null) {
                FileUploadPanel.this.uploadData.deleteFile();
            }
        }
    }
}
