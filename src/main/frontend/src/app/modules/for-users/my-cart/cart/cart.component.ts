import { Component } from '@angular/core';
import { Product } from 'src/app/DTOS/Product';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {

  cartItemList: Product[] = [];
  itemname: any;
  itemprice:any;

  ngOnInit(): void {
    this.getAllCartItem();
  }
  public getAllCartItem(){
    
  }
  public RemoveItem( ): void{

console.log('xóa thành công ')
this.RemoveItem === null
}
public checkout(): void{
  console.log('check it out')
}

}
