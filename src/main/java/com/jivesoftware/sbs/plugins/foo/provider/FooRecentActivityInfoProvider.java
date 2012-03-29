package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.User;
import com.jivesoftware.base.event.ContentEvent;
import com.jivesoftware.base.event.v2.BaseJiveEvent;
import com.jivesoftware.community.Activity;
import com.jivesoftware.community.ActivityEventHandlingStrategy;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.activity.type.BaseRecentActivityInfoProvider;
import com.jivesoftware.community.eae.view.stream.JiveObjectViewBean;
import com.jivesoftware.community.impl.activity.DefaultContentActivityEventHandlingStrategy;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooEvent;
import com.jivesoftware.sbs.plugins.foo.eae.view.stream.item.FooContentViewBeanBuilder;

public class FooRecentActivityInfoProvider extends BaseRecentActivityInfoProvider {
    private static final Logger log = Logger.getLogger(FooRecentActivityInfoProvider.class);

    private FooContentViewBeanBuilder fooContentViewBeanBuilder;

    @Override
    public ActivityEventHandlingStrategy getEventHandlingStrategy() {
        return new FooContentActivityEventHandlingStrategy();
    }

    @Override
    public boolean isUserAuthorizedToViewActivity(Activity activity, User user) {
        return super.isUserAuthorizedToViewActivity(activity, user) && ((Foo) activity.getJiveObject()).getStatus().isVisible();
    }

    @Override
    public JiveObjectViewBean<Foo> getJiveObjectViewBean(JiveObject obj, Locale locale, boolean renderToHTML) {
        if (!(obj instanceof Foo)) {
            log.error("Required obj parameter was not of type Foo");
        }

        Foo foo = (Foo) obj;
        String subject = renderSubjectToText(foo);
        String text = renderToText(foo);

        return fooContentViewBeanBuilder.build(foo, subject, text);
    }

    @Required
    public void setFooContentViewBeanBuilder(FooContentViewBeanBuilder fooContentViewBeanBuilder) {
        this.fooContentViewBeanBuilder = fooContentViewBeanBuilder;
    }

    public class FooContentActivityEventHandlingStrategy extends DefaultContentActivityEventHandlingStrategy {

        @Override
        public boolean isActivityRecordable(BaseJiveEvent event) {
           if(((FooEvent)event).getContentModificationType() == ContentEvent.ModificationType.Moderate) {
                return false;
            }

            return true;
        }

    }
}
