function rearrangePromotion() {
    let mainContainer = $("#main-container");
    let style = mainContainer.currentStyle || window.getComputedStyle(mainContainer);
    $(".promotion").style.marginLeft = -(screen.width - 960) / 2;
    $(".promotion").style.width = screen.width;
}

document.addEventListener("DOMContentLoaded", () => {
    rearrangePromotion();
});