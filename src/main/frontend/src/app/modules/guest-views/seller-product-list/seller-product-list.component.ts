import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Category } from 'src/app/dtos/Category';
import { Product } from 'src/app/dtos/Product';
import { Seller } from 'src/app/dtos/Seller';
import { User } from 'src/app/dtos/User';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { SellerService } from 'src/app/services/seller.service';
import { StorageService } from 'src/app/services/storage.service';
import { TagService } from 'src/app/services/tag.service';
import { UserService } from 'src/app/services/user.service';
import { DeleteProductComponent } from './delete-product/delete-product.component';

@Component({
  selector: 'app-seller-product-list',
  templateUrl: './seller-product-list.component.html',
  styleUrls: ['./seller-product-list.component.css']
})
export class SellerProductListComponent implements OnInit {

  @ViewChild('infoModal', { static: false }) private infoModal: any;

  sellerid: number;
  productList: Product[] = [];
  displayForSeller: Product[] = [];
  displayForUser: Product[] = [];
  categoryList: Category[] = [];
  tagList: Category[] = [];
  minprice: number = 0;
  maxprice: number = 10000000;
  chosenCategory: number = 0;
  keyword: string = "";
  p: number = 1;
  itemsPerPage: number = 9;
  totalResult: any;
  seller: Seller;
  user: User;
  loggedInStatus: boolean;
  sellerStatus: boolean = false;


  constructor(
    private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private categoryService: CategoryService,
    private tagService: TagService,
    private router: Router,
    private sellerService: SellerService,
    private storageService: StorageService,
    private userService: UserService,
    private modalService: NgbModal,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.sellerid = Number(this.activatedRoute.snapshot.paramMap.get('sellerId'));
    this.sellerService.getSellerById(this.sellerid).subscribe(data => {
      this.seller = data;
      this.getSellerAvatar();
    });
    if(this.storageService.isLoggedIn()){
      this.userService.getCurrentUserInfo().subscribe(data => {
        this.user = data;
        if(this.user.id == this.sellerid){
          this.productService.getProductsBySellerForSeller(this.sellerid, "", 0, [],0, 10000000).subscribe(
            data => {
              this.productList = data;
              this.sellerStatus = true;
            }
          )
        }else{
          this.productService.getProductsBySellerForUser(this.sellerid, "", 0, [],0, 10000000).subscribe(
            data => {
              console.log(data);
              this.productList = data;
              this.sellerStatus = false;
            },
            error => {
              console.log(error);
            }
          )
        }
      })
    }else{
      this.productService.getProductsBySellerForUser(this.sellerid, "", 0,[], 0, 10000000).subscribe(
        data => {
          this.productList = data;
          this.sellerStatus = false;
        }
      )
    }
    
    // this.productService.getProductsBySellerForUser(this.sellerid, "", 0, 0, 10000000).subscribe(
    //   data => {
    //     this.displayForUser = data;
    //   }
    // )
    this.getAllCategories();
    this.getAllTags();
    this.getSellerById();
  }

  getAllCategories() {
    this.categoryService.getAllCategories().subscribe(
      data => {
        this.categoryList = data;
      }
    )
  }

  getAllTags(){
    this.tagService.getAllTags().subscribe(
      data => {
        this.tagList = data;
      }
    )
  }

  checkIsLoggedIn() {
    if (this.storageService.isLoggedIn()) {
      this.userService.getCurrentUserInfo().subscribe(
        data => {
          this.user = data;
          this.loggedInStatus = true;
        }
      )
    }else{
      this.loggedInStatus = false;
    }
    
  }

  checkIfIsSeller() {
    if (this.storageService.isLoggedIn()) {
      this.userService.getCurrentUserInfo().subscribe(
        data => {
          this.user = data;
          console.log(data);
          if (this.user.id == this.sellerid) {
            this.sellerStatus = true;
          } else {
            this.sellerStatus = false;
          }
        }
      )
    }else{
      this.sellerStatus = false;
    }
  }

  getCoverImage(product: Product): string {
    if (product.coverImage != null) {
      return 'http://localhost:9000/public/serveMedia/image?source=' + product.coverImage.replace(/\\/g, '/');
    } else {
      return 'assets/images/noimage.png'
    }

  }

