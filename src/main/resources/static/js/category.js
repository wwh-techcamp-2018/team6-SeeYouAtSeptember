class CategoryManager {
    constructor() {
        this.categories = {};
        this.currentCid = 0;
        this.ulTag = $(".categories");
        getData("/api/categories", this.categoriesCallback.bind(this));
        getData("/api/categories/" + this.currentCid + "/last/0", this.categoryProjectCallback.bind(this));
        addEventListenerToTarget(this.ulTag, "click", this.liClickHandler.bind(this));
        addEventListenerToTarget($("#more_project_btn"), "click", this.projectsMoreViewHandler.bind(this));
    }

    fillCategoryContentHTML(category) {
        let categoryTemplate = `<li data-category-id="{id}"><img src="{categoryImageUrl}"><p>{title}</p></li>`;
        return categoryTemplate.replace(/{id}/g, category.id)
            .replace(/{categoryImageUrl}/g, category.categoryImageUrl)
            .replace(/{title}/g, category.title);
    }

    insertCategoriesContentHTML(res) {
        let html = ``;
        res.forEach(category => {
            html += this.fillCategoryContentHTML(category);
            this.categories[category.id] = null;
        })
        this.ulTag.insertAdjacentHTML("beforeend", html);
    }

    categoriesCallback(response) {
        response.json().then(res => {
            this.insertCategoriesContentHTML(res);
            addClassName("on", this.ulTag.firstElementChild);
        })
    }

    categoryProjectCallback(response) {
        response.json().then(projects => {
            this.addCategory(projects);
        })
    }

    addCategory(projects) {
        this.categories[this.currentCid] = new Category(projects, this.currentCid);
    }

    viewCategoryProject(target) {
        this.currentCid = target.dataset.categoryId;

        if (this.categories[this.currentCid] !== null) {
            eraseHTML($(".projects"));
            $("#category-content").insertAdjacentElement("beforeend", this.categories[this.currentCid].categoryChildProductsTag);
            return;
        }

        getData("/api/categories/" + this.currentCid + "/last/0", this.categoryProjectCallback.bind(this));
    }

    liClickHandler(evt) {
        evt.preventDefault();
        if (evt.target.tagName === 'UL') return;

        const target = evt.target.tagName === 'LI' ? evt.target : evt.target.parentElement;

        if (target.classList.contains("on")) return;
        this.viewCategoryProject(target);
        removeClassName("on", $('.on'));
        addClassName("on", target);
    }

    projectsMoreViewHandler(evt) {
        evt.preventDefault();

        this.categories[this.currentCid].projectsMoreViewApiManager(this.currentCid);
    }

}

class Category {
    constructor(projects) {
        this.insertProjectsContentHTML(projects);
    }

    fillProjectContentHTML(project) {
        let projectTemplate = `
        <a href="/products/{id}">
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
        </a>
            `;
        return projectTemplate.replace(/{id}/g, project.pid)
            .replace(/{thumbnailUrl}/g, project.thumbnailUrl)
            .replace(/{title}/g, project.title)
            .replace(/{owner}/g, project.owner)
            .replace(/{period}/g, project.period)
            .replace(/{fundraisingAmount}/g, project.fundraisingAmount);
    }

    insertProjectsContentHTML(projects) {
        let html = `<ul class="projects">`;
        projects.forEach(project => {
            html += this.fillProjectContentHTML(project);
        })
        html += `</ul>`;

        eraseHTML($(".projects"));
        $("#category-content").insertAdjacentHTML("beforeend", html);
        this.categoryChildProductsTag = $('.projects');
    }

    projectsMoreViewCallback(response) {
        response.json().then(projects => {
            const projectListUl = $(".projects");

            let html = ``;
            projects.forEach(project => {
                html += this.fillProjectContentHTML(project);
            })
            projectListUl.insertAdjacentHTML("beforeend", html);

            this.categoryChildProductsTag = $(".projects");
        })
    }

    projectsMoreViewApiManager(cid) {
        getData("/api/categories/" + cid + "/last/" + $('.projects').lastElementChild.firstElementChild.dataset.projectId, this.projectsMoreViewCallback.bind(this));
    }
}



window.addEventListener("DOMContentLoaded", () => {
    new CategoryManager();
})
