package com.jivesoftware.sbs.plugins.foo.action.remote;

import java.io.Serializable;

import org.apache.commons.httpclient.HttpStatus;

public class BaseRemoteResponse implements Serializable {
	private static final long serialVersionUID = -1173643912564735702L;

	private int m_requestStatus = -1;
	private String m_statusMessage = null;

	public BaseRemoteResponse() {
		setRequestStatus(HttpStatus.SC_OK);
	} // end constructor

	public int getRequestStatus() {	return m_requestStatus;	}
	public void setRequestStatus(int requestStatus) {
		m_requestStatus = requestStatus;
	} // end get/setRequestStatus

	public String getStatusMessage() {	return m_statusMessage;	}
	public void setStatusMessage(String statusMessage) {
		m_statusMessage = statusMessage;
	} // end get/setStatusMessage

} // end BaseRemoteResponse
