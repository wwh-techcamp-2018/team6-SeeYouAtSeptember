class CategoryManager {
    constructor() {
        this.categories = {};
        this.currentCid = "all";
        this.ulTag = $(".categories");
        getData("/api/categories", this.categoriesCallback.bind(this));
        getData("/api/categories/all/last/0", this.categoryProjectCallback.bind(this));
        addEventListenerToTarget(this.ulTag, "click", this.liClickHandler.bind(this));
        addEventListenerToTarget($("#more-project-btn"), "click", this.projectsMoreViewHandler.bind(this));
        addEventListenerToTarget(window, "scroll", this.setCategoryScrollOffset.bind(this));
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
            this.projectsMoreViewToggle();
        })
    }

    addCategory(projects) {
        this.categories[this.currentCid] = new Category(projects);
    }

    viewCategoryProject(target) {
        this.currentCid = target.dataset.categoryId;
        if (this.categories[this.currentCid] === null) {
            getData("/api/categories/" + this.currentCid + "/last/0", this.categoryProjectCallback.bind(this));
            return;
        }
        eraseHTML($(".projects"));
        $("#category-content").insertAdjacentElement("beforeend", this.categories[this.currentCid].categoryChildProductsTag);
        this.projectsMoreViewToggle();
        moveScroll(this.categories[this.currentCid].scrollLeft, this.categories[this.currentCid].scrollTop);
    }

    liClickHandler(evt) {
        evt.preventDefault();
        if (evt.target.tagName === 'UL') return;

        /* depth가 깊어지면 잠재적으로 parentElement에서 에러를 만들어 낼 수 있습니다. */
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

    projectsMoreViewToggle() {
        let button = $("#more-project-btn");
        if (this.categories[this.currentCid].no_more_data) {
            button.disabled = true;
            button.style.background = "#17aba6";
            button.style.cursor = "initial";
            button.textContent = "끝!";
            return;
        }
        button.disabled = false;
        button.removeAttribute("style");
        button.textContent = "더보기";
    }

    setCategoryScrollOffset() {
        if (this.categories[this.currentCid] !== null) {
            this.categories[this.currentCid].setScrollOffset();
        }
    }
}

class Category {
    constructor(projects) {
        this.insertProjectsContentHTML(projects);
        this.no_more_data = false;
        this.setScrollOffset();
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
                    </br>
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
                            <i class="far fa-calendar-alt"></i>
                            {dayRemaining}일 남음
                        </span>
                    </div>
                </div>
            </li>
        </a>
            `;
        return projectTemplate.replace(/{id}/g, project.pid)
            .replace(/{thumbnailUrl}/g, project.thumbnailUrl)
            .replace(/{title}/g, project.title.replace(/</g, "&lt;").replace(/>/g, "&gt;"))
            .replace(/{owner}/g, project.owner.replace(/</g, "&lt;").replace(/>/g, "&gt;"))
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
            if (projects.length === 0) {
                this.no_more_data = true;
                categoryManager.projectsMoreViewToggle();
            }
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

    setScrollOffset() {
        this.scrollLeft = window.pageXOffset || document.documentElement.scrollLeft;
        this.scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    }
}

function toggleStickyHeader() {
    if (window.pageYOffset > headerHeight) {
        addClassName("sticky", header);
        $("#category-content").style.paddingTop = "155px";
        $(".sticky-scissors").style.left = header.offsetLeft - 35;
        $(".sticky-scissors").style.display = "inline";
    } else {
        removeClassName("sticky", header);
        $("#category-content").style.paddingTop = "0px";
        $(".sticky-scissors").style.display = "none";
    }
}

function rearrangeStickyScissors() {
    $(".sticky-scissors").style.left = header.offsetLeft - 35;
}

function initStickyHeader() {
    header = $(".categories");
    headerHeight = header.offsetTop;
    window.addEventListener("scroll", toggleStickyHeader);
    window.addEventListener("resize", rearrangeStickyScissors);
}

class CategoryWebSocket {
    constructor() {
        const sock = new SockJS("/ws")
        const client = Stomp.over(sock);

        client.connect({}, function () {
            client.subscribe('/subscribe/project', this.updateProjectCallback.bind(this));
        }.bind(this));
    }

    updateProjectCallback(project) {
        const projectBody = JSON.parse(project.body);
        const updateProject = [...$("ul.projects").children].filter(project => project.firstElementChild.dataset.projectId === projectBody.pid.toString());
        if (updateProject.length > 0) {
            const currentFund = $at(updateProject[0], "span.current-fund-raising");
            const progressSpan = $at(updateProject[0], "span.progress-span");
            const progressBar = $at(updateProject[0], "div.progress span");
            this.highlightTarget(updateProject[0])
            this.rotateAndUpdateTarget(currentFund, projectBody.currentFundRaising.toLocaleString('ko-KR') + "원");
            this.rotateAndUpdateTarget(progressSpan, projectBody.progress.toLocaleString('ko-KR') + "%");
            this.refillProgressBar(progressBar, projectBody.progress);
        }
    }

    highlightTarget(target) {
        target.style.boxShadow = "0 0 20px";
        setTimeout(() => {
            target.style.boxShadow = "0 0 0px";
        }, 500);
    }

    rotateAndUpdateTarget(target, updateValue) {
        target.style.transform = "rotateX(90deg)";
        setTimeout(() => {
            target.textContent = updateValue;
            target.style.transform = "rotateX(0deg)";
        }, 500)
    }

    refillProgressBar(progress, progressValue) {
        progress.classList.remove("transition");
        progress.style.width = "0%";
        setTimeout(() => {
            progress.classList.add("transition");
            progress.style.width = (progressValue > 100 ? 100 : progressValue) + "%";
        }, 500);
    }
}

window.addEventListener("DOMContentLoaded", () => {
    categoryManager = new CategoryManager();
    initStickyHeader();
    new CategoryWebSocket();
})
