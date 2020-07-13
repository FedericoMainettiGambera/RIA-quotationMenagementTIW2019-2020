function Orchestrator(model, serverGate, view){
    var self=this;
    this.model = model;
    this.serverGate = serverGate;
    this.view = view;

    this.init = function(){
        this.view.registerBannerListeners();
         /*MODEL UPDATE*/
        serverGate.requestEmployee(function(responseBean){
            var employee = new User();
            employee.parseBean(responseBean);
            self.model.setEmployee(employee);

            console.log("UPDATED CLIENT: ");
            console.log(responseBean);

            self.goToManagedQuotations();
        });
    }

    this.goToManagedQuotations = function () {
        /*MODEL UPDATE*/
        serverGate.requestManagedQuotations(function(responseBean){
            var quotations = [];
            responseBean.forEach(quotationBean => {
                var quotation = new Quotation();
                quotation.parseBean(quotationBean);
                quotations.push(quotation);
            });
            self.model.setManagedQuotations(quotations);

            console.log("UPDATED MANAGED QUOTATIONS:");
            console.log(responseBean);

            /*VIEW RENDERING*/
            self.view.updateManagedQuotations();
        });
    }

    this.goToNotManagedQuotations = function(){
        /*MODEL UPDATE*/
        serverGate.requestNotManagedQuotations(function(responseBean){
            var quotations = [];
            responseBean.forEach(quotationBean => {
                var quotation = new Quotation();
                quotation.parseBean(quotationBean);
                quotations.push(quotation);
            });
            self.model.setNotManagedQuotations(quotations);

            console.log("UPDATED NOT MANAGED QUOTATIONS:");
            console.log(responseBean);

            /*VIEW RENDERING*/
            self.view.updateNotManagedQuotations();
        });
    }

}