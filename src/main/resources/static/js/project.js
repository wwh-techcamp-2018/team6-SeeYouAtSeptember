class ProjectForm {
    constructor() {
        this.productList = [];
        this.cropper = null;
        this.fileInput = $("#file-input");
        this.modal = $("#myModal");
        addEventListenerToTarget($("#create-project-btn"), "click", this.createProjectBtnHandler.bind(this));
        addEventListenerToTarget($("#addProduct"), "click", this.addProductCreateFormHandler.bind(this));
        addEventListenerToTarget($(".products-addList"), "click", this.removeProductCreateFormHandler.bind(this));
        addEventListenerToTarget($("#crop-btn"), "click", this.cropButtonHandler.bind(this));
        addEventListenerToTarget($("#thumbnailUrl"), "click", this.uploadFileHandler.bind(this));
        $all(".close").forEach(closeObject => closeObject.addEventListener("click", this.closeButtonHandler.bind(this)));
        addEventListenerToTarget(this.fileInput, "click", this.fileClickHandler.bind(this));
        addEventListenerToTarget(this.fileInput, "change", this.fileChangeHandler.bind(this));

        this.focusOutProjectsInfoTargetList = [
            $("#projects-title-input"),
            $("#projects-goalFundRaising-input"),
            $("#projects-endAt-input")
        ];

        this.focusOutProjectsInfoTargetList.forEach(target => {
            addEventListenerToTarget(target, "focusout", this.focusOutProjectInputHandler.bind(this));
        });
    }

    uploadFileHandler() {
        this.fileInput.click();
    }

    fileClickHandler(evt) {
        evt.target.value = null;
    }

    fileChangeHandler(input) {
        this.modal.style.display = "block";
        var el = $(".modal-body");
        if (el.classList.contains("croppie-container")) {
            this.cropper.bind({url: window.URL.createObjectURL(input.target.files[0])});
            return;
        }
        this.cropper = new Croppie(el, {
            viewport: {width: 288, height: 288},
            boundary: {width: 500, height: 500},
            showZoomer: true,
            enableOrientation: true
        });
        this.cropper.bind({url: window.URL.createObjectURL(input.target.files[0])});
    }

    closeButtonHandler(evt) {
        this.fileInput.value = "";
        const result = confirm("사진 크기를 조정해야 사용할 수 있습니다.\n창을 닫겠습니까?");
        if (result) {
            this.modal.style.display = "none";
        } else {
            evt.target.blur();
        }
    }

    cropButtonHandler() {
        this.cropper.result('blob').then(this.insertImgFile.bind(this));
    }

    addProductCreateFormHandler() {
        if (this.productList.length > 4 || this.productList.length < 1) return;
        const productTag = $('.products-addList');
        const html = ` <div class="product-addInfo">
                            <span>물품 이름:</span><input type="text" id="product-title-input"><br>
                            <span>물품 설명:</span><input type="text" id="product-description-input"><br>
                            <span>물품 가격:</span><input type="number" value="0" min="0" step="100" id="product-price-input"><br>
                            <span>물품 수량:</span><input type="number" value="10" min="10" step="1" id="product-supplyQuantity-input"><br>
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
        this.projectInfoSettingFuncList.forEach((settingFunc, i) => {
            if (settingFunc()) {
                this.cnt--;
            }
        });
        return this.cnt === 0;
    }

    setTitle() {
        const minTitleLength = 1;
        const maxTitleLength = 50;
        this.title = $("#projects-title-input").value;
        if (this.title.length >= minTitleLength && this.title.length <= maxTitleLength) {
            $("#project-title").style.visibility = "hidden";
            return true;
        }
        $("#project-title").style.visibility = "visible";
        return false;
    }

    setEndAt() {
        this.endAt = new Date($("#projects-endAt-input").value).getTime();
        let minCurrentDate = new Date();
        minCurrentDate.setDate(minCurrentDate.getDate() + 30);
        let maxCurrentDate = new Date();
        maxCurrentDate.setDate(maxCurrentDate.getDate() + 180);
        if (this.endAt >= minCurrentDate.getTime() && this.endAt <= maxCurrentDate.getTime()) {
            $("#project-end").style.visibility = "hidden";
            return true;
        }
        $("#project-end").style.visibility = "visible";
        return false;
    }

    setGoalFundRaising() {
        const minGoalFundRaising = 1000000;
        const maxGoalFundRaising = 1000000000;
        this.goalFundRaising = $("#projects-goalFundRaising-input").value;

        if (this.goalFundRaising >= minGoalFundRaising && this.goalFundRaising <= maxGoalFundRaising) {
            $("#project-goalFundRaising").style.visibility = "hidden";
            return true;
        }
        $("#project-goalFundRaising").style.visibility = "visible";
        return false;
    }

    setThumbnailUrl() {
        this.thumbnailUrl = $("#thumbnailUrl").src;
        if (this.thumbnailUrl !== "") {
            $("#project-img").style.visibility = "hidden";
            return true;
        }
        $("#project-img").style.visibility = "visible";
        return false;
    }

    focusOutProjectInputHandler(evt) {
        if (evt.target.id === "projects-title-input") this.setTitle();
        if (evt.target.id === "projects-goalFundRaising-input") this.setGoalFundRaising();
        if (evt.target.id === "projects-endAt-input") this.setEndAt();
    }

    insertImgFile(blob) {

        if (blob === undefined) return;

        if (blob["type"].split("/")[0] === "image") {
            var file = new File([blob], window.URL.createObjectURL(blob), {
                type: blob["type"],
                lastModified: Date.now()
            });
            fetchFormData(this.setFormData(file), "/api/projects/upload", this.imageUploadCallback.bind(this));
        }
        this.modal.style.display = "none";
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
        this.thumbnailUrl = $("#thumbnailUrl").src;
    }

    createProjectBtnHandler(evt) {
        evt.preventDefault();

        if (!this.setProjectInfoAll()) return;

        const products = [];
        for (const product of this.productList) {
            let productInfo = product.setProductAll();
            if (productInfo === null) {
                return;
            }
            products.push(productInfo);
        }

        const project = {
            "title": this.title,
            "description": editor.getHtml(),
            "goalFundRaising": this.goalFundRaising,
            "endAt": this.endAt,
            "thumbnailUrl": this.thumbnailUrl,
            "cid": $('.categories-dropbox select').value,
            "products": products
        };

        if (!this.checkMinProductsPrice(project["products"])) {
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

    checkMinProductsPrice(products) {
        let minPrice = 0;
        products.forEach(product => {
            minPrice += product["price"] * product["quantitySupplied"]
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

