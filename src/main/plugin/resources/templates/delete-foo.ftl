<div id="jive-modal-foo-delete">
    <header>
        <h2><@s.text name="foo.del.delete_content.title" /></h2>
    </header>
    <a href="#" class="j-modal-close-top close"><@s.text name="global.close" /> <span class="j-close-icon j-ui-elem"></span></a>
    
    <section class="jive-modal-content clearfix">
        <form action="<@s.url action='delete-foo' />" name="deleteform" class="j-form" method="post">
            <@jive.token name="foo.delete.${foo.ID?c}" />
            <input type="hidden" name="foo" value="${foo.ID?c}">
            
            <p class="j-deleteObject">
                <@s.text name="foo.del.confirm.text">
                    <@s.param><strong class="font-color-normal">${foo.title?html}</strong></@s.param>
                </@s.text>
            </p>
            
            <div class="jive-form-buttons">
                <input type="submit" class="j-btn-callout" name="delete" id="deletebutton" value="<@s.text name='global.delete' />">
                <input type="button" class="close" value="<@s.text name='global.cancel' />">
            </div>
        </form>        
        
        <script language="JavaScript" type="text/javascript">
            $j('#deletebutton').focus();
        </script>
    </section>
</div>