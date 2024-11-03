class GameRenderer {
    constructor(canvasId, cellSize) {
        this.canvas = document.getElementById(canvasId);
        this.context = this.canvas.getContext("2d");
        this.cellSize = cellSize; // розмір кожної клітинки
        this.zoom = 1; // масштабування
        this.offsetX = 0; // зсув по X для відображення камери
        this.offsetY = 0; // зсув по Y для відображення камери
    }

    // Метод для налаштування масштабу
    setZoom(zoom) {
        this.zoom = zoom;
    }

    // Метод для зміщення камери
    setCameraOffset(offsetX, offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    // Метод для малювання клітинки з певним кольором
    drawCell(x, y, color) {
        const scaledSize = this.cellSize * this.zoom;
        const screenX = (x - this.offsetX) * scaledSize;
        const screenY = (y - this.offsetY) * scaledSize;

        // Перевіряємо, чи клітинка видима на екрані
        if (
            screenX + scaledSize >= 0 &&
            screenY + scaledSize >= 0 &&
            screenX < this.canvas.width &&
            screenY < this.canvas.height
        ) {
            this.context.fillStyle = color;
            this.context.fillRect(screenX, screenY, scaledSize, scaledSize);
        }
    }

    // Метод для очищення полотна
    clearCanvas() {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }

    // Метод для малювання ігрового поля
    render(cells) {
        this.clearCanvas();

        for (const cell of cells) {
            const { x, y, color } = cell;
            this.drawCell(x, y, color);
        }
    }

    // Метод для малювання списку гравців
    drawPlayersList(players) {
        this.context.font = "16px Arial"; // стиль шрифту для тексту
        this.context.fillStyle = "black"; // колір тексту

        let x = this.canvas.width - 150; // положення по X для списку
        let y = 20; // початкова позиція по Y

        // Проходимо по списку гравців і малюємо їхні імена та довжину
        players.forEach(player => {
            const text = `${player.name} (${player.length})`;
            this.context.fillText(text, x, y);
            y += 20; // відступ між гравцями
        });
    }
}
