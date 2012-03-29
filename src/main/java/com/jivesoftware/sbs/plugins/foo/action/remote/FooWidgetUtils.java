package com.jivesoftware.sbs.plugins.foo.action.remote;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jivesoftware.community.DocumentManager;
import com.jivesoftware.community.action.util.JiveTextProvider;
import com.jivesoftware.community.dwr.RemoteSupport;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.util.ValueStack;

public class FooWidgetUtils extends RemoteSupport {
	private static final Logger s_log = Logger.getLogger(FooWidgetUtils.class);

	private DocumentManager documentManager = null;

	/************************************************************************************************
	 * NOTE: NEED TO MANUALLY CONFIGURE VIA /spring.xml
	 ************************************************************************************************/
	public void setDocumentManager(DocumentManager documentManager) {
		if (s_log.isDebugEnabled()) { s_log.debug("setDocumentManager called ..."); }
		this.documentManager = documentManager;
	} // end setDocumentManager - SPRING MANAGED

	public FooWidgetResponse getBean(String key, String value) {
		if (s_log.isDebugEnabled()) { s_log.debug("getBean called ..."); }
		FooWidgetResponse response = new FooWidgetResponse();
		response.setRequestStatus(HttpStatus.SC_OK);
		response.setStatusMessage("Success");
		FooWidgetBean bean = new FooWidgetBean(getRequest(),getUser());
		bean.setKey(key);
		bean.setValue(value);
		response.setBean(bean);
		return response;
	} // end getSearchResults

	private HttpServletRequest getRequest() {
		WebContext context = WebContextFactory.get();
		if (context != null) {
			return context.getHttpServletRequest();
		} // end if
		return null;
	} // end getRequest

	/*** FIXING ISSUE WITH REMOTE SUPPORT NOT USING SAME RESOURCE BUNDLES ***/
	/*** REMOVE ALL CODE UNDER THESE COMMENTS WHEN JIVE HAS INCORPORATED ***/
	private static final LocaleProvider s_localeProvider = new LocaleProvider() {	public Locale getLocale() { return Locale.ENGLISH; }	};
	private static final transient JiveTextProvider s_textProvider = new JiveTextProvider(FooWidgetUtils.class,s_localeProvider);

	@Override
    public String getText(String aTextName) {
        return s_textProvider.getText(aTextName);
    } // end getText

    public String getText(String aTextName, String defaultValue) {
        return s_textProvider.getText(aTextName, defaultValue);
    } // end getText

    public String getText(String aTextName, String defaultValue, String obj) {
        return s_textProvider.getText(aTextName, defaultValue, obj);
    } // end getText

    public String getText(String aTextName, List args) {
        return s_textProvider.getText(aTextName, args);
    } // end getText

    public String getText(String key, String[] args) {
        return s_textProvider.getText(key, args);
    } // end getText

    public String getText(String aTextName, String defaultValue, List args) {
        return s_textProvider.getText(aTextName, defaultValue, args);
    } // end getText

    public String getText(String key, String defaultValue, String[] args) {
        return s_textProvider.getText(key, defaultValue, args);
    } // end getText

    public String getText(String key, String defaultValue, List args, ValueStack stack) {
        return s_textProvider.getText(key, defaultValue, args, stack);
    } // end getText

    public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
        return s_textProvider.getText(key, defaultValue, args, stack);
    } // end getText

    public ResourceBundle getTexts() {
        return s_textProvider.getTexts();
    } // end getResourceBundle

    public ResourceBundle getTexts(String aBundleName) {
        return s_textProvider.getTexts(aBundleName);
    } // end getResourceBundle

	public void destroy() {

	} // end destroy - SPRING MANAGED


} // end class