function ServerGate(view){
    this.view = view;
    this.setModel = function (model) {
        this.model = model;
    }
    var self = this;

    this.setOrchestrator = function (orchestrator) {
        this.orchestrator = orchestrator;
    }

    this.requestEmployee = function(cback){
        this.makeCall("GET", "getEmployeeInfo", cback);
    }

    this.requestManagedQuotations = function(cback){
        this.makeCall("GET", "getManagedQuotations", cback);
    }

    this.requestNotManagedQuotations = function(cback){
        this.makeCall("GET", "getNotManagedQuotations", cback);
    }
    
    this.priceQuotation = function(cback){
        this.makeCall("GET", "PriceQuotation?quotationID=" + self.model.getQuotationToPriceID()
                + "&wholePart=" +self.model.getPrice().getWholePart()
                + "&decimalPart=" +self.model.getPrice().getDecimalPart() , cback);
    }

    this.logOut = function(){
        var cback = function(){
            window.location.href = "LogIn.html";
        }
        this.makeCall("GET", "LogOut", cback);
    }

    this.makeCall = function(method, url, cback) {
        var req = new XMLHttpRequest(); // visible by closure
        req.onreadystatechange = function() {
            if (req.readyState == XMLHttpRequest.DONE) {
                var response = req.responseText;
                switch (req.status) {
                case 200:
                    if(response ===""){
                        cback();
                    }
                    else{
                        cback(JSON.parse(response));
                    }
                    break;
                case 400: // bad request
                    self.view.updateError(response);
                    self.view.showError();
                    break;
                case 401: // unauthorized
                    self.view.updateError(response);
                    self.view.showError();
                    break;
                case 500: // server error
                    self.view.updateError(response);
                    self.view.showError();
                    break;
                case 0:
                    self.view.updateError("ERROR 0: CONNECTION LOST");
                    self.view.showError();
                    break;
                default:
                    self.view.updateError("ERROR " + req.status + ":" + response);
                    self.view.showError();
                    break;
                }
            }
        }; // closure
        req.open(method, url);
        req.send();
    }
}