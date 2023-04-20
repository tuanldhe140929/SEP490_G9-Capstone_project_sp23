
import { Account } from "./Account";
import { CartItem } from "./CartItem";
import { User } from "./User";

export enum Type {
  UPDATED = "UPDATED" ,  REMOVED = "REMOVED"
}

export class Change {
  item: string;
  type: Type;
}
export class Cart {

  id: number;
  account:Account;
  items: CartItem[];
  totalPrice: number;
  changes: Change[];

  constructor() {
    this.id = -1;
    this.account = new Account();
    this.items = [];
    this.totalPrice = 0;
    this.changes = [];
  }
 
}
