import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { AuthResponse } from '../../../DTOS/AuthResponse';
import { Preview } from '../../../DTOS/Preview';
import { Product } from '../../../DTOS/Product';
import { Tag } from '../../../DTOS/Tag';
import { User } from '../../../DTOS/User';
import { AuthService } from '../../../services/auth.service';
import { CommonService } from '../../../services/common.service';
import { ProductService } from '../../../services/product.service';
import { StorageService } from '../../../services/storage.service';
import { ReportProductComponent } from './report-product/report-product.component';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  own: boolean = false;

  owner: User = new User;
  visitorAuth: AuthResponse = new AuthResponse;
  visitor: User = new User;
  product: Product = new Product;
  totalSize: number | undefined;
  sellerTotalProductCount = 0;
  dots: number[] = [0];
  constructor(private activatedRoute: ActivatedRoute,
    private commonService: CommonService,
    private router: Router,
    private storageService: StorageService,
    private authService: AuthService,
    private dialog: MatDialog,
    private productService: ProductService) {

  }

  ngOnInit(): void {
    this.getProduct();
    
    if (this.storageService.isLoggedIn()) {
      this.getVisitor();
    }
  }


  getProfileImage() {
    var imageUrl = '';
    if (this.owner.avatar != null && this.owner.avatar != '') {
      imageUrl = 'http://localhost:9000/public/serveMedia/serveProfileImage?userId=' + this.owner.id;
    } else {
      imageUrl = 'https://img.freepik.com/premium-vector/cute-ladybug-vector-illustration-isolated-white-background_543090-46.jpg?w=2000';
    }
    this.SellerImage.setAttribute('style', 'width:30px; height:30px;background-size: cover;border:1px solid black; border-radius:5px; ; background-image:url("' + imageUrl + '");');
  }

  getSellerTotalProductCount(sellerId: number) {
    this.productService.getProductsCountBySellerId(sellerId).subscribe(
      (data) => {
        this.sellerTotalProductCount = data;
      },
      (error) => {
        console.log(error);
      }
    );
    }

  getVisitor() {
    this.visitorAuth = this.storageService.getAuthResponse();
    this.checkIfPurchased();
    
  }

  getProduct() {
    var productIdAndName = this.activatedRoute.snapshot.paramMap.get('productId');
    if (productIdAndName) {
      var productId = productIdAndName.split("-")[0];

      this.productService.getProductById(+productId).subscribe(
        data => {
          this.product = data;
          console.log(this.product);
          if (this.DescriptionTab) {
            this.DescriptionTab.innerHTML = this.product.details;
          }
          this.owner = data.seller;
          this.getSellerTotalProductCount(this.owner.id);
          this.getProfileImage();
        },
        error => {
          console.log(error);
        })
    }
  }

  checkIfPurchased() {

  }


  searchTag(tag: Tag) {

  }


  onChooseImage(preview: Preview): void {

  }

  hasPreviewPictures():boolean {
    return true;
  }

  getPreviewPictureSource(preview: Preview): string {
    return 'http://localhost:9000/public/serveMedia/image?source=' + preview.source.replace(/\\/g, '/');
  }

  get TotalSize() {
    var totalSize = 0;
    for (let i = 0; i < this.product.files.length; i++) {
      totalSize += this.product.files[i].size;
    }
    return this.formatFileSize(totalSize);
  }

  get TotalPreviewCount() {
    var count = 0;
    if (this.product.previewVideo != null) {
      count++;
      if (count == 7) {
        this.dots.push(1);
      }
    }
    count += this.product.previewPictures.length;
    return count;
  }

  get SellerJoinedDate() {
    return this.product.seller.accountCreatedDate.getDay
      + "-" + this.product.seller.accountCreatedDate.getMonth
      + "-" + this.product.seller.accountCreatedDate.getFullYear;
  }

  get Instruction() {
    if (this.product.instruction != '' && this.product.instruction != null)
      return 'Có';
    else
      return 'Không'
  }

  formatFileSize(fileSize: number) {
    if (fileSize < 1000000) {
      return fileSize / 1000 + 'Kb';
    } else {
      return (fileSize / 1000000).toFixed(3) + 'Mb';
    }
  }

  get DescriptionTab() {
    return document.getElementById('tab_specs');
  }

  get SellerImage() {
    return document.getElementById('sellerImage') as HTMLDivElement;
  }

  get OwnerUsername() {
    if (this.owner!=null&&this.owner.username != '' && this.owner.username != null) {
      return this.owner.username;
    }
    else {
      return '';
    }
  }

  get Dots() {
    return document.querySelectorAll('.dott');
  }

  currentSlide(index:number) {
      for (let i = 0; i < this.Dots.length; i++) {
        this.Dots[i].className = this.Dots[i].className.replace(" active", "");
      }
    this.Dots[index].className += " active";
  }


  openReportModal() {
    const dialogRef = this.dialog.open(ReportProductComponent,{
      height: '80%',
      width: '50%'
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }


  redirectSellerPage() {
    /*    let navigationExtras: NavigationExtras = {
          
          queryParams: { 'username': this.owner.username }
        };
        this.router.navigate(['/collection'], navigationExtras);
      */
  }

}
