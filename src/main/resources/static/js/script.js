$(document).ready(refreshStats);

function getIpData(ip) {
    $('.ipAddress').text("Dirección IP: " + ip);
    $('.countryName').text("Nombre del país: ");
    $('.countryCode').text("ISO code del país: ");
    $('.currency').text("Moneda: ");
    $('.currencyRate').text("");
    $('.distance').text("Distancia aproximada a Buenos Aires (km): ");
    $('.times').text("Hora: ");
    $('.languages').text("Idiomas: ");
    $('.errors').text("");
    $('.ip').val("");

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/location?originIp=" + ip
    }).then(function (data) {
        $('.countryName').append(data.name);
        $('.countryCode').append(data.isoCode);
        $('.currency').append(data.currency.name + " (" + data.currency.code + ")")
        if (data.usdRate) {
            $('.currencyRate').append("(1 " + data.currency.code + " = " + data.usdRate + " USD)");
        } else {
            $('.currencyRate').append("(Cotización no disponible)");
        }
        $('.distance').append(data.distanceToBue);
        data.times.forEach(function (item, index) {
            $('.times').append("<br>" + item + " (" + data.timezones[index] + ")");
        });
        data.languages.forEach(function (item, index) {
            $('.languages').append("<br>" + item.name + " [" + item.nativeName + "] (" + item.isoCode + ")");
        });
        if (data.errors) {
            $('.errors').text("Se ha producido un error:");
            data.errors.forEach(function (item, index) {
                $('.errors').append("<br>" + item.message);
            });
        }
    }, function (data) {
        $('.errors').text("Se ha producido un error: " + data.responseJSON.message);
    });
}

function refreshStats() {
    $('.averageDistance').text("Distancia promedio: ");
    $('.minDistance').text("Distancia mínima: ");
    $('.maxDistance').text("Distancia máxima: ");
    $('.errors').text("");

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/stats"
    }).then(function (data) {
        if (!jQuery.isEmptyObject(data)) {
            $('.averageDistance').append(data.averageDistance + " km");
            $('.minDistance').append(data.countryMinDistance.distance + " km" + " (" + data.countryMinDistance.country + " - " + data.countryMinDistance.numberOfInvocations + " invocaciones)");
            $('.maxDistance').append(data.countryMaxDistance.distance + " km" + " (" + data.countryMaxDistance.country + " - " + data.countryMaxDistance.numberOfInvocations + " invocaciones)");
            $('.statsMsg').text("");
        } else {
            $('.statsMsg').text("No hay estadísticas disponibles");
        }
    }, function (data) {
        $('.errors').text("Se ha producido un error: " + data.responseJSON.message);
    });
}

function deleteStats() {
    $('.errors').text("");

    $.ajax({
        type: "DELETE",
        url: "http://localhost:8080/stats"
    }).then(function (data) {
        $('.averageDistance').text("Distancia promedio: ");
        $('.minDistance').text("Distancia mínima: ");
        $('.maxDistance').text("Distancia máxima: ");
        $('.statsMsg').text("No hay estadísticas disponibles");
        alert("Estadísticas borradas")
    }, function (data) {
        $('.errors').text("Se ha producido un error: " + data.responseJSON.message);
    });
}
