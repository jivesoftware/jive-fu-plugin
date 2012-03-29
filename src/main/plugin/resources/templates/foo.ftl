<html>
<head>
    <title>${action.renderSubjectToText(foo)}</title>

    <style type="text/css" media="screen">
        @import "<@s.url value='/styles/jive-people.css'/>";
    </style>
    
    <#-- Include JavaScript Library and RTE -->
    <@resource.javascript header="true">
        ${macroJavaScript}
    </@resource.javascript>
    <#include "/template/global/include/comment-macros.ftl" />
    <@resource.dwr file="WikiTextConverter" />
    <@resource.template file="/soy/share/share.soy" />
    <@resource.javascript file="/resources/scripts/apps/share/views/share_view.js" />
    <@resource.javascript file="/resources/scripts/apps/share/models/share_source.js" />
    <@resource.javascript file="/resources/scripts/apps/share/main.js" />  
    
    <@resource.template file="/soy/nav/movecontent.soy" />
    
    <@resource.javascript>

        $j(function() {
            $j('.jive-shared-list-toggle').click(function() {
                $j('.jive-shared-list-short').toggle();
                $j('.jive-shared-list-all').toggle();
                                return false;
            });
        });

        <#if followable>
            <#assign startFollowText><@s.text name="foo.startFollow.desc" /></#assign>
            <#assign stopFollowText><@s.text name="foo.stopFollow.desc" /></#assign>
            <#assign followError><@s.text name='global.follow.error'/></#assign>
            var i18n = {startFollowing : '${startFollowText?js_string}',
                        stopFollowing  : '${stopFollowText?js_string}',
                        followError    : '${followError?js_string}'}

            var jiveFollow = new jive.FollowApp.Main({objectType: ${foo.objectType?c}, objectID:${foo.ID?c}, featureName:'foo', i18n:i18n});           
        </#if>
        
        var jiveShare = new jive.ShareApp.Main({objectType: ${foo.objectType?c}, objectID:${foo.ID?c}, linkId:'jive-link-foo-sendnotify'});
        var jiveMoveContent = new jive.Move.Content.Main({
            objectType: ${foo.objectType?c},
            objectID: ${foo.ID?c},
            personalContainerTitleKey:'nav.bar.create.personal_container.title.foo',
            personalContainerCaptionKey:'nav.bar.create.personal_container.caption.foo',
            containerID: ${container.ID?c},
            containerType: ${container.objectType?c}});
        var jiveDeleteContent = new jive.Modalizer.Main({triggers:['#jive-foo-delete a'], liveTriggers:['.js-link-delete']});
        var jiveReportAbuse = new jive.Modalizer.Main({triggers:['#jive-link-abuse a'], liveTriggers:['.js-link-abuse'], width: 'medium'});
        
    </@resource.javascript>

    <#if legacyBreadcrumb>
    <content tag="breadcrumb">
        <@s.action name="legacy-breadcrumb" executeResult="true" ignoreContextParams="true">
            <@s.param name="containerType" value="${container.objectType?c}" />
            <@s.param name="container" value="${container.ID?c}" />
        </@s.action>
        <a href="<@s.url value='${JiveResourceResolver.getJiveObjectURL(container)}'/>/content?filterID=content~objecttype~objecttype[${foo.objectType?c}]" class="jive-link-more"><@s.text name="foo.main.brdcrmb.foos.link" /></a>
    </content>
    </#if>

</head>

<@jive.featureContentObject objectType=foo.objectType?c objectID=foo.ID?c containerType=container.objectType?c containerID=container.ID?c/>

