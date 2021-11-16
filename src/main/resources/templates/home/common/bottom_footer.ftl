<footer>
    <img class="footer-tri" src="/home/imgs/footer-tri.png">
        <div class="friend-links">
        <div class="links-title center">学校链接</div>
        <ul class="links-wr center" style="margin-top: 22px;">
            <#if friendLinkList??>
            <#list friendLinkList as friendLink>
            <li class="first">
                <a href="http://www.gzu.edu.cn/" target="_blank" class="links">贵州大学</a>
            </li>
            </#list>
            </#if>
            <li class="first">
                <a href="/system/login" target="_blank" class="links">登录后台</a>
            </li>
        </ul>
    </div>
    <div class="site-msg line2">
        <#--<span class="power">${siteSetting.allRights!""}</span>-->
    </div>
</footer>