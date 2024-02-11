
let ws = new WebSocket("wss://" + location.hostname + ":" + location.port + "/websocket");
ws.onmessage = msg => parseMessage(msg);
ws.onclose = () => alert("WebSocket connection closed");
var utils = new p5.Utils();
var gameData = {};
var username = "";
var usernameInput = null;
var usernameSubmitted = false;
var usernameResponse = {};
var usernameRejected = false;
var lobbyJoinRejected = false;
var usernameButton = null;
var lobbyButton = null;
var lobbyInput = null;
var state = "usernameSelector";

function setup() {
    textAlign(CENTER);
    bg = loadImage('assets/background.png');
    playerImg = loadImage('assets/slimer.png');
    createCanvas(400, 400);
    //utils.enableRuler()
    usernameSelector()
}

function draw() {
    background(bg);
    switch (state){
        case "mainMenu":
            drawMainMenu();
            break;
        case "game":
            drawRunningGame();
            break;
        case "usernameSelector":
            drawUsernameSelector();
            break;
        case "lobbySelector":
            drawLobbySelector();
            break;
    }
}

function drawMainMenu(){
    background(220);
    textSize(40);
    fill(0);
    text("Main Menu", 200, 125);
    textSize(15);
    fill(255);
    if (mouseX>125 && mouseX<275 && mouseY>170 && mouseY<195){
        fill(150);
    }
    rect(125, 170, 150, 25);
    fill(0);
    text("Create Lobby", 200, 187);
    fill(255);
    if (mouseX>125 && mouseX<275 && mouseY>230 && mouseY<255){
        fill(150);
    }
    rect(125, 230, 150, 25);
    fill(0);
    text("Join Lobby", 200, 247);
}

function drawRunningGame(){
    //console.log(players);
    let players = gameData.players;
    for (let playerName in players) {
        let player = JSON.parse(players[playerName]);
        textSize(22);
        text(playerName, player.x+16, player.y-5);
        image(playerImg, player.x, player.y);
    }
    textSize(30);
    text("Lobby ID: "+gameData.lobby, 200, 30);
    if (gameData.started != true){
        textSize(20);
        text("Time to start: "+gameData.timeToStart, 200, 350);
    }
}

function keyPressed() {
    switch (state){
        case "game":
            sendKeyPressed(key);
            break;
    }
}

function keyReleased() {
    switch (state){
        case "game":
            sendKeyReleased(key);
            break;
    }
}

function mouseClicked(){
    switch (state){
        case "mainMenu":
            mainMenuMouseClicked();
            break;
    }
}

function mainMenuMouseClicked(){
    if (mouseX>125 && mouseX<275 && mouseY>170 && mouseY<195){
        console.log("Create");
        //usernameSelector();
        sendCreateLobby();
    }
    if (mouseX>125 && mouseX<275 && mouseY>230 && mouseY<255){
        console.log("Join");
        //usernameSelector();
        lobbySelector();
    }
}
