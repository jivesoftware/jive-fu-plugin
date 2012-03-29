package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.community.BulkIconGenerator;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.JiveObjectURLFactory;
import com.jivesoftware.community.LinkProvider;
import com.jivesoftware.community.ParamMapObjectFactory;
import com.jivesoftware.community.TypeUIProvider;
import com.jivesoftware.community.browse.rest.ItemBeanPropertyProvider;
import com.jivesoftware.community.objecttype.ContainerContentInfoProvider;
import com.jivesoftware.community.util.IconGenerator;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.util.LocaleUtils;

public class FooTypeUIProvider implements TypeUIProvider {

    private IconGenerator iconGenerator;
    private JiveObjectURLFactory jiveObjectUrlFactory;
    private LinkProvider linkProvider;
    private List<String> macroNames;
    private Map<String, ParamMapObjectFactory> paramMapObjectFactories;
    private FooItemBeanPropertyProvider fooItemBeanInfoProvider;

    @Override
    public String getContentTypeFeatureName() {
        return getLocalizedString("global.foos");
    }

    @Override
    public String getContentTypeFeatureName(Locale locale) {
        return getLocalizedString("global.foos", locale);
    }

    @Override
    public String getContentTypeFormCSS() {
        return "jive-icon-big jive-icon-foo";
    }

    @Override
    public String getContentTypeName() {
        return getLocalizedString("global.foo");
    }

    @Override
    public String getContentTypeName(Locale locale) {
        return getLocalizedString("global.foo", locale);
    }

    @Override
    public IconGenerator getIconGenerator() {
        return iconGenerator;
    }

    @Override
    public JiveObjectURLFactory getJiveObjectURLFactory() {
        return jiveObjectUrlFactory;
    }

    @Override
    public String getLinkMediumCSS() {
        return "jive-icon-med jive-icon-foo";
    }

    @Override
    public LinkProvider getLinkProvider() {
        return linkProvider;
    }

    @Override
    public List<String> getMacroNames() {
        return macroNames;
    }

    @Override
    public Map<String, ParamMapObjectFactory> getParamMapObjectFactories() {
        return paramMapObjectFactories;
    }

    protected String getLocalizedString(String key) {
        return LocaleUtils.getLocalizedString(key);
    }

    protected String getLocalizedString(String key, Locale locale) {
        return LocaleUtils.getLocalizedString(key, locale);
    }

    @Override
    public String getObjectName(JiveObject jiveObject) {
        return ((Foo) jiveObject).getSubject();
    }

    @Override
    public String getBrowseThumbnailTemplate() {
        return "jive.browse.content.foo.thumbnailFooItem";
    }

    @Override
    public Collection<ItemBeanPropertyProvider> getPropertyProviders() {
        List<ItemBeanPropertyProvider> propertyProviders = new ArrayList<ItemBeanPropertyProvider>();
        propertyProviders.add(fooItemBeanInfoProvider);
        return propertyProviders;
    }

    @Override
    public String getBrowseDetailTemplate() {
        return null;
    }

    @Override
    public BulkIconGenerator getBulkIconGenerator() {
        return null;
    }

    @Required
    public void setFooItemBeanInfoProvider(FooItemBeanPropertyProvider fooItemBeanInfoProvider) {
        this.fooItemBeanInfoProvider = fooItemBeanInfoProvider;
    }

    @Required
    public void setParamMapObjectFactories(Map<String, ParamMapObjectFactory> paramMapObjectFactories) {
        this.paramMapObjectFactories = paramMapObjectFactories;
    }

    @Required
    public void setIconGenerator(IconGenerator iconGenerator) {
        this.iconGenerator = iconGenerator;
    }

    @Required
    public void setJiveObjectURLFactory(JiveObjectURLFactory jiveObjectUrlFactory) {
        this.jiveObjectUrlFactory = jiveObjectUrlFactory;
    }

    @Required
    public void setLinkProvider(LinkProvider linkProvider) {
        this.linkProvider = linkProvider;
    }

    @Required
    public void setMacroNames(List<String> macroNames) {
        this.macroNames = macroNames;
    }

    /**
     * Obsolete interface method
     */
    @Override
    public ContainerContentInfoProvider getContainerContentInfoProvider(int containerType) {
        return null;
    }
}