document.addEventListener("DOMContentLoaded", () => {
    addEventListenerToTarget($("#my-project-btn"), "click", myProjectClickHandler)
});

function myProjectClickHandler(evt) {
    evt.preventDefault();
    const dropdownDiv = $("div.dropdown-div");
    const dropdownUl = [...$all("div.dropdown-div ul.dropdown-ul")];
    if (dropdownDiv.classList.contains("on")) {
        [...dropdownDiv.children].forEach(child => {
            closeDropdown(child);
        });
        dropdownDiv.classList.remove("on");
        return;
    }
    [...dropdownDiv.children].forEach(child => {
        openDropdown(child);
    });
    dropdownDiv.classList.add("on");
}

function openDropdown(target) {
    target.style.visibility = "visible";
    target.style.opacity = "1";
    target.style.zIndex = "1";
    target.style.height = "200px";
    [...target.children[1].children].forEach(child => {
        child.style.height = "50px"
    });
    $("i.fas").classList.remove("fa-caret-down")
    $("i.fas").classList.add("fa-caret-up")
}

function closeDropdown(target) {
    target.style.visibility = "hidden";
    target.style.opacity = "0";
    target.style.zIndex = "0";
    target.style.height = "0px";
    [...target.children[1].children].forEach(child => {
        child.style.height = "0px"
    });
    $("i.fas").classList.remove("fa-caret-up")
    $("i.fas").classList.add("fa-caret-down")
}