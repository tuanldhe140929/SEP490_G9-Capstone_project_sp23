import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Cart } from '../DTOS/Cart';
import { CartItem } from '../DTOS/CartItem';

const baseUrl = "http://localhost:9000/private/cart"

@Injectable({
  providedIn: 'root'
})
export class CartService {
  removeItem(cartItem: CartItem) {
    return this.httpClient.delete<Cart>(baseUrl+"/remove/"+cartItem.product.id);
  }
  public getAllProduct(): Observable<Cart> {
   
    return this.httpClient.get<Cart>(baseUrl+"/getCurrentCartDTO");
    
  
  }
  
  constructor(private httpClient: HttpClient) {

   }
}
