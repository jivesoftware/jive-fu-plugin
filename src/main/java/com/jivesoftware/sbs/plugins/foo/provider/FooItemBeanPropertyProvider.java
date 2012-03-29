package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.Map;

import com.jivesoftware.base.User;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.browse.rest.ItemBeanPropertyProvider;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.provider.FooItemBeanPropertyProvider.FooInfoBean;

/**
 * Provides information regarding how Events are represented in the Browse UI
 *
 */
public class FooItemBeanPropertyProvider implements ItemBeanPropertyProvider<JiveObject, FooInfoBean> {
    private static final String PROP_NAME = "fooInfo";

    @Override
    public String propertyName() {
        return PROP_NAME;
    }

    @Override
    public FooInfoBean provideProperty(JiveObject jiveObject, User targetUser, Map<String, Object> additionalContext) {
        if (!(jiveObject instanceof Foo)) {
            return null;
        }

        Foo foo = (Foo) jiveObject;

        FooInfoBean bean = new FooInfoBean();

		String title = foo.getTitle();
    	bean.setTitle(title);
		String description = String.valueOf(foo.getDescription());
    	bean.setDescription(description);

        return bean;
    }

    public static class FooInfoBean {
		private String title;
		private String description;

    	public void setTitle(String title) {
     	   this.title = title;
   		}

    	public String getTitle() {
      	  return this.title;
    	}
    	public void setDescription(String description) {
     	   this.description = description;
   		}

    	public String getDescription() {
      	  return this.description;
    	}
    }
}
