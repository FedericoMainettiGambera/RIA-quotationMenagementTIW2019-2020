function View(model){
    var self = this;
    this.model = model;
    this.productInfo = [];
    this.setOrchestrator = function(orchestrator){
        this.orchestrator = orchestrator;
    }
    this.setServerGate = function (serverGate) {
        this.serverGate = serverGate;
    }

    this.updateManagedQuotations = function(){
        this.closeTab();

        var quotationHTML = "";

        self.model.getManagedQuotations().forEach(quotation => {
            quotationHTML += 
            "<div class=\"quotationContainer\">"+ 
                "<div class=\"quotationImage\">"+ 
                    "<img src=\"./Image?productID=" + quotation.getProduct().getID() + "\">"+ 
                "</div>"+ 
                "<div class=\"quotationInfo\">"+ 
                    "<div class=\"quotationName\">"+ 
                        quotation.getProduct().getName() + 
                    "</div>"+ 
                    "<div class=\"options\">"+ 
                        "<div>"+ 
                            "<span class=\"optionLabel\">Options:</span>"+ 
                        "</div>";

            quotation.getProduct().getOptions().forEach(option => {
                var type = option.getType();
                var name = option.getName();
                quotationHTML += 
                        "<div class=\""+ type +"Div\">"+ 
                            "<span class=\""+ type +"Text\">promotion</span>"+ 
                            "<span class=\""+ type +"Option\">"+ name +"</span>"+ 
                        "</div>"; 
            });
                        
            if(quotation.getPrice().getDecimalPart() <= 9){
                        quotationHTML +=  "Price: " + quotation.getPrice().getWholePart() + ".0" + quotation.getPrice().getDecimalPart();
            }
            else{
                        quotationHTML +=  "Price: " + quotation.getPrice().getWholePart() + "." + quotation.getPrice().getDecimalPart();
            }

            quotationHTML += 
                    "</div>"+ 
                "</div>"+ 
            "</div>";

        });

        document.getElementsByClassName("mainContent")[0].innerHTML = quotationHTML; 
    }

    this.updateNotManagedQuotations = function(){

        var quotationHTML = "";

        self.model.getNotManagedQuotations().forEach(quotation => {
            quotationHTML +=
            "<div class=\"quotationContainer\">" +
            "    <div class=\"quotationImage\">" +
            "        <img src=\".\/Image?productID="+ quotation.getProduct().getID() +"\">" +
            "    <\/div>" +
            "    <div class=\"quotationInfo\">" +
            "        <div class=\"quotationName\">" +
                        quotation.getProduct().getName() +
            "        <\/div>" +
            "        <div class=\"options\"> " +
            "            <div>" +
            "                <span class=\"optionLabel\">Options:<\/span>" +
            "            <\/div>";

            quotation.getProduct().getOptions().forEach(option => {
                var type = option.getType();
                var name = option.getName();
                quotationHTML +=
                "        <div class=\""+type+"Div\"> " +
                "            <span class=\""+type+"Text\">promotion<\/span>" +
                "            <span class=\""+type+"Option\">"+name+"<\/span>" +
                "        <\/div>";
            });

            quotationHTML +=
            "            <div class=\"button MANAGEBUTTON\">MANAGE<\/div>" +
            "        <\/div>" +
            "    <\/div>" +
            "<\/div>";

        });

        document.getElementsByClassName("mainContent")[0].innerHTML = quotationHTML; 

        //adding listeners for MANAGE button
        var array = Array.from(document.getElementsByClassName("MANAGEBUTTON"));
        for (let i = 0; i < array.length; i++) {
            const button = array[i];
            const quotation = self.model.getNotManagedQuotations()[i];

            var manageButtonEventListener = function(e){
                e.stopPropagation();

                self.openTab();

                //new HTML to add:
                var innerHTML = 
                "<div class=\"productImageInOptionTab\">"+
                "    <img src=\".\/Image?productID=" + quotation.getProduct().getID() + "\">"+
                "<\/div>"+
                "<div class=\"productInfoContainer\">"+
                "    <div class=\"prodNameInOptionTab \">" + quotation.getProduct().getName().toUpperCase() + "<\/div>"+
                "    <div class=\"lorem\">Cool 3D-shape. Really nice :)<\/div>"+
                "<\/div>"+
                "<div class=\"productInfoContainer\">"+
                "    <div class=\"prodNameInOptionTab \">OPTIONS:<\/div>"+
                "    <div class=\"optionsContainer\" >"+
                "        <div >promotions:<\/div>";

                //promotions options:
                var atLeastOne = false;
                quotation.getProduct().getOptions().forEach(option => {
                    if(option.getType() === "promotion"){
                        atLeastOne = true;
                        innerHTML += "<div class=\"option\"><span class=\"optionSpan\">" + option.getName().toUpperCase()  +" <\/span><\/div>"
                    }
                });
                if(!atLeastOne){
                    innerHTML += "<div class=\"option\"><span>-no options-<\/span><\/div>";
                }

                innerHTML += 
                "        <br>"+
                "        <div >normals:<\/div>";

                //normal options:
                atLeastOne = false;
                quotation.getProduct().getOptions().forEach(option => {
                    if(option.getType() !== "promotion"){
                        atLeastOne = true;
                        innerHTML += "<div class=\"option\"><span class=\"optionSpan\">" + option.getName().toUpperCase()  +" <\/span><\/div>"
                    }
                });
                if(!atLeastOne){
                    innerHTML += "<div class=\"option\"><span>-no options-<\/span><\/div>";
                }

                innerHTML +=
                "        <br>"+
                "    <\/div>"+
                "<\/div>"+
                "<div class=\"productInfoContainer\">"+
                "    <div class=\"prodNameInOptionTab \">CLIENTS INFO:<\/div>"+
                "    <div class=\"userName\">Client\'s username: " + quotation.getClientUsername() +"<\/div>"+
                "    <div class=\"mail\">Client\'s email: " + quotation.getEmail() + "<\/div>"+
                "<\/div>"+
                "<form action=\"#\">"+
                "    <div class=\"priceCaption\">Please, insert price here:<\/div>" +
                "    <div class=\"priceInputs\">"+
                "        <input type=\"number\" name=\"wholePart\" id=\"wholePart\" placeholder=\"00\" min=\"0\" required>"+
                "        <span class=\"dot\">.<\/span>"+
                "        <input type=\"number\" name=\"decimalPart\" id=\"decimalPart\" placeholder=\"00\" min=\"0\" max=\"99\" required>"+
                "    <input type=\"hidden\" value=\"quotationIDGoesHERE\" name=\"quotationID\" id=\"quotationID\" \/>"+
                "    <\/div>"+
                "    <div id=\"price\" class=\"button createQuotation\">SET PRICE<\/div>"+
                "    <div class=\"priceError\"><\/div>" +
                "<\/form>";

                var container = document.createElement("DIV");
                container.innerHTML = innerHTML;
                document.getElementsByClassName("tabContainer")[0].appendChild(container);

                self.model.setQuotationToPriceID(quotation.getID());

                var priceEventListener = function(e){
                    e.preventDefault();
                    if(document.getElementsByTagName("FORM")[0].checkValidity()){
                        var wholePart = document.getElementById("wholePart").value;
                        var decimalPart = document.getElementById("decimalPart").value;
                        if(decimalPart.length > 2) {
                            document.getElementsByClassName("priceError")[0].innerHTML="price was not well-written, we tryed to correct, please, check changes.";                            
                            document.getElementById("decimalPart").value = decimalPart.charAt(0) + decimalPart.charAt(1);
                            return;
                        }; 
                        if(decimalPart.length == 1){
                            document.getElementsByClassName("priceError")[0].innerHTML="price was not well-written, we tryed to correct, please, check changes.";
                            document.getElementById("decimalPart").value = decimalPart + '0';
                            return;
                        }
                        
                        document.getElementsByClassName("priceError")[0].innerHTML="";
                        
                        var price = new Money();
                        price.setWholePart(wholePart);
                        price.setDecimalPart(decimalPart);
                        self.model.setPrice(price);
                        self.model.setQuotationToPriceID(quotation.getID());
                        self.serverGate.priceQuotation(function(){
                            document.getElementsByClassName("bannerButton")[0].click();
                        });
                    }
                    else{
                        document.getElementsByTagName("FORM")[0].reportValidity();
                    }
                }
                document.getElementById("price").addEventListener("click", priceEventListener);
            }
            button.addEventListener("click", manageButtonEventListener);
        }
    }

    this.registerBannerListeners = function(){
        document.getElementsByClassName("bannerButton")[0].addEventListener("click", function(){
            if(!document.getElementsByClassName("bannerButton")[0].classList.contains("selectedBanner")){
                document.getElementsByClassName("bannerButton")[0].classList.add("selectedBanner");
            }
            if(document.getElementsByClassName("bannerButton")[1].classList.contains("selectedBanner")){
                document.getElementsByClassName("bannerButton")[1].classList.remove("selectedBanner");
            }
            
            self.orchestrator.goToManagedQuotations();
        });
        document.getElementsByClassName("bannerButton")[1].addEventListener("click", function(){
            if(!document.getElementsByClassName("bannerButton")[1].classList.contains("selectedBanner")){
                document.getElementsByClassName("bannerButton")[1].classList.add("selectedBanner");
            }
            if(document.getElementsByClassName("bannerButton")[0].classList.contains("selectedBanner")){
                document.getElementsByClassName("bannerButton")[0].classList.remove("selectedBanner");
            }
    
            self.orchestrator.goToNotManagedQuotations();
        });
    }

    this.updateError = function(response){
        document.getElementsByClassName("errorMessage")[0].innerHTML += response + "<br>";
    }

    this.showError = function(){
        if(document.getElementsByClassName("errorBackground")[0].classList.contains("errorNone")){
            document.getElementsByClassName("errorBackground")[0].classList.remove("errorNone");
        }
        document.getElementsByClassName("errorBackground")[0].classList.add("errorVisible");
    }

    this.closeTab = function(){
        document.getElementsByClassName("main")[0].style.width = "60vw";
        document.getElementsByClassName("tab")[0].style.right = "-20vw";
        document.getElementsByClassName("banner")[0].style.right = "20%";
        document.getElementsByClassName("logOut")[0].style.right = "20%";

        //delete everything inside tab except banner
        var tabContainer = document.getElementsByClassName("tabContainer")[0];
        const tabChildrens = tabContainer.children;
        const tabChildrensArray = Array.from(tabChildrens);
        tabChildrensArray.forEach((node) => {
            if(node.tagName.toUpperCase() != "DIV" || !node.classList.contains("banner")){
                tabContainer.removeChild(node);
            }
        });
    }

    this.openTab = function(){
        document.getElementsByClassName("main")[0].style.width = "50vw";
        document.getElementsByClassName("tab")[0].style.right = "0vw";
        document.getElementsByClassName("banner")[0].style.right = "40%";
        document.getElementsByClassName("logOut")[0].style.right = "40%";

        //delete everything inside tab except banner
        var tabContainer = document.getElementsByClassName("tabContainer")[0];
        const tabChildrens = tabContainer.children;
        const tabChildrensArray = Array.from(tabChildrens);
        tabChildrensArray.forEach((node) => {
            if(node.tagName.toUpperCase() != "DIV" || !node.classList.contains("banner")){
                tabContainer.removeChild(node);
            }
        });
    }
}