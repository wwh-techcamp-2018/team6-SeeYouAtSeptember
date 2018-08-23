class ProjectForm {
    constructor() {
        this.productList = [];
        addEventListenerToTarget($("#create_project_btn"), "click", this.createProjectBtnHandler.bind(this));
        addEventListenerToTarget($(".projects_form.img input"), "change", this.insertImgFile.bind(this));
        addEventListenerToTarget($("#addProduct"), "click", this.addProductCreateFormHandler.bind(this));
        addEventListenerToTarget($(".products_addList"), "click", this.removeProductCreateFormHandler.bind(this));

        this.focusOutProjectsInfoTargetList = [
            $("#projects_title_input"),
            $("#projects_goalFundRaising_input"),
            $("#projects_endAt_input")
        ];

        this.focusOutProjectsInfoTargetList.forEach(target => {
            addEventListenerToTarget(target, "focusout", this.focusOutProjectInputHandler.bind(this));
        });
    }

    addProductCreateFormHandler() {
        if(this.productList.length > 4) return;
        const productTag = $('.products_addList');
        const html = ` <div class="product_addInfo">
                            <span>물품 이름:</span><input type="text" id="product_title_input"><br>
                            <span>물품 설명:</span><input type="text" id="product_description_input"><br>
                            <span>물품 가격:</span><input type="number" value="0" min="0" step="100" id="product_price_input"><br>
                            <span>물품 수량:</span><input type="number" value="10" min="10" step="1" id="product_supplyQuantity_input"><br>
                            <button id="removeProduct">물품 빼기</button>
                        </div> `
        productTag.insertAdjacentHTML('beforeend', html);
        this.productList.push(new Product(productTag.lastElementChild));
    }

    removeProductCreateFormHandler(evt) {
        const maybeRemoveProductBtn = evt.target;
        if (maybeRemoveProductBtn.id === "removeProduct") {
            for (let [i, product] of this.productList.entries()) {
                if (product.productTag === maybeRemoveProductBtn.parentElement) {
                    this.productList.splice(i, 1)
                    maybeRemoveProductBtn.parentElement.remove();
                    break;
                }
            }
        }
    }

    setProjectInfoAll() {
        this.projectInfoSettingFuncList = [
            this.setTitle.bind(this),
            this.setEndAt.bind(this),
            this.setGoalFundRaising.bind(this),
            this.setThumbnailUrl.bind(this)
        ];

        this.cnt = this.projectInfoSettingFuncList.length;
        this.projectInfoSettingFuncList.forEach((settingFunc,i) => {
            if (settingFunc()){ 
                this.cnt--;
            }
        });
        return this.cnt === 0;
    }

    setTitle() {
        const minTitleLength = 5;
        this.title = $("#projects_title_input").value;
        if(this.title.length >= minTitleLength){
            $("#project-title").style.visibility = "hidden";
            return true;
        }
        $("#project-title").style.visibility = "visible";
        return false;
    }

    setEndAt() {
        this.endAt = new Date($("#projects_endAt_input").value).getTime();
        let currentDate = new Date();
        currentDate.setDate(currentDate.getDate() + 30);
        if(this.endAt > currentDate.getTime()){
            $("#project-end").style.visibility = "hidden";
            return true;
        }
        $("#project-end").style.visibility = "visible";
        return false;
    }

    setGoalFundRaising() {
        const minGoalFundRaising = 1000000;
        this.goalFundRaising = $("#projects_goalFundRaising_input").value;
        console.log(this.goalFundRaising);
        
        if(this.goalFundRaising >= minGoalFundRaising){
            $("#project-goalFundRaising").style.visibility = "hidden";
            return true;
        }
        $("#project-goalFundRaising").style.visibility = "visible";
        return false;
    }

    setThumbnailUrl() {
        this.thumbnailUrl = $("#thumbnailUrl").src;
        if(this.thumbnailUrl !== ""){
            $("#project-img").style.visibility = "hidden";
            return true;
        }
        $("#project-img").style.visibility = "visible";
        return false;
    }

    focusOutProjectInputHandler(evt) {
        if (evt.target.id === "projects_title_input") this.setTitle();
        if (evt.target.id === "projects_goalFundRaising_input") this.setGoalFundRaising();
        if (evt.target.id === "projects_endAt_input") this.setEndAt();
    }

    insertImgFile(evt) {
        const maybeImg = evt.target.files[0];

        if (maybeImg === undefined) return;

        if (maybeImg["type"].split("/")[0] === "image") {
            fetchFormData(this.setFormData(maybeImg), "/api/projects/upload", this.imageUploadCallback.bind(this));
        }
    }

    setFormData(maybeImg) {
        const projectForm = new FormData();
        projectForm.append("file", maybeImg);
        if (this.thumbnailUrl !== undefined) {
            projectForm.append("previousFileUrl", this.thumbnailUrl);
            return projectForm;
        }
        projectForm.append("previousFileUrl", "");
        return projectForm;
    }

    imageUploadCallback(img) {
            $("#thumbnailUrl").src = img;
    }

    createProjectBtnHandler(evt) {
        evt.preventDefault();

        if (!this.setProjectInfoAll()) return;
        
        const products = [];
        for(const product of this.productList){
            let productInfo = product.setProductAll();
            if(productInfo === null){return;}
            products.push(productInfo);
        }
        
        const project = {
            "title": this.title,
            "description": editor.getHtml(),
            "goalFundRaising": this.goalFundRaising,
            "endAt": this.endAt,
            "thumbnailUrl": this.thumbnailUrl,
            "cid": $('.categories_dropbox select').value,
            "products": products
        };
        
        if(!this.checkMinProductsPrice(project["products"])){
            alert("상품 총 합의 가격이 목표금액보다 작습니다.");
            return;
        }

        fetchManager({
            url: '/api/projects',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(project),
            callback: this.createProjectCallback.bind(this)
        });
    }

    createProjectCallback(response) {
        if (response.status === 201) {
            location.href = "/categories"
        }
    }
    
    checkMinProductsPrice(products){
        let minPrice = 0;
        products.forEach(product=>{
            minPrice += product["price"]*product["quantitySupplied"]  
        })
        return minPrice >= this.goalFundRaising
    }

}

document.addEventListener("DOMContentLoaded", () => {
    new ProjectForm();
    editor = new tui.Editor({
        el: document.querySelector("#editSection"),
        initialEditType: "markdown",
        hooks: {
            'addImageBlobHook': insertEditorImg
        },
        height: "700px"
    })
});

