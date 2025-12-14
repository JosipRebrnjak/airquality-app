<html>
<head>
    <title>Uredi Postaju</title>
    <link rel="stylesheet" href="/path/to/bootstrap.css">
</head>
<body>
<div class="container">
    <h1>Uredi Postaju: ${postaja.naziv}</h1>

    <form method="post" action="">
        <input type="hidden" name="naziv" value="${postaja.naziv}">
        <input type="hidden" name="mrezaNaziv" value="${postaja.mrezaNaziv!}">

        <div class="mb-3">
            <label for="nazivEng" class="form-label">Naziv (eng)</label>
            <input type="text" id="nazivEng" name="nazivEng" class="form-control" value="${postaja.nazivEng}">
        </div>

        <div class="mb-3 form-check">
            <input type="checkbox" class="form-check-input" id="aktivna" name="aktivna" <#if postaja.aktivna?? && postaja.aktivna>checked</#if>>
            <label class="form-check-label" for="aktivna">Aktivna</label>
        </div>

        <button type="submit" class="btn btn-primary">Spremi</button>
        <a href="/airquality/fm/postaje?mrezaNaziv=${postaja.mrezaNaziv?url}" class="btn btn-secondary">Nazad</a>
    </form>
</div>
</body>
</html>
