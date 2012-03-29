<html>
<head>
    <title>
        <#if (edit)><@s.text name="foo.edit.success.updated.title" /><#else><@s.text name="foo.edit.success.new.title" /></#if>: ${foo.title}
    </title>

    <meta name="nosidebar" content="true" />

    <#if legacyBreadcrumb>
    <content tag="breadcrumb">
        <#if (community?exists)>
            <@s.action name="legacy-breadcrumb" executeResult="true" ignoreContextParams="true">
                <@s.param name="community" value="${community.ID?c}" />
            </@s.action>
        <#else>
            <@s.action name="legacy-breadcrumb" executeResult="true" ignoreContextParams="true" />
        </#if>
    </content>
    </#if>
</head>
<body>


<!-- BEGIN header & intro  -->
<div id="jive-body-intro">
    <div id="jive-body-intro-content">
        <h1>
            <span class="jive-icon-big jive-icon-foo"></span>
            <#if (edit)><@s.text name="foo.edit.success.updated.title" /><#else><@s.text name="foo.edit.success.new.title" /></#if> Foo
        </h1>
        <p>

            <#if (fooModerated)>
                <@s.text name="foo.edit.success.moderated.text" />
            <#else>
                <@s.text name="foo.edit.success.posted.text" />
            </#if>

        </p>
    </div>
</div>
<!-- END header & intro -->


<!-- BEGIN main body -->
<div id="jive-body-main">


    <!-- BEGIN main body column -->
    <div id="jive-body-maincol-container">
    <div id="jive-body-maincol">


        <ul>

                    <li><#-- Go to: the community you posted in -->
                        <@s.text name="global.go_to" /><@s.text name="global.colon" />
                        <a href="<@s.url value='${JiveResourceResolver.getJiveObjectURL(container)}'/>"
                        ><@s.text name="global.the_space_you_posted_in" /></a>
                    </li>

                    <li><#-- Go to: the main forum page -->
                        <@s.text name="global.go_to" /><@s.text name="global.colon" />
                        <a id="jive-main-community" href="index.jspa"><@s.text name="global.the_main_community_page" /></a>
                    </li>

        </ul>

    </div>
    </div>
    <!-- END main body column -->


</div>
<!-- END main body -->


</body>
</html>