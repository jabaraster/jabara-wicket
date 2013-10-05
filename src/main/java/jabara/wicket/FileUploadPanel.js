function initializeFileUploadPanel(pContainerId, pSubmitUrl, pHiddenUploaderId, pFileFieldId) {

	// このレベルにDOM、あるいはjQueryオブジェクトの変数を宣言してはいけない.

	$(document).on('change', '#' + pFileFieldId, function() {
		var root = $('#' + pContainerId);
		var fileValue = root.find('.fileValue');

		var fileField = $(this);
		fileValue.text(fileField.val());
		sendFileByAjax(pContainerId, pSubmitUrl, pHiddenUploaderId, fileField);
		return false;
	});

	function sendFileByAjax(pContainerId, pUrl, pHiddenUploaderId, pJQueryFileField) {
		var root = $('#' + pContainerId);
		var fileValue = root.find('.fileValue');

		var submitAjax = function() {
            $('#' + pHiddenUploaderId).get(0).click();
            fileValue.text('');
        };
        if (!window.FormData) {
            submitAjax();
            return;
        }

        var files = pJQueryFileField.get(0).files;
        if (!files) {
            submitAjax();
            return;
        }

        if (files.length == 0) return;

		var progressBarContainer = root.find('div.progressBarArea');
		var progressBar = progressBarContainer.find('> .progress > .progress-bar');
		progressBar.css('width', '5%'); // アニメーションが見えるように少し伸ばす.
        progressBarContainer.show('slow', function() {
            var file = files[0];
            if (file.size > 5*1024*1024) { // 5MBを越えるなら
                if (!confirm('大きなサイズのファイルのアップロードには時間がかかる可能性があります。アップロードしてよろしいですか？')) {
                    progressBarContainer.hide('slow');
                    pJQueryFileField.val(''); // クリアしておかないと、ユーザが同じファイルを選択したときにイベントが着火しない.
                    fileValue.text('');
                    return;
                }
            }
            doSend(pUrl, file, pJQueryFileField);
        });
	}

	function doSend(pUrl, pJSFile, pJQueryFileField) {
		var root = $('#' + pContainerId);
		var progressBarContainer = root.find('div.progressBarArea');
		var progressBar = progressBarContainer.find('> .progress > .progress-bar');

		progressBar.text('');
        progressBar.css('width', '0');

		var fd = new FormData();
		fd.append(pJQueryFileField.attr('name'), pJSFile);
		$.ajax({
			url: pUrl,
			type: 'post',
			data: fd,
			contentType: false,
			processData: false,
			dataType: 'text', // レスポンスのデータタイプ. textを明示しておかないと、Wicket.Ajax.processに通せない.
            xhr: function() {
                var xhr = $.ajaxSettings.xhr();
                $(xhr.upload).on('progress', function(e) {
                    var evt = e.originalEvent;
                    var percent = Math.floor(evt.loaded / evt.total * 100);
                    progressBar.text('アップロード中... ' + percent + '% (' + evt.loaded + "/" + evt.total + ")");
                    progressBar.css('width', percent + '%');
                });
                return xhr;
            },
            dummy: null
		}).done(function(pData) {
           Wicket.Ajax.process(pData);
           pJQueryFileField.val('');
           root.find('.fileValue').text('');
           progressBar.text('アップロード完了！');
           progressBar.css('width', '100%');
           setTimeout(function() {
               progressBarContainer.hide('slow', function() {
                   progressBar.text('');
                   progressBar.css('width', '0');
               });
           }, 1000);
		});
	}
}
