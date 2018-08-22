class Product {
    constructor(productTag) {
        this.productTag = productTag;
        this.product = {};
    }

    validProductDescription() {
        this.description = this.productTag.children.product_description_input.value;

        if (this.description.length < 3) return false;

        this.product.description = this.description;
        return true;
    }

    validPrice() {
        this.price = this.productTag.children.product_price_input.value;

        if (this.price < 0) return false;

        this.product.price = this.price;
        return true;
    }

    validSupplyQuantity() {
        this.supplyQuantity = this.productTag.children.product_supplyQuantity_input.value;

        if (this.supplyQuantity < 10) return false;

        this.product.supplyQuantity = this.supplyQuantity;
        return true;
    }

    validProductAll() {
        this.validProductList = [
            this.validProductDescription.bind(this),
            this.validPrice.bind(this),
            this.validSupplyQuantity.bind(this)
        ];

        let cnt = this.validProductList.length;

        this.validProductList.forEach(valid => {
            if (valid()) cnt--;
        })
        return cnt === 0 ? this.product : null;
    }
}