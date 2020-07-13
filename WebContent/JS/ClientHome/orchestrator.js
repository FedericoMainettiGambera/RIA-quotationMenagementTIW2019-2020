function Orchestrator(model, serverGate, view){
    var self=this;

    this.model = model;
    this.serverGate = serverGate;
    this.view = view;

    this.init = function(){
        self.view.close();
        
        /*MODEL UPDATE*/
        //AJAX client
        serverGate.requestClient(function(responseBean){
            var client = new Client();
            client.parseBean(responseBean);
            self.model.setClient(client);

            console.log("UPDATED CLIENT: ");
            console.log(responseBean);

            self.goToHome(); 
        });
    }

    this.goToNewQuotation = function(){
        self.view.close();
        
        /*MODEL UPDATE*/
        //AJAX products
        serverGate.requestAllProducts(function(responseBean){
            var products = [];
            responseBean.forEach(productBean => {
                var product = new Product();
                product.parseBean(productBean);
                products.push(product);
            });
            self.model.setAllProducts(products);

            console.log("UPDATED PRODUCTS:");
            console.log(responseBean);

            /*VIEW RENDERING*/
            self.view.renderAfterClose(function(){
                self.view.cleanWorkSpace();
                self.view.createProductsPage();
                self.view.open();
            });
        });
    }

    this.goToHome = function(){
        self.view.close();

        /*MODEL UPDATE*/
        //AJAX quotations
        serverGate.requestQuotations(function(responseBean){
            var quotations = [];
            responseBean.forEach(quotationBean => {
                var quotation = new Quotation();
                quotation.parseBean(quotationBean);
                quotations.push(quotation);
            });
            self.model.setQuotations(quotations);

            console.log("UPDATED QUOTATIONS:");
            console.log(responseBean);

            /*VIEW RENDERING*/
            self.view.renderAfterClose(function(){
                self.view.cleanWorkSpace();
                self.view.updateQuotations();
                self.view.updateNewQuotationTab();
                document.getElementById("newQuotationButton").addEventListener("click", function(){ self.goToNewQuotation();});
                self.view.open();
            });
        });
    }
}