  openDetails(id: number) {
    this.router.navigate(['/products', id]);
  }


  onChangeCategory(event: any) {
    console.log(this.chosenCategory)
  }

  compareMinMax(): boolean {
    if (this.minprice <= this.maxprice && this.minprice != null && this.maxprice != null) {
      return true;
    } else {
      return false;
    }
  }

  checkValidMinMax(): boolean{
    if(this.minprice<0||this.maxprice<0||!Number.isInteger(this.minprice)||!Number.isInteger(this.maxprice)){
      return false;
    }else{
      return true;
    }
  }

  getSellerById() {
    this.sellerService.getSellerById(this.sellerid).subscribe(
      data => {
        this.seller = data;
      }
    )
  }

  refresh() {
    console.log(this.checkedTags);
    if(this.storageService.isLoggedIn()){
      this.userService.getCurrentUserInfo().subscribe(data => {
        this.user = data;
        if(this.user.id == this.sellerid){
          this.productService.getProductsBySellerForSeller(this.sellerid, this.keyword, this.chosenCategory, this.checkedTags, this.minprice, this.maxprice).subscribe(
            data => {
              this.productList = data;
              this.sellerStatus = true;
            }
          )
        }else{
          this.productService.getProductsBySellerForUser(this.sellerid, this.keyword, this.chosenCategory, this.checkedTags, this.minprice, this.maxprice).subscribe(
            data => {
              this.productList = data;
              this.sellerStatus = false;
            }
          )
        }
      })
    }else{
      this.productService.getProductsBySellerForUser(this.sellerid, this.keyword, this.chosenCategory, this.checkedTags, this.minprice, this.maxprice).subscribe(
        data => {
          this.productList = data;
          this.sellerStatus = false;
        }
      )
    }
    // this.productService.getProductsBySellerForSeller(this.sellerid, this.keyword, this.chosenCategory, [], this.minprice, this.maxprice).subscribe(
    //   data => {
    //     this.productList = data;
    //   }
    // )
  }

  createNewProduct() {
    this.productService.createNewProduct(this.sellerid).subscribe(
      data => {
        this.router.navigate(['product/update/' + data.id]);
      },
      error => {
        console.log(error);
      });
  }

  redirectUpdatePage(productId: number) {
    this.router.navigate(['product/update/' + productId]);
  }

  deleteProduct(id: number) {
    console.log(id);
    this.productService.deleteProduct(id).subscribe(
      data => {
        if (data) {
          var index = -1;
          for (let i = 0; i < this.productList.length; i++) {
            console.log(i);
            if (this.productList[i].id == id) {
              console.log(this.productList[i].id);
              console.log(id);
              console.log(i);
              index = i;
            }
          }
          if (index != -1) {
            this.productList.slice(index, 1);
            console.log(index);
          }
         
          console.log(this.productList);
          this.dismissModal();
        }
      },
      error => {

      }
    );
  }

  openConfirmDelete(productId: number) {
    const dialogRef = this.dialog.open(DeleteProductComponent, {
      data: {
        productId: productId
       }
     });
     dialogRef.afterClosed().subscribe(result => {
       console.log(`Dialog result: ${result}`);
       if (!result) {
         this.openInfoModal();
       }
       setTimeout(() => this.refresh(), 400)
     });
   }

  dismissModal() {
    this.modalService.dismissAll();
  }

  openInfoModal() {
    this.modalService.open(this.infoModal, { centered: true });
  }

  getSellerAvatar(): string{
    if (this.seller!=null && this.seller.avatar != null && this.seller.id != -1) {
      return 'http://localhost:9000/public/serveMedia/image?source=' + this.seller.avatar.replace(/\\/g, '/');
    } else {
      return 'assets/images/noavatar.png'
    }
  }

  checkedTags: number[] = [];
  updateCheckedValues(event: any){
    const value = event.target.value;
    if(event.target.checked){
      this.checkedTags.push(value);
    }else{
      this.checkedTags = this.checkedTags.filter(v => v !== value);
    }
  }
}
