package com.jivesoftware.sbs.plugins.foo.provider;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.jivesoftware.community.browse.BrowseFilterContext;
import com.jivesoftware.community.browse.QueryFilterDef;
import com.jivesoftware.community.browse.filter.AbstractUserBoundFilter;
import com.jivesoftware.community.browse.filter.BrowseFilter;
import com.jivesoftware.community.browse.filter.CompositeBrowseFilter;
import com.jivesoftware.community.browse.filter.ObjectTypeFilter;
import com.jivesoftware.community.browse.filter.group.BrowseFilterGroup;
import com.jivesoftware.community.browse.provider.AbstractBrowseFilterProvider;
import com.jivesoftware.sbs.plugins.foo.FooObjectType;

/**
 * Defines various filters for Foos within the Browse UI
 *
 */
public class FooBrowseFilterProvider extends AbstractBrowseFilterProvider {

    public static final String FILTER_ALL = "all";
    //add new filter here:  public static final String MY_OTHER_FILTER = "other";

    @Override
    public BrowseFilter getRootBrowseFilter(BrowseFilterGroup group, BrowseFilterContext context) {
        CompositeBrowseFilter foosFilter = new ObjectTypeFilter("browse.filter.foo.type", ImmutableSet.of(FooObjectType.FOO_TYPE_ID));
        foosFilter.addChild(new FooStateFilter("browse.filter.foo.all", FILTER_ALL));

        //register new filters here, with visibility rules
        //foosFilter.addChild(new FooStateFilter("browse.filter.foo.user.other", MY_OTHER_FILTER) {
        //    public boolean isVisible(BrowseFilterContext context) {
        //        return context.getUserID() != AnonymousUser.ANONYMOUS_ID;
        //    }
        //});

        return foosFilter;
    }

    public static class FooStateFilter extends AbstractUserBoundFilter {
        private String discriminator;

        public FooStateFilter(String s, String discriminator) {
            super(makeId("foo", discriminator), s);
            this.discriminator = discriminator;
        }

        public String getDiscriminator() {
            return discriminator;
        }

        @Override
        public QueryFilterDef getQueryFilterDef(Set<BrowseFilter> browseFilters, QueryFilterDef.Archetype archetype) {
            final String alias;
            if (archetype == QueryFilterDef.Archetype.Container) {
                throw new UnsupportedOperationException();
            }

            alias = QueryFilterDef.getBrowseTableAlias(archetype);

            if (discriminator.equals(FILTER_ALL)) {
                return null;
            }

            //define other filters here
            //StringBuilder tableJoin = new StringBuilder();
            //tableJoin.append("jiveFoo foo ON foo.fooID=").append(alias).append(".objectID");
            //StringBuilder predicate = new StringBuilder();
            //long now = System.currentTimeMillis();

            //if (discriminator.equals(MY_OTHER_FILTER)) {
            //    predicate.append("foo.somefield = ? ");
            //    return new QueryFilterDef(tableJoin.toString(), null, null, predicate.toString(), ImmutableList.of(ParamType.BIGINT), ImmutableList.of(now));
            //}

            return null;
        }
    }
}
