<#include "layout.ftlh">

<#assign page_title="Administration"/>
<#assign page_description="Sand Saga"/>
<#assign enable_google_analytics=false/>

<#macro page_head>
  <script src="/data/admin/PageAdministration.js" type="module"></script>
</#macro>

<#macro page_body>
  <div id="administration-root"></div>
</#macro>

<#macro page_bottom>
  <div class="container">
    <a href="/#logout">Logout</a>
  </div>
</#macro>

<@display_page/>
