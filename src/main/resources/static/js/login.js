class Login {
    constructor() {
        addEventListenerToTarget($("#login-btn"), "click", this.loginBtnHandler.bind(this))
        addEventListenerToTarget($("#email"), "focus", this.focusLoginBoxInput)
        addEventListenerToTarget($("#password"), "focus", this.focusLoginBoxInput)
    }

    loginBtnHandler(evt) {
        evt.preventDefault();

        this.loginForm = {
            "email": $("#email").value,
            "password": $("#password").value
        };

        $("#password").value = "";

        fetchManager({
            url: '/api/users/login',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(this.loginForm),
            callback: this.login
        });
    }

    login(response) {
        if (response.status === 200) {
            location.href = "/";
            return;
        }

        $(".form").style.border = "1px solid rgb(226, 65, 65)";
        $("#login-btn").style.background = "rgb(226, 65, 65)";
    }

    focusLoginBoxInput() {
        $(".form").removeAttribute("style");
        $("#login-btn").removeAttribute("style");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new Login();
});
