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


//here just to have fun in the developer console...
function close(){
    document.getElementsByClassName("leftHider")[0].style.width = "60%";
    document.getElementsByClassName("rightHider")[0].style.width = "40%";
}
function open(){
    document.getElementsByClassName("leftHider")[0].style.width = "0%";
    document.getElementsByClassName("rightHider")[0].style.width = "0%";
}
