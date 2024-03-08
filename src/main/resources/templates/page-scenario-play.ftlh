<#include "layout.ftlh">

<#assign page_title="${scenario.title}"/>
<#assign page_description="Sand Saga"/>
<#assign enable_google_analytics=true/>

<#macro page_head>
    <link rel="stylesheet" type="text/css" href="/data/page-scenario-play.css" />
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

                <#if !user??>
                const localStorageKey = 'scenario_${scenario.name}';
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

<#macro page_body_title>
    <h1><a href="/">Sand Saga</a> / ${scenario.title}</h1>
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
</#macro>

<@display_page/>