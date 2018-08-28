class ProductBtns {
    constructor() {
        this.SUPPORT_BTN_TEXT = "후원하기"
        this.CANCEL_BTN_TEXT = "취소"

        this.productBtns = $all(".product-list .product-btn");

        this.productBtns.forEach(btn => {
            addEventListenerToTarget(btn, "click", this.productBtnClickHandler.bind(this));
        });
        addEventListenerToTarget($(".need-login-cover"), "click", this.needLoginCoverClickHandler);
    }

    needLoginCoverClickHandler() {
        window.location.href = "/users/login";
    }

    productBtnClickHandler(evt) {
        if (evt.target.textContent === this.SUPPORT_BTN_TEXT) {
            this.supportProduct(evt.target);
            return;
        }
        if (evt.target.textContent === this.CANCEL_BTN_TEXT) {
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
        this.showCaution(target, 2000);
        return false;
    }

    showCaution(target, milleSec) {
        const cautionElement = target.parentElement.querySelector(".caution");
        cautionElement.style.display = "block";
        setTimeout(function () {
            cautionElement.style.display = "none";
        }, milleSec)
    }

    supportProduct(target) {
        if (!this.validQuantity(target)) {
            return;
        }
        const productPrice = target.parentElement.parentElement.getElementsByClassName("product-info-price").item(0).lastElementChild.textContent.replace(/[^0-9]/g, '');
        const quantity = target.parentElement.firstElementChild.value;
        const name = target.parentElement.parentElement.getElementsByClassName("product-info-title").item(0).textContent;

        const supportForm = {
            "pid": target.closest("div.product-btn").dataset.productId,
            "quantity": quantity,
            "purchasePrice": productPrice * quantity,
            "name": name
        };

        fetchManager({
            url: '/api/orders',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(supportForm),
            callback: this.supportCallback.bind(this)
        });
        this.hideProductSupport(target);
    }

    supportCallback(response) {
        if (response.status === 401) {
            window.location.href = "/users/login";
            return;
        }

        if (response.status === 200) {
            response.json().then(result => {
                this.requestImport(result);
            })
        }
    }

    requestImport(result) {
        IMP.request_pay({
            pg: "html5_inicis",
            name: result.name,
            merchant_uid: result.id,
            amount: 100,
            buyer_email: result.uid
        }, function (rsp) {
            if (rsp.success) {
                fetchManager({
                    url: '/api/products',
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: rsp.merchant_uid,
                    callback: successCallback
                });
            } else {
                alert("결제가 실패하였습니다.");
            }
        })
    }

}

function fillProgressBar(milleSec) {
    let widthValue = $("#state-box .achievement-rate").textContent.replace(/[^0-9]/g, '');
    const baedalImg = $("#state-box .baedal-img");
    if (widthValue <= 0)
        return;
    widthValue = widthValue > 100 ? "100%" : widthValue + "%";
    setTimeout(() => {
        $("#state-box #progress-inner-bar").style.width = widthValue;
        baedalImg.style.marginLeft = widthValue;
        baedalImg.style.transform = "rotate(-30deg)";
        setTimeout(() => {
            baedalImg.style.transform = "rotate(0deg)";
        }, 1500)
    }, milleSec)
}

document.addEventListener("DOMContentLoaded", () => {
    Viewer = new tui.Editor({
        el: $('#viewer-section'),
        height: '300px',
        initialValue: $(".description-viewer").dataset.description
    });
    IMP.init('imp68124833');
    new ProductBtns();
    fillProgressBar(500);
});