<#include "layout.ftlh">

<#assign page_title="Snapshot"/>
<#assign enable_google_analytics=false/>

<#macro page_head>
  <link rel="stylesheet" type="text/css" href="/data/page-scenario-play.css" />
  <link rel="stylesheet" type="text/css" href="${config.urlSandGameJsCss}" />

  <script defer src="${config.urlSandGameJsScript}"></script>

  <script>
      document.addEventListener('DOMContentLoaded', () => {
          if (window.SandGameJS !== undefined) {
              const root = document.getElementById('sand-game-root');

              const config = {
                  version: '${config.versionSandGameJs}',
                  debug: true,
                  scene: {
                      init: async function (sandGame, controller) {

                          const url = '${snapshotUrl}';

                          function showError(msg) {
                              sandGame.scenario().createSplash({
                                  visible: true,
                                  content: msg,
                                  buttons: []
                              });
                          }

                          try {
                              const response = await fetch(url, { method: 'GET' });

                              if (!response.ok) {
                                  showError('HTTP error, status = ' + response.status);
                                  return;
                              }

                              const buffer = await response.arrayBuffer();
                              try {
                                  window.SandGameJS.Resources.parseZipResource(buffer)
                                      .then(resource => controller.openScene(resource))
                                      .catch(e => showError('' + e));

                              } catch (e) {
                                  showError('' + e);
                              }
                          } catch (error) {
                              showError('' + error);
                          }
                      }
                  },
                  disableSizeChange: true,
                  disableSceneSelection: true
              };
              SandGameJS.init(root, config);

          } else {
              const placeholder = document.getElementById('sand-game-root');
              placeholder.innerHTML = '<span style="color: red; font-weight: bold;">Failed to load the game.</span>';
          }
      });
  </script>
</#macro>

<#macro page_body_title>
  <h1><a href="/">Sand Saga</a> / Snapshot</h1>
</#macro>

<#macro page_body>
  <div id="sand-game-root">
    Loading...
  </div>
</#macro>

<@display_page/>