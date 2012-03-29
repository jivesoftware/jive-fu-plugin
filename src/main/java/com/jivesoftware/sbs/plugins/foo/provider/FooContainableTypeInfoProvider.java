package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.User;
import com.jivesoftware.community.JiveConstants;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.JiveContentObject;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.JiveObjectLoader;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.aaa.authz.EntitlementTypeProvider.EntitlementType;
import com.jivesoftware.community.impl.AbstractContainableTypeInfoProvider;
import com.jivesoftware.community.objecttype.ContentObjectType;
import com.jivesoftware.community.objecttype.EntitlementCheckProvider;
import com.jivesoftware.community.stats.StatisticsGenerator;
import com.jivesoftware.sbs.plugins.foo.Foo;

import edu.emory.mathcs.backport.java.util.Collections;

public class FooContainableTypeInfoProvider extends AbstractContainableTypeInfoProvider {

    private List<StatisticsGenerator> statisticsGenerators = new ArrayList<StatisticsGenerator>();
    private JiveObjectLoader jiveObjectLoader;
    private EntitlementCheckProvider<Foo> entitlementCheckProvider;

    @Override
    public JiveObject getContainerFor(JiveObject jiveObject) {
        if (jiveObject instanceof JiveContentObject) {
            JiveContentObject contentObject = (JiveContentObject) jiveObject;
            try {
                return jiveObjectLoader.getJiveObject(contentObject.getContainerType(), contentObject.getContainerID());
            }
            catch (NotFoundException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public List<StatisticsGenerator> getStatisticsGenerators() {
        return Collections.unmodifiableList(statisticsGenerators);
    }

    @Required
    public void setStatisticsGenerators(List<StatisticsGenerator> statisticsGenerators) {
        this.statisticsGenerators = statisticsGenerators;
    }

    @Override
    public List<ContentObjectType> getSubContentTypes() {
        return new ArrayList<ContentObjectType>();
    }

    @Override
    public String getTabViewID() {
        return "foo";
    }

    @Override
    public JiveContainer getUserPersonalContainerForContentType(User owner) {
        return null;
    }

    @Override
    public boolean isAvailableForContainer(int containerType) {
        switch(containerType) {
        case JiveConstants.COMMUNITY:
        case JiveConstants.PROJECT:
        case JiveConstants.SOCIAL_GROUP:
        case JiveConstants.USER_CONTAINER:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean isEnabledByDefaultForContainer(int containerType) {
        return true;
    }

    @Override
    public boolean isRequiredForContainer(int containerType) {
        return false;
    }

    @Override
    public boolean userHasCreatePermsFor(JiveContainer container, User user) {
        return entitlementCheckProvider.isUserEntitled(container, EntitlementType.CREATE);
    }

    @Override
    public boolean isAvailableForContainer(int containerType, long containerID) {
        return isAvailableForContainer(containerType) && !isRootCommunity(containerType, containerID);
    }

    @Required
    public void setJiveObjectLoader(JiveObjectLoader jiveObjectLoader) {
        this.jiveObjectLoader = jiveObjectLoader;
    }

    @Required
    public void setEntitlementCheckProvider(EntitlementCheckProvider<Foo> entitlementCheckProvider) {
        this.entitlementCheckProvider = entitlementCheckProvider;
    }
}
