package com.jivesoftware.sbs.plugins.foo.action;

import com.jivesoftware.community.Document;
import com.jivesoftware.community.DocumentManager;
import com.jivesoftware.community.action.JiveActionSupport;

/**
 * @author rrutan
 *
 */
public class JiveFooActionSupport extends JiveActionSupport {
	private static final long serialVersionUID = 1852427441449383007L;

	private long ID = -1;
	private int type = -1;
	private String subject = null;

	private Document document = null;

	private DocumentManager documentManager = null;

	/********************************************************************************************************
	 * Struts2 is Auto-Wired by Spring Bean Name.  To get a reference to ANY Spring Bean, simply
	 * create a setter.  For example, if the Spring beanID = "testBall", simply create a setTestBall(...)
	 * method with a matching data-type and Spring will inject a reference on the fly.
	 * @param documentManager
	 ********************************************************************************************************/
	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	} // end setDocumentManager - SPRING MANAGED

	public long getID() {	return ID;	}
	public void setID(long ID) {
		this.ID = ID;
	} // end get/setID - STRUTS MANAGED

	public int getType() {	return type;	}
	public void setType(int type) {
		this.type = type;
	} // end get/setType - STRUTS MANAGED

	public String getSubject() {	return subject;	}
	public void setSubject(String subject) {
		this.subject = subject;
	} // end get/setSubject - STRUTS MANAGED

	/********************************************************************************************************
	 * NOTE:  STRUTS2 CONVERTER IS LOOKING FOR THE DOCUMENT ID (i.e. 1234, not DOC-1234)
	 * @see: com.jivesoftware.community.web.struts.converter.DocumentConverter
	 ********************************************************************************************************/
	public Document getDocument() { return document; }
	public void setDocument(Document document) {
		this.document = document;
	} // end get/setDocument - STRUTS MANAGED

} // end class