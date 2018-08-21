document.addEventListener("DOMContentLoaded", () => {
    Viewer = new tui.Editor({
        el: $('#viewer-section'),
        height: '300px',
        initialValue:$(".description-viewer").dataset.description
    });
});