
import { TransactionFee } from "./TransactionFee";
import { Product } from "./Product";
import { Change } from "./Cart";
export class Cart{
  id: number;
  items: Item[];
  changes: Change[];
}

export class ProductVersionKey {
  productId: number;
  version: string;
}
export class CartItemKey {
  productVersionKey: ProductVersionKey;
  cartId: number;
}

export class Item {
  cartItemKey: CartItemKey;
  productDetails: Product;
  price:number
}
export enum TransactionStatus{
	FAILED = "FAILED",COMPLETED="COMPLETED",CREATED="CREATED",
	CANCELED="CANCELED",APPROVED ="APPROVED",EXPIRED = "EXPIRED"
}

export class Payer{
	
}

export class Transaction{
	id:number;
	amount:number;
	status:TransactionStatus;
	createdDate:Date;
	lastModified:Date;
	description:string;
  paypalId: number;
  fee: TransactionFee;
	approvalUrl:string;
  payer: Payer;
  cart: Cart;
  change: boolean;
	constructor(){
		this.payer = new Payer;
    this.id = -1;
    this.cart = new Cart
    this.amount = -1;
    this.fee = new TransactionFee;
		this.status = TransactionStatus.FAILED;
		this.createdDate = new Date;
		this.lastModified = new Date;
		this.paypalId = -1;
		this.approvalUrl = '';
    this.change = false;
	}
	
	
}


