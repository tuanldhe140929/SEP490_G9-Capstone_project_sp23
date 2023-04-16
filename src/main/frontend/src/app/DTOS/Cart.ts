
import { Account } from "./Account";
import { CartItem } from "./CartItem";
import { User } from "./User";
export class Cart {
  id: number;
  account:Account;
  items: CartItem[];
  totalPrice:number ;
  constructor() {
    this.id = -1;
    this.account = new Account();
    this.items = [];
    this.totalPrice = 0;
  }
 
}
