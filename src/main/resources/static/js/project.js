class ProjectForm {
    constructor() {
        this.products = [];
        this.cropper = null;
        this.fileInput = $("#file-input");
        this.modal = $("#myModal");

        /* thumbnail image upload */
        addEventListenerToTarget($("#thumbnailUrl"), "click", this.uploadFileHandler.bind(this));
        addEventListenerToTarget($("#crop-btn"), "click", this.cropButtonHandler.bind(this));
        $all(".close").forEach(closeObject => closeObject.addEventListener("click", this.closeButtonHandler.bind(this)));
        addEventListenerToTarget(this.fileInput, "click", this.fileClickHandler.bind(this));
        addEventListenerToTarget(this.fileInput, "change", this.fileChangeHandler.bind(this));

        /* products */
        addEventListenerToTarget($(".product-cards"), "click", this.highlightCard);
        addEventListenerToTarget($(".trash-bin"), "click", this.removeProductCard.bind(this));
        addEventListenerToTarget($(".product-add-button"), "click", this.addProductCard.bind(this));

        /* validation */
        /* todo: 이름 바뀌었어요 */
        this.focusOutProjectsInfoTargetList = [
            $("#projects-title-input"),
            $("#projects-goalFundRaising-input"),
            $("#projects-endAt-input")
        ];
        this.focusOutProjectsInfoTargetList.forEach(target => {
            addEventListenerToTarget(target, "focusout", this.focusOutProjectInputHandler.bind(this));
        });

        /* create projecdt */
        addEventListenerToTarget($(".create-project-button"), "click", this.createProjectBtnHandler.bind(this));
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

    addProductCard(event) {
        const html = `<li class="product-card">
            <input type="number" class="price" min="1000" placeholder="가격"></input>
            <input type="text" class="title" maxlength="30" placeholder="반찬 이름"></input>
            <textarea class="description" maxlength="100" placeholder="설명"></textarea>
            <input type="number" class="quantity" min="1" placeholder="공급량"></input>
        </li>`
        event.target.insertAdjacentHTML('beforeBegin', html);
        this.products.push(event.target.previousElementSibling);
        if (this.products.length == 4) {
            event.target.style.display = "none";
        }
    }

    highlightCard(event) {
        if (!event.target.classList.contains("product-card")) {
            return;
        }
        event.target.classList.toggle("selected");
    }

    removeProductCard(evt) {
        [...$all(".selected")].forEach(card => {
            this.products.splice(this.products.indexOf(card), 1);
            card.remove();
        });
        $(".product-add-button").removeAttribute("style");
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
        for (const product of this.products) {
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
        initialEditType: "wysiwyg",
        hooks: {
            'addImageBlobHook': insertEditorImg
        },
        height: "700px"
    })
});

