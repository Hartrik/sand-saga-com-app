<#setting locale="en_US">

<#macro page_head></#macro>
<#macro page_bar></#macro>
<#macro page_body></#macro>
<#macro page_bottom></#macro>

<#macro display_page>
<!DOCTYPE html>
<html lang="${page_lang!'en'}">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="author" content="Patrik Harag">
    <meta property="og:site_name" content="SandSaga.com">
    <#if page_description?has_content><meta name="Description" content="${page_description}"></#if>
    <#if page_description?has_content><meta property="og:description" content="${page_description}"></#if>
    <meta property="og:title" content="${page_title!'Sand Saga'}">
    <#if page_image?has_content><meta property="og:image" content="${page_image}"><#else><meta property="og:image" content="/social-preview.png"</#if>

    <!-- icons -->
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png?v=1">
    <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png?v=1">
    <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png?v=1">
    <link rel="manifest" href="/site.webmanifest?v=1">
    <link rel="mask-icon" href="/safari-pinned-tab.svg?v=1" color="#ffc40d">
    <link rel="shortcut icon" href="/favicon.ico?v=1">
    <meta name="msapplication-TileColor" content="#ffc40d">
    <meta name="theme-color" content="#ffffff">

    <title>Sand Saga<#if page_title?has_content> - ${page_title}</#if></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>

    <link rel="stylesheet" type="text/css" href="/data/sand-saga.css">

    <#if enable_google_analytics?? && enable_google_analytics>
    <#include "part-google-analytics.ftlh">
    </#if>

    <@page_head/>
  </head>

  <body class="d-flex">
    <div id="page-content-wrapper" class="d-flex flex-column">

      <nav class="navbar navbar-dark bg-dark">
        <div class="container">
          <a class="navbar-brand" href="/">
            <img src="/favicon-32x32.png" width="32" height="32" alt="Logo">
            <h1 style="display: inline;">Sand Saga</h1>
          </a>
          <@page_bar/>
        </div>
      </nav>

      <noscript>
        <div class="container">
          <div class="alert alert-danger alert-dismissible fade show" role="alert">
            For full functionality of this site it is necessary to enable JavaScript.
            Here are the <a href="https://www.enablejavascript.io/">
              instructions how to enable JavaScript in your web browser</a>.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        </div>
      </noscript>

      <main class="${page_container_style!'container'}">
        <@page_body/>
      </main>

      <div class="spacer"></div>

      <@page_bottom/>
    </div>
  </body>
</html>
</#macro>