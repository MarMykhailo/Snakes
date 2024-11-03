//що має робитися на фронтенді
//гравіці мають натискати на клавіші і передавати їх на сервер
//сервер це обробляє і відправляє на клієнта

//такс трбе клас snake і клас player
//клас game відповідає за відмальовку ігри
//треба ще клас для сокетів які будуть відправляти і приймати дані


window.onload = function() {
    const controlContainer = document.getElementById("controlContainer");

    if (controlContainer) {
        controlContainer.onmousedown = function(event) {
            event.preventDefault();
            let shiftX = event.clientX - controlContainer.getBoundingClientRect().left;
            let shiftY = event.clientY - controlContainer.getBoundingClientRect().top;

            function moveAt(pageX, pageY) {
                controlContainer.style.left = pageX - shiftX + "px";
                controlContainer.style.top = pageY - shiftY + "px";
            }

            function onMouseMove(event) {
                moveAt(event.pageX, event.pageY);
            }

            document.addEventListener("mousemove", onMouseMove);

            document.onmouseup = function() {
                document.removeEventListener("mousemove", onMouseMove);
                document.onmouseup = null;
            };
        };

        controlContainer.ondragstart = function() {
            return false;
        };
    } else {
        console.error("Control container not found");
    }
};
