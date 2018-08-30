function $(selector) {
    return document.querySelector(selector);
}

function $at(target, selector) {
    return target.querySelector(selector);
}

function $all(selector) {
    return document.querySelectorAll(selector);
}

function isEmpty(value) {
    return value.length == 0
}

function fetchManager({url, method, body, headers, callback}) {
    fetch(url, {method, body, headers, credentials: "same-origin"})
        .then((response) => {
            callback(response)
        })
}

function getData(url, callback) {
    fetchManager({
        url: url,
        method: "GET",
        headers: {"content-type": "application/json"},
        callback: callback
    });
}

function addEventListenerToTarget(target, event, handler) {
    if (!target)
        return;
    target.addEventListener(event, handler)
}

function eraseHTML(target) {
    if (!target) return;
    target.remove();
}

function removeClassName(classname, target) {
    if (!target) return;
    target.classList.remove(classname);
}

function addClassName(classname, target) {
    if (!target) return;
    target.classList.add(classname);
}

function fetchFormData(formData, url, callback) {
    fetch(url, {method: "POST", body: formData})
        .then((response) => {
            response.text().then(img => {
                callback(img);
            })
        }).catch(() => {
        alert("잘못된 형식의 이미지입니다.")
    })
}

function insertEditorImg(blob, callback) {
    if (blob === undefined) {
        alert("잘못된 형식의 이미지입니다.")
        return;
    }

    if (blob["type"].split("/")[0] === "image") {
        const projectForm = new FormData();
        projectForm.append("file", blob);
        projectForm.append("previousFileUrl", "");
        fetchFormData(projectForm, "/api/projects/upload", callback)
        return;
    }
    alert("잘못된 형식의 이미지입니다.")
}

function successCallback(response) {
    if (response.status === 201) {
        window.location.reload();
        return;
    }
}

function moveScroll(scrollLeft, scrollTop) {
    document.documentElement.scrollLeft = document.body.scrollLeft = scrollLeft;
    document.documentElement.scrollTop = document.body.scrollTop = scrollTop;
}

function highlightBorderValid(target) {
    target.style.border = "1px solid #2ac1bc";
}

function highlightBorderInvalid(target) {
    target.style.border = "1px solid #E24141";
}

function highlightBackgroundInvalid(target) {
    target.style.background = "#E24141";
}