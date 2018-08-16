class Category {
    constructor() {
        this.init();
    }
    init() {
        this.categoryTemplate = `
            <li class="category-li" data-category-id="{id}">
                {title}
            </li>
            `;
        this.projectTemplate = `
            <li class="project-li" data-project-id="{id}">
                <div class="project-img">
                    <img src="{thumbnailUrl}"/>
                </div>
                <dl class="project-info-list">
                    <dt class="project-title">
                        {title}
                    </dt>
                    <dd class="project-owner">
                        {owner}
                    </dd>
                    <dd class="project-bar">
                        <progress value="{goalFundRaising}" max="{total}"></progress>
                    </dd>
                    <dd class="project-endAt">
                        {endAt}
                    </dd>
                </div>
            </li>
            `;
        getData("/api/categories", this.categoriesCallback.bind(this));
        $(".categories-ul").addEventListener("click", this.liClickHandler.bind(this));
    }
    fillCategoryContentHTML(category) {
        let template = this.categoryTemplate;
        template = template.replace(/{id}/g, category.id)
                            .replace(/{title}/g, category.title);
        return template;
    }
    insertCategoriesContentHTML(res) {
        let html = ``;
        res.forEach(category => {
            html += this.fillCategoryContentHTML(category);
        })
        $(".categories-ul").insertAdjacentHTML("beforeend", html);
    }
    categoriesCallback(response) {
        response.json().then(res => {
            this.insertCategoriesContentHTML(res);
        })
    }
    erasefirstChildHTML(parent) {
        if(parent.childElementCount === 0)
            return;
        parent.removeChild(parent.firstElementChild);
    }
    fillProjectContentHTML(project) {
        let template = this.projectTemplate;
        template = template.replace(/{id}/g, project.id)
                            .replace(/{thumbnailUrl}/g, project.thumbnailUrl)
                            .replace(/{title}/g, project.title)
                            .replace(/{owner}/g, project.owner.name)
                            .replace(/{endAt}/g, project.endAt)
                            .replace(/{goalFundRaising}/g, project.goalFundRaising)
                            .replace(/{total}/g, project.total);
        return template;
    }
    insertProjectsContentHTML(projects) {
        const parent = $(".category-project-list");
        let html = `<ul class="category-project-ul">`;
        projects.forEach(project => {
            html += this.fillProjectContentHTML(project);
        })
        html += `</ul>`;
        this.erasefirstChildHTML(parent);
        parent.insertAdjacentHTML("beforeend",html);
    }
    categoryProjectCallback(response) {
        response.json().then(res => {
            this.insertProjectsContentHTML(res);
        })
    }
    viewCategoryProject(target) {
        const cid = target.getAttribute("data-category-id");
        getData("/api/categories/"+cid+"/page/0", this.categoryProjectCallback.bind(this));
    }
    removeClassName(classname) {
        const on = $(".category-li.on");
        if(!on)
            return;
        on.classList.remove(classname);
    }
    addClassName(classname, target){
        target.classList.add(classname);
    }
    liClickHandler(evt) {
        evt.preventDefault();
        const target = evt.target;
        if(target.classList.contains("on"))
            return;
        if(!target.classList.contains("category-li"))
            return;
        this.viewCategoryProject(target);
        this.removeClassName("on");
        this.addClassName("on",target);
    }
}

window.addEventListener("DOMContentLoaded", () => {
    new Category();
})

function $(selector) {
    return document.querySelector(selector);
}

function fetchManager({ url, method, body, headers, callback }) {
  fetch(url, { method, body, headers, credentials: "same-origin" }).then(
    response => {
      callback(response);
    }
  );
}

function getData(url, callback) {
  fetchManager({
    url: url,
    method: "GET",
    headers: { "content-type": "application/json" },
    callback: callback
  });
}