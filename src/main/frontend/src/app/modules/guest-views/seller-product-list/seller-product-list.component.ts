import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from 'src/app/DTOS/Category';
import { Product } from 'src/app/DTOS/Product';
import { Seller } from 'src/app/DTOS/Seller';
import { User } from 'src/app/DTOS/User';
import { CategoryService } from 'src/app/services/category.service';
import { ManageAccountInfoService } from 'src/app/services/manage-account-info.service';
import { ProductService } from 'src/app/services/product.service';
import { SellerService } from 'src/app/services/seller.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-seller-product-list',
  templateUrl: './seller-product-list.component.html',
  styleUrls: ['./seller-product-list.component.css']
})
export class SellerProductListComponent implements OnInit{

  sellerid: number
  productList: Product[] = [];
  categoryList: Category[] = [];
  minprice: number = 0;
  maxprice: number = 10000000;
  chosenCategory: number = 0;
  keyword: string = "";
  p:number = 1;
  itemsPerPage: number = 9;
  totalResult: any;
  seller: Seller;
  user: User;
  loggedInStatus: boolean;
  sellerStatus: boolean;

  constructor(
    private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private categoryService: CategoryService,
    private router: Router,
    private sellerService: SellerService,
    private storageService: StorageService,
    private manageAccountInfoService: ManageAccountInfoService
  ){}

  ngOnInit(): void {
    this.sellerid = Number(this.activatedRoute.snapshot.paramMap.get('sellerId'));
    this.productService.getProductsBySeller(this.sellerid,"",0,0,10000000).subscribe(
      data => {
        this.productList = data;
      }
    )
    this.getAllCategories();
    this.getSellerById();
    this.checkIfIsSeller();
  }

  getAllCategories(){
    this.categoryService.getAllCategories().subscribe(
      data => {
        this.categoryList = data;
      }
    )
  }

  checkIsLoggedIn(){
    if(this.storageService.isLoggedIn()){
      this.manageAccountInfoService.getCurrentUserInfo().subscribe(
        data => {
          this.user = data;
          this.loggedInStatus = true;
        }
      )
    }
    this.loggedInStatus = false;
  }

  checkIfIsSeller(){
    if(this.storageService.isLoggedIn()){
      this.manageAccountInfoService.getCurrentUserInfo().subscribe(
        data => {
          this.user = data;
          this.loggedInStatus = true;
          if(this.loggedInStatus){
            if(this.user.id==this.sellerid){
              this.sellerStatus = true;
            }else{
              this.sellerStatus = false;
            }
          }
        }
      )
    }
    this.sellerStatus = false;
  }

  getCoverImage(product: Product): string{
    console.log(product.coverImage==null);
    if(product.coverImage!=null){
      return 'http://localhost:9000/public/serveMedia/image?source=' + product.coverImage.replace(/\\/g, '/');
    }else{
      return 'assets/images/noimage.png'
    }
    
  }
  
  openDetails(id: number){
    this.router.navigate(['/products',id]);
  }


  onChangeCategory(event: any){
    console.log(this.chosenCategory)
  }

  compareMinMax(): boolean{
    if(this.minprice<=this.maxprice&&this.minprice!=null&&this.maxprice!=null){
      return true;
    }else{
      return false;
    }
  }

  getSellerById(){
    this.sellerService.getSellerById(this.sellerid).subscribe(
      data => {
        this.seller = data;
      }
    )
  }

  refresh(){
    this.productService.getProductsBySeller(this.sellerid,this.keyword,this.chosenCategory,this.minprice,this.maxprice).subscribe(
      data => {
        this.productList = data;
      }
    )
  }
}
