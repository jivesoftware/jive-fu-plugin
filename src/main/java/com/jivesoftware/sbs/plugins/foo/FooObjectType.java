package com.jivesoftware.sbs.plugins.foo;

import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.community.ContainableTypeInfoProvider;
import com.jivesoftware.community.FilteredContentProvider;
import com.jivesoftware.community.RecentContentInfoProvider;
import com.jivesoftware.community.TaggableTypeInfoProvider;
import com.jivesoftware.community.TypeUIProvider;
import com.jivesoftware.community.acclaim.AcclaimInfoProvider;
import com.jivesoftware.community.acclaim.AcclaimType;
import com.jivesoftware.community.activity.type.RecentActivityInfoProvider;
import com.jivesoftware.community.activity.type.RecentActivitySoyTemplateInfoProvider;
import com.jivesoftware.community.activity.type.RecentActivityTemplateInfoProvider;
import com.jivesoftware.community.browse.BrowsableType;
import com.jivesoftware.community.browse.BrowseFilterProvider;
import com.jivesoftware.community.browse.provider.BrowseDataProvider;
import com.jivesoftware.community.comments.type.CommentableReplyableTypeInfoProvider;
import com.jivesoftware.community.comments.type.CommentableTypeInfoProvider;
import com.jivesoftware.community.eae.objecttype.UpdateNotificationInfoProvider;
import com.jivesoftware.community.eae.upgrade.provider.ActivityUpgradeProvider;
import com.jivesoftware.community.eae.upgrade.provider.ActivityUpgradeType;
import com.jivesoftware.community.favorites.type.FavoritableType;
import com.jivesoftware.community.favorites.type.FavoriteInfoProvider;
import com.jivesoftware.community.history.RecentHistoryProvider;
import com.jivesoftware.community.mail.objecttype.ShareObjectProvider;
import com.jivesoftware.community.moderation.ModeratableType;
import com.jivesoftware.community.moderation.ModerationStrategy;
import com.jivesoftware.community.moderation.ModerationUIProvider;
import com.jivesoftware.community.navbar.NavBarProvider;
import com.jivesoftware.community.objecttype.CommentableType;
import com.jivesoftware.community.objecttype.ContainableType;
import com.jivesoftware.community.objecttype.ContainableTypeManager;
import com.jivesoftware.community.objecttype.ContentObjectType;
import com.jivesoftware.community.objecttype.ContentObjectTypeInfoProvider;
import com.jivesoftware.community.objecttype.EntitlementCheckableType;
import com.jivesoftware.community.objecttype.EntitlementCheckProvider;
import com.jivesoftware.community.objecttype.FeaturedContentEnabledType;
import com.jivesoftware.community.objecttype.FilteredIndexableType;
import com.jivesoftware.community.objecttype.JiveObjectFactory;
import com.jivesoftware.community.objecttype.MoveObjectProvider;
import com.jivesoftware.community.objecttype.MoveableObjectType;
import com.jivesoftware.community.objecttype.RatingEnabledType;
import com.jivesoftware.community.objecttype.RecentActivityEnabledType;
import com.jivesoftware.community.objecttype.RecentContentEnabledType;
import com.jivesoftware.community.objecttype.RecentHistoryEnabledType;
import com.jivesoftware.community.objecttype.ReplyableTypeInfoProvider;
import com.jivesoftware.community.objecttype.ShareableType;
import com.jivesoftware.community.objecttype.TaggableType;
import com.jivesoftware.community.objecttype.ViewCountSupport;
import com.jivesoftware.community.objecttype.ViewCountSupportedType;
import com.jivesoftware.community.search.IndexInfoProvider;
import com.jivesoftware.community.tags.type.CommunityTaggableType;
import com.jivesoftware.community.tags.type.GenericTaggableTypeInfoProvider;

