<!DOCTYPE html>
<html lang="hr">
<head>
    <meta charset="UTF-8">
    <title>Postaje za ${mreza.naziv}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <style>
        .active-postaja { color: green; font-weight: bold; }
        .inactive-postaja { color: gray; }
    </style>
</head>
<body>
    <header class="mb-4 p-3 bg-light">
        <h1>Air Quality - ${mreza.naziv}</h1>
        <nav>
            <a href="/airquality/fm/mreze">Povratak na mreže</a>
        </nav>
    </header>

    <main class="container">
        <ul class="list-group">
        <#list mreza.postaje as postaja>
            <li class="list-group-item ${postaja.aktivna?then('active-postaja','inactive-postaja')}">
                <a href="/airquality/fm/postaja?naziv=${postaja.naziv?url}&mrezaNaziv=${mreza.naziv?url}">${postaja.naziv}</a>
            </li>
        </#list>
        </ul>
    </main>
</body>
</html>
