test=''
class ProjectForm {
  constructor() {
    this.productList = [];
    addEventListenerToTarget($("#create_project_btn"),"click",this.createProjectBtnHandler.bind(this));
    addEventListenerToTarget($(".projects_form.img input"),"change",this.insertImgFile.bind(this));
    addEventListenerToTarget($("#addProduct"),"click",this.addProductCreateFormHandler.bind(this));
    addEventListenerToTarget($(".products_addList"),"click",this.removeProductCreateFormHandler.bind(this));

  this.focusOutProjectsInfoTargetList = [
      $("#projects_title_input"),
      $("#projects_goalFundRaising_input"),
      $("#projects_endAt_input")
  ];

  this.focusOutProjectsInfoTargetList.forEach(target => {
      addEventListenerToTarget(target,"focusout",this.focusOutProjectInputHandler.bind(this));
    });
  }

  addProductCreateFormHandler(){
    const productTag = $('.products_addList');
    const html = ` <div class="product_addInfo">
                        <span>물품 설명:</span><input type="text" id="product_description_input"><br>
                        <span>물품 가격:</span><input type="number" value="0" min="0" step="100" id="product_price_input"><br>
                        <span>물품 수량:</span><input type="number" value="10" min="10" step="1" id="product_supplyQuantity_input"><br>
                        <button id="removeProduct">물품 빼기</button>
                    </div> `
    productTag.insertAdjacentHTML('beforeend',html);
    this.productList.push(new Product(productTag.lastElementChild));   
  }

  removeProductCreateFormHandler(evt){
    const maybeRemoveProductBtn = evt.target;
    if(maybeRemoveProductBtn.id === "removeProduct"){
       
    for(let product of iterable){
        if(product.productTag === maybeRemoveProductBtn.parentElement){ 
            this.productList.splice(i,1) 
            break;
        }
    }
    
     maybeRemoveProductBtn.parentElement.remove();
    }
  }

  validProjectAll() {
    this.validProjectList = [
      this.validTitle.bind(this),
      this.validEndAt.bind(this),
      this.validGoalFundRaising.bind(this)
    ];

    this.cnt = this.validProjectList.length;
    this.validProjectList.forEach(valid => {
      if (valid()) this.cnt--;
    });
    return this.cnt === 0;
  }

  validTitle() {
    const minTitleLength = 5;
    this.title = $("#projects_title_input").value;
    return this.title.length >= minTitleLength;
  }

  validEndAt() {
    this.endAt = new Date($("#projects_endAt_input").value);
    this.currentDate = new Date();
    this.currentDate.setDate(this.currentDate.getDate() + 30);
    return this.endAt > this.currentDate;
  }

  validGoalFundRaising() {
    const minGoalFundRaising = 1000000;
    this.goalFundRaising = $("#projects_goalFundRaising_input").value;
    return this.goalFundRaising >= minGoalFundRaising;
  }

  focusOutProjectInputHandler(evt) {
    if (evt.target.id === "projects_title_input") this.validTitle();
    if (evt.target.id === "projects_goalFundRaising_input")
      this.validGoalFundRaising();
    if (evt.target.id === "projects_endAt_input") this.validEndAt();
  }

  insertImgFile(evt) {
    const maybeImg = evt.target.files[0];
    if (maybeImg["type"].split("/")[0] === "image") {
      this.thumbnailUrl = maybeImg;
      this.readImage();
    }
  }

  readImage() {
    const fileReader = new FileReader();
    fileReader.onload = () => {
      $("#thumbnailUrl").src = fileReader.result;
    };
    fileReader.readAsDataURL(this.thumbnailUrl);
  }

  createProjectBtnHandler(evt) {
    evt.preventDefault();

    if (!this.validProjectAll()) return;
    
    const products = [];
    this.productList.forEach(product=>{
        products.push(product.validProductAll());
    })

    const projectForm = new FormData();

    const project = {
        "title":this.title,
        "decription":editor.getHtml(),
        "goalFundRaising":this.goalFundRaising,
        "cid":$('.categories_dropbox select').value,
        "endAt":this.endAt.getTime(),
        "products": JSON.stringify(products)
    };

    projectForm.append("project",JSON.stringify(project));
    if (this.thumbnailUrl !== undefined) {
        projectForm.append("file",this.thumbnailUrl);
    }
    test = projectForm;
    fetchManager({
        url : "/api/projects",
        method : "POST",
        body : projectForm , 
        headers :{"content-type": "multipart/mixed"}, 
        callback : this.createProjectCallback.bind(this)
    })
  }

  createProjectCallback(response){
      response.json().then(result=>{
          console.log(result);
      })
  }
  
}

document.addEventListener("DOMContentLoaded", () => {
  new ProjectForm();
  editor = new tui.Editor({
    el: document.querySelector("#editSection"),
    initialEditType: "markdown",
    previewStyle: "vertical",
    height: "300px"
  });
});
