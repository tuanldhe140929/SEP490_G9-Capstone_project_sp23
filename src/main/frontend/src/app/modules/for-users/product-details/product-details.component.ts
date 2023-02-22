import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { Preview } from '../../../DTOS/Preview';
import { Product } from '../../../DTOS/Product';
import { Tag } from '../../../DTOS/Tag';
import { User } from '../../../DTOS/User';
import { AuthService } from '../../../services/auth.service';
import { CommonService } from '../../../services/common.service';
import { StorageService } from '../../../services/storage.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  own: boolean = false;

  owner: User = new User;
  visitor: User = new User;
  product: Product = new Product;
  totalSize: number | undefined;
  sellerTotalProductCount = 0;
  dots: number[] = [0];
  constructor(private activatedRoute: ActivatedRoute,
    private commonService: CommonService,
    private router: Router,
    private storageService: StorageService,
    private authService: AuthService) {
  }

  ngOnInit(): void {
    this.getOwner(); //=> getProduct()
    if (this.storageService.isLoggedIn()) {
      this.getVisitor();
    }
  }

  getOwner() {
    var username = this.activatedRoute.snapshot.paramMap.get('username')?.toString();
    if (username) {
      this.commonService.getUserInfoByUsername(username).subscribe(
        data => {
          this.owner = data;
          console.log(data);
          this.getSellerTotalProductCount(this.owner.id);
          this.getProfileImage();
          this.getProduct();
        },
        error => {
          this.router.navigate(['error']);
        }
      );
    }
  }
  getProfileImage() {
    var imageUrl = '';
    if (this.owner.image != null && this.owner.image != '') {
      imageUrl = 'http://localhost:9000/public/serveMedia/serveProfileImage?userId=' + this.owner.id;
    } else {
      imageUrl = 'https://img.freepik.com/premium-vector/cute-ladybug-vector-illustration-isolated-white-background_543090-46.jpg?w=2000';
    }
    this.SellerImage.setAttribute('style', 'width:30px; height:30px;background-size: cover;border:1px solid black; border-radius:5px; ; background-image:url("' + imageUrl + '");');
  }

  getSellerTotalProductCount(sellerId: number) {
    this.commonService.getSellerTotalProductCount(sellerId).subscribe(
      (data) => {
        this.sellerTotalProductCount = data;
      },
      (error) => {
        console.log(error);
      }
    );
    }

  getVisitor() {
    this.authService.getCurrentLogedInUser().subscribe(
      data => {
        this.visitor = data;
        if (this.visitor.id == this.owner.id) {
          this.own = true;
        }

        this.checkIfPurchased();
      },
      error => {
        console.log(error);
      })
  }

  getProduct() {
    var productId = this.activatedRoute.snapshot.paramMap.get('productId');
    if (productId) {
      this.commonService.getProductByIdAndUserId(+productId, this.owner.id).subscribe(
        data => {
          this.product = data;
          console.log(this.product);
          if (this.DescriptionTab) {
            this.DescriptionTab.innerHTML = this.product.details;
          }

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

  hasPreviewPictures():boolean {
    return true;
  }

  getPreviewPictureSource(preview: Preview): string {
    return 'http://localhost:9000/public/serveMedia/servePreviewPicture/' + preview.id;
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

  onChooseImage(preview: Preview): void {

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

  }


  redirectSellerPage() {
    /*    let navigationExtras: NavigationExtras = {
          
          queryParams: { 'username': this.owner.username }
        };
        this.router.navigate(['/collection'], navigationExtras);
      */
  }
}
