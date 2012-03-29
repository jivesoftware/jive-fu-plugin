package com.jivesoftware.sbs.plugins.foo.provider;

import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.util.BaseIconGenerator;

public class FooIconGenerator extends BaseIconGenerator {

    public static final String COMMENT = "jive-icon-med jive-icon-foo-comment";
    public static final String SMALL = "jive-icon-sml jive-icon-foo";
    public static final String MEDIUM = "jive-icon-med jive-icon-foo";
    public static final String BIG = "jive-icon-big jive-icon-foo";

    @Override
    public String getCommentIcon(JiveObject object, boolean returnAsCssClass, int type) {
        return COMMENT;
    }

    @Override
    public String getIcon(boolean returnAsCssClass, int type) {
        return getIcon(null, returnAsCssClass, type);
    }

    @Override
    public String getIcon(JiveObject object, boolean returnAsCssClass, int type) {
        switch(type) {
        case 0:
            return SMALL;
        case 1:
            return MEDIUM;
        case 2:
        case 3:
        case 4:
        case 5:
            return BIG;
        default:
            return MEDIUM;
        }
    }
}
