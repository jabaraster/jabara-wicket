/**
 * 
 */
package jabara.wicket;

import jabara.general.ArgUtil;
import jabara.general.Empty;
import jabara.general.ExceptionUtil;
import jabara.general.IProducer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class FileUploadPanel extends Panel {
    private static final long  serialVersionUID     = -220850110042516428L;

    private IProducer<File>    temporaryFileCreator = DefaultTemporaryFileCreator.GLOBAL;

    private Operation          operation            = Operation.NOOP;
    private Data               uploadData;

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

    private WebMarkupContainer getContainer() {
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

    private AjaxButton getDeleter() {
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

    private FileUploadField getFile() {
        if (this.file == null) {
            this.file = new FileUploadField("file"); //$NON-NLS-1$
            this.file.setOutputMarkupId(true);
        }
        return this.file;
    }

    @SuppressWarnings("nls")
    private Label getFileValue() {
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

    private AjaxButton getHiddenUploader() {
        if (this.hiddenUploader == null) {
            this.hiddenUploader = new AjaxButton("hiddenUploader") { //$NON-NLS-1$
                private static final long serialVersionUID = 3637109892153262303L;

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    FileUploadPanel.this.handler.onUpload(pTarget);
                }
            };
            this.hiddenUploader.setOutputMarkupId(true);
        }
        return this.hiddenUploader;
    }

    private CharSequence getHiddenUploaderCallbackUrl() {
        final List<AjaxFormSubmitBehavior> bs = getHiddenUploader().getBehaviors(AjaxFormSubmitBehavior.class);
        return bs.isEmpty() ? Empty.STRING : bs.get(0).getCallbackUrl();
    }

    private AjaxButton getRestorer() {
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

    @SuppressWarnings("resource")
    private Data saveToTemporaryFile(final FileUpload pUpload) {
        final File temp = this.temporaryFileCreator.produce();
        OutputStream out = null;
        BufferedOutputStream bufOut = null;
        try {
            out = new FileOutputStream(temp);
            bufOut = new BufferedOutputStream(out);
            IOUtils.copy(pUpload.getInputStream(), bufOut);

            return new Data(pUpload.getClientFileName(), pUpload.getContentType(), temp);

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);

        } finally {
            pUpload.closeStreams();
            IOUtils.closeQuietly(bufOut);
            IOUtils.closeQuietly(out);
        }
    }

    @SuppressWarnings("boxing")
    private static String buildDataInformation(final String pPrefix, final Data pData) {
        return pPrefix + MessageFormat.format("{0}({1} {2,number,#,##0}KB)", pData.getName(), pData.getContentType(), pData.getSize() / 1024); //$NON-NLS-1$
    }

    /**
     * @author jabaraster -
     */
    public static class Data implements Serializable, Closeable {
        private static final long serialVersionUID = 3840006366554216969L;

        private final String      contentType;
        private final String      name;

        private File              saveFile;
        private InputStream       in;

        /**
         * @param pContentType -
         * @param pName -
         * @param pSaveFile -
         */
        private Data(final String pName, final String pContentType, final File pSaveFile) {
            ArgUtil.checkNullOrEmpty(pContentType, "pContentType"); //$NON-NLS-1$
            ArgUtil.checkNull(pSaveFile, "pSaveFile"); //$NON-NLS-1$
            this.name = pName == null ? Empty.STRING : pName;
            this.contentType = pContentType;
            this.saveFile = pSaveFile;
        }

        /**
         * @see java.io.Closeable#close()
         */
        @Override
        public void close() {
            if (this.saveFile == null) {
                return;
            }
            if (this.in != null) {
                try {
                    this.in.close();
                } catch (final IOException e) {
                    // 無視
                }
            }
            this.saveFile.delete();
            this.saveFile = null;
        }

        /**
         * @return -
         */
        public String getContentType() {
            return this.contentType;
        }

        /**
         * @return -
         */
        public InputStream getInputStream() {
            if (this.saveFile == null) {
                throw new IllegalStateException("closeを呼び出した後に当メソッドを呼び出すことは出来ません."); //$NON-NLS-1$
            }
            if (this.in != null) {
                return this.in;
            }
            try {
                this.in = new BufferedInputStream(new FileInputStream(this.saveFile));
                return this.in;
            } catch (final FileNotFoundException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }

        /**
         * @return -
         */
        public String getName() {
            return this.name;
        }

        /**
         * @return -
         */
        public long getSize() {
            return getSizeCore();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @SuppressWarnings("nls")
        @Override
        public String toString() {
            return "Data [contentType=" + this.contentType + ", size=" + getSizeCore() + ", name=" + this.name + "]";
        }

        private long getSizeCore() {
            return this.saveFile.length();
        }
    }

    /**
     * @author jabaraster -
     */
    public static class DataOperation implements Serializable, Closeable {
        private static final long serialVersionUID = -3960649395769259898L;

        private final Operation   operation;
        private final Data        data;

        /**
         * @param pOperation -
         * @param pData pOperationが{@link Operation#UPDATE}以外の場合、ここで指定されたオブジェクトは使われません.
         */
        public DataOperation(final Operation pOperation, final Data pData) {
            ArgUtil.checkNull(pOperation, "pOperation"); //$NON-NLS-1$
            if (this.operation == Operation.UPDATE && pData == null) {
                throw new IllegalArgumentException("pOperationがUPDATEのとき、pDataはnullであってはいけません."); //$NON-NLS-1$
            }

            this.operation = pOperation;
            this.data = this.operation == Operation.UPDATE ? pData : null;
        }

        /**
         * @see java.io.Closeable#close()
         */
        @Override
        public void close() {
            this.data.close();
        }

        /**
         * @return dataを返す.
         * @throws IllegalStateException operationがUPDATE以外の場合.
         */
        public Data getData() {
            if (this.data == null) {
                throw new IllegalStateException("operationがUPDATEでなければこのメソッドは呼び出せません."); //$NON-NLS-1$
            }
            return this.data;
        }

        /**
         * @return operationを返す.
         */
        public Operation getOperation() {
            return this.operation;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @SuppressWarnings("nls")
        @Override
        public String toString() {
            return "DataOperation [operation=" + this.operation + ", data=" + this.data + "]";
        }
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

    /**
     * @author jabaraster
     */
    public enum Operation {
        /**
         * 
         */
        NOOP,
        /**
         * 
         */
        UPDATE,
        /**
         * 
         */
        DELETE, ;
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = -5086355719055362617L;

        void onDelete(final AjaxRequestTarget pTarget) {
            FileUploadPanel.this.operation = Operation.DELETE;
            if (FileUploadPanel.this.uploadData != null) {
                FileUploadPanel.this.uploadData.close();
            }
            pTarget.add(getFileValue());
            FileUploadPanel.this.onDelete.call(pTarget);
        }

        void onRestore(final AjaxRequestTarget pTarget) {
            FileUploadPanel.this.operation = Operation.NOOP;
            if (FileUploadPanel.this.uploadData != null) {
                FileUploadPanel.this.uploadData.close();
            }
            pTarget.add(getFileValue());
            FileUploadPanel.this.onReset.call(pTarget);
        }

        void onUpload(final AjaxRequestTarget pTarget) {
            final FileUpload upload = getFile().getFileUpload();
            if (upload != null) {
                FileUploadPanel.this.uploadData = saveToTemporaryFile(upload);
                FileUploadPanel.this.operation = Operation.UPDATE;
            }
            pTarget.add(getFileValue());
            FileUploadPanel.this.onUpload.call(pTarget);
        }
    }
}
