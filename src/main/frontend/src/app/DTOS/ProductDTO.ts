import { License } from "./License";
import { Product } from "./Product";
import { Seller } from "./Seller";

export class ProductDTO {
  id: number;
  enabled: boolean;
  seller: Seller;
  draft: boolean;
    license: License;
  productDetails: Product[];
  constructor() {

  }
}
