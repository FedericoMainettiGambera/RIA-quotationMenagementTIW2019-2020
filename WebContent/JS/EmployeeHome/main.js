(function() { 

    //model
    var model = new Model();
    // model.buildFakeModel(); debugging offline

    //view
    var view = new View(model);

    //serverGate
    var serverGate = new ServerGate(view);

    //orchestrator
    var orchestrator = new Orchestrator(model, serverGate, view);

    view.setOrchestrator(orchestrator);
    view.setServerGate(serverGate);
    serverGate.setModel(model);
    serverGate.setOrchestrator(orchestrator);

    document.getElementsByClassName("errorOk")[0].addEventListener("click", function(){
        document.getElementsByClassName("errorMessage")[0].innerHTML = "";
        if(document.getElementsByClassName("errorBackground")[0].classList.contains("errorVisible")){
            document.getElementsByClassName("errorBackground")[0].classList.remove("errorVisible");
        }
        document.getElementsByClassName("errorBackground")[0].classList.add("errorNone");
    });

    document.getElementsByClassName("logOut")[0].addEventListener("click", function(e){
        e.preventDefault();
        serverGate.logOut();
    })

    //debugging
    console.log(model);
    console.log(view);
    console.log(serverGate);
    console.log(orchestrator);

    orchestrator.init();

})();


/*just for fun in console*/
var closeTab = function(){
    document.getElementsByClassName("main")[0].style.width = "60vw";
    document.getElementsByClassName("tab")[0].style.right = "-20vw";
    document.getElementsByClassName("banner")[0].style.right = "20%";
}
var openTab = function(){
    document.getElementsByClassName("main")[0].style.width = "50vw";
    document.getElementsByClassName("tab")[0].style.right = "0vw";
    document.getElementsByClassName("banner")[0].style.right = "40%";
}


//TODO: LOG OUT
//TODO: FILTRI