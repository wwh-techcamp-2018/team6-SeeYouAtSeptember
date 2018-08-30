class ProductBtns {
    constructor() {
        this.productBtns = $all(".product-card");

        this.productBtns.forEach(btn => {
            addEventListenerToTarget(btn, "click", this.productBtnClickHandler.bind(this));
        });
        addEventListenerToTarget($(".support-btn"), "click", this.supportBtnClickHandler.bind(this));
        addEventListenerToTarget($(".support-product-cart"), "click", this.cancelBtnHandler.bind(this));
        addEventListenerToTarget($(".support-product-cart ul"),"input", this.cartInputHandler.bind(this));
    }

    /*
    needLoginCoverClickHandler() {
        window.location.href = "/users/login";
    }
    */

    cartInputHandler(evt) {
        const target = evt.target;
        if(target.tagName !== "INPUT") {
            return;
        }

        if(!this.validCartAmount(target)) {
            this.showCaution(target.parentElement.parentElement);
            if(parseInt(target.value) > parseInt(target.max)) {
                target.value = target.max;
            } else {
                target.value = 1;
            }
        }
        this.setTotal();
    }

    cancelBtnHandler(evt) {
        const target = evt.target;
        if (target.className !== "close") {
            return;
        }
        const id = target.parentElement.dataset.productId;
        const matchProduct = [...this.productBtns].filter(product => {
            return product.dataset.productId === id
        })[0];

        $("#money").innerText = parseInt($("#money").innerText.replace(/[^0-9]/g, '')) - parseInt(this.multiplyPriceAndQuantity(target.parentElement));
        $("#money").innerText = parseInt($("#money").innerText).toLocaleString('ko-KR');
        removeClassName("in", matchProduct);
        target.parentElement.remove();
    }

    supportBtnClickHandler(evt) {
        this.supportProduct($(".support-product-cart"));
    }

    productBtnClickHandler(evt) {
        const target = evt.currentTarget;
        if (parseInt($at(target, ".remainedQuantity").innerText.replace(/[^0-9]/g, '')) === 0) {
            return;
        }

        if (target.classList.contains("in")) {
            removeClassName("in", target);
            this.removeCartItem(target);
            return;
        }
        addClassName("in", target);
        this.addCartItem(target);
    }

    multiplyPriceAndQuantity(target) {
        const productPrice = target.dataset.productPrice.replace(/[^0-9]/g, '');
        const quantity = $at(target, ".amount input").value;
        return parseInt(productPrice) * quantity;
    }

    setTotal() {
        const cartList = [...$(".support-product-cart ul").children];
        let sum = 0;
        cartList.forEach(cartLi => {sum += this.multiplyPriceAndQuantity(cartLi)});
        $("#money").innerText = sum.toLocaleString('ko-KR');
    }

    removeCartItem(target) {
        const id = target.dataset.productId;
        const matchCartItem = [...$(".support-product-cart ul").children].filter(cart => {
            return cart.dataset.productId === id
        })[0];

        matchCartItem.remove();
        $("#money").innerText = parseInt($("#money").innerText.replace(/[^0-9]/g, '')) - parseInt(this.multiplyPriceAndQuantity(matchCartItem));
        $("#money").innerText = parseInt($("#money").innerText).toLocaleString('ko-KR');
    }

    addCartItem(target) {
        const id = target.dataset.productId;
        const remainedQuantity = $at(target, ".remainedQuantity").innerText.replace(/[^0-9]/g, '');
        const productPrice = $at(target, ".price span").innerText;
        const title = $at(target, ".title").innerText.replace(/</g, "&lt;").replace(/>/g, "&gt;");

        let html = `<li data-product-price="${productPrice}" data-product-id="${id}" >
                        <div class="title">${title}</div>
                        <div class="amount">
                            <input type="number" value="1" min="1" max="${remainedQuantity}"/>개
                        </div>
                        <div class="close">&times;</div>
                    </li>`;
        $(".support-product-cart ul").insertAdjacentHTML("beforeend", html);
        $("#money").innerText = parseInt($("#money").innerText.replace(/[^0-9]/g, '')) + parseInt(productPrice.replace(/[^0-9]/g, ''));
        $("#money").innerText = parseInt($("#money").innerText).toLocaleString('ko-KR');
    }

    setSupportForm(liElement) {
        const quantity = $at(liElement, ".amount input").value;
        const productPrice = liElement.dataset.productPrice.replace(/[^0-9]/g, '');

        const supportForm = {
            "productDTO": {
                "pid": liElement.dataset.productId,
            },
            "quantity": quantity
        };

        return supportForm;
    }

    showCaution(cartLi) {
        $at(cartLi, ".amount input").style.border = "red 2px solid";
        setTimeout(function () {
            $at(cartLi, ".amount input").removeAttribute("style");
        }, 2000);
    }

    validCartAmount(inputTag) {
        if (inputTag.value === "" || inputTag.value === "0" || parseInt(inputTag.value) > parseInt(inputTag.max)) {
            return false;
        }
        return true;
    }

    supportProduct(target) {
        const cartList = [...$(".support-product-cart ul").children];
        if (cartList.length === 0) {
            return;
        }

        const supportFormList = [];
        const filterCart = cartList.filter(cart => {
            return !this.validCartAmount($at(cart, ".amount input"))
        });
        if (filterCart.length !== 0) {
            filterCart.forEach(cartLi => {
                this.showCaution(cartLi)
            });
            return;
        }

        for (const cart of cartList) {
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

        if (response.status === 201) {
            response.json().then(result => {
                this.requestImport(result);
            })
        }
    }

    requestImport(result) {
        IMP.request_pay({
            pg: "html5_inicis",
            name: result.name,
            merchant_uid: result.merchantUid,
            amount: 100,
            buyer_email: result.userDTO.email,
            buyer_name: result.userDTO.name,
            buyer_tel: result.userDTO.phoneNo,
            buyer_addr: result.userDTO.address
        }, function (rsp) {

            if (rsp.success) {
                fetchManager({
                    url: '/api/orders',
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: rsp.merchant_uid,
                    callback: successCallback
                });
                return;
            }

            fetchManager({
                url: '/api/orders/fail',
                method: 'PUT',
                headers: {'content-type': 'application/json'},
                body: result.merchantUid,
                callback: successCallback
            });
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

    //websocket 테스트용 api입니다 나중에 삭제해야해요!
    addEventListenerToTarget($(".project-title"), "click", function (evt) {
        getData('/api/projects/test/' + document.location.href.split('/')[4], () => {
        })
    })
});