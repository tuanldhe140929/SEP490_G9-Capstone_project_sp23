import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../../../DTOS/Product';
import { User } from '../../../DTOS/User';
import { ManageProductService } from '../../../services/manage-product.service';
import { ProductService } from '../../../services/product.service';
import { StorageService } from '../../../services/storage.service';

@Component({
  selector: 'app-seller-product-list',
  templateUrl: './seller-product-list.component.html',
  styleUrls: ['./seller-product-list.component.css']
})
export class SellerProductListComponent {
  product: Product = new Product();
  owner: User = new User();
  constructor(
    private FormBuilder: FormBuilder,
    private storageService: StorageService,
    private manageProductService: ManageProductService,
    private route: ActivatedRoute,
    private productService: ProductService
    // private productDetailsService: ProductDetailsService
  ) {

  }
  ngOnInit(): void {
    console.log(this.getSellersId());
    var SellerId = this.getSellersId();
    if (SellerId != 0) {
      this.getProductBySellersId(this.getSellersId());
    }
  }

  sellerId: String;
  //     this.getOwner(this.owner.username)
  //   }


  //   getOwner(username: string) {
  //     this.manageProductService.getCurrentOwnerInfo(username).subscribe(
  //       data => {
  //         this.owner = data;
  //         console.log(data)
  //       },
  //       error => {
  //         console.log(error)
  //       }
  //     )
  //   }

  //   getVisitor(username: string) {
  //     if (this.storageService.isLoggedIn()) {
  //       //
  //     }
  //   }

  getSellersId(): number {
    var sellerId = this.route.snapshot.paramMap.get('sellerId');
    if (sellerId != null)
      return +sellerId;
    else
      return 0;
  }

  public ps: Product[] = []

  getProductBySellersId(SellersId: number) {
    //Lay Product sau khi da co sellerid
    this.productService.GetProductDetails(SellersId).subscribe(
      data => {
        this.ps = data;
        console.log(data)
      },
      error => {
        console.log(error)
      }
    )
  }


}
