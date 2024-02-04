<#include "layout.ftlh">

<#assign page_title=""/>
<#assign page_description="Sand Saga"/>
<#assign enable_google_analytics=true/>

<#macro page_head>
  <link href="/data/page-index.css" rel="stylesheet">

  <#if !user??>
    <script>
      function sand_saga_com_showLoginForm() {
        const dialogNode = document.getElementById('dialog-login');
        const modal = new window.bootstrap.Modal(dialogNode);
        modal.show();
      }
      if (window.location.hash === '#login' || window.location.hash === '#login-failed') {
        document.addEventListener('DOMContentLoaded', () => {
          sand_saga_com_showLoginForm();
        });
      }
    </script>
  </#if>
  <#if user??>
    <script>
      if (window.location.hash === '#logout') {
        document.cookie = "login-session-id=; Max-Age=0;path=/";
        window.location.href = "/";
      }
    </script>
  </#if>

  <script>
    document.addEventListener('DOMContentLoaded', () => {
      if (window.localStorage !== undefined) {
        for (const key of Object.keys(localStorage)) {
          if (key.startsWith('scenario_')) {
            if (localStorage.getItem(key)) {
              let element = document.getElementById(key);
              if (element) {
                const text = key.includes('sandbox') ? 'played' : 'completed';
                element.innerHTML = '<span class="badge bg-success rounded-pill">' + text + '</span>';
              }
            }
          }
        }
      }
    });
  </script>
</#macro>

<#macro page_body>

  <#if !user??>
  <#-- login dialog -->
    <div class="modal fade" id="dialog-login" tabindex="-1" role="dialog" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <strong>Login</strong>
          </div>
          <div class="modal-body">
            <form class="" action="/login" method="POST">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
              <div class="mb-3">
                <label class="form-label" for="username">Name</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Enter login" maxlength="30" required>
              </div>
              <div class="mb-3">
                <label class="form-label" for="password">Pass</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Enter password" maxlength="30" required>
              </div>
              <div class="mb-3">
                <button type="submit" class="btn btn-primary">Send</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </#if>

  <h1>Sand Saga</h1>
  <div class="alert alert-info alert-dismissible fade show">
    <p>
      The Sand Saga is a collection of scenarios built on the <a href="https://harag.cz/app/sand-game-js">Sand Game JS</a> engine.
      Use a mouse or a touch screen to complete the scenarios below.
      Step into a universe where every pixel is simulated.
    </p>
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  </div>

  <#list categories as category>
  <ul class="list-group sand-saga-scenario-list">
    <#list category.scenarios as scenario>
    <a href="/s/${scenario.name}/play" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
      ${scenario.title}
      <span id="scenario_${scenario.name}">
        <span class="badge bg-danger rounded-pill">
          <#if scenario.name?contains('sandbox')>not played<#else>not completed</#if>
        </span>
      </span>
    </a>
    </#list>
  </ul>
  </#list>

</#macro>

<#macro page_bottom>
  <div class="container sand-saga-footer">
    <a href="https://harag.cz/app/sand-game-js">Sand Game JS</a> & Sand Saga; &copy; Patrik Harag, <a href="https://harag.cz">harag.cz</a>; all rights reserved
    &bull;
    <#if user??>
      <a href="/#logout">Logout</a>
    <#else>
      <a href="#" onclick="sand_saga_com_showLoginForm(); return false;">Login</a>
    </#if>
  </div>

  <div id="footer-image"></div>
</#macro>

<@display_page/>
