
import { CartItem } from "./CartItem";
import { User } from "./User";
export class Cart {
  id: number;
  user: User;
  cartitem: CartItem;
  totalPrice:Number ;
  constructor() {
    this.id = -1;
    this.user = new User();
    this.cartitem = new CartItem();
    this.totalPrice = 0;
  }
 
}
