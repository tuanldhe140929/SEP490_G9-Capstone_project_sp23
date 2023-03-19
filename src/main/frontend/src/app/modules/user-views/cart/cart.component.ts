import { Component, OnInit } from '@angular/core';
import { Cart } from 'src/app/DTOS/Cart';
import { CartItem } from 'src/app/DTOS/CartItem';
import { Product } from 'src/app/DTOS/Product';
import { CartService } from 'src/app/services/cart.service';
import { TransactionService } from 'src/app/services/transaction.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit{

  cart: Cart = new Cart;
  cartItemList: CartItem[] = [];
  itemname: any;
  itemprice:any;
  constructor(private cartService: CartService, private transactionService:TransactionService) {

   }
  ngOnInit(): void {
    this.getAllCartItem();
  }
  public getAllCartItem(){
    this.cartService.getAllProduct().subscribe(
      data=>{
		  console.log(data);
        this.cart = data;
        this.cartItemList=data.items;
      },
      (error)=>{
        console.log(error);
      }
    )
  }
public getItemCoverImageSrc(cartItem:CartItem){
  if(cartItem!=null && cartItem.product.id!=-1 && cartItem.product.coverImage!=null){
	  
  return 'http://localhost:9000/public/serveMedia/image?source='+cartItem.product.coverImage.replace(/\\/g, '/');
}
else{
  return "";
}
}

  public RemoveItem(cartItem:CartItem): void{

    this.cartService.removeItem(cartItem).subscribe(
      (data)=>{
        this.cart = data;
       this.cartItemList = data.items;
      },
      (error)=>{
        console.log(error);
      }
    )
console.log('xóa thành công '+ cartItem.product.id)
this.RemoveItem === null
}
public checkout(): void{
  this.transactionService.purchase(this.cart.id).subscribe(
	  data=>{
		  console.log(data.approvalUrl);
		  var strWindowFeatures = "location=yes,height=570,width=520,scrollbars=yes,status=yes";
var URL = data.approvalUrl;
window.open(URL, "_blank", strWindowFeatures);
	  },
	  error=>{
		  console.log(error);
	  }
  )
}

get TotalPrice(){
  return this.cart.totalPrice;
}

}
