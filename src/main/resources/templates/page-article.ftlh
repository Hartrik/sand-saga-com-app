<#include "layout.ftlh">

<#assign page_title="${articleTitle}"/>
<#assign enable_google_analytics=true/>

<#macro page_body>
  ${articleContent?no_esc}
</#macro>

<@display_page/>