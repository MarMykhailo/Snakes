class GameSocket {
    constructor(url) {
        this.socket = new WebSocket(url);

        this.socket.onopen = () => {
            console.log("WebSocket connection established");
        };

        this.socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            this.handleServerMessage(data);
        };

        this.socket.onclose = () => {
            console.log("WebSocket connection closed");
        };

        this.socket.onerror = (error) => {
            console.error("WebSocket error:", error);
        };
    }

    // Метод для надсилання повідомлення на сервер
    sendMoveCommand(direction) {
        const message = { type: "move", direction: direction };
        this.socket.send(JSON.stringify(message));
    }

    // Обробка повідомлень від сервера
    handleServerMessage(data) {
        if (data.type === "update") {
            // Обробка оновлення стану гри, наприклад, позицій змій
            console.log("Game update:", data.payload);
        } else if (data.type === "scoreUpdate") {
            // Обробка оновлення рахунку
            document.getElementById("score").textContent = `Score: ${data.payload.score}`;
        }
    }
}

// Ініціалізація WebSocket-з'єднання
const gameSocket = new GameSocket("ws://localhost:8080");

// Функція для обробки натиснень кнопок керування
function sendMove(direction) {
    gameSocket.sendMoveCommand(direction);
}
