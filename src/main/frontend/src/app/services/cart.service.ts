import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Cart } from '../dtos/Cart';
import { CartItem } from '../dtos/CartItem';

const baseUrl = "http://localhost:9000/private/cart"

@Injectable({
  providedIn: 'root'
})
export class CartService {
  isPurchasedByUser(userId: number, productId: number): Observable<boolean> {
    return this.httpClient.get<boolean>(baseUrl + "/isUserPurchasedProduct?userId="+userId+"&productId="+productId);
  }
  removeItem(cartItem: CartItem) {
    return this.httpClient.delete<Cart>(baseUrl+"/remove/"+cartItem.product.id);
  }
  public getAllProduct(): Observable<Cart> {
    return this.httpClient.get<Cart>(baseUrl+"/getCurrentCartDTO");
  }
  
  addToCart(productId: number): Observable<any> {
    const url = `${baseUrl}/add/${productId}`;
    return this.httpClient.post(url, {});
  }
  
  constructor(private httpClient: HttpClient) {

   }
}
