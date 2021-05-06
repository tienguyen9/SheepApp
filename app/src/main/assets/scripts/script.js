var map = L.map('map');

var firstPointMarker;
var currentPositionMarker;

var predatorMarker;
var predatorMarkerLat;
var predatorMarkerLon;

var sheepMarker;
var sheepMarkerLat;
var sheepMarkerLon;
sheepCircles = {};
var sheepCircleRadius = 25;

//Corners of rectangle for downloading map
var corner1;
var corner2;
var rectangle;

//Strings to be used in Java as input for gownloadTile
var SouthEastString;
var NorthWestString;

function hei(){
    console.log("SGFSAG")
}

function loadOnlineMap(currentLat, currentLon){
    map.setView([currentLat, currentLon], 13);
    L.tileLayer('https://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=norges_grunnkart&zoom={z}&x={x}&y={y}', {
        attribution: '<a href="http://www.kartverket.no/">Kartverket</a>',
    }).addTo(map);

}

function loadOfflineMap(folder,centerLat, centerLon, minZoom, maxZoom){
    startZoom = (minZoom+maxZoom)/2;
    map.setView([centerLat, centerLon], startZoom);
    L.tileLayer('file:////sdcard/Documents/SheepTracker/' + folder + '/tile_z={z}_x={x}_y={y}.png', {
        attribution: '<a href="http://www.kartverket.no/">Kartverket</a>',
        minZoom: minZoom,
        maxZoom: maxZoom,
    }).addTo(map);
}
/*
function loadOfflineMap(folder,centerLat, centerLon, minZoom, maxZoom){
    startZoom = (minZoom+maxZoom)/2;
    map.setView([centerLat, centerLon], startZoom);
    L.tileLayer('./tiles/' + folder + '/tile_z={z}_x={x}_y={y}.png', {
        attribution: '<a href="http://www.kartverket.no/">Kartverket</a>',
        minZoom: minZoom,
        maxZoom: maxZoom,
    }).addTo(map);
}
*/

function addPoint(lat, lon){
    var point = new L.LatLng(lat, lon);
    map.panTo(point);
    return point;
}

//used in OfflineMapActivity
function addFirstPoint(lat, lon){
    firstPointMarker = L.marker([lat, lon]).addTo(map).bindPopup("This is your starting point").openPopup();
    var point = new L.LatLng(lat, lon);
    map.panTo(point);
    return point;
}

//used in OnlineMapActivity
function showCurrentPosition(lat, lon){
    if(currentPositionMarker != null){
        map.removeLayer(currentPositionMarker);
    }
    currentPositionMarker = L.marker([lat, lon]).addTo(map).bindPopup("This is your current position").openPopup();
    map.panTo(currentPositionMarker.getLatLng());
    return currentPositionMarker;
}

function drawLineBetweenPoints(pointA, pointB){
    var pointList = [pointA, pointB];
    var firstpolyline = new L.Polyline(pointList, {color: 'red'}).addTo(map);
}

function moveSheepMarker(startLat, startLon){
    sheepMarker = L.marker([startLat, startLon], {draggable: true}).addTo(map);
    sheepMarker.setOpacity(1);
    sheepMarker.bindPopup("Drag marker to where you spotted the sheep").openPopup();
    sheepMarkerLat = startLat;
    sheepMarkerLon = startLon;

    sheepMarker.on('dragend', function() {
        sheepMarkerLat = sheepMarker.getLatLng().lat;
        sheepMarkerLon = sheepMarker.getLatLng().lng;
    });
}

function getSheepMarkerPos(){
    return sheepMarkerLat + "_" + sheepMarkerLon;
}

function hideSheepMarker(){
    map.removeLayer(sheepMarker)
    sheepMarker.closePopup();
}

function movePredatorMarker(startLat, startLon){
    predatorMarker = L.marker([startLat, startLon], {draggable: true}).addTo(map);
    predatorMarker.setOpacity(1);
    predatorMarker.bindPopup("Drag marker to where you spotted the predator").openPopup();
    predatorMarkerLat = startLat;
    predatorMarkerLon = startLon;

    predatorMarker.on('dragend', function() {
        predatorMarkerLat = predatorMarker.getLatLng().lat;
        predatorMarkerLon = predatorMarker.getLatLng().lng;
    });
}

function getPredatorMarkerPos(){
    return predatorMarkerLat + "_" + predatorMarkerLon;
}

function hidePredatorMarker(){
    map.removeLayer(predatorMarker)
    predatorMarker.closePopup();
}



function isExistingCircle(){
    for (let i = 0;i < sheepCircles.length;i++) {
        console.log(i.getLatLng())
        var ltln = new L.LatLng(sheepMarkerLat, sheepMarkerLon);
        var isInCircleRadius = Math.abs(ltln.distanceTo(sheepCircles[i.getLatLng()])) <= sheepCircleRadius;
        if(isInCircleRadius) {
        console.log("in")
        return true;
        }else{
        console.log("out")
        return false;
        }
}
}
//for offline activity
function registerSheepPointMarker(sheepID){
    var circle = L.circle([sheepMarkerLat, sheepMarkerLon], {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.5,
        radius: sheepCircleRadius
    }).addTo(map);
    sheepCircles[sheepID] = circle;
    circle.on('click', function (e) {
        alert("Hello, circle!" + sheepID);
        AndroidFunction.editSheepRegister(sheepID);
    });
    }


//for past trips
function registerSheepPointLatLng(lat, lon){
var circle = L.circle([lat, lon], {
    color: 'red',
    fillColor: '#f03',
    fillOpacity: 0.5,
    radius: sheepCircleRadius
}).addTo(map);
    var point = new L.LatLng(lat, lon);
    return point;
}

