/*
//Functions
-drawUsernamSelector()
-checkUsername()
-usernameSelector()
-usernameServerResponded()
-sendUsername()
*/


function drawUsernameSelector(){
    background(220);
    textSize(40);
    fill(0);
    text("User Selector", 200, 125);
    textSize(25);
    if (usernameRejected){
        fill(255, 0, 0);
        text("Username rejected try again", 50, 300);
    }
}

function checkUsername(){
    if (usernameResponse.accepted){
            usernameInput.remove();
            usernameInput = null;
            usernameButton.remove();
            usernameButton = null;
        setTimeout(() => {
            state = "mainMenu";
        }, 250);
    }else{
        usernameRejected = true;
    }
    usernameResponse = null;
}

function usernameSelector(){
    state = "usernameSelector";
    usernameInput = createInput();
    usernameInput.position(125, 170);
    usernameInput.size(150, 25);
    usernameButton = createButton('Submit');
    usernameButton.position(160, 230);
    usernameButton.size(80, 25);
    usernameButton.mousePressed(() => {
        sendUsername(usernameInput.value());
    });
}

function usernameServerResponded(data){
    usernameResponse = data;
    checkUsername();
}

function sendUsername(newName){
    data = {
        "packetType": "usernameChange",
        "username": username,
        "newUsername": newName
    }
    sendMessage(JSON.stringify(data));
}