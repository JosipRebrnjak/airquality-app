<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Air Quality</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
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
            <li class="list-group-item">
                <a class="${postaja.aktivna?then('text-success','text-secondary')}" href="/airquality/fm/postaja?naziv=${postaja.naziv?url}&mrezaNaziv=${mreza.naziv?url}">${postaja.naziv}</a>
            </li>
        </#list>
        </ul>
    </main>
</body>
</html>
