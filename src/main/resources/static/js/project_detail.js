class ProductBtns {
    constructor() {
        this.productBtns = [...$all(".product-list .product-btn")];

        this.productBtns.forEach(btn => {
            addEventListenerToTarget(btn, "click", this.productBtnClickHandler.bind(this));
        })
        addEventListenerToTarget($(".need-login-cover"), "click", this.needLoginCoverClickHandler)
    }

    needLoginCoverClickHandler() {
        window.location.href = "/users/login"
    }

    productBtnClickHandler(evt) {
        if (evt.target.textContent === "후원하기") {
            this.supportProduct(evt.target);
            return;
        }
        if (evt.target.textContent === "취소") {
            this.hideProductSupport(evt.target);
            return;
        }
        const target = evt.target.closest("div.product-btn");
        if (!target.classList.contains("soldout"))
            target.querySelector(".product-info-container .product-support").style.display = "block";
    }

    hideProductSupport(target) {
        target.parentElement.firstElementChild.value = "";
        target.closest("div.product-support").style.display = "none";
    }

    validQuantity(target) {
        const numRegex = /^[0-9]{1,}/g;
        const num = target.parentElement.firstElementChild.value;
        if (numRegex.test(num) && num > 0)
            return true;
        this.showCaution(target);
        return false;
    }

    showCaution(target) {
        target.parentElement.querySelector(".caution").style.display = "block";
        setTimeout(function () {
            this.parentElement.querySelector(".caution").style.display = "none";
        }.bind(target), 2000)
    }

    supportProduct(target) {
        if (!this.validQuantity(target)) {
            return;
        }
        const supportForm = {
            "pid": target.closest("div.product-btn").dataset.productId,
            "quantity": target.parentElement.firstElementChild.value
        };

        fetchManager({
            url: '/api/products',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(supportForm),
            callback: this.supportCallback.bind(target)
        });

        this.hideProductSupport(target);
    }

    supportCallback(response) {
        if (response.status === 200) {
            window.location.reload();
            return;
        }
        if (response.status === 401) {
            window.location.href = "/users/login";
            return;
        }
    }
}

document.addEventListener("DOMContentLoaded", () => {
    Viewer = new tui.Editor({
        el: $('#viewer-section'),
        height: '300px',
        initialValue: $(".description-viewer").dataset.description
    });

    new ProductBtns();
});