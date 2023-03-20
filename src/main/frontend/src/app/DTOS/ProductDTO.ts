import { Product } from "./Product";
import { Seller } from "./Seller";

export class ProductDTO {
  id: number;
  enabled: boolean;
  seller: Seller;
  draft:boolean;
  productDetails: Product[];
  constructor() {

  }
}