public class FooObjectType implements ActivityUpgradeType, ContentObjectType, EntitlementCheckableType<Foo>, BrowsableType, CommentableType, CommunityTaggableType, TaggableType, ContainableType, FavoritableType, FeaturedContentEnabledType, FilteredIndexableType, ModeratableType, MoveableObjectType, RatingEnabledType, RecentActivityEnabledType, RecentContentEnabledType, RecentHistoryEnabledType, ShareableType<Foo>, ViewCountSupportedType {

    private static final String FOO_TYPE_CODE = "foo";
    public static final int FOO_TYPE_ID = Math.abs(FOO_TYPE_CODE.hashCode());

    public static final String FOOS_ENABLED_PROP = "foos.enabled";

    private JiveObjectFactory<Foo> jiveObjectFactory;

    private TypeUIProvider typeUiProvider;
    private ContentObjectTypeInfoProvider contentObjectTypeInfoProvider;

    private AcclaimInfoProvider acclaimInfoProvider;
    private ActivityUpgradeProvider activityUpgradeProvider;
    private BrowseDataProvider browseDataProvider;
    private BrowseFilterProvider browseFilterProvider;
    private ContainableTypeInfoProvider containableTypeInfoProvider;
    private ContainableTypeManager containableTypeManager;
    private FilteredContentProvider filteredContentProvider;
    private GenericTaggableTypeInfoProvider genericTaggableTypeInfoProvider;
    private TaggableTypeInfoProvider taggableTypeInfoProvider;
    private RecentContentInfoProvider recentContentInfoProvider;
    private IndexInfoProvider indexInfoProvider;
    private ModerationStrategy moderationStrategy;
    private ModerationUIProvider moderationUIProvider;
    private RecentActivityInfoProvider recentActivityInfoProvider;
    private UpdateNotificationInfoProvider updateNotificationInfoProvider;
    private RecentActivitySoyTemplateInfoProvider recentActivitySoyTemplateInfoProvider;
    private CommentableTypeInfoProvider commentableTypeInfoProvider;
    private ReplyableTypeInfoProvider replyableTypeInfoProvider = new CommentableReplyableTypeInfoProvider();
    private ShareObjectProvider<Foo> shareProvider;
    private NavBarProvider navBarProvider;
    private FavoriteInfoProvider<Foo, FooObjectType> favoriteInfoProvider;
    private EntitlementCheckProvider<Foo> entitlementCheckProvider;
    private MoveObjectProvider<Foo> moveObjectProvider;
    private ViewCountSupport viewCountSupport;

    @Override
    public String getCode() {
        return FOO_TYPE_CODE;
    }

    @Override
    public int getID() {
        return FOO_TYPE_ID;
    }

