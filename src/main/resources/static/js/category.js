class CategoryManager {
    constructor() {
        this.categoryList = {};
        this.cid = 0;
        this.lastPid = 0;
        this.ulTag = $(".categories");
        getData("/api/categories", this.categoriesCallback.bind(this));
        getData("/api/categories/0/page/0", this.categoryProjectCallback.bind(this));
        this.ulTag.addEventListener("click", this.liClickHandler.bind(this));
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
            this.categoryList[category.id] = ''; 
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
        if(!target) return;
        target.remove();
    }
    fillProjectContentHTML(project) {
        let projectTemplate = `
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
        return projectTemplate.replace(/{id}/g, project.pid)
                              .replace(/{thumbnailUrl}/g, project.thumbnailUrl)
                              .replace(/{title}/g, project.title)
                              .replace(/{owner}/g, project.owner)
                              .replace(/{period}/g, project.period)
                              .replace(/{fundraisingAmount}/g, project.fundraisingAmount);

    }
    insertProjectsContentHTML(projects) {
        const parent = $(".content");
        
        let html = `<ul class="projects">`;
        projects.forEach(project => {
            html += this.fillProjectContentHTML(project);
        })
        this.lastPid = projects[projects.length - 1].pid;
        html += `</ul>`;

        this.eraseTargetdHTML($(".projects"));
        parent.insertAdjacentHTML("beforeend",html);
        this.categoryList[this.cid] = $(".projects");
    }
    categoryProjectCallback(response) {
        response.json().then(res => {
            this.insertProjectsContentHTML(res);
        })
    }
    viewCategoryProject(target) {
        this.cid = target.getAttribute("data-category-id");
        
        if(this.categoryList[this.cid] !== ''){
            this.eraseTargetdHTML($(".projects"));
            $(".content").insertAdjacentElement("beforeend",this.categoryList[this.cid]);
            return;
        }

        getData("/api/categories/"+this.cid+"/last/"+this.lastPid, this.categoryProjectCallback.bind(this));
    }
    removeClassName(classname) {
        const on = $(".on");
        if(!on) return;
        on.classList.remove(classname);
    }
    addClassName(classname, target){
        target.classList.add(classname);
    }
    liClickHandler(evt) {
        evt.preventDefault();
        if(evt.target.tagName === 'UL' ) return;

        const target = evt.target.tagName === 'LI' ? evt.target : evt.target.parentElement;

        if(target.classList.contains("on")) return;
        this.viewCategoryProject(target);
        this.removeClassName("on");
        this.addClassName("on",target);
    }
}



window.addEventListener("DOMContentLoaded", () => {
   test = new CategoryManager();
})
