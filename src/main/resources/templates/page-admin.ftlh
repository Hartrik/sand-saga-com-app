<#include "layout.ftlh">

<#assign page_title="Administration"/>
<#assign page_description="Sand Saga"/>
<#assign enable_google_analytics=false/>

<#macro page_head>
  <script src="/data/admin/PageAdministration.js" type="module"></script>
</#macro>

<#macro page_body>
  <h2>Administration</h2>
  <div id="administration-root"></div>
</#macro>

<@display_page/>
