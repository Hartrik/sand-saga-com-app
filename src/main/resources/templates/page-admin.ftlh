<#include "layout.ftlh">

<#assign page_title="Administration"/>
<#assign page_description="Sand Saga"/>
<#assign enable_google_analytics=false/>

<#macro page_head>
  <script type="module">
    import { DomBuilder } from "/data/admin/DomBuilder.js";

    const root = document.getElementById('administration-root');

    function handle(fetchPromise) {
        fetchPromise.then(r => {
            alert('Successful');
        }).catch(e => {
            console.log(e);
            alert('Error: ' + e);
        })
    }

    root.append(DomBuilder.par(null, [
        DomBuilder.button('Reload Config', {class: 'btn btn-primary'}, () => {
            handle(fetch('/api/admin/reload-config', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: ''
            }));
        })
    ]));

    const reportDiv = DomBuilder.div(null);

    fetch('/api/admin/reports', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    }).then(response => response.json()).then(reports => {
        const table = DomBuilder.bootstrapTableBuilder();
        for (const report of reports) {
            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('td', null, '' + report.id),
                DomBuilder.element('td', null, report.location),
                DomBuilder.element('td', null, report.scenario),
                DomBuilder.element('td', null, [
                    DomBuilder.par(null, '' + new Date(report.time)),
                    DomBuilder.par(null, report.message),
                    DomBuilder.par(null, report.metadata),
                    DomBuilder.par(null, report.ip),
                ]),
            ]));
        }
        reportDiv.append(table.createNode());
    });

    root.append(reportDiv);
  </script>
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
