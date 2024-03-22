<#include "layout.ftlh">

<#assign page_title="Stats"/>
<#assign enable_google_analytics=true/>

<#macro page_body>
  <h2>Statistics</h2>

  <div class="table-responsive" style="margin-top: 1.5em; max-width: 45em;">
    <table class="table table-striped">
      <tbody>
      <tr>
        <th>Name</th>
        <th>Minutes Played</th>
        <th>Completed</th>
      </tr>
      <#list statsByScenario as s>
      <tr>
        <td>${s.name}</td>
        <td>${s.updates?string.number}</td>
        <td>${s.completed?string.number}</td>
      </tr>
      </#list>
      <tr style="font-weight: bold;">
        <td>&sum;</td>
        <td>${stats.updates?string.number}</td>
        <td>${stats.completed?string.number}</td>
      </tr>
      </tbody>
    </table>
  </div>

  <small>Only whole minutes count. Sandbox scenarios are marked as completed (played) after 10 000 iterations.</small>
</#macro>

<@display_page/>