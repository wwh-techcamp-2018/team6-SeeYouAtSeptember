function rearrangePromotion() {
    let mainContainer = $("#main-container");
    let style = mainContainer.currentStyle || window.getComputedStyle(mainContainer);
    $(".promotion").style.marginLeft = -parseInt(style.marginLeft);
}

document.addEventListener("DOMContentLoaded", () => {
    rearrangePromotion();
});