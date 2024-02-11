/*
//Functions
-parseMessage()
-sendMessage()
-sendKeyPressed()
-sendKeyReleased()
*/
function parseMessage(msg){
    //console.log(msg.data);
    let data = JSON.parse(msg.data);
    //console.log(data);
    switch (data.packetType){
        case "update":
            gameData = data;
            break;
        case "username":
            username = data.username;
            //console.log(data);
            break;
        case "joinLobby":
            console.log(data);
            joinLobby(data);
            break;
        case "usernameResponse":
            console.log(data);
            usernameServerResponded(data);
            break;
        case "lobbyJoinResponse":
            console.log(data);
            joinServerResponded(data);
            break;
    }
}

function sendMessage(msg){
    ws.send(msg);
}

function sendKeyReleased(key){
    data = {
        "packetType": "keyReleased",
        "key": key,
        "username": username
    }
    sendMessage(JSON.stringify(data))
}

function sendKeyPressed(key){
    data = {
        "packetType": "keyPressed",
        "key": key,
        "username": username
    }
    sendMessage(JSON.stringify(data))
}

