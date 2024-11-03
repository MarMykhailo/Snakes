var id = localStorage.getItem('userId');
var token = localStorage.getItem('sessionToken');
document.addEventListener('DOMContentLoaded', function() {
    var sidebar = document.querySelector('.sidebar');
    var startX, endX;

    document.body.addEventListener('touchstart', function(event) {
        startX = event.touches[0].clientX;
    });

    document.body.addEventListener('touchend', function(event) {
        endX = event.changedTouches[0].clientX;
        var screenWidth = window.innerWidth;
        var menuWidth = sidebar.offsetWidth;
        var swipeDistance = endX - startX;

        // Визначаємо мінімальну довжину свайпа та відхилення
        var minSwipeDistance = 50;
        var maxMenuOffset = 50; // Відхилення для відкриття/закриття меню

        // Якщо відстань свайпу більше minSwipeDistance і відбувається з відповідним відхиленням, викликаємо функцію toggleSidebar
        if (Math.abs(swipeDistance) > minSwipeDistance) {
            if (swipeDistance > 0 && startX < maxMenuOffset) { // Відкриття меню
                openSidebar();
            } else if (swipeDistance < 0 && endX < menuWidth + maxMenuOffset) { // Закриття меню
                closeSidebar();
            }
        }
    });
});

function openSidebar() {
    var sidebar = document.querySelector('.sidebar');
    sidebar.classList.add('open');
}

function closeSidebar() {
    var sidebar = document.querySelector('.sidebar');
    sidebar.classList.remove('open');
}

function toggleSidebar() {
    var sidebar = document.querySelector('.sidebar');
    sidebar.classList.toggle('open');
}


