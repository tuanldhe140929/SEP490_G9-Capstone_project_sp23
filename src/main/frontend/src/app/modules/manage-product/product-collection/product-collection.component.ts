
import { ProductService } from './../../../services/product.service';
import { Seller } from './../../../DTOS/Seller';
import { ProductDetailsComponent } from './../../for-users/product-details/product-details.component';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ManageProductService } from 'src/app/services/manage-product.service';
import { StorageService } from 'src/app/services/storage.service';
import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/DTOS/User';
import { Product } from 'src/app/DTOS/Product';
@Component({
  selector: 'app-product-collection',
  templateUrl: './product-collection.component.html',
  styleUrls: ['./product-collection.component.css']
})
export class ProductCollectionComponent implements OnInit{
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
    if(SellerId !=0){
     this.getProductBySellersId(this.getSellersId());}
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

getSellersId():number{
  var sellerId= this.route.snapshot.paramMap.get('sellerId');
  if(sellerId!=null)
  return+sellerId;
  else
  return 0;
}

public ps: Product[] = []

getProductBySellersId(SellersId: number){
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
