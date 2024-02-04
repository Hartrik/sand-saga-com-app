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
        DomBuilder.button('Reload', {class: 'btn btn-primary'}, () => {
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
