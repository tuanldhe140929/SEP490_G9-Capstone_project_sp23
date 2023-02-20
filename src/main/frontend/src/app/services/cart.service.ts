import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Cart } from '../DTOS/Cart';
const baseUrl = "http://localhost:9000/private/cart"
export class CartService {
  public getAllProduct(): Observable<Cart[]> {
   
        return this.ht    
  }
  
  constructor(private httpClient: HttpClient) { }
}
