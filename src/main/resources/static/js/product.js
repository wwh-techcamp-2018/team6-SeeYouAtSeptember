class Product {
    constructor(productTag) {
        this.productTag = productTag;
        this.product = {};
    }


    setProductTitle() {
        this.title = this.productTag.children.product_title_input.value;

        if (this.title.length < 3) return false;

        this.product.title = this.title;
        return true;
    }

    setProductDescription() {
        this.description = this.productTag.children.product_description_input.value;

        if (this.description.length < 3) return false;

        this.product.description = this.description;
        return true;
    }

    setPrice() {
        this.price = this.productTag.children.product_price_input.value;

        if (this.price < 0){
            return false;
        }
        this.product.price = this.price;
        return true;
    }

    setSupplyQuantity() {
        this.quantitySupplied = this.productTag.children.product_supplyQuantity_input.value;

        if (this.quantitySupplied < 10){
            return false;
        }
        this.product.quantitySupplied = this.quantitySupplied;
        return true;
    }

    setProductAll() {
        this.productInfoSettingFuncList = [
            this.setProductDescription.bind(this),
            this.setPrice.bind(this),
            this.setSupplyQuantity.bind(this)
        ];

        let cnt = this.productInfoSettingFuncList.length;

        this.productInfoSettingFuncList.forEach(settingFunc => {
            if (settingFunc()) cnt--;
        })
        return cnt === 0 ? this.product : null;
    }
}