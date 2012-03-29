<#assign isSpellCheckEnabled = JiveGlobals.getJiveBooleanProperty("skin.default.spellCheckEnabled", true)>
<#assign isInlineSpellEnabled = JiveGlobals.getJiveBooleanProperty("skin.default.inlineSpellCheckEnabled", false)>
<#assign isWikiTablesEnabled = WikiUtils.isWikiTableSyntaxEnabled(container)>
<#assign isWikiImagesEnabled = WikiUtils.isWikiImageSyntaxEnabled(container) && action.hasPermissionsToUploadImages()>

<html>
<head>
    <title><#if (edit)><@s.text name="foo.edit.title" /><#else><@s.text name="foo.create.title" /></#if></title>

    <meta name="nosidebar" content="true"/>
    <meta name="noprint" content="true"/>

    <#if legacyBreadcrumb>
    <content tag="breadcrumb">
        <#if container?exists>
            <@s.action name="legacy-breadcrumb" executeResult="true" ignoreContextParams="true">
                <@s.param name="container" value="${container.ID?c}"/>
                <@s.param name="containerType" value="${container.objectType?c}"/>
            </@s.action>
        </#if>
    </content>
    </#if>

    <link rel="stylesheet" href="<@resource.url value='/styles/jive-compose.css'/>" type="text/css" media="all" />
    <link rel="stylesheet" href="<@resource.url value='/styles/jive-content.css'/>" type="text/css" media="all" />

    <@resource.javascript file="/resources/scripts/global.js" />

    <#include "/template/global/include/rte-macros.ftl"/>
    <#include "/template/decorator/default/header-javascript-minirte.ftl" />

    <@resource.javascript file="/resources/scripts/jquery/ui/ui.sortable.js" />
    <@resource.javascript file="/resources/scripts/jquery/ui/effects.core.js"/>
    <@resource.javascript file="/resources/scripts/jquery/ui/effects.highlight.js"/>
    <@resource.javascript file="/resources/scripts/apps/shared/views/rte_view.js" />
    <@resource.javascript file="/resources/scripts/apps/shared/views/notification_view.js"/>
    <@resource.javascript file="/resources/scripts/jive/action.js" />
    <@resource.javascript file="/resources/scripts/apps/wall/main.js" />
    <@resource.javascript file="/resources/scripts/apps/status_input/status_input.js" />  
    <@resource.template file="/soy/status_input/status_input_attachments.soy"/>
    <@resource.template file="/soy/polls/create/poll-option.soy"/>
    <@resource.dwr file="WikiTextConverter" />
    
    <@resource.javascript>
        $j(function() {
                $j("#jive-compose-categories input").change(function() {
                    $j(this).closest('span').removeClass('jive-category-highlight');
                    $j(this).closest('span').find('img').remove();
                    return false;
                });
            });
    
    
            function highlightCategory(theTag, theCategory) {
                var tag = ("#" + theTag);
                var category = ("#" + theCategory);
                if ( $j(category).is(":not(:checked)") && ($j(category).closest('span').hasClass('jive-category-highlight') == false) ) {
                    $j(category).closest('span').toggleClass('jive-category-highlight', 300, function() {
                        $j(category).closest('span').find('label').append("<img id='tags-tooltip' class='jive-icon-med jive-icon-help jiveTT-hover-suggest' alt='' src='images/transparent.png'/>");
                        $j('#jive-tag2').text($j(theTag).text());
                        $j('#jive-cat2').text($j(category).closest('span').find('label').text());
                    });
                }
                return false;
            }
        </@resource.javascript>
    
        
