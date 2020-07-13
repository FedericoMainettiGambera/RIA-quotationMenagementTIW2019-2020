
(function() {
    //SIGN UP//
    //form submission
    function checkSignUpValidity(){
        var email = document.getElementById("email").value;
        var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        if(!(email.match(mailformat))){
            document.getElementById("mailError").textContent = "You have entered an invalid email address!";
            return false;
        }
        if ((email.length > 100)){
            document.getElementById("mailError").textContent = "Please, enter no more than 100 characters.";
            return false;
        }
        var username = document.getElementById("username").value;
        if(username==""){
            document.getElementById("usernameError").textContent = "Please, enter your name.";
            return false;
        }
        if(!isNaN(username)){
            document.getElementById("usernameError").textContent = "Please, enter only characters.";
            return false;
        }
        if ((username.length > 40)){
            document.getElementById("usernameError").textContent = "Please, enter no more than 40 characters.";
            return false;
        }
        var password = document.getElementById("password").value;
        if(password==""){
            document.getElementById("passwordError").textContent = "Please, enter your password.";
            return false;
        }
        if(!isNaN(password)){
            document.getElementById("passwordError").textContent = "Please, enter only characters.";
            return false;
        }
        if ((password.length > 40)){
            document.getElementById("passwordError").textContent = "Please, enter no more than 40 characters.";
            return false;
        }
        var repeatPassword = document.getElementById("repeatPassword").value;
        if(password !== repeatPassword){
            document.getElementById("repeatPasswordError").textContent = "Password and repeat password flieds are different."
            return false;
        }
        if ((repeatPassword.length > 40)){
            document.getElementById("repeatPasswordError").textContent = "Please, enter no more than 40 characters.";
            return false;
        }
        return true;
    }
    document.getElementById("signUpButton").addEventListener('click', (e) => {
        //reset error messages
        document.getElementById("mailError").textContent = "";
        document.getElementById("usernameError").textContent = "";
        document.getElementById("passwordError").textContent = "";
        document.getElementById("repeatPasswordError").textContent = "";
        //submit
        if(document.getElementById("signUpForm").checkValidity()){
            if (checkSignUpValidity()) {
                makeCall("POST", 'SignUp', e.target.closest("form"),
                function(req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        console.log(req);
                        var message = req.responseText;
                        switch (req.status) {
                        case 200:
                            window.location.href = "ClientHome.html";
                            break;
                        case 400: // bad request
                            document.getElementById(message.split(":")[0]).textContent = message.split(":")[1];
                            break;
                        case 401: // unauthorized
                            document.getElementById("messageError").textContent = message;
                            break;
                        case 500: // server error
                            document.getElementById("messageError").textContent = message;
                            break;
                        }
                    }
                }, false);
            }
        }
        else{
            document.getElementById("signUpForm").reportValidity();
        }
    });


    //SIGN IN
    function checkSignInValidity(){
        var username = document.getElementById("usernameSignIn").value;
        if(username==""){
            document.getElementById("messageErrorSignIn").textContent = "Please, enter your name.";
            return false;
        }
        if(!isNaN(username)){
            document.getElementById("messageErrorSignIn").textContent = "Please, enter only characters.";
            return false;
        }
        if ((username.length > 40)){
            document.getElementById("messageErrorSignIn").textContent = "Please, enter no more than 40 characters.";
            return false;
        }
        var password = document.getElementById("passwordSignIn").value;
        if(password==""){
            document.getElementById("messageErrorSignIn").textContent = "Please, enter your password.";
            return false;
        }
        if(!isNaN(password)){
            document.getElementById("messageErrorSignIn").textContent = "Please, enter only characters.";
            return false;
        }
        if ((password.length > 40)){
            document.getElementById("messageErrorSignIn").textContent = "Please, enter no more than 40 characters.";
            return false;
        }
        return true;
    }
    document.getElementById("signInButton").addEventListener('click', (e) => {
        //reset error messages
        document.getElementById("messageErrorSignIn").textContent = "";
        //submit
        if(document.getElementById("signInForm").checkValidity()){
            if (checkSignInValidity()) {
                makeCall("POST", 'CheckLogin', e.target.closest("form"),
                function(req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                        case 200:
                            var user = JSON.parse(req.responseText);
                            sessionStorage.setItem('username', user);
                            if(user.isClient){
                                window.location.href = "ClientHome.html";
                            }
                            else{
                                window.location.href = "EmployeeHome.html";
                            }
                            break;
                        case 400: // bad request
                            document.getElementById("messageErrorSignIn").textContent = message;
                            break;
                        case 401: // unauthorized
                            document.getElementById("messageErrorSignIn").textContent = message;
                            break;
                        case 500: // server error
                            document.getElementById("messageErrorSignIn").textContent = message;
                            break;
                        }
                    }
                }, false);
            }
        }
        else{
            document.getElementById("signInForm").reportValidity();
        }
    });

})();