    @Override
    public JiveObjectFactory<Foo> getObjectFactory() {
        return jiveObjectFactory;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public TypeUIProvider getTypeUIProvider() {
        return typeUiProvider;
    }

    @Required
    public void setTypeUiProvider(TypeUIProvider typeUiProvider) {
        this.typeUiProvider = typeUiProvider;
    }

    @Required
    public void setJiveObjectFactory(JiveObjectFactory<Foo> jiveObjectFactory) {
        this.jiveObjectFactory = jiveObjectFactory;
    }

    @Override
    public ContentObjectTypeInfoProvider getContentObjectTypeInfoProvider() {
        return contentObjectTypeInfoProvider;
    }

    @Required
    public void setContentObjectTypeInfoProvider(ContentObjectTypeInfoProvider contentObjectTypeInfoProvider) {
        this.contentObjectTypeInfoProvider = contentObjectTypeInfoProvider;
    }

    @Override
    public ContainableTypeInfoProvider getContainableTypeInfoProvider() {
        return containableTypeInfoProvider;
    }

    @Required
    public void setContainableTypeInfoProvider(ContainableTypeInfoProvider containableTypeInfoProvider) {
        this.containableTypeInfoProvider = containableTypeInfoProvider;
    }

    @Override
    public ContainableTypeManager getContainableTypeManager() {
        return containableTypeManager;
    }

    @Required
    public void setContainableTypeManager(ContainableTypeManager containableTypeManager) {
        this.containableTypeManager = containableTypeManager;
    }

    @Override
    public FilteredContentProvider getFilteredContentProvider() {
        return filteredContentProvider;
    }

    @Required
    public void setFilteredContentProvider(FilteredContentProvider filteredContentProvider) {
        this.filteredContentProvider = filteredContentProvider;
    }

    @Override
    public GenericTaggableTypeInfoProvider getGenericTaggableTypeInfoProvider() {
        return genericTaggableTypeInfoProvider;
    }

    @Required
    public void setGenericTaggableTypeInfoProvider(GenericTaggableTypeInfoProvider genericTaggableTypeInfoProvider) {
        this.genericTaggableTypeInfoProvider = genericTaggableTypeInfoProvider;
    }

    @Override
    public TaggableTypeInfoProvider getTaggableTypeInfoProvider() {
        return taggableTypeInfoProvider;
    }

    @Required
    public void setTaggableTypeInfoProvider(TaggableTypeInfoProvider taggableTypeInfoProvider) {
        this.taggableTypeInfoProvider = taggableTypeInfoProvider;
    }

    @Override
    public RecentContentInfoProvider getRecentContentInfoProvider() {
        return recentContentInfoProvider;
    }

    @Required
    public void setRecentContentInfoProvider(RecentContentInfoProvider recentContentInfoProvider) {
        this.recentContentInfoProvider = recentContentInfoProvider;
    }

    @Override
    public IndexInfoProvider getIndexInfoProvider() {
        return indexInfoProvider;
    }

    @Required
    public void setIndexInfoProvider(IndexInfoProvider indexInfoProvider) {
        this.indexInfoProvider = indexInfoProvider;
    }

    @Override
    public boolean includeInDefaultContentSearch() {
        return true;
    }

    @Override
    public boolean includeInSpotlightSearch() {
        return true;
    }

    @Override
    public boolean isDefaultType() {
        return false;
    }

    @Override
    public ModerationStrategy getModerationStrategy() {
        return moderationStrategy;
    }

    @Required
    public void setModerationStrategy(ModerationStrategy moderationStrategy) {
        this.moderationStrategy = moderationStrategy;
    }

    @Override
    public ModerationUIProvider getModerationUIProvider() {
        return moderationUIProvider;
    }

    @Required
    public void setModerationUIProvider(ModerationUIProvider moderationUIProvider) {
        this.moderationUIProvider = moderationUIProvider;
    }

    @Override
    public RecentActivityInfoProvider getRecentActivityInfoProvider() {
        return recentActivityInfoProvider;
    }

    @Required
    public void setRecentActivityInfoProvider(RecentActivityInfoProvider recentActivityInfoProvider) {
        this.recentActivityInfoProvider = recentActivityInfoProvider;
    }

    @Override
    public RecentActivityTemplateInfoProvider getRecentActivityTemplateInfoProvider() {
        return null;
    }

    @Required
    public UpdateNotificationInfoProvider getNotificationInfoProvider() {
        return updateNotificationInfoProvider;
    }

    @Required
    public void setNotificationInfoProvider(UpdateNotificationInfoProvider updateNotificationInfoProvider) {
        this.updateNotificationInfoProvider = updateNotificationInfoProvider;
    }

    @Override
    public RecentActivitySoyTemplateInfoProvider getRecentActivitySoyTemplateInfoProvider() {
        return recentActivitySoyTemplateInfoProvider;
    }

    @Required
    public void setRecentActivitySoyTemplateInfoProvider(RecentActivitySoyTemplateInfoProvider recentActivitySoyTemplateInfoProvider) {
        this.recentActivitySoyTemplateInfoProvider = recentActivitySoyTemplateInfoProvider;
    }

    @Override
    public CommentableTypeInfoProvider getCommentableTypeInfoProvider() {
        return commentableTypeInfoProvider;
    }

    @Required
    public void setCommentableTypeInfoProvider(CommentableTypeInfoProvider commentableTypeInfoProvider) {
        this.commentableTypeInfoProvider = commentableTypeInfoProvider;
    }

    @Override
    public ReplyableTypeInfoProvider getReplyableTypeInfoProvider() {
        return replyableTypeInfoProvider;
    }

    @Required
    public void setReplyableTypeInfoProvider(ReplyableTypeInfoProvider replyableTypeInfoProvider) {
        this.replyableTypeInfoProvider = replyableTypeInfoProvider;
    }


    @Override
    public FavoriteInfoProvider<Foo, FooObjectType> getFavoriteInfoProvider() {
        return favoriteInfoProvider;
    }

    @Required
    public void setFavoriteInfoProvider(FavoriteInfoProvider<Foo, FooObjectType> favoriteInfoProvider) {
        this.favoriteInfoProvider = favoriteInfoProvider;
    }

    @Override
    public boolean isFavoriteViewable() {
        return false;
    }

	@Override
	public MoveObjectProvider<Foo> getMoveObjectProvider() {
	    return moveObjectProvider;
	}

    @Required
	public void setMoveObjectProvider(MoveObjectProvider<Foo> moveObjectProvider) {
	    this.moveObjectProvider = moveObjectProvider;
	}

	@Override
	public ViewCountSupport getViewCountSupport() {
	    return viewCountSupport;
	}

    @Required
	public void setViewCountSupport(ViewCountSupport viewCountSupport) {
	    this.viewCountSupport = viewCountSupport;
	}

	@Override
	public boolean isRatingsEnabled() {
	   return true;
    }

    @Required
    public void setAcclaimInfoProvider(AcclaimInfoProvider acclaimInfoProvider) {
        this.acclaimInfoProvider = acclaimInfoProvider;
    }

    @Override
    public AcclaimInfoProvider getAcclaimInfoProvider() {
        return acclaimInfoProvider;
    }

    @Override
    public boolean isAcclaimEnabled(AcclaimType acclaimType) {
        return true;
    }

	@Override
	public EntitlementCheckProvider<Foo> getEntitlementCheckProvider() {
	    return entitlementCheckProvider;
	}

    @Required
	public void setEntitlementCheckProvider(EntitlementCheckProvider<Foo> entitlementCheckProvider) {
	    this.entitlementCheckProvider = entitlementCheckProvider;
	}

    @Override
    public ActivityUpgradeProvider getActivityUpgradeProvider() {
        return activityUpgradeProvider;
    }

    @Required
    public void setActivityUpgradeProvider(ActivityUpgradeProvider activityUpgradeProvider) {
        this.activityUpgradeProvider = activityUpgradeProvider;
    }

    @Override
    public BrowseDataProvider getBrowseDataProvider() {
        return browseDataProvider;
    }

    @Required
    public void setBrowseDataProvider(BrowseDataProvider browseDataProvider) {
        this.browseDataProvider = browseDataProvider;
    }

    @Override
    public BrowseFilterProvider getBrowseFilterProvider() {
        return browseFilterProvider;
    }

    @Required
    public void setBrowseFilterProvider(BrowseFilterProvider browseFilterProvider) {
        this.browseFilterProvider = browseFilterProvider;
    }

    @Override
    public ShareObjectProvider<Foo> getShareProvider() {
        return shareProvider;
    }


    @Required
    public void setShareProvider(ShareObjectProvider<Foo> shareProvider) {
        this.shareProvider = shareProvider;
    }

    @Required
    public void setNavBarProvider(NavBarProvider navBarProvider) {
        this.navBarProvider = navBarProvider;
    }

    @Override
    public NavBarProvider getNavBarProvider() {
        return navBarProvider;
    }

    @Override
    public RecentHistoryProvider getRecentHistoryProvider() {
        return null;
    }
}
