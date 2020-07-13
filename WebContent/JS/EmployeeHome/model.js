class User {
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
            this.clientUsername = bean.clientUsername;
            this.email = bean.email;
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

        this.getClientUsername = function(){
            return this.clientUsername;
        }
        this.setClientUsername = function(clientUsername){
            this.clientUsername = clientUsername;
        }

        this.getEmail = function(){
            return this.email;
        }
        this.setEmail = function(email){
            this.email = email;
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
        this.Employee = null;
        this.quotationsManaged = null;
        this.quotationsNotManaged = null;
        this.quotationToPriceID = null
        this.price = null;
        this.error = null;

        this.setEmployee = function (employee) {
            this.employee = employee;
        };
        this.getEmployee = function () {
            return this.employee;
        };

        this.setNotManagedQuotations = function (quotationsNotManaged) {
            this.quotationsNotManaged = quotationsNotManaged;
        };
        this.getNotManagedQuotations = function () {
            return this.quotationsNotManaged;
        };

        this.setManagedQuotations = function (quotationsManaged) {
            this.quotationsManaged = quotationsManaged;
        };
        this.getManagedQuotations = function () {
            return this.quotationsManaged;
        };

        this.setError = function (error) {
            this.error = error;
        };
        this.getError = function () {
            return this.error;
        };

        this.setPrice = function (price) {
            this.price = price;
        };
        this.getPrice = function () {
            return this.price;
        };

        this.setQuotationToPriceID = function (quotationToPriceID) {
            this.quotationToPriceID = quotationToPriceID;
        };
        this.getQuotationToPriceID = function () {
            return this.quotationToPriceID;
        };
    }
}
