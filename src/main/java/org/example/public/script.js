
let ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/websocket");
ws.onmessage = msg => parseMessage(msg);
var players = {};
var username = "";
function setup() {
    createCanvas(400, 400);
}

function draw() {
    background(220);

    for (let playerName in players) {
        let player = JSON.parse(players[playerName]);
        ellipse(player.x,player.y,40,40);
    }
}
function parseMessage(msg){
    //console.log(msg.data);
    let data = JSON.parse(msg.data);
    console.log(data);
    switch (data.packetType){
        case "position":

            players = data.players;
            break;
        case "username":
            username = data.username;
            break;
    }
}
function keyPressed() {
    sendUserKeyPressed(key);
}

function keyReleased() {
    sendUserKeyReleased(key);
}
function sendUserKeyPressed(key){
    data = {
        "packetType": "keyPressed",
        "key": key,
        "username": username
    }
    sendMessage(JSON.stringify(data))
}
function sendUserKeyReleased(key){
    data = {
        "packetType": "keyReleased",
        "key": key,
        "username": username
    }
    sendMessage(JSON.stringify(data))
}
function sendMessage(msg){
    ws.send(msg);
}