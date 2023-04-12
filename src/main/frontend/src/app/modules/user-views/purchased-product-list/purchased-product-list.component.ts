import { Transaction } from 'src/app/dtos/Transaction';
import { ProductDTO } from 'src/app/dtos/ProductDTO';
import { AuthResponse } from './../../../dtos/AuthResponse';
import { Component } from '@angular/core';
import { Product } from 'src/app/dtos/Product';
import { TransactionService } from 'src/app/services/transaction.service';
import { StorageService } from 'src/app/services/storage.service';
import { User } from 'src/app/dtos/User';
import { ProductDetailsDTO } from 'src/app/dtos/ProductDetailsDTO';


@Component({
  selector: 'app-purchased-product-list',
  templateUrl: './purchased-product-list.component.html',
  styleUrls: ['./purchased-product-list.component.css']
})
export class PurchasedProductListComponent {
  authResponse: AuthResponse = new AuthResponse;
  purchasedList: ProductDetailsDTO[] = [];
  user: User = new User();
  userId = this.user.id
  constructor(
    private transactionService: TransactionService,
    private storageService: StorageService
  ) { }

  ngOnInit(): void {
    this.getPurchasedProductList();
  }

  getPurchasedProductList() {
    this.transactionService.getPurchasedProductList().subscribe(
      data => {
        console.log(data);
        this.purchasedList = data;
      }
    )
  }
}
