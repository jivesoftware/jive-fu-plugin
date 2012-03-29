package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jivesoftware.community.impl.BaseContentLinkProvider;

public class FooLinkProvider extends BaseContentLinkProvider {
    public static final Pattern FOO_LINK_PATTERN = Pattern.compile("/?(foos)/([\\w\\#]+)(\\^[\\w.]+)?$", Pattern.CASE_INSENSITIVE);

    @Override
    public Matcher getAnchorHrefMatcher(String href) {
        Matcher matcher = FOO_LINK_PATTERN.matcher(href);
        if(matcher.find()) {
            matcher.reset();
            return matcher;
        }

        return null;
    }

    @Override
    public String getAnchorLinkFromAnchorHrefMatcher(Matcher urlMatcher) {
        return urlMatcher.group(2).contains("#") ? urlMatcher.group(2) : null;
    }

    @Override
    public String getAttachmentNameFromAnchorHrefMatcher(Matcher urlMatcher) {
        return null;
    }

    @Override
    public String getLinkCSS() {
        return "jive-link-foo";
    }

    @Override
    public String getObjectIdFromAnchorHrefMatcher(Matcher urlMatcher) {
        String objectId = urlMatcher.group(2);
        return objectId.contains("#") ? objectId.substring(0, objectId.indexOf("#")) : objectId;
    }

    @Override
    public String parseRelativeURL(String href, String baseUrl) {
        String part = "/foos/";
        String hrefPart;

        if(href.startsWith(baseUrl + part) || href.startsWith(part)) {
            hrefPart = part;
        } else {
            return null;
        }

        return hrefPart + href.substring(href.lastIndexOf("=") + 1);
    }

}
