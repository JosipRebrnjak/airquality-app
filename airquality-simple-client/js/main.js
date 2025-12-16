$(function() {
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


    $("#syncData").click(function() {
        $.getJSON(`${API_BASE}/networks/sync`, function(data) {
            $("#messageSection").text(data.message)
            setTimeout(() => {
                $("#messageSection").text("");
            }, 3000);
        }).fail(() => alert("Greška pri sinkroniziranju podataka"));
    });

    function fetchNetworks() {
        $.getJSON(`${API_BASE}/networks`, function(data) {
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

    $("#backToNetworks").click(function() {
        $stationsSection.hide();
        $networksSection.show();
        selectedNetwork = null;
    });

    function fetchPostaje(nazivMreze) {
        $.getJSON(`${API_BASE}/stations/${encodeURIComponent(nazivMreze)}`, function(data) {
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

    $("#editForm").submit(function(e) {
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
            success: function() {
                selectedPostaja.nazivEng = updated.nazivEng;
                selectedPostaja.aktivna = updated.aktivna;
                renderPostaje();
                bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
            },
            error: function() {
                alert("Greška pri spremanju postaje");
            }
        });
    });

    fetchNetworks();
});