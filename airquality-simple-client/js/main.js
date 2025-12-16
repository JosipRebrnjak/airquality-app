$(function () {
    const API_BASE = "http://localhost:8081/airquality/api";
    let mreze = [];
    let postaje = [];
    let selectedNetwork = null;
    let selectedPostaja = null;

    const $networksList = $("#networksList");
    const $postajeList = $("#postajeList");
    const $networksSection = $("#networksSection");
    const $stationsSection = $("#stationsSection");
    const $mrezaNaziv = $("#mrezaNaziv");

    const container = document.getElementById('popup');
    const content = document.getElementById('popup-content');
    const closer = document.getElementById('popup-closer');



    const wmsLayer = new ol.layer.Tile({
        source: new ol.source.TileWMS({
            url: 'https://servisi.azo.hr/inspire/geos1/ef/wms',
            params: {
                LAYERS: 'EF.EnvironmentalMonitoringFacilities',
                TILED: true,
                VERSION: '1.1.1'
            },
            serverType: 'geoserver',
            crossOrigin: 'anonymous'
        })
    });


    const map = new ol.Map({
        target: 'mapa',
        layers: [
            // Base layer
            new ol.layer.Tile({
                source: new ol.source.OSM(),

            }),
            // WMS sloj mjernih stanica
            wmsLayer
        ],
        view: new ol.View({
            center: ol.proj.fromLonLat([15.9819, 45.8150]), // Zagreb 
            zoom: 10
        })
    });

    const overlay = new ol.Overlay({
        element: container,
        autoPan: true,
        autoPanAnimation: { duration: 250 }
    });

    map.addOverlay(overlay);

    closer.onclick = function () {
        overlay.setPosition(undefined);
        closer.blur();
        return false;
    };


    map.on('singleclick', function (evt) {
        const viewResolution = map.getView().getResolution();
    
        const url = wmsLayer.getSource().getFeatureInfoUrl(
            evt.coordinate,
            viewResolution,
            'EPSG:3857',
            {
                INFO_FORMAT: 'text/html',
                FEATURE_COUNT: 1
            }
        );
    
        if (!url) return;
    
        fetch(url)
            .then(r => r.text())
            .then(html => {
                if (!html.includes('<table')) {
                    overlay.setPosition(undefined);
                    return;
                }
    
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                const cells = doc.querySelectorAll('table.featureInfo td');
    
                if (cells.length === 0) return;
    
                const data = {
                    naziv: cells[4]?.innerText,
                    opis: cells[3]?.innerText,
                    svrha: cells[11]?.innerText,
                    odgovorni: cells[13]?.innerText
                };
    
                content.innerHTML = `
                    <strong>${data.naziv}</strong><br/>
                    <em>${data.opis}</em><br/><br/>
                    <b>Svrha:</b> ${data.svrha}<br/>
                    <b>Odgovorni:</b> ${data.odgovorni}
                `;
    
                overlay.setPosition(evt.coordinate);
            });
    });
    


    $("#syncData").click(function () {
        $.getJSON(`${API_BASE}/networks/sync`, function (data) {
            $("#messageSection").text(data.message)
            setTimeout(() => {
                $("#messageSection").text("");
            }, 3000);
        }).fail(() => alert("Greška pri sinkroniziranju podataka"));
    });

    function fetchNetworks() {
        $.getJSON(`${API_BASE}/networks`, function (data) {
            mreze = data;
            renderNetworks();
        }).fail(() => alert("Greška pri dohvaćanju mreža"));
    }

    function renderNetworks() {
        $networksList.empty();
        mreze.forEach(m => {
            $("<li>")
                .addClass("list-group-item pointer")
                .text(m.naziv)
                .click(() => openNetwork(m.naziv))
                .appendTo($networksList);
        });
    }

    function openNetwork(naziv) {
        selectedNetwork = naziv;
        $networksSection.hide();
        $stationsSection.show();
        $mrezaNaziv.text(naziv);
        fetchPostaje(naziv);
    }

    $("#backToNetworks").click(function () {
        $stationsSection.hide();
        $networksSection.show();
        selectedNetwork = null;
    });

    function fetchPostaje(nazivMreze) {
        $.getJSON(`${API_BASE}/stations/${encodeURIComponent(nazivMreze)}`, function (data) {
            postaje = data;
            renderPostaje();
        }).fail(() => alert("Greška pri dohvaćanju postaja"));
    }

    function renderPostaje() {
        $postajeList.empty();
        postaje.forEach(p => {
            $("<li>")
                .addClass("list-group-item pointer")
                .addClass(p.aktivna ? "list-group-item-success" : "list-group-item-secondary")
                .text(p.naziv)
                .click(() => editPostaja(p))
                .appendTo($postajeList);
        });
    }

    function editPostaja(p) {
        selectedPostaja = p;
        $("#nazivEng").val(p.nazivEng);
        $("#aktivnaCheck").prop("checked", p.aktivna);
        const modal = new bootstrap.Modal(document.getElementById('editModal'));
        modal.show();
    }

    $("#editForm").submit(function (e) {
        e.preventDefault();
        const updated = {
            nazivEng: $("#nazivEng").val(),
            aktivna: $("#aktivnaCheck").is(":checked")
        };
        $.ajax({
            url: `${API_BASE}/stations/${encodeURIComponent(selectedPostaja.naziv)}`,
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify(updated),
            success: function () {
                selectedPostaja.nazivEng = updated.nazivEng;
                selectedPostaja.aktivna = updated.aktivna;
                renderPostaje();
                bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
            },
            error: function () {
                alert("Greška pri spremanju postaje");
            }
        });
    });

    fetchNetworks();
});