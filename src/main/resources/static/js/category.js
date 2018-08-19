class Category {
    constructor() {
        this.init();
    }

    init() {
        this.categoryTemplate = `
            <li data-category-id="{id}">
                <img src="{categoryImageUrl}">
                <p>{title}</p>
            </li>
         `;
        this.projectTemplate = `
            <li data-project-id="{id}">
                <div class="project-img">
                    <img src="{thumbnailUrl}"/>
                </div>
                <div class="project-div">
                    <div class="title">
                    <h4>
                       {title}
                    </h4>
                    <div>
                        {owner}
                    </div>
                    </div >
                    <div class="progress">
                     <span style="width: 25%"></span>
                    </div>
                    <span>
                        {fundraisingAmount}
                    </span>
                    <span class="remain_day">
                        {period}일 남음
                    </span>
                </div>
            </li>
            `;
        this.ulTag = $(".categories");
        getData("/api/categories", this.categoriesCallback.bind(this));
        getData("/api/categories/0/page/0", this.categoryProjectCallback.bind(this));
        this.ulTag.addEventListener("click", this.liClickHandler.bind(this));
    }

    fillCategoryContentHTML(category) {
        let template = this.categoryTemplate;
        template = template.replace(/{id}/g, category.id)
            .replace(/{categoryImageUrl}/g, category.categoryImageUrl)
            .replace(/{title}/g, category.title);
        return template;
    }

    insertCategoriesContentHTML(res) {
        let html = ``;
        res.forEach(category => {
            html += this.fillCategoryContentHTML(category);
        })
        this.ulTag.insertAdjacentHTML("beforeend", html);
    }

    categoriesCallback(response) {
        response.json().then(res => {
            this.insertCategoriesContentHTML(res);
            this.addClassName("on", this.ulTag.firstElementChild);
        })
    }

    eraseTargetdHTML(target) {
        if (!target) return;
        target.remove();
    }

    fillProjectContentHTML(project) {
        let template = this.projectTemplate;
        template = template.replace(/{id}/g, project.pid)
            .replace(/{thumbnailUrl}/g, project.thumbnailUrl)
            .replace(/{title}/g, project.title)
            .replace(/{owner}/g, project.owner)
            .replace(/{period}/g, project.period)
            .replace(/{fundraisingAmount}/g, project.fundraisingAmount);
        return template;
    }

    insertProjectsContentHTML(projects) {
        const parent = $("#category-content");
        let html = `<ul class="projects">`;
        projects.forEach(project => {
            html += this.fillProjectContentHTML(project);
        })
        html += `</ul>`;
        this.eraseTargetdHTML($(".projects"));
        parent.insertAdjacentHTML("beforeend", html);
    }

    categoryProjectCallback(response) {
        response.json().then(res => {
            this.insertProjectsContentHTML(res);
        })
    }

    viewCategoryProject(target) {
        const cid = target.getAttribute("data-category-id");
        getData("/api/categories/" + cid + "/page/0", this.categoryProjectCallback.bind(this));
    }

    removeClassName(classname) {
        const on = $(".on");
        if (!on) return;
        on.classList.remove(classname);
    }

    addClassName(classname, target) {
        target.classList.add(classname);
    }

    liClickHandler(evt) {
        evt.preventDefault();
        if (evt.target.tagName === 'UL') return;

        const target = evt.target.tagName === 'LI' ? evt.target : evt.target.parentElement;

        if (target.classList.contains("on")) return;
        this.viewCategoryProject(target);
        this.removeClassName("on");
        this.addClassName("on", target);
    }
}

window.addEventListener("DOMContentLoaded", () => {
    new Category();
})
