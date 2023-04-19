import { Item, Transaction } from 'src/app/dtos/Transaction';
import { ProductDTO } from 'src/app/dtos/ProductDTO';
import { AuthResponse } from './../../../dtos/AuthResponse';
import { Component } from '@angular/core';
import { Product } from 'src/app/dtos/Product';
import { TransactionService } from 'src/app/services/transaction.service';
import { StorageService } from 'src/app/services/storage.service';
import { User } from 'src/app/dtos/User';
import { ProductDetailsDTO } from 'src/app/dtos/ProductDetailsDTO';
import { PaginationInstance } from 'ngx-pagination';
import { Router } from '@angular/router';
import { ProductFileService } from '../../../services/product-file.service';

export class DisplayItem {
  item: Item;
  date: Date;

  public static fromTransaction(transaction: Transaction, i: Item): DisplayItem {
    var item: DisplayItem = new DisplayItem;
    i.price = Number.parseFloat(i.price.toFixed(2));
    item.item = i;
    item.date = transaction.lastModified;
    console.log(item.date);
    return item;
  }
}
@Component({
  selector: 'app-purchased-product-list',
  templateUrl: './purchased-product-list.component.html',
  styleUrls: ['./purchased-product-list.component.css']
})

export class PurchasedProductListComponent {
  authResponse: AuthResponse = new AuthResponse;
  purchasedList: Transaction[] = [];
  displayItems: DisplayItem[] = [];
  user: User = new User();

  userId = this.user.id
  constructor(
    private transactionService: TransactionService,
    private storageService: StorageService,
    private router: Router,
    private productFileService: ProductFileService
  ) {}
 
  totalLength:any;
  page:number = 1;
  showpost:any = [];
  ngOnInit(): void {
    this.getPurchasedProductList();
  }

  getPurchasedProductList() {
    this.transactionService.getPurchasedProductList().subscribe(
      data => {
        console.log(data);
        this.purchasedList = data;
        for (let i = 0; i < this.purchasedList.length; i++) {
          console.log(this.purchasedList[i]);
          for (let j = 0; j < this.purchasedList[i].cart.items.length; j++) {
            var item = DisplayItem.fromTransaction(this.purchasedList[i], this.purchasedList[i].cart.items[j]);
            console.log(item);
            this.displayItems.push(item);
          }
        }
        console.log(this.displayItems);
      }
    )
  }
  formatTime(createdDate: Date){
    const timestamp = createdDate;
    const date = new Date(timestamp);
    const formattedDate = date.getDate().toString().padStart(2, '0') + '-' +
                          (date.getMonth() + 1).toString().padStart(2, '0') + '-' +
                          date.getFullYear().toString();
    
    const formattedTime = date.getHours().toString().padStart(2, '0') + ':' +
                          date.getMinutes().toString().padStart(2, '0') + ':' +
                          date.getSeconds().toString().padStart(2, '0');
    
    const formattedTimestamp = formattedDate + ' ' + formattedTime;
    return formattedTimestamp;
  }

  download(item: DisplayItem) {
    var token = "";
    this.productFileService.generateDownloadToken(0, item.item.cartItemKey.productVersionKey.productId).subscribe(
      (data: string) => {
        console.log(data);
        token = data;
        this.router.navigate(['/download', item.item.cartItemKey.productVersionKey.productId], { queryParams: { token: token } });
      }
    );
  }

  viewProduct(item: DisplayItem) {
    this.router.navigate(['products/' + item.item.cartItemKey.productVersionKey.productId]);
  }
  
}
