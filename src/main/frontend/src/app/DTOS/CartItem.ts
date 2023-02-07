

import { Cart } from "./Cart";
import { Product } from "./Product";
export class CartItem {
  

  cartId: Number;
  productId:number;
  price:Number;


  constructor() {
    this.cartId = new Cart().id;
    this.productId=new Product().id;
    this.price =0;


    
  }
 
}
