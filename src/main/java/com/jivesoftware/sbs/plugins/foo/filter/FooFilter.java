package com.jivesoftware.sbs.plugins.foo.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jivesoftware.community.JiveGlobals;
import com.jivesoftware.community.renderer.PostProcessingRenderFilter;
import com.jivesoftware.community.renderer.RenderContext;
import com.jivesoftware.community.renderer.RenderFilter;
import com.jivesoftware.community.renderer.RenderPlugin;
import com.jivesoftware.community.renderer.impl.v2.JAXPUtils;

public class FooFilter implements RenderFilter, RenderPlugin, PostProcessingRenderFilter {
	private static final Logger s_log = Logger.getLogger(FooFilter.class);

	private boolean m_enabled = true;
	private int m_order = -1;
	private String m_uncLinkXPath = null;
	private String m_style = null;
	private Map<String,String> m_parameters = null;

	public FooFilter() {
		m_parameters = new HashMap<String,String>();
	} // end constructor

	public void init() {
		if (s_log.isInfoEnabled()) { s_log.info("init called..."); }
		m_enabled = JiveGlobals.getJiveBooleanProperty("foo.filter.enabled",true);
		m_order = JiveGlobals.getJiveIntProperty("foo.filter.order",100);
		m_uncLinkXPath = JiveGlobals.getJiveProperty("foo.filter.xpath","//*[starts-with(text(),'foo')]");
		m_style = JiveGlobals.getJiveProperty("foo.filter.style","color: red; font-weight: bold;");
	} // end init - SPRING MANAGED

	public void execute(Document document, RenderContext renderContext) {
		if (s_log.isDebugEnabled()) { s_log.debug("execute called..."); }
		List<Element> elementList = JAXPUtils.selectNodes(document,m_uncLinkXPath);
		for (Element element : elementList) {
			processElement(element);
		} // end for element
		elementList = null;
	} // end execute

	private void processElement(Element element) {
		String fullText = element.getTextContent();
		element.setTextContent("");
		for (String text : fullText.trim().split("\n")) {
			text = text.trim();
			if (text.length() > 1) {
				Element pElement = element.getOwnerDocument().createElement("p");
				pElement.setAttribute("style",m_style);
				pElement.setTextContent(text);
				element.appendChild(pElement);
			} // end if
			/*** IF WE ARE IN A PRE/CODE SET, WE NEED TO RE-ADD THE NEW LINES ***/
			if (element.getTagName().equals("code")) {
				element.appendChild(element.getOwnerDocument().createElement("br"));
			} // end if
		} // end for text
	} // end processElement

	public int getOrder() { return m_order; }
	public String[] getUserDocumentation(String language) { return null; }
	public boolean isDisplayable() { return false; }
	public String getName() { return this.getClass().getSimpleName(); }

	public boolean isEnabled() { return m_enabled; }
	public void setEnabled(boolean enabled) {
		m_enabled = enabled;
	} // end setEnabled - UI/SPRING MANAGED

	public Map<String, String> getParameters() { return m_parameters;}
	public void setParameters(Map<String, String> parameters) {
		if (parameters != null) {
			m_parameters.clear();
			m_parameters.putAll(parameters);
		} // end if
	} // end setParameters - UI/SPRING MANAGED

	public void destroy() {
		if (s_log.isDebugEnabled()) { s_log.debug("destroy called..."); }
	} // end destroy - SPRING MANAGED

} // end class
