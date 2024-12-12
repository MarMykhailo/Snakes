class GameSocket {
    constructor() {
        const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const wsUrl = `${wsProtocol}//${window.location.hostname}:${window.location.port}/snake-websocket`;
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
            roomId: localStorage.roomId,
            player: localStorage.nickname,
            roomId: localStorage.roomId,
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

    sendConnectConmmand() {
        console.log("connection....");
        console.log("localStorage.nickname: ", localStorage.nickname);
        this.sendMessage({
            type: "room",
            action: "attribution",
            name: localStorage.nickname,
            roomId: localStorage.roomId
        });
    }


    handleGameUpdate(payload) {
        console.log("Game update received:", payload);
        renderer.render(payload.cells);
    }

    updateScore(payload) {
        const scoreElement = document.getElementById("score");
        if (scoreElement && payload.score !== undefined) {
            scoreElement.textContent = `Score: ${payload.score}`;
        }
    }

    sendStartCommand() {
        this.sendMessage({
            type: "game",
            roomId: localStorage.roomId,
            player: localStorage.getItem('nickname'),
            action: "changeState",
            state: "start"
        });
    }

    sendPauseCommand() {
        this.sendMessage({
            type: "game",
            roomId: localStorage.roomId,
            action: "changeState",
            state: "pause"
        });
    }

    sendResumeCommand() {
        this.sendMessage({
            type: "game",
            roomId: localStorage.roomId,
            action: "changeState",
            state: "resume"
        });
    }
}

// Створюємо єдиний екземпляр GameSocket
const socket = new GameSocket();

// Функція для обробки натиснень кнопок керування
function sendMove(direction) {
    socket.sendMoveCommand(direction);
}

// Експортуємо socket для використання в інших файлах
window.socket = socket;
/*
// Ініціалізація WebSocket-з'єднання
const socket = new GameSocket("ws://192.168.0.103:8080/snake-websocket");

// Функція для обробки натиснень кнопок керування
function sendMove(direction) {
    socket.sendMoveCommand(direction);
}*/



