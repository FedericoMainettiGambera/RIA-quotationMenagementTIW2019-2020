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

    this.cleanWorkSpace = function () {
        document.getElementsByClassName("mainContent")[0].innerHTML="";
        document.getElementsByClassName("tabContainer")[0].innerHTML="";
    }

    this.updateQuotations = function(){
        var container = document.getElementsByClassName("mainContent")[0];
        self.model.getQuotations().forEach(quotation => {
            var quotationContainer = document.createElement("DIV");
            quotationContainer.classList.add("quotationContainer");
            container.appendChild(quotationContainer);

            var quotationImage = document.createElement("DIV");
            quotationImage.classList.add("quotationImage");
            quotationContainer.appendChild(quotationImage);

            var image = document.createElement("IMG");
            image.setAttribute("src", "./Image?productID=" + quotation.getProduct().getID());
            quotationImage.appendChild(image);

            var quotationInfo = document.createElement("DIV");
            quotationInfo.classList.add("quotationInfo");
            quotationContainer.appendChild(quotationInfo);

            var quotationName = document.createElement("DIV");
            quotationName.classList.add("quotationName");
            quotationName.innerHTML = "" + quotation.getProduct().getName();
            quotationInfo.appendChild(quotationName);

            var options = document.createElement("DIV");
            options.classList.add("options");
            quotationInfo.appendChild(options);

            var div = document.createElement("DIV");
            options.appendChild(div);

            var optionLabel = document.createElement("LABEL");
            optionLabel.classList.add("optionLabel");
            optionLabel.innerHTML = "Options:";
            options.appendChild(optionLabel);

            quotation.getProduct().getOptions().forEach(option => {
                var optionContainer = document.createElement("DIV");
                optionContainer.classList.add( option.getType() + "Div");
                options.appendChild(optionContainer);

                if(option.getType() === "promotion"){
                    var promotionText = document.createElement("SPAN");
                    promotionText.classList.add("promotionText");
                    promotionText.innerHTML = "promotion";
                    optionContainer.appendChild(promotionText);
                }

                var optionName = document.createElement("SPAN");
                optionName.classList.add(option.getType() +  "Option");
                optionName.innerHTML = "" + option.getName();
                optionContainer.appendChild(optionName);
            });

            if(quotation.getPrice() != null){
                if(quotation.getPrice().getDecimalPart() <= 9){
                    options.innerHTML = options.innerHTML +  "<span class=\"price\">Price: " + quotation.getPrice().getWholePart() + ".0" + quotation.getPrice().getDecimalPart() + "</span>";
                }
                else{
                    options.innerHTML = options.innerHTML +  "<span class=\"price\">Price: " + quotation.getPrice().getWholePart()  +"."+ quotation.getPrice().getDecimalPart()  + "</span>";
                }
            }
            else{
                options.innerHTML = options.innerHTML + " <span class=\"price\">Price: waiting to be quoted..."  + "</span>";
            }
        });
    }

    this.updateNewQuotationTab = function(){
        var container = document.getElementsByClassName("tabContainer")[0];
        container.innerHTML =
        "              <div class=\"quotationsBanner\">Your quotations:</div>"
        + "            <div class=\"NQtitle\">Start creating NOW!</div>"
        + "            <div class=\"NQlorem\">"
        + "                Lorem ipsum dolor sit amet consectetur adipisicing elit. Eveniet illum voluptates. omnis eaque harum voluptas, corporis, exercitationem, maxime possimus fugiat reiciendis quibusdam tenetur officiis vero."
        + "            </div>"
        + "            <div id=\"newQuotationButton\" class=\"button\">"
        + "                NEW QUOTATION"
        + "            </div>";
    }

    this.createProductsPage = function(){
        self.productInfo = [];

        document.getElementsByClassName("tabContainer")[0].innerHTML = 
        "                  <div class=\"noProductSelectedContainer\">"
        + "                    <div  class=\"Otitle\"> "
        + "                        <span>SELECT A PRODUCT</span>"
        + "                    </div>"
        + "                    <div class=\"Otitle\">"
        + "                        <span>TO SEE ALL THE INFORMATION</span>"
        + "                    </div>"
        + "                    <div class=\"Otitle\"> "
        + "                        <span>AND ALL THE OPTIONS</span>"
        + "                    </div>"
        + "                </div>";
        
        var productsBanner = document.createElement("DIV");
        productsBanner.classList.add("productsBanner");
        productsBanner.innerHTML= "Select a product:";
        document.getElementsByClassName("tabContainer")[0].appendChild(productsBanner);

        var backButton = document.createElement("DIV");
        backButton.classList.add("backButton");
        backButton.innerHTML= "goBACK";
        document.getElementsByClassName("tabContainer")[0].appendChild(backButton);

        var mainContent = document.getElementsByClassName("mainContent")[0];

        var productsContainer = document.createElement("DIV");
        productsContainer.classList.add("productsContainer");
        mainContent.appendChild(productsContainer);

        self.model.getAllProducts().forEach(product => {
            var productContainer = document.createElement("DIV");
            productContainer.classList.add("productContainer");
            productContainer.classList.add("flexCenter");
            productsContainer.appendChild(productContainer);

            var productImage = document.createElement("DIV");
            productImage.classList.add("productImage");
            productContainer.appendChild(productImage);

            var image = document.createElement("IMG");
            image.setAttribute("src", "./Image?productID=" + product.getID());
            productImage.appendChild(image);

            var chooseButton = document.createElement("DIV");
            chooseButton.classList.add("chooseButton");
            chooseButton.innerHTML = "MORE INFO";
            productContainer.appendChild(chooseButton); 
            
            chooseButton.addEventListener("click", function (){
                self.model.setProductSelected(product.getID());
                self.model.setOptionsSelected(new Array());
                self.closeRight();
                self.renderAfterCloseRight(self.chooseButtonFunction, product);
            });
        });

        document.getElementsByClassName("backButton")[0].addEventListener("click", function(){            
            self.orchestrator.goToHome();
        })

    }

    this.chooseButtonFunction = function(productInfo){
        var tabContainer = document.getElementsByClassName("tabContainer")[0];

        tabContainer.innerHTML ="";

        var productsBanner = document.createElement("DIV");
        productsBanner.classList.add("productsBanner");
        productsBanner.innerHTML= "Select a product:";
        tabContainer.appendChild(productsBanner);

        var backButton = document.createElement("DIV");
        backButton.classList.add("backButton");
        backButton.innerHTML= "goBACK";
        tabContainer.appendChild(backButton);

        tabContainer.innerHTML = tabContainer.innerHTML 
            + "            <div class=\"productImageInOptionTab\">"
            + "                <img src=\"./Image?productID=" + productInfo.getID() + "\">"
            + "            </div>" 
            + "            <div class=\"productInfoContainer\">"
            + "                <div class=\"prodNameInOptionTab \">" + productInfo.getName().toUpperCase() + "</div>"
            + "                <div class=\"lorem\">A really beautiful 3D-solid.</div>"
            + "            </div>";
        
        var productInfoContainer = document.createElement("DIV");
        productInfoContainer.classList.add("productInfoContainer");
        productInfoContainer.innerHTML = "<div class=\"prodNameInOptionTab \">OPTIONS:</div>";
        tabContainer.appendChild(productInfoContainer);

        var optionsContainer = document.createElement("DIV");
        optionsContainer.classList.add("optionsContainer");
        productInfoContainer.appendChild(optionsContainer);
        
        //check for promotions
        optionsContainer.innerHTML = "<div>promotions:</div>";
        productInfo.getOptions().forEach(option => {
            if(option.getType() === "promotion"){
                var optionDIV1 = document.createElement("DIV");
                optionDIV1.classList.add("option");
                optionsContainer.appendChild(optionDIV1);

                var span1 = document.createElement("SPAN");
                span1.classList.add("optionSpan");
                span1.innerHTML = "" + option.getName();
                span1.setAttribute("optionID", option.getID());
                optionDIV1.appendChild(span1); 
            }
        });

        //check for normals
        optionsContainer.innerHTML = optionsContainer.innerHTML + "<br><div id=\"normals\">normals:</div>";
        productInfo.getOptions().forEach(option => {
            if(option.getType() !== "promotion"){
                var optionDIV2 = document.createElement("DIV");
                optionDIV2.classList.add("option");
                optionsContainer.appendChild(optionDIV2);

                var span2 = document.createElement("SPAN");
                span2.classList.add("optionSpan");
                span2.innerHTML = "" + option.getName();
                span2.setAttribute("optionID", option.getID());
                optionDIV2.appendChild(span2);
            }
        });
        optionsContainer.innerHTML = optionsContainer.innerHTML + "<br>";

        var createQuotationButton = document.createElement("SPAN");
        createQuotationButton.classList.add("button");
        createQuotationButton.classList.add("createQuotation");
        createQuotationButton.innerHTML = "CREATE QUOTATION";

        var quotationError = document.createElement("DIV");
        quotationError.classList.add("quotationError");
        tabContainer.appendChild(quotationError);

        createQuotationButton.addEventListener("click", function(){
            document.getElementsByClassName("createQuotation")[0].style.display = "none"; //avoid double click of button

            for (let j = 0; j < self.model.getOptionsSelected().length; j++) {
                const optionID = self.model.getOptionsSelected()[j];
                if(optionID != null){
                    self.serverGate.submitQuotation();
                    return;
                }
            }

            //must select at least one option:
            document.getElementsByClassName("quotationError")[0].innerHTML = "Select at least one option.";
            document.getElementsByClassName("createQuotation")[0].style.display = "inline";

        })
        tabContainer.appendChild(createQuotationButton);

        var spans = document.getElementsByClassName("optionSpan")
        for (let index = 0; index < spans.length; index++) {
            const span = spans[index];
            span.addEventListener("click", function(){
                self.selectOption(span, span.getAttribute("optionID"));
            });
        }

        document.getElementsByClassName("backButton")[0].addEventListener("click", function(){
            self.orchestrator.goToHome();
        })

        self.openRight();
    }

    this.selectOption = function (spanElement, optionID){
        if(spanElement.classList.contains("selected")){ //deselecting
            spanElement.classList.remove("selected");
            spanElement.classList.add("optionSpan");
            //delete from model the option deselected. (optionID)
            for (let i = 0; i < self.model.getOptionsSelected().length; i++) {
                const option = self.model.getOptionsSelected()[i];
                if(option == optionID){
                    self.model.getOptionsSelected()[i] = null;
                }
            }
        }
        else{ //selecting
            spanElement.classList.remove("optionSpan");
            spanElement.classList.add("selected");
            //save in model the option selected. (optionID)
            self.model.getOptionsSelected().push(optionID);
        }
    };

    this.updateError = function(response){
        document.getElementsByClassName("errorMessage")[0].innerHTML += response + "<br>";
    }

    this.showError = function(){
        if(document.getElementsByClassName("errorBackground")[0].classList.contains("errorNone")){
            document.getElementsByClassName("errorBackground")[0].classList.remove("errorNone");
        }
        document.getElementsByClassName("errorBackground")[0].classList.add("errorVisible");
    }

    this.open = function(){
        document.getElementsByClassName("leftHider")[0].style.width = "0%";
        document.getElementsByClassName("rightHider")[0].style.width = "0%";
    }

    this.close = function(){
        document.getElementsByClassName("leftHider")[0].style.width = "60%";
        document.getElementsByClassName("rightHider")[0].style.width = "40%";
    }

    this.renderAfterClose = function(renderFunction){
        if(document.getElementsByClassName("leftHider")[0].offsetWidth + document.getElementsByClassName("rightHider")[0].offsetWidth 
            >=  document.getElementsByTagName("body")[0].offsetWidth){ //if is almost closed or closed we can update the content and open.
            renderFunction();
        }
        else{ //if is not already closed, set an event listener to see when is closed and than update content
            var onTransitionEnd = function(){
                document.getElementsByClassName("rightHider")[0].removeEventListener("transitionend", onTransitionEnd);
                renderFunction();
            }
            document.getElementsByClassName("rightHider")[0].addEventListener("transitionend", onTransitionEnd);
        }
    }

    this.closeRight = function(){
        document.getElementsByClassName("rightHider")[0].style.width = "40%";
    }

    this.openRight = function(){
        document.getElementsByClassName("rightHider")[0].style.width = "0%";
    }

    this.renderAfterCloseRight = function(renderFunction, productInfo){
        if(document.getElementsByClassName("rightHider")[0].offsetWidth >=  ((document.getElementsByTagName("body")[0].offsetWidth*39)/100) ){ //if is almost closed or closed we can update the content and open.
            renderFunction();
        }
        else{ //if is not already closed, set an event listener to see when is closed and than update content
            var onTransitionEnd = function(){
                document.getElementsByClassName("rightHider")[0].removeEventListener("transitionend", onTransitionEnd);
                renderFunction(productInfo);
            }
            document.getElementsByClassName("rightHider")[0].addEventListener("transitionend", onTransitionEnd);
        }
    }
}