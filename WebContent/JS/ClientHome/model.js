class Client {
    constructor() {
        this.parseBean = function(bean){
            this.ID = bean.ID;
            this.username = bean.username;
            this.mail = bean.mail;
        }

        this.setID = function (ID) {
            this.ID = ID;
        };
        this.getID = function () {
            return this.ID;
        };

        this.setUsername = function (username) {
            this.username = username;
        };
        this.getUsername = function () {
            return this.username;
        };

        this.setMail = function (mail) {
            this.mail = mail;
        };
        this.getMail = function () {
            return this.mail;
        };
    }
}

class Money {
    constructor() {
        this.parseBean = function(bean){
            this.wholePart = bean.wholePart;
            this.decimalPart = bean.decimalPart;
        }

        this.setWholePart = function (wholePart) {
            this.wholePart = wholePart;
        };
        this.getWholePart = function () {
            return this.wholePart;
        };

        this.setDecimalPart = function (decimalPart) {
            this.decimalPart = decimalPart;
        };
        this.getDecimalPart = function () {
            return this.decimalPart;
        };
    }
}

class Error {
    constructor() {
        this.parseBean = function(bean){
            this.errorCode = bean.errorCode;
            this.errorMessage = bean.errorMessage;
        }

        this.setErrorCode = function (errorCode) {
            this.errorCode = errorCode;
        };
        this.getErrorCode = function () {
            return this.errorCode;
        };

        this.setErrorMessage = function (errorMessage) {
            this.errorMessage = errorMessage;
        };
        this.getErrorMessage = function () {
            return this.errorMessage;
        };
    }
}

class Option {
    constructor() {
        this.parseBean = function(bean){
            this.ID = bean.ID;
            this.name = bean.name;
            this.type = bean.type;
        }

        this.setID = function (ID) {
            this.ID = ID;
        };
        this.getID = function () {
            return this.ID;
        };

        this.setName = function (name) {
            this.name = name;
        };
        this.getName = function () {
            return this.name;
        };

        this.setType = function (type) {
            this.type = type;
        };
        this.getType = function () {
            return this.type;
        };
    }
}

class Product {
    constructor() {
        this.parseBean = function(bean){
            this.ID = bean.ID;
            this.name = bean.name;
            this.options = []; 
            bean.options.forEach(option => {
                var opt = new Option();
                opt.parseBean(option);
                this.options.push(opt);
            });
        }

        this.setID = function (ID) {
            this.ID = ID;
        };
        this.getID = function () {
            return this.ID;
        };

        this.setName = function (name) {
            this.name = name;
        };
        this.getName = function () { 
            return this.name;
        };

        this.setOptions = function (options) {
            this.options = options;
        };
        this.getOptions = function () {
            return this.options;
        };
    }
}

class Quotation {
    constructor() {
        this.parseBean = function(bean){
            this.ID = bean.ID;
            var prod = new Product();
            prod.parseBean(bean.product);
            this.product = prod;
            if(bean.price != null){
                var price = new Money();
                price.setDecimalPart(bean.price.decimalPart);
                price.setWholePart(bean.price.wholePart);
                this.price = price;
            }
            else{
                this.price = null;
            }
        }

        this.setID = function (ID) {
            this.ID = ID;
        };
        this.getID = function () {
            return this.ID;
        };

        this.setProduct = function (product) {
            this.product = product;
        };
        this.getProduct = function () {
            return this.product;
        };

        this.setPrice = function (price) {
            this.price = price;
        };
        this.getPrice = function () {
            return this.price;
        };
    }
}


class Model {
    constructor() {
        this.client = null;
        this.quotations = null;
        this.productSelected = null;
        this.optionsSelected = [];
        this.error = null;
        this.allProducts = null;

        this.buildFakeModel = function () {
            this.client= new Client();
            this.client.setID(1);
            this.client.setUsername("FakeName");
            this.client.setMail("Fake@Mail.com");

            this.allProducts = [];
            for (let i = 0; i < 9; i++) {
                let product = new Product();
                product.setID(i);
                product.setName("prodname" + i);
                product.setOptions(new Array());
                for(let j= 0; j<7; j++){
                    let option = new Option();
                    option.setID(j);
                    option.setName("optname" + j);
                    option.setType("optType" + j);
                    product.getOptions().push(option);
                }

                this.allProducts.push(product);
            }  
            
            this.quotations = [];
            for (let i = 0; i < 5; i++) {
                let quotation = new Quotation();
                quotation.setID(i);
                quotation.setPrice(new Money());
                quotation.getPrice().setWholePart(i*100);
                quotation.getPrice().setDecimalPart(i);
                quotation.setProduct(this.allProducts[i]);
                this.quotations.push(quotation);
            }   

            this.productSelected = null;
            this.optionsSelected = null;
            this.error = null;   
        };

        this.setClient = function (client) {
            this.client = client;
        };
        this.getClient = function () {
            return this.client;
        };

        this.setQuotations = function (quotations) {
            this.quotations = quotations;
        };
        this.getQuotations = function () {
            return this.quotations;
        };

        this.setProductSelected = function (productSelected) {
            this.productSelected = productSelected;
        };
        this.getProductSelected = function () {
            return this.productSelected;
        };

        this.setOptionsSelected = function (optionsSelected) {
            this.optionsSelected = optionsSelected;
        };
        this.getOptionsSelected = function () {
            return this.optionsSelected;
        };

        this.setError = function (error) {
            this.error = error;
        };
        this.getError = function () {
            return this.error;
        };

        this.setAllProducts = function (allProducts) {
            this.allProducts = allProducts;
        };
        this.getAllProducts = function () {
            return this.allProducts;
        };
    }
}
