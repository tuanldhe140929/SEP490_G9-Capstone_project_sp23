import { style } from '@angular/animations';
import { DecimalPipe } from '@angular/common';
import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthResponse } from 'src/app/DTOS/AuthResponse';
import { Preview } from 'src/app/DTOS/Preview';
import { Product } from 'src/app/DTOS/Product';
import { Report } from 'src/app/DTOS/Report';
import { Seller } from 'src/app/DTOS/Seller';
import { Tag } from 'src/app/DTOS/Tag';
import { User } from 'src/app/DTOS/User';
import { ViolationType } from 'src/app/DTOS/ViolationType';
import { CartService } from 'src/app/services/cart.service';
import { ProductFileService } from 'src/app/services/product-file.service';
import { ProductService } from 'src/app/services/product.service';
import { ReportService } from 'src/app/services/report.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';
import { ViolationTypeService } from 'src/app/services/violation-type.service';
import { ReportedProductDownloadComponent } from '../reported-product-download/reported-product-download.component';
import { UpdateReportStatusComponent } from '../update-report-status/update-report-status.component';


class DisplayPreview {
  preview: Preview;
  thumb: string;

  constructor() {
    this.preview = new Preview;
    this.thumb = "";
  }

  public static fromPreview(preview: Preview): DisplayPreview {
    var ret = new DisplayPreview;
    ret.preview = preview;
    if (preview.type == 'picture')
      ret.thumb = this.getSrc(preview.source);
    else
      ret.thumb = 'https://xn--b1akdajq8j.xn--p1ai/app/plugins/video-thumbnails/default.jpg';
    return ret;
  }

  public static getSrc(source: string) {
    const encodedSource = encodeURIComponent(source.replace(/\\/g, '/').replace('//', '/').replace(/\(/g, '%28').replace(/\)/g, '%29'));
    return 'http://localhost:9000/public/serveMedia/image?source=' + encodedSource;


  }

}

@Component({
  selector: 'app-reported-product-details',
  templateUrl: './reported-product-details.component.html',
  styleUrls: ['./reported-product-details.component.css']
})
export class ReportedProductDetailsComponent implements OnInit {
  own: boolean = false;

  owner: Seller = new Seller;
  visitorAuth: AuthResponse = new AuthResponse;
  visitor: User = new User;//thằng đang xem trang ấy
  visitorId: number;
  productId: number;
  loginStatus = false;
  product: Product = new Product;//hiển thị
  totalSize: number | undefined;
  sellerTotalProductCount = 0;
  report: Report;
  dots: number[] = [0];
  isOwner: boolean;

  displayPreviews: DisplayPreview[] = [];
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {productId: number, status: string}, 
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private storageService: StorageService,
    private decimalPipe: DecimalPipe,
    private dialog: MatDialog,
    private productService: ProductService,
    private cartService: CartService,
    private productFileService: ProductFileService,
    private toastr: ToastrService,
    private userService: UserService,