<body class="jive-body-main jive-body-foo jive-body-content <#if foo.status == enums['com.jivesoftware.community.JiveContentObject$Status'].AWAITING_MODERATION || foo.status == enums['com.jivesoftware.community.JiveContentObject$Status'].ABUSE_HIDDEN>j-foo-mod</#if>">

    <#if !legacyBreadcrumb>
    <header id="jive-body-intro">
        <div class="j-context">
            <#if action.isUserContainer()>
                <@s.text name="foo.main.up_to_user_foos_in.link">
                    <@s.param><a href="<@s.url value='${JiveResourceResolver.getJiveObjectURL(container)}'/>/content?filterID=content-objecttype-objecttype[${foo.objectType?c}]"></@s.param>
                    <@s.param></a></@s.param>
                    <@s.param><a href="<@s.url value='${JiveResourceResolver.getJiveObjectURL(container)}'/>"><@jive.displayUserDisplayName user=foo.user/></a></@s.param>
                </@s.text>
            <#else>
                <@s.text name='foo.main.up_to_foos_in.link'>
                    <@s.param><a href="<@s.url value='${JiveResourceResolver.getJiveObjectURL(container)}'/>/content?filterID=content~objecttype~objecttype[${foo.objectType?c}]"></@s.param>
                    <@s.param></a></@s.param>
                    <@s.param><a href="<@s.url value='${JiveResourceResolver.getJiveObjectURL(container)}'/>"><span class="${SkinUtils.getJiveObjectCss(container, 1)}"></span>${container.name?html}</a></@s.param>
                </@s.text>
                <@s.action name="context-menu" executeResult="true" ignoreContextParams="true">
                    <@s.param name="container" value="${container.ID?c}" />
                    <@s.param name="containerType" value="${container.objectType?c}" />
                </@s.action>
            </#if>
        </div>
    </header>
    </#if>

    <!-- BEGIN main body -->
    <div class="jive-foo-moderating jive-content-header-moderating">
        <span class="jive-icon-med jive-icon-moderation"></span><@s.text name="mod.post_in_moderation.text" />
    </div>
    <div id="jive-body-main" class="j-layout j-layout-ls clearfix">
        <div class="j-column-wrap-l">
            <div class="j-column j-column-l lg-margin">
                <#if (action.isUserContainer()) && (!SkinUtils.isPersonalContentEnabled(foo.objectType))>
                <div class="jive-warn-box">
                    <div>
                        <span class="jive-icon-med jive-icon-warn"></span>
                        <@s.text name="foo.main.personalDisabled.text" />
                    </div>
                </div>
                </#if>
                
                <div id="object-follow-notify" class="jive-info-box" style="display:none"></div>
                <div id="content-featured-notify" class="jive-info-box" style="display:none"></div>
                <@jive.showMovedMesage content=foo container=container />                
                <#include "/template/global/include/form-message.ftl"/>

                <div class="jive-content clearfix jive-foo">
                    <header>
                        <h2><span class="jive-icon-big jive-icon-foo"></span>${action.renderSubjectToText(foo)}</h2>
                        <div class="j-byline font-color-meta">
                            <@s.text name="foo.byline.created.text" />&nbsp;
                            <@jive.userDisplayNameLink user=foo.user />
                            <@s.text name="global.on" /> ${foo.creationDate?datetime?string.medium_short},
                            <@s.text name="foo.byline.modified.text" /> ${foo.modificationDate?datetime?string.medium_short}
                        </div>
                    </header>
                    <section class="jive-content-body">
                        <ul class="jive-foo-values">
                            <li>
                                <h4><@s.text name="foo.fooID.label" /><@s.text name="global.colon" /></h4>
                                	<span>${foo.fooID!?html}</span>
                            </li>
                            <li>
                                <h4><@s.text name="foo.containerID.label" /><@s.text name="global.colon" /></h4>
                                	<span>${foo.containerID!?html}</span>
                            </li>
                            <li>
                                <h4><@s.text name="foo.containerType.label" /><@s.text name="global.colon" /></h4>
                                	<span>${foo.containerType!?html}</span>
                            </li>
                            <li>
                                <h4><@s.text name="foo.userID.label" /><@s.text name="global.colon" /></h4>
                                	<span>${foo.userID!?html}</span>
                            </li>
                            <li>
                                <h4><@s.text name="foo.status.label" /><@s.text name="global.colon" /></h4>
                                	<span>${foo.status!?html}</span>
                            </li>
                            <li>
                                <h4><@s.text name="foo.creationDate.label" /><@s.text name="global.colon" /></h4>
                            		<span>${foo.creationDate!?date}</span>
                            </li>
                            <li>
                                <h4><@s.text name="foo.modificationDate.label" /><@s.text name="global.colon" /></h4>
                            		<span>${foo.modificationDate!?date}</span>
                            </li>
                            <li>
                                <h4><@s.text name="foo.title.label" /><@s.text name="global.colon" /></h4>
                                	<span>${foo.title!?html}</span>
                            </li>
                            <li>
                                <h4><@s.text name="foo.description.label" /><@s.text name="global.colon" /></h4>
                            		<span>${action.renderToHtml(foo)}</span>
                            </li>
                        </ul>
                    </section>
                    <footer class="jive-content-footer clearfix font-color-meta">
                        <@jive.displayViewCount viewCount=foo.viewCount containerClass='jive-content-footer-item'/>
                        
                        <#assign allObjectTagNames = "" />
                        <#assign objectTagSets = action.getObjectTagSets(foo)>
                        <#if (objectTagSets.hasNext())>
                        <span class="jive-content-footer-item">
                            <span class="jive-icon-med jive-icon-folder"></span><@s.text name="global.categories" /><@s.text name="global.colon" />
                            <#list objectTagSets as tagSet> ${tagSet.name}<#if objectTagSets.hasNext()>, </#if></#list>
                        </span>
                        </#if>
                        
                        <#assign canEditTags=canEdit >
                        <#include "/template/global/include/tag-macros.ftl" />
                        <@viewAndEditTags tags=fooTags entityDescriptor=foo container=foo.jiveContainer user=user canEditTags=canEditTags />                        
                    </footer>
                </div>
                <@jive.ratings container foo "foo"/>
            
                <@comments contentObject=foo />
            </div>
        </div>
        <div id="jive-body-sidebarcol-container" class="j-column j-column-s j-content-extras">
            <#include "/plugins/foo-type/resources/templates/foo-sidebar.ftl" />
        </div>
    </div>
</body>
</html>