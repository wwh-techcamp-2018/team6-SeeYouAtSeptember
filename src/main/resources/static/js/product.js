class Product {
    constructor(productTag) {
        this.productTag = productTag;
        this.product = {};
    }

    setTitle() {
        const titleTag = $at(this.productTag, ".title")
        this.title = titleTag.value;
        if (this.title.trim().length < 1 || this.title.trim().length > 30) {
            highlightBorderInvalid(titleTag);
            return false;
        }
        highlightBorderValid(titleTag);
        this.product.title = this.title;
        return true;
    }

    setDescription() {
        const descriptionTag = $at(this.productTag, ".description");
        this.description = descriptionTag.value;
        if (this.description.trim().length < 1 || this.description.trim().length > 100) {
            highlightBorderInvalid(descriptionTag);
            return false;
        }
        highlightBorderValid(descriptionTag);
        this.product.description = this.description;
        return true;
    }

    setPrice() {
        const priceTag = $at(this.productTag, ".price")
        this.price = priceTag.value;
        if (this.price < 1000) {
            highlightBorderInvalid(priceTag);
            return false;
        }
        highlightBorderValid(priceTag);
        this.product.price = this.price;
        return true;
    }

    setSupplyQuantity() {
        const quantityTag = $at(this.productTag, ".quantity")
        this.quantitySupplied = quantityTag.value;
        if (this.quantitySupplied < 1) {
            highlightBorderInvalid(quantityTag);
            return false;
        }
        highlightBorderValid(quantityTag);
        this.product.quantitySupplied = this.quantitySupplied;
        return true;
    }

    setProductAll() {
        let ret = true;
        ret &= this.setTitle();
        ret &= this.setDescription();
        ret &= this.setPrice();
        ret &= this.setSupplyQuantity();
        return ret;
    }
}