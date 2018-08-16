document.addEventListener("DOMContentLoaded", () => {
   $("#my-project-btn").addEventListener("click", myProjectClickHandler)
});

function myProjectClickHandler(evt) {
    evt.preventDefault();
    const dropdownDiv = $("div.dropdown-div");
    if(dropdownDiv.classList.contains("on")){
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
    target.style.opacity="1";
    target.style.zIndex = "1";
    [...target.children].forEach(child => {
        child.style.height = "50px"
    });
    $("i.fas").classList.remove("fa-caret-down")
    $("i.fas").classList.add("fa-caret-up")
}

function closeDropdown(target) {
    target.style.visibility = "hidden";
    target.style.opacity="0";
    target.style.zIndex = "0";
    [...target.children].forEach(child => {
        child.style.height = "0px"
    });
    $("i.fas").classList.remove("fa-caret-up")
    $("i.fas").classList.add("fa-caret-down")
}