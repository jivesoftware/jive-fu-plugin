package com.jivesoftware.sbs.plugins.foo.eae.view.stream.item;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.aaa.AuthenticationProvider;
import com.jivesoftware.community.activity.type.DefaultSoyTemplateTypes;
import com.jivesoftware.community.activity.type.RecentActivitySoyTemplateInfoProvider;
import com.jivesoftware.community.objecttype.ObjectTypeUtils;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;
import com.jivesoftware.util.LocaleUtils;


/**
 * Utility class for constructing FooContentViewBeans from Foo objects
 *
 */
public class FooContentViewBeanBuilder {

    private AuthenticationProvider authProvider;

    public FooContentViewBean build(Foo foo, String subject, String text) {
        FooObjectType type = (FooObjectType) ObjectTypeUtils.getJiveObjectType(foo.getObjectType());
        RecentActivitySoyTemplateInfoProvider provider = type.getRecentActivitySoyTemplateInfoProvider();

        String featureName = ObjectTypeUtils.getContentTypeFeatureName(foo, this.getLocale());
        String typeName = ObjectTypeUtils.getContentTypeName(foo, this.getLocale());

        FooContentViewBean bean = new FooContentViewBean(foo, subject, text, featureName, typeName);
        bean.setActivityContentExcerptTemplate(provider.getTemplateName(DefaultSoyTemplateTypes.activityContentExcerpt));
        bean.setActivityTemplate(provider.getTemplateName(DefaultSoyTemplateTypes.detailed));
        bean.setCollapsedActivityStreamExcerptTemplate(provider.getTemplateName(DefaultSoyTemplateTypes.collapsedActivityStreamExcerpt));
        bean.setCommListItemViewTemplate(provider.getTemplateName(DefaultSoyTemplateTypes.commListItemView));
        bean.setExpandedActivityStreamViewTemplate(provider.getTemplateName(DefaultSoyTemplateTypes.expandedActivityStreamView));
        bean.setExpandedCommItemViewTemplate(provider.getTemplateName(DefaultSoyTemplateTypes.expandedCommItemView));
        bean.setGroupedActivityStreamTemplate(provider.getTemplateName(DefaultSoyTemplateTypes.groupedActivityStream));
        bean.setGroupedCommActivityTemplate(provider.getTemplateName(DefaultSoyTemplateTypes.groupedComm));

		String title = foo.getTitle();
    	bean.setTitle(title);
		String description = String.valueOf(foo.getDescription());
    	bean.setDescription(description);

        return bean;
    }

    protected Locale getLocale() {
        return LocaleUtils.getUserLocale(authProvider.getJiveUser());
    }

    @Required
    public void setAuthProvider(AuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }
}
