import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Category } from 'src/app/DTOS/Category';
import { Product } from 'src/app/DTOS/Product';
import { Seller } from 'src/app/DTOS/Seller';
import { User } from 'src/app/DTOS/User';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { SellerService } from 'src/app/services/seller.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-seller-product-list',
  templateUrl: './seller-product-list.component.html',
  styleUrls: ['./seller-product-list.component.css']
})
export class SellerProductListComponent implements OnInit {

  @ViewChild('confirmDelete', { static: false }) private confirmDelete: any;

  @ViewChild('infoModal', { static: false }) private infoModal: any;

  sellerid: number
  productList: Product[] = [];
  displayForSeller: Product[] = [];
  displayForUser: Product[] = [];
  categoryList: Category[] = [];
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
    private router: Router,
    private sellerService: SellerService,
    private storageService: StorageService,
    private userService: UserService,
    private modalService: NgbModal
  ) { }

  ngOnInit(): void {
    this.sellerid = Number(this.activatedRoute.snapshot.paramMap.get('sellerId'));
    this.checkIfIsSeller();
    this.productService.getProductsBySellerForSeller(this.sellerid, "", 0, 0, 10000000).subscribe(
      data => {
        this.productList = data;
      }
    )
    // this.productService.getProductsBySellerForUser(this.sellerid, "", 0, 0, 10000000).subscribe(
    //   data => {
    //     this.displayForUser = data;
    //   }
    // )
    this.getAllCategories();
    this.getSellerById();
  }

  getAllCategories() {
    this.categoryService.getAllCategories().subscribe(
      data => {
        this.categoryList = data;
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
    console.log(product.coverImage == null);
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

  getSellerById() {
    this.sellerService.getSellerById(this.sellerid).subscribe(
      data => {
        this.seller = data;
      }
    )
  }

  refresh() {
    if(this.sellerStatus){
      this.productService.getProductsBySellerForSeller(this.sellerid, "", 0, 0, 10000000).subscribe(
        data => {
          this.productList = data;
        }
      )
    }else{
      this.productService.getProductsBySellerForUser(this.sellerid, "", 0, 0, 10000000).subscribe(
        data => {
          this.productList = data;
        }
      )
    }
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

  deleteProduct(product: Product) {
    this.productService.deleteProduct(product).subscribe(
      data => {
        if (data) {
          var index = -1;
          for (let i = 0; i < this.productList.length; i++) {
            if (this.productList[i].id == product.id) {
              index = i;
              break;
            }
          }
          if (index != -1) {
            this.productList.slice(index, 1);
          }
        }
      },
      error => {

      }
    );
  }

  openConfirmDelete() {
    this.modalService.open(this.confirmDelete, { centered: true });
  }

  dismissModal() {
    this.modalService.dismissAll();
  }

  openInfoModal() {
    this.modalService.open(this.infoModal, { centered: true });
  }
}
