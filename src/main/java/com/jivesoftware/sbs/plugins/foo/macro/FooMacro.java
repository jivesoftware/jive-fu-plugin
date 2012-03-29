package com.jivesoftware.sbs.plugins.foo.macro;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.community.lifecycle.JiveApplication;
import com.jivesoftware.community.renderer.BaseMacro;
import com.jivesoftware.community.renderer.RenderContext;
import com.jivesoftware.community.renderer.filter.wiki.link.Link;
import com.jivesoftware.community.renderer.filter.wiki.link.LinkRenderer;
import com.jivesoftware.community.renderer.filter.wiki.link.LinkType;
import com.jivesoftware.community.renderer.impl.v2.JAXPUtils;
import com.jivesoftware.community.renderer.macro.MacroParameter;
import com.jivesoftware.community.web.JiveResourceResolver;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooManager;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

public class FooMacro extends BaseMacro {
    private static final Logger log = Logger.getLogger(FooMacro.class);

    public static final String TAG_NAME = "foo";
    protected static final String TITLE = "title";
    protected static final String ID = "id";
    protected static final String DEFAULTATTR = "__default_attr";

    private static final boolean enabledByDefault = true;

    private Map<String, String> parameters = new HashMap<String, String>();
    private String url = "/foo";

    @Override
    public String getName() {
        return TAG_NAME;
    }

    @Override
    public boolean isShowInRTE() {
        return false;
    }

    @Override
    public List<MacroParameter<?>> getAllowedParameters() {
        LinkedList<MacroParameter<?>> l = new LinkedList<MacroParameter<?>>();
        l.add(new MacroParameter<String>(ID, ""));
        l.add(new MacroParameter<String>(DEFAULTATTR, ""));
        l.add(new MacroParameter<String>(TITLE, ""));
        return l;
    }

    @Override
    protected boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    @Override
    public String getShortMacroName() {
        return TAG_NAME;
    }

    @Override
    public boolean isSingleTagMacro() {
        return true;
    }

    @Override
    public void execute(Element element, RenderContext renderContext) {
        String id = parameters.get("id");
        Foo foo = null;
        if (parameters.get("id") == null) {
            id = getDefaultParameterValue(element);
        }
        long fooID = -1;
        try {
            fooID = Long.parseLong(id);
            foo = getFooManager().getFoo(fooID);
        } catch (NumberFormatException e) {
            log.debug(e);
        } catch (UnauthorizedException e) {
            handleError(e, element, "error.unauth.document", id);
            return;
        }

        try {
            if (foo != null) {
                Link link = createLink(foo);
                Element newElement = LinkRenderer.render(link, element.getOwnerDocument(), renderContext, false);
                JAXPUtils.replace(element, newElement);
            }
        } catch (UnauthorizedException e) {
            handleError(e, element, "error.unauth.document", id);
        }
    }

    private Link createLink(Foo foo) {
        Link link = new Link();
        String title = parameters.get(TITLE);
        if (parameters.get("title") == null) {
            link.setLinkText(foo.getSubject());
        } else {
            link.setLinkText(title);
        }
        link.setAltText(foo.getSubject());
        link.setLinkType(LinkType.JIVE_OBJECT_LINK);
        link.setObjectTypeID(new FooObjectType().getID());
        link.setObjectID(foo.getID());
        link.setUrl(JiveResourceResolver.getJiveObjectURL(foo, true));
        return link;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private FooManager getFooManager() {
        return (FooManager) JiveApplication.getEffectiveContext().getSpringBean("fooManager");
    }
}
