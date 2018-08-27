class CategoryManager {
    constructor() {
        this.categories = {};
        this.currentCid = "all";
        this.ulTag = $(".categories");
        getData("/api/categories", this.categoriesCallback.bind(this));
        getData("/api/categories/all/last/0", this.categoryProjectCallback.bind(this));
        addEventListenerToTarget(this.ulTag, "click", this.liClickHandler.bind(this));
        addEventListenerToTarget($("#more-project-btn"), "click", this.projectsMoreViewHandler.bind(this));
    }

    fillCategoryContentHTML(category) {
        let categoryTemplate = `<li data-category-id="{id}"><img src="{categoryImageUrl}"><p>{title}</p></li>`;
        if (category.id === 1) {
            category.id = "all";
        }
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
        <a href="/projects/{id}">
            <li data-project-id="{id}">
                <div class="project-img">
                    <img src="{thumbnailUrl}"/>
                </div>
                <div class="project-div">
                    <div class="title">
                    <h4>
                       {title}
                    </h4>
                    <span>
                        {owner}
                    </span>
                    <span class="current-fund-raising">
                        {currentFundRaising}원
                    </span>
                    </div >
                    <div class="progress">
                     <span style="width: {progressPercentage}%"></span>
                    </div>
                    <div>
                        <span class="progress-span">
                            {progress}%
                        </span>
                        <span class="day-remaining">
                            {dayRemaining}일 남음
                        </span>
                    </div>
                </div>
            </li>
        </a>
            `;
        return projectTemplate.replace(/{id}/g, project.pid)
            .replace(/{thumbnailUrl}/g, project.thumbnailUrl)
            .replace(/{title}/g, project.title)
            .replace(/{owner}/g, project.owner)
            .replace(/{currentFundRaising}/g, project.currentFundRaising.toLocaleString('ko-KR'))
            .replace(/{progressPercentage}/g, project.progress <= 100 ? project.progress : 100)
            .replace(/{progress}/g, project.progress.toLocaleString('ko-KR'))
            .replace(/{dayRemaining}/g, project.dayRemainingUntilDeadline);
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

function toggleStickyHeader() {
    if (window.pageYOffset > headerHeight) {
        addClassName("sticky", header);
        $("#category-content").style.marginTop = "208px";
    } else {
        removeClassName("sticky", header);
        $("#category-content").style.marginTop = "0px";
    }
}

function initStickyHeader() {
    header = $(".categories");
    headerHeight = header.offsetTop;
    window.addEventListener("scroll", toggleStickyHeader);
}

window.addEventListener("DOMContentLoaded", () => {
    new CategoryManager();
    initStickyHeader();

    const sock = new SockJS("/ws")
    const client = Stomp.over(sock);

    client.connect({}, function () {
        client.subscribe('/subscribe/project', function (project) {
            const projectBody = JSON.parse(project.body);
            console.log(projectBody.currentFundRaising);
            const updateProject = [...$("ul.projects").children].filter(project => project.firstElementChild.dataset.projectId === projectBody.pid.toString());
            console.log(updateProject);
            if(updateProject.length > 0){
                const currentFund = $at(updateProject[0],"span.current-fund-raising");
                const progressSpan = $at(updateProject[0],"span.progress-span");
                const progressBar = $at(updateProject[0],"div.progress span");
                updateProject[0].style.boxShadow = "0 0 20px";
                setTimeout(() => {
                    updateProject[0].style.boxShadow = "0 0 0px";
                }, 500);
                currentFund.style.transform = "rotateX(90deg)";
                progressSpan.style.transform = "rotateX(90deg)";
                setTimeout(() => {
                    currentFund.textContent = projectBody.currentFundRaising.toLocaleString('ko-KR') + "원";
                    progressSpan.textContent = projectBody.progress.toLocaleString('ko-KR') + "%";
                    currentFund.style.transform = "rotateX(0deg)";
                    progressSpan.style.transform = "rotateX(0deg)";
                }, 500)
                progressBar.classList.remove("transition");
                progressBar.style.width = "0%";
                setTimeout(()=>{
                    progressBar.classList.add("transition");
                    progressBar.style.width = (projectBody.progress > 100? 100 : projectBody.progress) + "%";
                }, 500);

            }
        });
    });
})
