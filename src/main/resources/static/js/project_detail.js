class ProductBtns {
    constructor() {
        this.SUPPORT_BTN_TEXT = "후원하기"
        this.CANCEL_BTN_TEXT = "취소"

        this.productBtns = $all(".product-list .product-btn");

        this.productBtns.forEach(btn => {
            addEventListenerToTarget(btn, "click", this.productBtnClickHandler.bind(this));
        })
        addEventListenerToTarget($(".need-login-cover"), "click", this.needLoginCoverClickHandler)
    }

    needLoginCoverClickHandler() {
        window.location.href = "/users/login"
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

        const supportForm = {
            "pid": target.closest("div.product-btn").dataset.productId,
            "quantity": target.parentElement.firstElementChild.value
        };

        fetchManager({
            url: '/api/products',
            method: 'POST',
            headers: { 'content-type': 'application/json' },
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
            response.json().then(result=>{
              this.requestImport(result.pid+result.uid);        
            })
        }
    }

    requestImport(merchant_uid){
         IMP.request_pay({ 
            pg: "html5_inicis",
            name:"테스트 상품",
            merchant_uid: merchant_uid,
            amount: 100
        }, function (rsp) { 
            if (rsp.success) {
                window.location.reload();
            } else {
                alert("결제가 실패하였습니다.");           
            }
        })
    }

}

function fillProgressBar() {
    let widthValue = $("#state-box .achievement-rate").textContent.replace(/[^0-9]/g, '');
    widthValue = widthValue > 100 ? "100%" : widthValue + "%";
    setTimeout(() => {
        $("#state-box #progress-inner-bar").style.width = widthValue;
    }, 500)
}

document.addEventListener("DOMContentLoaded", () => {
    Viewer = new tui.Editor({
        el: $('#viewer-section'),
        height: '300px',
        initialValue: $(".description-viewer").dataset.description
    });
    IMP.init('imp68124833'); 
    new ProductBtns();
    fillProgressBar();
});