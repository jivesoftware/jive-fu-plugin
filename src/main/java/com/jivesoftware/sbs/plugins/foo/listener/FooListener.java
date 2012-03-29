package com.jivesoftware.sbs.plugins.foo.listener;

import org.apache.log4j.Logger;

import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.base.event.v2.EventListener;
import com.jivesoftware.community.Document;
import com.jivesoftware.community.DocumentManager;
import com.jivesoftware.community.DocumentObjectNotFoundException;
import com.jivesoftware.community.event.DocumentEvent;

public class FooListener implements EventListener<DocumentEvent>{
	private static final Logger s_log = Logger.getLogger(FooListener.class);

	private DocumentManager documentManager = null;

	/************************************************************************************************
	 * NOTE: NEED TO MANUALLY CONFIGURE VIA /spring.xml
	 ************************************************************************************************/
	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	} // end setDocumentManager - SPRING MANAGED

	@Override
	public void handle(DocumentEvent event) {
		if (s_log.isDebugEnabled()) { s_log.debug("handle called..."); }
			if (event.getType().equals(DocumentEvent.Type.ADDED)) {
				try {
					Document document = loadDocument(event);
					if (s_log.isDebugEnabled()) { s_log.debug("Document["+document.getSubject()+"] was ADDED!"); }
				} catch (UnauthorizedException ue) {
					s_log.warn("Unexpected Condition",ue);
				} catch (DocumentObjectNotFoundException donfe) {
					s_log.warn("Unexpected Condition",donfe);
				} // end try/catch
			} // end if
	} // end handle

	private Document loadDocument(DocumentEvent event) throws UnauthorizedException, DocumentObjectNotFoundException {
		return documentManager.getDocument(event.getDocID());
	} // end loadDocument

} // end class
