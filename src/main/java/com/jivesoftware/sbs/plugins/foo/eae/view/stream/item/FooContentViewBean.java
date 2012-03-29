package com.jivesoftware.sbs.plugins.foo.eae.view.stream.item;

import javax.xml.bind.annotation.XmlRootElement;

import com.jivesoftware.community.eae.view.stream.JiveContentObjectViewBean;
import com.jivesoftware.sbs.plugins.foo.Foo;

/**
 * View bean for exposing object data to the What Matters stream
 *
 */
@XmlRootElement(name = "content")
public class FooContentViewBean extends JiveContentObjectViewBean<Foo> {
		private String title;
		private String description;

    public FooContentViewBean(Foo jiveContentObject, String subject, String text, String contentTypeFeatureName, String contentTypeName) {
        super(jiveContentObject, subject, text, contentTypeFeatureName, contentTypeName, true);
    }

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
