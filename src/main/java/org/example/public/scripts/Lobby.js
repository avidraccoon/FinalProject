/*
//Functions
-drawLobbySelector()
-checkLobby()
-lobbySelector()
-joinServerResponded()
-sendCreateLobby()
-sendJoinLobby()
*/

function drawLobbySelector(){
    background(220);
    textSize(40);
    fill(0);
    text("Lobby Selector", 200, 125);
    textSize(25);
    if (lobbyJoinRejected){
        fill(255, 0, 0);
        text("Lobby rejected try again", 200, 300);
    }
}

function checkLobby(){
    if (lobbyJoinResponse.accepted){
        state = "game";
        if (lobbyButton != null){
            lobbyInput.remove();
            lobbyInput = null;
            lobbyButton.remove();
            lobbyButton = null;
        }
    }else{
        lobbyJoinRejected = true;
    }
    usernameResponse = null;
}

function lobbySelector(){
    state = "lobbySelector";
    lobbyInput = createInput();
    lobbyInput.position(125, 170);
    lobbyInput.size(150, 25);
    lobbyButton = createButton('Join');
    lobbyButton.position(160, 230);
    lobbyButton.size(80, 25);
    lobbyButton.mousePressed(() => {
        sendJoinLobby(lobbyInput.value());
    });
}

function joinServerResponded(data){
    lobbyJoinResponse = data;
    checkLobby();
}

function sendCreateLobby(){
    
    data = {
        "packetType": "createLobby",
        "username": username,
    }
    sendMessage(JSON.stringify(data));
}

function sendJoinLobby(lobby){
    data = {
        "packetType": "joinLobby",
        "username": username,
        "lobby": lobby
    }
    sendMessage(JSON.stringify(data));
}