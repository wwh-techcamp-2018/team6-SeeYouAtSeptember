class Join {
    constructor() {
        addEventListenerToTarget($(".btn"), "click", this.joinBtnClickHandler.bind(this));
        this.focusOutTargetList = [$("#email"), $("#pw1"), $("#pw2"), $("#name"), $("#phone-last")];
        this.focusOutTargetList.forEach(target => {
            addEventListenerToTarget(target, "focusout", this.focusOutHandler.bind(this));
        })
        this.addEventListenerToInputs();
    }

    focusOutHandler(evt) {
        if (evt.target.id === "email")
            this.validEmail();
        if (evt.target.id === "pw1")
            this.validPassword();
        if (evt.target.id === "pw2")
            this.validPasswordConfirm();
        if (evt.target.id === "name")
            this.validName();
        if (evt.target.id === "phone-last")
            this.validPhoneNo();
    }

    joinBtnClickHandler(evt) {
        evt.preventDefault();

        if (!this.validAll()) {
            highlightBackgroundInvalid($(".btn"));
            highlightBorderInvalid($("#join-form"));
            return;
        }

        this.joinForm = {
            "email": this.email,
            "password": this.password,
            "name": this.name,
            "phoneNo": this.phoneNo,
            "address": this.address
        };

        $("#pw1").value = "";
        $("#pw2").value = "";

        fetchManager({
            url: '/api/users',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(this.joinForm),
            callback: this.join
        });
    }

    validAll() {
        this.validList = [this.validEmail.bind(this), this.validPassword.bind(this), this.validPasswordConfirm.bind(this),
            this.validName.bind(this), this.validPhoneNo.bind(this), this.validAddress.bind(this)];
        this.cnt = this.validList.length;
        this.validList.forEach(valid => {
            if (valid())
                this.cnt--;
        });
        return this.cnt === 0;
    }

    validEmail() {
        this.emailPattern = /^[_0-9a-zA-Z-]+@[0-9a-zA-Z]+(.[_0-9a-zA-Z-]+)*$/;
        this.email = $("#email").value;
        if (!this.emailPattern.test(this.email)) {
            highlightBorderInvalid($("#email"));
            return false;
        }
        highlightBorderValid($("#email"));
        return true;
    }

    validPassword() {
        this.passwordPattern = /^(?=.*?[a-zA-Z])(?=.*?[0-9]).{8,12}$/;
        this.password = $("#pw1").value;
        if (!this.passwordPattern.test(this.password)) {
            highlightBorderInvalid($("#pw1"));
            return false;
        }
        highlightBorderValid($("#pw1"));
        return true;
    }

    validPasswordConfirm() {
        this.confirmPassword = $("#pw2").value;
        if (!this.validPassword() || this.password !== this.confirmPassword) {
            highlightBorderInvalid($("#pw2"));
            return false;
        }
        highlightBorderValid($("#pw2"));
        return true;
    }

    validName() {
        this.name = $("#name").value;
        if (!this.name || this.name.length > 20) {
            highlightBorderInvalid($("#name"));
            return false;
        }
        highlightBorderValid($("#name"));
        return true;
    }

    validPhoneNo() {
        this.phoneNoPattern = /^01[0|1|6-9]-[0-9]{3,4}-[0-9]{4}$/;
        this.phoneNo = $("#phone-first").value + "-" + $("#phone-middle").value + "-" + $("#phone-last").value;
        if (!this.phoneNoPattern.test(this.phoneNo)) {
            highlightBorderInvalid($("#phone-first"));
            highlightBorderInvalid($("#phone-middle"));
            highlightBorderInvalid($("#phone-last"));
            return false;
        }
        highlightBorderValid($("#phone-first"));
        highlightBorderValid($("#phone-middle"));
        highlightBorderValid($("#phone-last"));
        return true;
    }

    validAddress() {
        this.address = $("#postcode").value + $("#address").value;

        if (this.address !== addressAPI.address || $("#address-detail").value === "") {
            highlightBorderInvalid($("#postcode"));
            highlightBorderInvalid($("#address"));
            highlightBorderInvalid($("#address-detail"));
            highlightBackgroundInvalid($("button.address-search"));
            return false;
        }
        highlightBorderValid($("#postcode"));
        highlightBorderValid($("#address"));
        highlightBorderValid($("#address-detail"));
        $("button.address-search").removeAttribute("style");

        this.address += $("#address-detail").value;
        return true;
    }

    addEventListenerToInputs() {
        [...$all("input")].forEach(input =>
            addEventListenerToTarget(input, "click", (evt) => {
                evt.target.removeAttribute("style");
                $("#join-form").removeAttribute("style");
                $(".btn").removeAttribute("style");
            }));
    }

    join(response) {
        if (response.status === 201) {
            location.href = "/";
        }
        highlightBorderInvalid($("#email"));
        //todo: e-mail을 제외한 validation 에러처리
    }

}

class AddressAPI {
    constructor() {
        addEventListenerToTarget($("button.address-search"), "click", this.loadAddressAPI.bind(this));
        addEventListenerToTarget($("#btnCloseLayer"), "click", this.closeDaumPostcode.bind(this));
    }

    loadAddressAPI(evt) {
        evt.preventDefault();
        const elementLayer = $('#layer');
        const currentThis = this;
        daum.postcode.load(function () {
            new daum.Postcode({
                oncomplete: function (data) {
                    let fullAddr = '';
                    let extraAddr = '';
                    if (data.userSelectedType === 'R') {
                        fullAddr = data.roadAddress;
                    } else {
                        fullAddr = data.jibunAddress;
                    }

                    if (data.userSelectedType === 'R') {
                        if (data.bname !== '') {
                            extraAddr += data.bname;
                        }
                        if (data.buildingName !== '') {
                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                        }
                        fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
                    }

                    currentThis.address = data.zonecode + fullAddr;
                    $("#postcode").value = data.zonecode;
                    $("#address").value = fullAddr;

                    elementLayer.style.display = 'none';
                }
            }).embed(elementLayer, currentThis);
        })
        elementLayer.style.display = 'block';

        this.initLayerPosition(elementLayer);
    }

    initLayerPosition(elementLayer) {
        const width = 500;
        const height = 400;
        const borderWidth = 2;
        elementLayer.style.width = width + 'px';
        elementLayer.style.height = height + 'px';
        elementLayer.style.border = borderWidth + 'px solid';
        elementLayer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width) / 2 - borderWidth) + 'px';
        elementLayer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height) / 2 - borderWidth) + 'px';
    }

    closeDaumPostcode() {
        $("#layer").style.display = 'none';
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new Join();
    addressAPI = new AddressAPI();
});