//for offline activity
function registerPredatorPointMarker(){
var circle = L.circle([predatorMarkerLat, predatorMarkerLon], {
    color: 'black',
    fillColor: 'black',
    fillOpacity: 1,
    radius: 40
}).addTo(map);
    return point;
}

//for past trips
function registerPredatorPointLatLng(lat, lon){
var circle = L.circle([lat, lon], {
    color: 'black',
    fillColor: 'black',
    fillOpacity: 1,
    radius: 40
}).addTo(map);
    var point = new L.LatLng(lat, lon);
    return point;
}


//for OfflineMapActivity
function drawLineSheepPositionPoint(lastPoint, sheepLat, sheepLon) {
    var point = new L.LatLng(sheepLat, sheepLon);
    var pointList = [lastPoint, point];
    var polyline = new L.Polyline(pointList, {color: 'blue'}).addTo(map);
}

//for TrackingHistoryActivity
function drawLineSheepPositionLatLng(spottedLat, spottedLon, sheepLat, sheepLon) {
    var point1 = new L.LatLng(spottedLat, spottedLon);
    var point2 = new L.LatLng(sheepLat, sheepLon);
    var pointList = [point1, point2];
    var polyline = new L.Polyline(pointList, {color: 'blue'}).addTo(map);
}

function chooseMapRectangle(){

    var mapbounds = map.getBounds();
    if(currentPositionMarker != null){
            currentPositionMarker.setOpacity(0);
    }
    var northWest = mapbounds.getNorthWest();
    var southEast = mapbounds.getSouthEast();
    var centerXdiff = northWest.lat - southEast.lat;
    var centerYdiff = northWest.lng - southEast.lng;
    var corner1StartX = northWest.lat - centerXdiff/4;
    var corner1StartY = northWest.lng - centerYdiff/4;
    var corner2StartX = southEast.lat + centerXdiff/4;
    var corner2StartY = southEast.lng + centerYdiff/4;
    corner1 = L.marker([corner1StartX, corner1StartY], {draggable: true}).addTo(map);
    corner2 = L.marker([corner2StartX, corner2StartY], {draggable: true}).addTo(map);
    UpdateNorthWestCorner();
    UpdateSouthEastCorner();
    
    rectangle = L.polygon([
        corner1.getLatLng(),
        [corner1.getLatLng().lat, corner2.getLatLng().lng],
        corner2.getLatLng(),
        [corner2.getLatLng().lat, corner1.getLatLng().lng]
    ]).addTo(map);
    corner1.on('dragend', function() {
    if(rectangle != null){
        map.removeLayer(rectangle);
    }
    rectangle = L.polygon([
        corner1.getLatLng(),
        [corner1.getLatLng().lat, corner2.getLatLng().lng],
        corner2.getLatLng(),
        [corner2.getLatLng().lat, corner1.getLatLng().lng]
    ]).addTo(map);
    UpdateNorthWestCorner();
    UpdateSouthEastCorner();
    });

    corner2.on('dragend', function() {
    if(rectangle != null){
        map.removeLayer(rectangle);
    }

    rectangle = L.polygon([
        corner1.getLatLng(),
        [corner1.getLatLng().lat, corner2.getLatLng().lng],
        corner2.getLatLng(),
        [corner2.getLatLng().lat, corner1.getLatLng().lng]
    ]).addTo(map);
    UpdateNorthWestCorner();
    UpdateSouthEastCorner();
    });
}

function hideMapRectangle(){
    if(currentPositionMarker != null){
            currentPositionMarker.setOpacity(0);
    }
    map.removeLayer(rectangle);
    map.removeLayer(corner1);
    map.removeLayer(corner2);

}

function UpdateNorthWestCorner(){
    c1Lat = corner1.getLatLng().lat;
    c1Lon = corner1.getLatLng().lng;
    c2Lat = corner2.getLatLng().lat;
    c2Lon = corner2.getLatLng().lng;
    if(c1Lat >= c2Lat && c1Lon <= c2Lon){
        NorthWestString = corner1.getLatLng().lat + "_" + corner1.getLatLng().lng;
    } else if(c1Lat <= c2Lat && c1Lon <= c2Lon){
        NorthWestString = corner2.getLatLng().lat + "_" + corner1.getLatLng().lng;
    } else if(c1Lat >= c2Lat && c1Lon >= c2Lon){
        NorthWestString = corner1.getLatLng().lat + "_" + corner2.getLatLng().lng;
    } else{
        NorthWestString = corner2.getLatLng().lat + "_" + corner2.getLatLng().lng;
    }
}

function UpdateSouthEastCorner(){
    c1Lat = corner1.getLatLng().lat;
    c1Lon = corner1.getLatLng().lng;
    c2Lat = corner2.getLatLng().lat;
    c2Lon = corner2.getLatLng().lng;
    if(c1Lat >= c2Lat && c1Lon <= c2Lon){
        SouthEastString = corner2.getLatLng().lat + "_" + corner2.getLatLng().lng;
    } else if(c1Lat <= c2Lat && c1Lon <= c2Lon){
        SouthEastString = corner1.getLatLng().lat + "_" + corner2.getLatLng().lng;
    } else if(c1Lat >= c2Lat && c1Lon >= c2Lon){
        SouthEastString = corner2.getLatLng().lat + "_" + corner1.getLatLng().lng;
    } else{
        SouthEastString = corner1.getLatLng().lat + "_" + corner1.getLatLng().lng;
    }
}

function GetNorthWestCorner(){
    return NorthWestString;
}

function GetSouthEastCorner(){
    return SouthEastString;
}

//remove later
//loadOnlineMap(63.416798, 10.399254);
//loadOfflineMap("bymarka_13_17", 63.429047, 10.398547, 13, 17);