class ProjectForm {
    constructor() {
        this.products = [];
        this.cropper = null;
        this.fileInput = $("#file-input");
        this.modal = $("#myModal");
        this.thumbnailUploaded = false;

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
        this.addEventListenerToProjectInputs();

        /* create project */
        addEventListenerToTarget($(".create-project-button"), "click", this.submitCreateProjectForm.bind(this));
    }

    addEventListenerToProjectInputs() {
        addEventListenerToTarget($("#project-title"), "focusout", this.setTitle);
        addEventListenerToTarget($("#project-goalFundRaising"), "focusout", this.setGoalFundRaising);
        addEventListenerToTarget($("#project-endAt"), "focusout", this.setEndAt);
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
            viewport: {width: 600, height: 600},
            boundary: {width: 700, height: 700},
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
            <input type="number" class="price" min="1000" value="5000" placeholder="가격 (1000원 이상)"></input>
            <input type="text" class="title" maxlength="30" placeholder="반찬 이름 (최대 30자)"></input>
            <textarea class="description" maxlength="100" placeholder="설명 (최대 100자)"></textarea>
            <input type="number" class="quantity" min="1" value="1" placeholder="공급량"></input>
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

    validateThumbnailUrl() {
        if (this.thumbnailUploaded) {
            highlightBorderValid($(".thumbnail"));
            return true;
        }
        highlightBorderInvalid($(".thumbnail"));
        return false;
    }

    setTitle() {
        const minTitleLength = 1;
        const maxTitleLength = 50;
        this.title = $("#project-title").value;
        if (this.title.length >= minTitleLength && this.title.length <= maxTitleLength) {
            highlightBorderValid($("#project-title"));
            return true;
        }
        highlightBorderInvalid($("#project-title"));
        return false;
    }

    setGoalFundRaising() {
        const minGoalFundRaising = 1000000;
        const maxGoalFundRaising = 1000000000;
        this.goalFundRaising = $("#project-goalFundRaising").value;

        if (this.goalFundRaising >= minGoalFundRaising && this.goalFundRaising <= maxGoalFundRaising) {
            highlightBorderValid($("#project-goalFundRaising"));
            return true;
        }
        highlightBorderInvalid($("#project-goalFundRaising"));
        return false;
    }

    setEndAt() {
        this.endAt = new Date($("#project-endAt").value).getTime();
        let minCurrentDate = new Date();
        minCurrentDate.setDate(minCurrentDate.getDate() + 30);
        let maxCurrentDate = new Date();
        maxCurrentDate.setDate(maxCurrentDate.getDate() + 180);
        if (this.endAt >= minCurrentDate.getTime() && this.endAt <= maxCurrentDate.getTime()) {
            highlightBorderValid($("#project-endAt"));
            return true;
        }
        highlightBorderInvalid($("#project-endAt"));
        return false;
    }

    setProjectInfoAll() {
        let ret = true;
        ret &= this.validateThumbnailUrl();
        ret &= this.setTitle();
        ret &= this.setGoalFundRaising();
        ret &= this.setEndAt();
        return ret;
    }

    insertImgFile(blob) {
        if (blob === undefined) return;

        if (blob["type"].split("/")[0] === "image") {
            var file = new File([blob], window.URL.createObjectURL(blob), {
                type: blob["type"],
                lastModified: Date.now()
            });
            fetchFormData(this.setFormData(file), "/api/projects/upload", this.imageUploadCallback.bind(this));
            /* todo: fetchFormData에서 에러시 */
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
        this.thumbnailUploaded = true;
        highlightBorderValid($(".thumbnail"));
    }

    submitCreateProjectForm(evt) {
        evt.preventDefault();
        const products = [];
        let validation = this.setProjectInfoAll();
        for (const product of this.products) {
            let productInfo = new Product(product);
            validation &= productInfo.setProductAll();
            products.push(productInfo.product);
        }

        if (!validation || this.products.length === 0) {
            return;
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

