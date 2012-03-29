<@jive.socialActions user followable followed foo foo "foo" favoriteManager.enabled />

<@jive.renderActionSidebar 'foo-actions' container />

<#include "/template/global/include/jive-search-macros.ftl" />

<@moreLikeThis objectType=foo.objectType objectID=foo.ID />

<@s.action name="incoming-links" executeResult="true" ignoreContextParams="true">
    <@s.param name="objectType">${foo.objectType?c}</@s.param>
    <@s.param name="objectID">${foo.ID?c}</@s.param>
</@s.action>
