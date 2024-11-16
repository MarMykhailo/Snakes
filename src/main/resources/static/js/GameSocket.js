class GameSocket {
    constructor() {
        const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const wsUrl = `${wsProtocol}//192.168.0.103:8080/snake-websocket`;
        this.connect(wsUrl);
    }

    connect(wsUrl) {
        this.socket = new WebSocket(wsUrl);

        this.socket.onopen = () => {
            console.log("WebSocket connection established");
            this.sendMessage({
                type: "connect",
                message: "Hello server!",
                timestamp: Date.now()
            });
        };

        this.socket.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data);
                console.log("Received message:", data);
                this.handleServerMessage(data);
            } catch (e) {
                console.error("Error parsing message:", e);
                console.log("Raw message:", event.data);
            }
        };

        this.socket.onclose = (event) => {
            console.log("WebSocket connection closed", event);
            setTimeout(() => this.connect(wsUrl), 5000);
        };

        this.socket.onerror = (error) => {
            console.error("WebSocket error:", error);
        };
    }

    sendMessage(message) {
        if (this.socket.readyState === WebSocket.OPEN) {
            const messageWithTimestamp = {
                ...message,
                timestamp: Date.now()
            };
            this.socket.send(JSON.stringify(messageWithTimestamp));
        } else {
            console.warn("WebSocket is not open. Message not sent:", message);
        }
    }

    sendMoveCommand(direction) {
        this.sendMessage({
            type: "game",
            action: "changeDirection",
            direction: direction
        });
    }

    handleServerMessage(data) {
        switch (data.type) {
            case "connected":
                console.log("Connected to server:", data.message);
                break;
            case "welcome":
                console.log("Welcome message:", data.message);
                break;
            case "moveConfirmed":
                console.log("Move confirmed:", data.direction);
                break;
            case "update":
                this.handleGameUpdate(data);
                break;
            case "scoreUpdate":
                this.updateScore(data.payload);
                break;
            case "error":
                console.error("Server error:", data.message);
                break;
            default:
                console.warn("Unknown message type:", data.type);
        }
    }

    handleGameUpdate(payload) {
        console.log("Game update received:", payload);
        //window.
        renderer.render(payload.cells);
    }

    updateScore(payload) {
        const scoreElement = document.getElementById("score");
        if (scoreElement && payload.score !== undefined) {
            scoreElement.textContent = `Score: ${payload.score}`;
        }
    }
}

// Створюємо єдиний екземпляр GameSocket
const gameSocket = new GameSocket();

// Функція для обробки натиснень кнопок керування
function sendMove(direction) {
    gameSocket.sendMoveCommand(direction);
}

// Експортуємо gameSocket для використання в інших файлах
window.gameSocket = gameSocket;
/*
// Ініціалізація WebSocket-з'єднання
const gameSocket = new GameSocket("ws://192.168.0.103:8080/snake-websocket");

// Функція для обробки натиснень кнопок керування
function sendMove(direction) {
    gameSocket.sendMoveCommand(direction);
}*/



