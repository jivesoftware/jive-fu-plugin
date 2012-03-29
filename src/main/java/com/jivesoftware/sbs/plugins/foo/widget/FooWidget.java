package com.jivesoftware.sbs.plugins.foo.widget;

import java.util.Map;

import com.jivesoftware.community.DocumentManager;
import com.jivesoftware.community.annotations.PropertyNames;
import com.jivesoftware.community.widget.BaseWidget;
import com.jivesoftware.community.widget.WidgetCategory;
import com.jivesoftware.community.widget.WidgetCategoryMarker;
import com.jivesoftware.community.widget.WidgetContext;
import com.jivesoftware.community.widget.WidgetType;
import com.jivesoftware.community.widget.WidgetTypeMarker;

@WidgetTypeMarker({WidgetType.HOMEPAGE, WidgetType.COMMUNITY, WidgetType.SOCIALGROUP, WidgetType.PROJECT})
@WidgetCategoryMarker({WidgetCategory.OTHER})
@PropertyNames({"freemarker","title","message"})
public class FooWidget extends BaseWidget {

	private DocumentManager documentManager = null;

	private boolean freemarker = true;
	private String title = null;
	private String message = null;

	/************************************************************************************************
	 * NOTE:  SIMILAR TO Struts2 ACTIONS, WIDGETS ARE AUTO-WIRED BY SPRING.  SIMPLY CREATE A SETTER
	 * THAT MATCHES THE SPRING ID (in this case "documentManager" => "setDocumentManager") WITH A
	 * CORRESPONDING TYPE (DocumentManager) AND YOU ARE GOOD TO GO
	 ************************************************************************************************/
	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	} // end setDocumentManager - SPRING MANAGED

	public boolean isFreemarker() {	return freemarker;	}
	public void setFreemarker(boolean freemarker) {
		this.freemarker = freemarker;
	} // end is/setFreemarker - WIDGET MANAGED

	public String getTitle() {	return title;	}
	public void setTitle(String title) {
		this.title = title;
	} // end get/setTitle - WIDGET MANAGED

	public String getMessage() {	return message;	}
	public void setMessage(String message) {
		this.message = message;
	} // end get/setMessage - WIDGET MANAGED

	@Override
	public String getDescription(WidgetContext widgetContext) {
		 return getLocalizedString("foo."+this.getClass().getSimpleName()+".description", widgetContext);
	} // end getDescription

	@Override
	public String getTitle(WidgetContext widgetContext) {
		return getLocalizedString("foo."+this.getClass().getSimpleName()+".title", widgetContext);
	} // end getTitle

	/************************************************************************************************
	 * NOTE:  RETURNING FALSE WILL DISALLOW THE WIDGET TO BE MANUALLY REFRESHABLE IN THE UI
	 ************************************************************************************************/
	@Override
	public boolean isRefreshable() { return true; }

	/************************************************************************************************
	 * NOTE:  RETURNING FALSE REMOVES THE WIDGET FROM THE DASHBOARD AS WELL AS THE MENU, MAKE SURE THIS LOGIC IS SOUND AND REPEATABLE FOR ALL USERS
	 ************************************************************************************************/
	@Override
	public boolean isEnabled(WidgetContext widgetContext) {
		return true;
	} // end isEnabled

	public String getCssClass() {	return  "jive-foo-widget";	}

	private static final String _SOY_TEMPLATE = "jive.widget.foo.fooWidget.body";
	@Override
	protected Map<String, Object> loadPropertiesForSoy(WidgetContext widgetContext, ContainerSize size) {
       Map<String, Object> properties = super.loadPropertiesForSoy(widgetContext, size);
       properties.put("title",getTitle());
       properties.put("message",getMessage());
       properties.put("freemarker",isFreemarker());
       return properties;
	} // end loadPropertiesForSoy

	private static final String _FREEMARKER_TEMPLATE = "/plugins/jive-fu-plugin/resources/templates/widget/foo_widget.ftl";
	@Override
	protected Map<String, Object> loadProperties(WidgetContext widgetContext, ContainerSize size) {
		 Map<String, Object> properties = super.loadProperties(widgetContext, size);
		 properties.put("title",getTitle());
		 properties.put("message",getMessage());
		 properties.put("freemarker",isFreemarker());
		 return properties;
	} // end loadProperties

	@Override
	public String render(WidgetContext widgetContext, ContainerSize size) {
		return (isFreemarker()) ?
				applyFreemarkerTemplate(widgetContext, size, _FREEMARKER_TEMPLATE) :
				applySoyTemplate(widgetContext, size, _SOY_TEMPLATE);
	} // end render

} // end class
