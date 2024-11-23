window.onload = function() {
    const controlContainer = document.getElementById("controlContainer");

    if (controlContainer) {
        function moveAt(pageX, pageY, shiftX, shiftY) {
            controlContainer.style.left = (pageX - shiftX) + "px";
            controlContainer.style.top = (pageY - shiftY) + "px";
        }

        function onPointerMove(event) {
            moveAt(event.pageX, event.pageY, shiftX, shiftY);
        }

        let shiftX, shiftY;

        controlContainer.onpointerdown = function(event) {
            event.preventDefault();
            shiftX = event.clientX - controlContainer.getBoundingClientRect().left;
            shiftY = event.clientY - controlContainer.getBoundingClientRect().top;

            moveAt(event.pageX, event.pageY, shiftX, shiftY);
            document.addEventListener("pointermove", onPointerMove);

            document.onpointerup = function() {
                document.removeEventListener("pointermove", onPointerMove);
                document.onpointerup = null;
            };
        };

        controlContainer.ondragstart = function() {
            return false;
        };
    } else {
        console.error("Control container not found");
    }
};


function startGame() {
    
    window.socket.sendConnectConmmand();
    window.socket.sendStartCommand();
}
function pauseGame() {
    window.socket.sendPauseCommand();
}

function resumeGame() {
    window.socket.sendResumeCommand();
}


//window.socket.sendConnectConmmand();
