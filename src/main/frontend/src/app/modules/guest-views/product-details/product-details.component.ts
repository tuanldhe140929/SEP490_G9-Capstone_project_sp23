import { style } from '@angular/animations';
import { DecimalPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { Seller } from 'src/app/DTOS/Seller';
import { AuthResponse } from '../../../DTOS/AuthResponse';
import { Preview } from '../../../DTOS/Preview';
import { Product } from '../../../DTOS/Product';
import { ProductFile } from '../../../DTOS/ProductFile';
import { Tag } from '../../../DTOS/Tag';
import { User } from '../../../DTOS/User';
import { AuthService } from '../../../services/auth.service';
import { CartService } from '../../../services/cart.service';
import { CommonService } from '../../../services/common.service';
import { ProductFileService } from '../../../services/product-file.service';
import { ProductService } from '../../../services/product.service';
import { StorageService } from '../../../services/storage.service';
import { ReportProductComponent } from './report-product/report-product.component';


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
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})

export class ProductDetailsComponent implements OnInit {

  own: boolean = false;

  owner: Seller = new Seller;
  visitorAuth: AuthResponse = new AuthResponse;
  visitor: User = new User;//th???ng ??ang xem trang ???y
  product: Product = new Product;//hi???n th???
  totalSize: number | undefined;
  sellerTotalProductCount = 0;
  dots: number[] = [0];

  displayPreviews: DisplayPreview[] = [];
  constructor(private activatedRoute: ActivatedRoute,
    private commonService: CommonService,
    private router: Router,
    private storageService: StorageService,
    private authService: AuthService,
    private decimalPipe: DecimalPipe,
    private dialog: MatDialog,
    private productService: ProductService,
    private cartService: CartService,
    private productFileService: ProductFileService) {

  }



  ngOnInit(): void {
    this.getProduct();

    if (this.storageService.isLoggedIn()) {
      this.getVisitor();
    }

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
    var productIdAndName = this.activatedRoute.snapshot.paramMap.get('productId');
    if (productIdAndName) {
      var productId = productIdAndName.split("-")[0];

      this.productService.getProductById(+productId).subscribe(
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
      return 'C??';
    else
      return 'Kh??ng'
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

  openReportModal() {

    const data = {
      productId: this.product.id,
      userId: this.visitor.id
    }
    const dialogRef = this.dialog.open(ReportProductComponent, {

      height: '80%',
      width: '50%',
      data:data
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
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
        alert('S???n ph???m ???? ???????c th??m v??o gi??? h??ng.');
      },
      () => {
        // Error, show an error message to the user
        alert('???? c?? l???i x???y ra, vui l??ng th??? l???i sau.');
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

}
