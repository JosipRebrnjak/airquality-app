<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Air Quality</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css">
</head>
<body>
<header class="bg-primary text-white p-3 mb-4">
    <h1>Air Quality</h1>
</header>

<div class="container">
    <h2>Mreže</h2>
    <ul class="list-group">
        <#list mreze as mreza>
            <li class="list-group-item">
                <a href="/airquality/fm/postaje?mrezaNaziv=${mreza.naziv?url}">${mreza.naziv}</a>
            </li>
        </#list>
    </ul>
</div>
</body>
</html>
