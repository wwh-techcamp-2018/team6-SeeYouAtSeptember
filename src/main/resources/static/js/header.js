document.addEventListener("DOMContentLoaded", () => {
    addEventListenerToTarget($("#my-project-btn"), "click", myProjectClickHandler);
    addEventListenerToTarget($("#logout"), "click", logoutHandler);
});

function myProjectClickHandler(evt) {
    evt.preventDefault();
    const dropdownDiv = $("div.dropdown-div");
    if (dropdownDiv.classList.contains("on")) {
        dropdownDiv.style.opacity = "0";
        dropdownDiv.style.height = "0px";
        [...dropdownDiv.children].forEach(child => {
            closeDropdown(child);
        });
        dropdownDiv.classList.remove("on");
        return;
    }
    getData("/api/projects/dropdown", myProjectOpenCallback.bind(dropdownDiv));

}

function myProjectOpenCallback(response) {
    if (response.status === 200) {
        response.json().then(projectsList => {
            const targets = [$(".dropdown-div .dropdown-owner"), $(".dropdown-div .dropdown-support")];
            deleteMyProjects(targets);
            addMyProjects(projectsList, targets);
            this.style.opacity = "1";
            this.style.height = "200px";
            [...this.children].forEach(child => {
                openDropdown(child);
            });
            this.classList.add("on");
        })
    }
}

function deleteMyProjects(targets) {
    targets.forEach(target => {
        [...target.children].forEach(child => {
            target.removeChild(child);
        })
    })
}

function addMyProjects(projectsList, targets) {
    projectsList.forEach((projects, index) => {
        projects.forEach(project => {
            targets[index].insertAdjacentHTML('afterbegin', getDropdownProjectHTML(project))
        })
    })
}

function getDropdownProjectHTML(project) {
    return `
        <li class="dropdown-item card">
        <a href="/projects/${project.pid}">
            <div class="progress" style="width:${project.progress}%;"></div>
            <div class="content">
                <span class="project-title">${project.title}</span>
                <span class="project-info"><span>${project.dayRemainingUntilDeadline}일 남음</span><span style="float:right">${project.progress}%</span></span>
            </div>
            </a>
        </li>
    `
}

function openDropdown(target) {
    target.style.visibility = "visible";
    target.style.opacity = "1";
    target.style.zIndex = "1";
    target.style.height = "200px";
    [...target.children[1].children].forEach(child => {
        child.style.height = "40px"
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

function logoutHandler() {
    fetchManager({
        url: "/api/users/logout",
        method: "POST",
        headers: { "content-type": "application/json" },
        callback: response => { if (response.status === 200) window.location.reload(); }
    });
}
