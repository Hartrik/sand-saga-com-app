<#include "layout.ftlh">

<#assign page_title="${scenario.title}"/>
<#assign page_description="Sand Saga"/>
<#assign enable_google_analytics=true/>

<#macro page_head>
  <link rel="stylesheet" type="text/css" href="${scenario.urlSandGameJsCss}" />

  <script defer src="${scenario.urlSandGameJsScript}"></script>
  <script defer src="${scenario.urlSandSagaScript}"></script>

  <script>
    <#if scenario.scriptingEnabled>
    // global variables, accessible from browser console
    var sandGame = null;
    var brushes = null;
    </#if>


    document.addEventListener('DOMContentLoaded', () => {
      if (window.SandGameJS !== undefined && window.SandSaga !== undefined) {
        const root = document.getElementById('sand-game-root');

        const config = {
          version: '${scenario.versionSandGameJs}',
          debug: false,
          scenario: '${scenario.name}',
          lobbyUrl: '/',
          nextUrl: <#if scenario.nextScenarioName??>'/s/${scenario.nextScenarioName}/play'<#else>undefined</#if>,
          includeSnapshot: ${scenario.storeSnapshot?c}
        };

        <#if !user??> <#-- not logged -->
        if (config.scenario.startsWith('sandbox')) {
          function sandSaga_showTryOtherScenarios() {
            const infoElement = document.getElementById('sand-saga-info_try-other-scenarios');
            if (infoElement) {
              infoElement.classList.add('show');
            }
          }
          setTimeout(() => sandSaga_showTryOtherScenarios(), 1000);
        }

        const localStorageKey = 'scenario_' + config.scenario;
        config.onCompleted = function () {
          if (window.localStorage !== undefined) {
            const oldValue = localStorage.getItem(localStorageKey);
            if (oldValue === null) {
              localStorage.setItem(localStorageKey, 'true');
            }
          }
        };
        config.onAccepted = function (id) {
          if (window.localStorage !== undefined) {
            localStorage.setItem(localStorageKey, '' + id);
          }
        };
        </#if>

        const controller = SandSaga.init(root, config);
        <#if scenario.scriptingEnabled>
        if (controller !== undefined) {
          controller.addOnInitialized(s => {
            sandGame = s;
          });
          brushes = SandGameJS.brushes;
          console.log('Globals:');
          console.log('  sandGame, brushes');
          console.log('Examples:');
          console.log('  sandGame.graphics().drawLine(10, 10, 300, 150, 2, brushes.sand);');
          console.log('  sandGame.graphics().draw(100, 20, brushes.meteor);');
        }
        </#if>
      } else {
        const placeholder = document.getElementById('sand-game-placeholder-content');
        placeholder.innerHTML = '<span style="color: red; font-weight: bold;">' +
            'Failed to load the game. Possible reasons:<br>' +
            '&bull; Your browser may not support modern JavaScript<br>' +
            '&bull; Internet connection<br>' +
            '&bull; A server side problem' +
            '</span>';
      }
    });
  </script>
</#macro>

<#macro page_bar>
  <span class="navbar-text">
    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="green" class="bi bi-play-fill" viewBox="0 0 16 16">
      <path d="m11.596 8.697-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01 1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393"/>
    </svg>
    ${scenario.title}
  </span>
</#macro>

<#macro page_body>
  <div id="sand-game-root">
    <!-- Sand Game JS placeholder -->
    <div style="user-select: none;">
      <div>
        <button disabled class="btn" type="button" style="min-width: 4em; line-height: 2; padding: 0 0.4em 0 0.4em; font-size: 75%; border: none; background-color: rgb(230, 230, 230); color: black;">&nbsp;</button>
      </div>
      <div style="height: 70vh; display: flex; align-items: center; justify-content: center; margin-top: 4pt; padding: 2em; outline: 1px solid #b7b7b7;">
        <div id="sand-game-placeholder-content">
          Loading...
        </div>
      </div>
      <div style="height: 20vh"></div>
    </div>
  </div>

  <div id="sand-saga-info_try-other-scenarios" class="alert alert-info alert-dismissible fade" role="alert" style="margin-top: 2em;">
    Enjoying the ${scenario.title}? It’s just one of Sand Saga scenarios. Try our challenge-based scenarios or start with our tutorial for guidance.
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  </div>
</#macro>

<@display_page/>