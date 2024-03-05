<#include "layout.ftlh">

<#assign page_title=""/>
<#assign page_description="The Sand Saga is a unique falling-sand game, built on the Sand Game JS engine. It belongs to a genre that includes popular titles like The Powder Toy or Noita."/>
<#assign enable_google_analytics=true/>

<#macro page_head>
  <link href="/data/page-index.css?v=1" rel="stylesheet">

  <#if !user??> <#-- if not logged -->
    <script>
        function sand_saga_com_showFormLogin() {
            const dialogNode = document.getElementById('dialog-login');
            const modal = new window.bootstrap.Modal(dialogNode);
            modal.show();
        }

        if (window.location.hash === '#form-login' || window.location.hash === '#form-login-failed') {
            document.addEventListener('DOMContentLoaded', () => {
                sand_saga_com_showFormLogin();
            });
        }
    </script>
  </#if>
  <#if user?? && user_tenant == 'form'>
    <script>
        function sand_saga_com_formLogout() {
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
                      if (localStorage.getItem(key) !== null) {
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

  <#if user??>  <#-- if logged -->
  <script>
      <#-- claim completed -->
      if (window.localStorage !== undefined) {
          let completed = [];
          for (const key of Object.keys(localStorage)) {
              if (key.startsWith('scenario_')) {
                  const scenario = key.substring(9);
                  const value = localStorage.getItem(key);
                  const valueAsNumber = Number.parseInt(value);
                  if (Number.isInteger(valueAsNumber)) {
                      completed.push({ id: valueAsNumber, scenario: scenario });
                  }
              }
          }

          if (completed.length > 0) {
              const jsonData = JSON.stringify({
                  completed: completed
              });

              fetch('/api/user/claim-completed', {
                  method: 'POST',
                  headers: {
                      'Accept': 'application/json',
                      'Content-Type': 'application/json'
                  },
                  body: jsonData
              }).then(r => {
                  if (r.ok) {
                      return r.json();
                  } else {
                      throw r.status;
                  }
              }).then(json => {
                  for (let {id, scenario} of json.accepted) {
                      localStorage.removeItem('scenario_' + scenario);
                  }
                  for (let {id, scenario} of json.refused) {
                      localStorage.removeItem('scenario_' + scenario);
                  }
              }).catch(e => {
                  console.error(e);
              });
          }
      }
  </script>
  </#if>
</#macro>

<#macro page_body>

  <#if !user??>  <#-- if not logged -->
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

  <div style="float: left;">
    <img class="small-preview" src="/data/preview-sm-01.png" style="top: 50px; margin-left: -300px;" alt="Background image">
  </div>
  <div style="float: right;">
    <img class="small-preview" src="/data/preview-sm-04.png" style="top: 50px;" alt="Background image">
    <img class="small-preview" src="/data/preview-sm-07.png" style="top: 500px;" alt="Background image">
  </div>

  <h1>Sand Saga <span style="color: rgba(var(--bs-danger-rgb));">BETA</span></h1>
  <div class="alert alert-info">
    <p>
      The Sand Saga is a unique falling-sand game, built on the <a href="https://harag.cz/app/sand-game-js">Sand Game JS</a> engine.
      It belongs to a&nbsp;genre that includes popular titles like The Powder Toy or Noita.
    </p>
    <p style="text-align: center;">
      <span style="white-space: nowrap;"><strong>${stats.completed?string.number}</strong> <small style="margin-right: 1em;">scenarios completed</small></span>
      <span style="white-space: nowrap;"><strong>${stats.updates?string.number}</strong> <small>minutes played</small></span>
    </p>
  </div>

  <div class="sand-saga-play-sandbox">
    <a class="btn btn-lg btn-primary" href="/s/sandbox-empty/play">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-play-fill" viewBox="0 0 16 16">
        <path d="m11.596 8.697-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01 1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393"/>
      </svg>
      Play Sandbox
    </a>
    <br>
    <small>Or play scenarios below</small>
  </div>

  <#list categories as category>
  <#if category?counter == 2>
  <div class="alert alert-info">
    <#if user??>
    <h5>Account</h5>
    <p>Logged as ${user_name!''}</p>
    <#if user_tenant == 'form'>
      <a href="javascript: sand_saga_com_formLogout();" class="btn btn-primary">Logout</a>
    <#else>
      <a href="/logout" class="btn btn-primary">Logout</a>
    </#if>
    <#else>
    <h5>Save your progress</h5>
    <p>Log in with Discord to save your progress. If you don't have an account, creating one is fast.</p>
    <a href="/login/discord" class="btn btn-primary btn-discord">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-discord" viewBox="0 0 16 16">
        <path d="M13.545 2.907a13.2 13.2 0 0 0-3.257-1.011.05.05 0 0 0-.052.025c-.141.25-.297.577-.406.833a12.2 12.2 0 0 0-3.658 0 8 8 0 0 0-.412-.833.05.05 0 0 0-.052-.025c-1.125.194-2.22.534-3.257 1.011a.04.04 0 0 0-.021.018C.356 6.024-.213 9.047.066 12.032q.003.022.021.037a13.3 13.3 0 0 0 3.995 2.02.05.05 0 0 0 .056-.019q.463-.63.818-1.329a.05.05 0 0 0-.01-.059l-.018-.011a9 9 0 0 1-1.248-.595.05.05 0 0 1-.02-.066l.015-.019q.127-.095.248-.195a.05.05 0 0 1 .051-.007c2.619 1.196 5.454 1.196 8.041 0a.05.05 0 0 1 .053.007q.121.1.248.195a.05.05 0 0 1-.004.085 8 8 0 0 1-1.249.594.05.05 0 0 0-.03.03.05.05 0 0 0 .003.041c.24.465.515.909.817 1.329a.05.05 0 0 0 .056.019 13.2 13.2 0 0 0 4.001-2.02.05.05 0 0 0 .021-.037c.334-3.451-.559-6.449-2.366-9.106a.03.03 0 0 0-.02-.019m-8.198 7.307c-.789 0-1.438-.724-1.438-1.612s.637-1.613 1.438-1.613c.807 0 1.45.73 1.438 1.613 0 .888-.637 1.612-1.438 1.612m5.316 0c-.788 0-1.438-.724-1.438-1.612s.637-1.613 1.438-1.613c.807 0 1.451.73 1.438 1.613 0 .888-.631 1.612-1.438 1.612"/>
      </svg>
      Continue with Discord
    </a>
    </#if>
  </div>
  </#if>

  <ul class="list-group sand-saga-scenario-list">
    <#list category.scenarios as scenario>
    <a href="/s/${scenario.name}/play" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
      <span>
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-play-fill" viewBox="0 0 16 16">
          <path d="m11.596 8.697-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01 1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393"/>
        </svg>
        ${scenario.title}
      </span>
      <span id="scenario_${scenario.name}">
        <#if completed?? && completed[scenario.name]??>
        <span class="badge bg-success rounded-pill"><#if scenario.name?contains('sandbox')>played<#else>completed</#if></span>
        <#else>
        <span class="badge bg-danger rounded-pill"><#if scenario.name?contains('sandbox')>not played<#else>not completed</#if></span>
        </#if>
      </span>
    </a>
    </#list>
  </ul>
  </#list>

</#macro>

<#macro page_bottom>
  <div class="container sand-saga-footer">
    <a href="https://harag.cz/app/sand-game-js">Sand Game JS</a> & Sand Saga; &copy; Patrik Harag, <a href="https://harag.cz">harag.cz</a>; all rights reserved
    <#if user?? && user == 'ADMIN'>
      &bull; <a href="/admin">Administration</a>
    <#elseif user??>
    <#else>
      &bull; <a href="#" onclick="sand_saga_com_showFormLogin(); return false;">Administration</a>
    </#if>
  </div>

  <div id="footer-image"></div>
</#macro>

<@display_page/>
