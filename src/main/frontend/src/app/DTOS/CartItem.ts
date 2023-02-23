

import { Version } from "@angular/compiler";
import { Cart } from "./Cart";
import { Product } from "./Product";
export class CartItem {
  
  product:Product;
  cartId: Number;



  constructor() {
    this.cartId = new Cart().id;
  this.product = new Product;

    
  }
 
}
