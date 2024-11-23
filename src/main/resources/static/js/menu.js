function createRoom() {
    var roomName = document.getElementById("roomName").value;
    var roomType = document.getElementById("gameType").value;
    fetch('/create-room', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: roomName, type: roomType })
    }).then(response => {
        if (response.ok) {
            //alert("Room created successfully");
            updateRooms();
        } else {
            //alert("Error creating room");
        }
    }).catch(error => {
        console.error('Error:', error);
        alert("Error creating room");
    });
}



function updateRooms() {
    fetch('/rooms', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            response.json().then(data => {
                var rooms = data.body;
                var table = document.querySelector(".roomTable");
                // Clear all rows except the first one (header)
                while (table.rows.length > 1) {
                    table.deleteRow(1);
                }
                rooms.forEach(room => {
                    addRoomToTable(room);
                });
            });
        } else {
            alert("Error getting rooms");
        }
    });
}

function addRoomToTable(room) {
    var table = document.querySelector(".roomTable");
    var row = table.insertRow();
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);

    //cell1.innerHTML = room.type;
    cell1.innerHTML = "Змії";
    cell2.innerHTML = room.name;
    cell3.innerHTML =
    '<button onclick="joinRoom(\'' + room.id + '\')">' +
    '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-door-closed" viewBox="0 0 16 16">' +
        '<path d="M3 2a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1v13h1.5a.5.5 0 0 1 0 1h-13a.5.5 0 0 1 0-1H3zm1 13h8V2H4z"/>' +
        '<path d="M9 9a1 1 0 1 0 2 0 1 1 0 0 0-2 0"/>' +
    '</svg>' +
    '</button>' +
    '<button onclick="deleteRoom(\'' + room.id + '\')">' +
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash3" viewBox="0 0 16 16">' +
            '<path d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>' +
        '</svg>' +
    '</button>';
}

function deleteRoom(id) {
    fetch(`/delete-room/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            //alert("Room deleted successfully");
            updateRooms();
        } else {
            //alert("Error deleting room");
        }
    }).catch(error => {
        console.error('Error:', error);
        //alert("Error deleting room");
    });
}

function joinRoom(id) {
    var nickname = document.getElementById("nickname").value;

    fetch(`/join-room/${nickname}/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            localStorage.setItem('nickname', nickname);
            window.socket.sendConnectConmmand(id);
            window.location.href = "/snakes";
        } else {
            response.text().then(text => {
                console.error('Error joining room:', text);
                alert("Error joining room");
            });
        }
    }).catch(error => {
        console.error('Error:', error);
        alert("Error joining room");
    });
}

window.onload = function() {
}
updateRooms();