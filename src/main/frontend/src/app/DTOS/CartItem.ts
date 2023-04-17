

import { Version } from "@angular/compiler";
import { Cart } from "./Cart";
import { Product } from "./Product";
export class CartItem {
  
  product:Product;
  cartId: number;
	price:number;


  constructor() {
    this.cartId = new Cart().id;
  this.product = new Product;
this.price = -1;
    
  }
 
}
