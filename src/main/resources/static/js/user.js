class loginBtn {
    constructor() {
        addEventListenerToTarget($("#login .login-box form button.btn-login"), "click", this.loginBtnHandler.bind(this))
    }

    loginBtnHandler(evt) {
        evt.preventDefault();

        this.loginForm = {
            "email": $("#email").value,
            "password": $("#pwd").value
        };

        $("#pwd").value = "";

        fetchManager({
            url: '/api/users/login',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(this.loginForm),
            callback: this.login
        });
    }

    login(response) {
        if (response.status === 200)
            location.href = "/";
        $("p.err-msg").style.display = "block";
    }
}

class join {
    constructor() {
        addEventListenerToTarget($("#email_domain_select"), "change", this.domainSelectChangeHandler.bind(this))
        addEventListenerToTarget($(".join_form_box .btn"), "click", this.joinBtnClickHandler.bind(this));
    }

    domainSelectChangeHandler(evt) {
        this.emailDomain = $("input#email_domain");

        if (evt.target.value === "0") {
            this.emailDomain.value = "";
            this.emailDomain.disabled = false;
            return;
        }

        this.emailDomain.value = evt.target.value;
        this.emailDomain.disabled = true;


    }

    joinBtnClickHandler(evt) {
        evt.preventDefault();

        //todo: 유효성 검사
        this.joinForm = {
            "email": $("#email_id").value+"@"+$("#email_domain"),
            "password": $("#pw1").value,
            "name": $("#name").value,
            "phoneNo": $("#cell1").value + "-" + $("#cell2").value + "-" +$("#cell3").value,
            "address": $("#address").value
        };

        $("#pw1").value = "";
        $("#pw2").value = "";

        console.log(this.joinForm)
        fetchManager({
            url: '/api/users',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(this.joinForm),
            callback: this.join
        });
    }

    join(response){
        if(response.status === 200) {
            location.href = "/";
        }
        //todo: 에러처리
    }

}

document.addEventListener("DOMContentLoaded", () => {
    new loginBtn();
    new join();
});
