<@resource.dwr file="FooWidgetUtils"/>
<@resource.javascript>
	var fooWidget = new Object();

	fooWidget.handleShowBean = function() {
		$j('#foo-button').val('Processing...');
		$j('#foo-button').attr('disabled','disabled');
		var key = $j('#foo-key').val();
		var value = $j('#foo-value').val();
		fooWidget.showBean(key,value);
	}; // end showBean

	fooWidget.showBean = function(key,value) {
		FooWidgetUtils.getBean(key,value,fooWidget.processShowBean);
	}; // end getBean

	fooWidget.processShowBean = function(response) {
		if (response) {
			$j('#foo-results').html('<p>Status: '+response.requestStatus+'</p>');
			$j('#foo-results').append('<p>Message: '+response.statusMessage+'</p>');
			if (response.bean) {
				$j('#foo-results').append('<p>bean.key: '+response.bean.key+'</p>');
				$j('#foo-results').append('<p>bean.value: '+response.bean.value+'</p>');
				$j('#foo-results').append('<p>bean.timestamp: '+response.bean.timestamp+'</p>');
			} else {
				$j('#foo-results').append('<p style="color: red;">Bean:  Not Defined</p>');
			} // end if
		} else {
			$j('#foo-results').html('<p style="color: red;">Error:  Not a Valid Response</p>');
		} // end if
		$j('#foo-button').val('Test');
		$j('#foo-button').removeAttr('disabled');
	}; // end processShowBean
</@resource.javascript>

<div class="jive-foo-widget-wrapper clearfix">
	<h1>Freemarker</h1>
	<p><@s.text name="global.title" /><@s.text name="global.colon" /> ${title!'No Title Defined'} </p>
	<p><@s.text name="global.message" /><@s.text name="global.colon" /> ${message!'No Message Defined'} </p>
	<p></p>
	<hr/>
	<p><strong>DWR Example:</strong></p>
	<form>
		<p>
		<label for="foo-key">Enter Key:</label><br/>
		<input id="foo-key" type"text" value="${title!}">
		</p>
		<p>
		<label for="foo-value">Enter Value:</label><br/>
		<input id="foo-value" type"text" value="${message!}">
		</p>
		<br/>
		<input id="foo-button" type="button" value="Test" onclick="fooWidget.handleShowBean();" >
	</form>
	<div id="foo-results">
		<span>Results will be printed here</span>
	</div>
</div>
