class ProductBtns {
    constructor() {
        this.SUPPORT_BTN_TEXT = "담기"
        this.CANCEL_BTN_TEXT = "취소"

        this.productBtns = $all(".product-list .product-btn");

        this.productBtns.forEach(btn => {
            addEventListenerToTarget(btn, "click", this.productBtnClickHandler.bind(this));
        });
        addEventListenerToTarget($(".need-login-cover"), "click", this.needLoginCoverClickHandler);
        addEventListenerToTarget($(".support-btn"), "click", this.supportBtnClickHandler.bind(this));
        addEventListenerToTarget($(".support-product-cart"),"click", this.xBtnHandler.bind(this));
    }

    needLoginCoverClickHandler() {
        window.location.href = "/users/login";
    }

    xBtnHandler(evt) {
        if(evt.target.className !== "close") {
            return;
        }
        evt.target.parentElement.remove();
    }

    supportBtnClickHandler(evt) {
        this.supportProduct($(".support-product-cart"));
    }

    productBtnClickHandler(evt) {
        if (evt.target.textContent === this.SUPPORT_BTN_TEXT) {
            this.addCart(evt.target);
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

    insertCartHTML(target) {
        const id = target.closest("div.product-btn").dataset.productId;
        const suppliedQuantity = target.parentElement.firstElementChild.max;
        const productPrice = target.parentElement.parentElement.getElementsByClassName("product-info-price").item(0).lastElementChild.textContent.replace(/[^0-9]/g, '');
        const quantity = target.parentElement.firstElementChild.value;
        const name = target.parentElement.parentElement.getElementsByClassName("product-info-title").item(0).textContent;

        let html = `<li data-product-price="${productPrice}" data-product-id="${id}" >
                        <div class="title">${name}</div>
                        <div class="amount">
                            <input type="number" value="${quantity}" min="1" max="${suppliedQuantity}"/>개
                        </div>
                        <div class="close">&times;</div>
                    </li>`;
        $(".support-product-cart ul").insertAdjacentHTML("beforeend", html);
    }

    appendCartHTML(matchCart, target) {
        let amount = $at(matchCart,".amount input");
        const inputQuantity = target.parentElement.firstElementChild.value;
        amount.value = parseInt(amount.value) + parseInt(inputQuantity);
    }

    showOverAmount(target, milleSec) {
        const cautionElement = target.parentElement.querySelector(".caution.over");
        cautionElement.style.display = "block";
        setTimeout(function () {
            cautionElement.style.display = "none";
        }, milleSec)
    }

    addCart(target) {
        if (!this.validQuantity(target)) {
            return;
        }

        const quantity = target.parentElement.firstElementChild.value;
        const suppliedQuantity = target.parentElement.firstElementChild.max;
        if(parseInt(quantity) > parseInt(suppliedQuantity)) {
            this.showOverAmount(target, 2000);
            return;
        }

        const id = target.closest("div.product-btn").dataset.productId;
        const cartList = [...$(".support-product-cart ul").children];
        const matchCart = cartList.filter(cart => {return cart.dataset.productId === id});

        if(matchCart.length === 0) {
            this.insertCartHTML(target);
        } else {
            this.appendCartHTML(matchCart[0], target);
        }
        this.hideProductSupport(target);
    }

    hideProductSupport(target) {
        $at(target.parentElement, ".product-quantity").value="";
        target.parentElement.style.display = "none";
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

    setSupportForm(liElement) {
        const quantity = $at(liElement, ".amount input").innerText;
        const productPrice = liElement.dataset.productPrice;

        const supportForm = {
            "pid": liElement.dataset.productId,
            "quantity": quantity,
            "purchasePrice": productPrice * quantity,
            "name": $at(liElement, ".title").innerText
        };
        return supportForm;
    }

    showCartAmount(filterCart) {
        filterCart.forEach(cartLi => {
            $at(cartLi,".amount input").style.borderColor = "red";
            setTimeout(function () {
                $at(cartLi,".amount input").style.borderColor = "";
            }, 2000)});
    }

    validCartAmount(target) {
        const inputTag = $at(target,".amount input");
        if(parseInt(inputTag.value) > parseInt(inputTag.max)) {
            return false;
        }
        return true;
    }

    supportProduct(target) {
        const cartList =  [...$(".support-product-cart").firstElementChild.children];
        const supportFormList = [];
        const filterCart = cartList.filter(cart => {return !this.validCartAmount(cart)});

        if(filterCart.length !== 0) {
            this.showCartAmount(filterCart);
            return;
        }

        for(const cart of cartList) {
            supportFormList.push(this.setSupportForm(cart));
        }

        fetchManager({
            url: '/api/orders',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(supportFormList),
            callback: this.supportCallback.bind(this)
        });
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