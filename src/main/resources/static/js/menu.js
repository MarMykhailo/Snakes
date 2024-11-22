function createRoom()
{
    var roomName = document.getElementById("roomName").value;
    var roomType = document.getElementById("gameType").value;
    //var roomDescription = document.getElementById("roomDescription").value;

    var room = {
        name: roomName,
        /*password: roomPassword,
        capacity: roomCapacity,*/
        type: roomType
    };

    fetch('/create-room', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(room)
    }).then(response => {
        if(response.ok) {
            alert("Room created successfully");
            updateRooms();
        } else {
            alert("Error creating room");
        }
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

    cell1.innerHTML = room.type;
    cell2.innerHTML = room.name;
    cell3.innerHTML = '<button onclick="joinRoom(\'' + room.name + '\')">Долучитися</button>';
}

function joinRoom(roomName) {
    // Implement the logic to join the room
    alert("Joining room: " + roomName);
}


window.onload = function() {
}
updateRooms();