</head>
<body class="jive-body-formpage jive-body-formpage-foo">
    <div class="jive-create-foo jive-create-large jive-content j-rc4">
       <#include "/template/global/include/form-message.ftl" />
           
       <#if (container.objectType == JiveConstants.USER_CONTAINER)>
       <div class="jive-info-box">
           <div>
               <span class="jive-icon-med jive-icon-info"></span>
               <@s.text name="foo.personal.container.msg" />
           </div>
       </div>
       </#if>
       
       <#if fooModerated>
       <div class="jive-info-box">
           <div>
               <span class="jive-icon-med jive-icon-info"></span>
               <@s.text name="foos.moderation.text" />
           </div>
       </div>
       </#if>
       
       <header>
           <h2>
               <span class="jive-icon-big jive-icon-foo"></span>
               <#if edit>
               <@s.text name="foo.edit.title" />
               <#else>
               <@s.text name="foo.create_foo.title">
                   <@s.param><span class="details"></@s.param>
                   <#if (container.objectType == JiveConstants.USER_CONTAINER)>
                   <@s.param><@s.text name="ctr.choose.myctr.foos.header" /></@s.param>
                   <#else>
                   <@s.param>${container.name?html}</@s.param>
                   </#if>
                   <@s.param></span></@s.param>
               </@s.text>
               </#if>
           </h2>
       </header>

        <form class="j-form" id="foo-form" action=<#if (edit)>"edit-foo.jspa"<#else>"create-foo.jspa"</#if> method="post" name="fooform" <#if !rteDisabledBrowser>onsubmit="return processDescription();"</#if>>
            <#if foo?exists>
            <input type="hidden" name="foo" value="${foo.ID?c}"/>
            </#if>
            <#if container?exists>
            <input type="hidden" name="container" value="${container.ID?c}"/>
            <input type="hidden" name="containerType" value="${container.objectType?c}"/>
            </#if>
            
            <#if foo?exists>
                <@jive.token name="foo.create.${foo.ID?c}" />
            <#else>
                <@jive.token name="foo.create.${container.ID?c}" />
            </#if>
            
            <p id="jive-compose-title">
                <label for="title">
                    <@s.text name="foo.title.label" /> 
                    <strong class="required">
                        <@s.text name="global.required" />
                    </strong>
                </label>
                <input type="text" name="title" id="title" size="50" maxlength="255" value="${title!?html}"/>
                <@macroFieldErrors name="title"/>
            </p>
            
            <div class="j-form-row jive-editor-panel jive-large-editor-panel" id="jive-foo-description">
                <label for="description">
                    <@s.text name="foo.description.label" />
                    <strong class="required">
                        <@s.text name="global.required" />
                    </strong>
                </label>
                <div id="jive-foo-bodybox">
                    <textarea id="wysiwygtext" name="description" rows="15" cols="30">${description!?html}</textarea>
                    <@macroFieldErrors name="description" />
                </div>                
            </div>
            
                     
            <!-- BEGIN compose section -->
            <div class="jive-compose-section jive-compose-section-cats-tags">

                <#if !action.isUserContainer(container)>
                    <#assign objectTagSetIDs = action.getObjectTagSetIDs(foo)>
                    <#include "/template/global/include/category.ftl" />
                <#else>
                    <div></div>
                </#if>
            
                <@macroFieldErrors name="tags"/>

                <div id="jive-compose-tags">
                    <span id="jive-compose-tags-container">

                        <h4>
                            <label for="jive-tags">
                                <span class="jive-icon-med jive-icon-tag"></span>
                                <@s.text name="foo.tags.title" />
                            </label>
                            <img id="tags-tooltip" class="jive-icon-med jive-icon-help jiveTT-hover-tags" alt="" src="<@resource.url value='/images/transparent.png' />" />
                            <span id="tag_directions" class="jive-compose-directions font-color-meta-light">(<@s.text name="foo.spaceSeprtsTags.text" />)</span>
                        </h4>

                        <div id="jive-compose-tags-form">

                            <input type="text" name="tags" size="65" id="jive-tags"
                                value="${tags!?html}" />
                            
                            <ul class="autocomplete" id="jive-tag-choices"></ul>

                            <#if (popularTags?size > 0)>
                                <div id="jive-populartags-container">
                                    <span>
                                        <strong><@s.text name="foo.create.popular_tags.gtitle" /><@s.text name="global.colon" /></strong>
                                        <#if container.objectType == JiveConstants.SOCIAL_GROUP>
                                            <@s.text name="foo.tags.group.popular.instructions" />
                                        <#elseif container.objectType == JiveConstants.PROJECT>
                                            <@s.text name="foo.tags.project.popular.instructions" />
                                        <#else>
                                            <@s.text name="foo.tags.popular.instructions" />
                                        </#if>
                                    </span>
                                    <div>
                                        <#list popularTags as tag>
                                            <a name="populartag" rel="nofollow" href="#" onclick="swapTag(this); <#if !action.isUserContainer(container)>TagSet.highlightCategory('${tag?js_string}');</#if> return false;"
                                            <#if (tags?exists && ((tags.indexOf(' ' + tag + ' ') > -1) || (tags.startsWith(tag + ' ')) || (tags.endsWith(' ' + tag))))>
                                                class="jive-tagname-${tag?html} jive-tag-selected"
                                            <#else>
                                                class="jive-tagname-${tag?html} jive-tag-unselected"
                                            </#if>
                                            >${action.renderTagToHtml(tag)}</a>&nbsp;
                                        </#list>
                                    </div>
                                </div>
                            </#if>
                        </div>

                        <!-- NOTE: this include MUST come after the 'tags' input element -->
                        <@resource.javascript file="/resources/scripts/tag-selector.js" />
                    </span>
                </div>
            </div>
            <!-- END compose section -->
            
            <p>
                <#if (edit)>
                <input class="j-btn-callout" type="submit" value="<@s.text name='foo.edit.update.button.text' />" name="save" />
                <#else>
                    <input class="j-btn-callout" type="submit" value="<@s.text name='foo.edit.create.button.text' />" name="save" />
                </#if>
                <input type="submit" value="<@s.text name='foo.edit.cancel.button.text' />" name="method:cancel" />
            </p>
        </form>
    </div>
    
     <@resource.javascript header="true">
        var _jive_video_picker__url = "?container=${container.ID?c}&containerType=${container.objectType?c}";       
        <#if (edit)>
            var _jive_image_picker_url = '/edit-foo!imagePicker.jspa?foo=${(foo.ID?c)!-1}&postedFromGUIEditor=true';
        <#else>
            var _jive_image_picker_url = '/create-foo!imagePicker.jspa?foo=${(foo.ID?c)!-1}&containerType=${container.objectType?c}&container=${container.ID?c}&postedFromGUIEditor=true';
        </#if>                   
        var _jive_images_enabled = true;

        function buildRTE() {
            <#if (edit)>
                var objectType= "foo";
                var objectID = ${foo.ID?c};
                var entitlement = "VIEW";
            <#else>
                var objectType = ${container.objectType?c};
                var objectID = ${container.ID?c};
                var entitlement = "VIEW_CONTENT";
            </#if>
            
            var imageService = new jive.rte.ImageService({              
                contentResourceSessionKey: "${sessionKey}",
                objectId: objectID,
                objectType: objectType,
                containerId: ${container.ID?c},
                containerType: ${container.objectType?c},
                sessionPrimUrl: _jive_image_picker_url
            });

            var formService = new jive.rte.FormService({
                $form: $j("#wysiwygtext").closest("form")
            });            
            
            var entitlementService = new jive.rte.EntitlementService({
                objectID: objectID,
                objectType: objectType,
                entitlement: entitlement
            });            
                    
            <#assign toggleDisplay><@s.text name="rte.toggle_display" /></#assign>
            <#assign editDisabled><@s.text name="rte.edit.disabled" /></#assign>
            <#assign editDisabledSummary><@s.text name="rte.edit.disabled.desc" /></#assign>
            var options = {
                $element: $j("#wysiwygtext"),
                controller : jiveControl,
                preset: "mini",
                autoSave: window.autoSave,
                preferredMode: '<#if rteDisabledBrowser>rawhtml<#else>${preferredEditorMode}</#if>',
                startMode: '<#if rteDisabledBrowser>rawhtml<#else>${preferredEditorMode}</#if>',
                mobileUI: <#if rteDisabledBrowser>true<#else>false</#if>,
                rteDisabledBrowser: <#if rteDisabledBrowser>true<#else>false</#if>,
                isAnonymous: false,     
                isEditing: <#if (edit)>true<#else>false</#if>,
                imagePickerUrl: _jive_image_picker_url,
                toggleText: '${toggleDisplay?js_string}',
                editDisabledText: '${editDisabled?js_string}',
                editDisabledSummary : '${editDisabledSummary?js_string}',
                toggleText: '${toggleDisplay?js_string}',       
                height: 200,                
                services: {
                    imageService: imageService,
                    formService: formService,
                    entitlementService: entitlementService
                }
            };        
            var rte = new jive.rte.RTEWrap(options);
        }
        
        $j(buildRTE);
        
        function processDescription() {
            var description = window.editor.get('wysiwygtext').getHTML();
            $j('#wysiwygtext').val(description);
            // safari 1.x and 2.x bug: http://lists.apple.com/archives/Web-dev/2005/Feb/msg00106.html
            if (window.editor.get('wysiwygtext').isTextOnly()) {
                $j('#wysiwygtext').show().text(body).hide();
            }
            return true;
        }
    </@resource.javascript>      
    
    <content tag="jiveTooltip">
        <div id="jiveTT-note-tags" class="jive-tooltip-help notedefault snp-mouseoffset" >
            <@s.text name="doc.create.tag_explained.info" />
        </div>
        
        <div id="jiveTT-note-suggest" class="jive-tooltip2 notedefault snp-mouseoffset"  >
            <div class="jive-tooltip2-top"></div>
            <div class="jive-tooltip2-mid">
                <div class="jive-tooltip2-mid-padding" id="jive-note-category-suggestion-body">
                    <strong>Suggested Category</strong>
                </div>
        
            </div>
            <div class="jive-tooltip2-btm"></div>
        </div>
    </content>
    
    <script type="text/javascript">
        function focusOnGUIEditor(callEvent) {
            var keycode = callEvent.keyCode;
            if (keycode == 9 && !callEvent.shiftKey) { // tab
                tinyMCE.execCommand("mceFocus", null, "wysiwygtext");
            }
        }
    </script>
</body>
