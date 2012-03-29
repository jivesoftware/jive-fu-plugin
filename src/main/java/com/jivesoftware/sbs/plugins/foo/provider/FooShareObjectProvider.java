package com.jivesoftware.sbs.plugins.foo.provider;

import com.jivesoftware.community.mail.objecttype.ShareObjectProvider;
import com.jivesoftware.sbs.plugins.foo.Foo;

/**
 * Defines how Foos are shared
 *
 */
public class FooShareObjectProvider implements ShareObjectProvider<Foo> {

    @Override
    public String getAttachmentName(Foo foo) {
        return "";
    }

    @Override
    public long getAttachmentSize(Foo foo) {
        return 0;
    }

    @Override
    public String getObjectTitle(Foo foo) {
        return foo.getTitle();
    }

    @Override
    public boolean isAttachmentAvailable() {
        return false;
    }

}