    private reportService: ReportService) {
  }


  ngOnInit(): void {
    this.getProduct();
    this.productId = this.data.productId;
  }

  currentPreview: DisplayPreview = new DisplayPreview;

  onChoosePreview(preview: DisplayPreview): void {
    this.currentPreview = preview;
    var index = -1;
    for (let i = 0; i < this.displayPreviews.length; i++) {
      if (preview == this.displayPreviews[i]) {
        index = i;
        break;
      }
    }

    for (let i = 0; i < this.BlackThumbs.length; i++) {
      this.BlackThumbs.item(i)?.setAttribute("style", "border-radius: 4px; position: absolute; top: 0; right: 9px; bottom: 0; left: 0; background: #000; opacity: .3;");
    }

    if (index != -1) {
      this.BlackThumbs.item(index)?.setAttribute("style", "border-radius: 4px; position: absolute; top: 0; right: 9px; bottom: 0; left: 0; background: #000; opacity: 0;");
    }

  }

  getPreviewVideoSource() {
    if (this.product.previewVideo != null) {
      return 'http://localhost:9000/public/serveMedia/video?source=' + this.product.previewVideo.source.replace(/\\/g, '/');
    }
    return "";
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

  src = 'http://localhost:9000/public/serveMedia/image?source=account_id_2%2Fproducts%2F2%2FDatabase%20V2.drawio%20(2).png';

  getProduct() {
    var productIdAndName = this.data.productId;
    if (productIdAndName) {
      var productId = productIdAndName;

      this.productService.getActiveVersion(this.data.productId).subscribe(
        data => {
          this.product = data;
          if (this.DescriptionTab) {
            this.DescriptionTab.innerHTML = this.product.details;
          }
          this.owner = data.seller;
          this.getSellerTotalProductCount(this.owner.id);
          this.getProfileImage();
          if (this.product.previewVideo != null)
            this.displayPreviews.push(DisplayPreview.fromPreview(this.product.previewVideo));

          if (this.product.previewPictures != null)
            for (let i = 0; i < this.product.previewPictures.length; i++) {
              var a = DisplayPreview.fromPreview(this.product.previewPictures[i]);
              this.displayPreviews.push(a);
              console.log(a);
            }

          if (this.BlackThumbs.length > 0)
            this.BlackThumbs.item(0)?.setAttribute("style", "border-radius: 4px; position: absolute; top: 0; right: 9px; bottom: 0; left: 0; background: #000; opacity: 0;");

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

  getSrc(p: DisplayPreview) {

  }


  hasPreviewPictures(): boolean {
    return true;
  }

  getPreviewPictureSource(): string {
    return 'http://localhost:9000/public/serveMedia/image?source=' + this.currentPreview.preview.source.replace(/\\/g, '/');
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
    if (this.product.seller != null) {
      var joinedDate: Date = new Date(this.product.seller.createdDate.toString());
      if (joinedDate)
        return joinedDate.getDate()
          + "-" + joinedDate.getMonth()
          + "-" + joinedDate.getFullYear();
    }
    return "";
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
    if (this.owner != null && this.owner.username != '' && this.owner.username != null) {
      return this.owner.username;
    }
    else {
      return '';
    }
  }

  get Dots() {
    return document.querySelectorAll('.dott');
  }

  currentSlide(index: number) {
    for (let i = 0; i < this.Dots.length; i++) {
      this.Dots[i].className = this.Dots[i].className.replace(" active", "");
    }
    this.Dots[index].className += " active";
  }

  getFormattedValue(value: any): string {
    const stringToTransform = String(value ?? '')
      .replace(/\D/g, '')
      .replace(/^0+/, '');
    return (
      this.decimalPipe.transform(stringToTransform === '' ? '0' : stringToTransform, '1.0')
      + '');
  }

  get BlackThumbs() {
    return document.getElementsByClassName('thumb_column_black');
  }



  get Price() {
    return this.getFormattedValue(this.product.price);
  }

  redirectSellerPage() {
    this.router.navigate(['collection/' + this.owner.username]);
  }
  addToCart() {
    if (!this.storageService.isLoggedIn()) {
      // If user is not logged in, redirect to login page
      this.router.navigate(['/login']);
      return;
    }

    this.cartService.addToCart(this.product.id).subscribe(
      () => {
        // Success, show a message to the user
        // this.toastr.success('Sản phẩm đã được thêm vào giỏ hàng.')
        alert('Sản phẩm đã được thêm vào giỏ hàng.');
      },
      () => {
        // Error, show an error message to the user
        // this.toastr.error('Đã có lỗi xảy ra, vui lòng thử lại sau.')
        alert('Đã có lỗi xảy ra, vui lòng thử lại sau.');
      }
    );
  }

  generateDownloadToken() {
    var token = "";
    this.productFileService.generateDownloadToken(0, this.product.id).subscribe(
      (data: string) => {
        console.log(data);
        token = data;
        this.router.navigate(['/download', this.product.id], { queryParams: { token: token } });
      }
    );
  }

  refresh(productId: number) {
    this.productService.getProductById(productId).subscribe(
      data => {
        this.displayPreviews = [];
        this.product = data;
        if (this.DescriptionTab) {
          this.DescriptionTab.innerHTML = this.product.details;
        }
        this.owner = data.seller;
        this.getSellerTotalProductCount(this.owner.id);
        this.getProfileImage();
        if (this.product.previewVideo != null)
          this.displayPreviews.push(DisplayPreview.fromPreview(this.product.previewVideo));

        if (this.product.previewPictures != null)
          for (let i = 0; i < this.product.previewPictures.length; i++) {
            var a = DisplayPreview.fromPreview(this.product.previewPictures[i]);
            this.displayPreviews.push(a);
            console.log(a);
          }

        if (this.BlackThumbs.length > 0)
          this.BlackThumbs.item(0)?.setAttribute("style", "border-radius: 4px; position: absolute; top: 0; right: 9px; bottom: 0; left: 0; background: #000; opacity: 0;");

      },
      error => {
        console.log(error);
      })
  }

  updateReport(){
    const dialogRef = this.dialog.open(UpdateReportStatusComponent, {
      data: {
        productId: this.productId,
        status: this.data.status
      },
      width: '80%'

    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      this.refresh(this.data.productId);
    });
  }

  openDownload(){
    const dialogRef = this.dialog.open(ReportedProductDownloadComponent, {
      data: {
        productId: this.productId,
        productName: this.product.name,
        version: this.product.version
      },
      width: '70%',
      height: '70%'
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      this.refresh(this.data.productId);
    });
  }
}
