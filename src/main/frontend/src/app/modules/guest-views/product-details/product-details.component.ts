import { style } from '@angular/animations';
import { DecimalPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Report } from 'src/app/dtos/Report';
import { Seller } from 'src/app/dtos/Seller';
import { ReportService } from 'src/app/services/report.service';
import { UserService } from 'src/app/services/user.service';
import { AuthResponse } from '../../../dtos/AuthResponse';
import { Preview } from '../../../dtos/Preview';
import { Product } from '../../../dtos/Product';
import { ProductFile } from '../../../dtos/ProductFile';
import { Tag } from '../../../dtos/Tag';
import { User } from '../../../dtos/User';
import { CartService } from '../../../services/cart.service';
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
    console.log(preview);
    var ret = new DisplayPreview;
    ret.preview = preview;
    ret.preview.id = preview.id;
    if (preview.type == 'picture')
      ret.thumb = this.getSrc(preview.source);
    else
      ret.thumb = 'https://xn--b1akdajq8j.xn--p1ai/app/plugins/video-thumbnails/default.jpg';

    console.log(ret);
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
  visitor: User = new User;//thằng đang xem trang ấy
  visitorId: number;
  productId: number;
  version: string;
  loginStatus = false;
  product: Product = new Product;//hiển thị
  totalSize: number | undefined;
  sellerTotalProductCount = 0;
  report: Report;
  dots: number[] = [0];
  isOwner: boolean;

  displayPreviews: DisplayPreview[] = [];
  constructor(private activatedRoute: ActivatedRoute,

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

  isPurchased = false;
  ngOnInit(): void {


    // this.getProduct();

    // tôi đang có việc cần phải cho hẳn vào đây
    var productIdAndName = this.activatedRoute.snapshot.paramMap.get('productId');
    if (productIdAndName) {
      var productId = productIdAndName.split("-")[0];

      this.productService.getProductById(+productId).subscribe(
        data => {
          this.product = data;
          this.version = this.product.version;
          this.product.price = Number.parseFloat(this.product.price.toFixed(1));
          console.log(this.product);
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
          
            }
          console.log(this.displayPreviews);
          if (this.BlackThumbs.length > 0)
            this.BlackThumbs.item(0)?.setAttribute("style", "border-radius: 4px; position: absolute; top: 0; right: 9px; bottom: 0; left: 0; background: #000; opacity: 0;");

          if (this.product.previewVideo == null && this.product.previewPictures.length == 0) {
            var dum: DisplayPreview = new DisplayPreview;
            var dumPreview = new Preview;
            dumPreview.id = -1;
            dumPreview.type = "picture";
            dumPreview.source = "https://www.generationsforpeace.org/wp-content/uploads/2018/03/empty.jpg";
            dum.preview = dumPreview;
            dum.thumb = "https://www.generationsforpeace.org/wp-content/uploads/2018/03/empty.jpg";
            this.displayPreviews.push(dum);
          }
         
          this.currentPreview = this.displayPreviews[0];
            //sau phần getProduct
          this.productId = Number(this.activatedRoute.snapshot.paramMap.get('productId'));
          if (this.storageService.getToken()) {
            this.userService.getCurrentUserInfo().subscribe(
              data => {
                this.visitor = data;
                this.visitorId = data.id;
                if (this.visitorId == this.owner.id) {
                  this.isOwner = true;
                } else {
                  this.isOwner = false;
                }
                this.cartService.isPurchasedByUser(this.visitor.id, this.product.id).subscribe(
            data => {
              this.isPurchased = data;
                    console.log(data);
            },
            error => {
              console.log(error);
            }
          )
                this.reportService.getReportByProductUserVersion(this.productId, this.visitorId, this.version).subscribe((data: any) => {
                  this.report = data;
                })
              }
            )
          }
        },
        error => {
          this.router.navigate(['error']);
        })
    }
  }

  currentPreview: DisplayPreview;

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

  totalPurchasedCount = 0;
  checkIfPurchased() {

  }


  searchTag(tagId: number) {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['tag/'+tagId]);
    })
  }

  getSrc(p: DisplayPreview) {

  }


  hasPreviewPictures(): boolean {
    return true;
  }

  getPreviewPictureSource(): string {
   
    if (this.currentPreview.preview.id == -1) {
      return "https://www.generationsforpeace.org/wp-content/uploads/2018/03/empty.jpg";
    }
    return 'http://localhost:9000/public/serveMedia/image?source=' + this.currentPreview.preview.source.replace(/\\/g, '/');
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
    return this.product.price;
  }

  onCheckIfReported() {
    if (!this.storageService.getToken()) {
      this.toastr.error('Vui lòng đăng nhập để báo cáo');
    } else if (this.report != null) {
      this.toastr.error('Bạn đã báo cáo phiên bản này của sản phẩm');
    } else {
      this.openReportModal();
    }
  }

  openReportModal() {
    const data = {
      productId: this.product.id,
      version: this.product.version,
      userId: this.visitor.id
    }
    const dialogRef = this.dialog.open(ReportProductComponent, {

      // height: '57%',
      width: '50%',
      data: data
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(), 300)
    });
  }
  redirectSellerPage() {
    this.router.navigate(['collection/' + this.owner.id]);
  }
  addToCart() {
    if (!this.storageService.isLoggedIn()) {
      // If user is not logged in, redirect to login page
      this.router.navigate(['/login']);
      return;
    }

    this.cartService.addToCart(this.product.id).subscribe(
      (data) => {
        // Success, show a message to the user
        // this.toastr.success('Sản phẩm đã được thêm vào giỏ hàng.')
        alert('Sản phẩm đã được thêm vào giỏ hàng.');
      },
      (err) => {
        console.log(err);
        // Error, show an error message to the user
        // this.toastr.error('Đã có lỗi xảy ra, vui lòng thử lại sau.')
        alert('Đã có lỗi xảy ra, vui lòng thử lại sau.');
      }
    );
  }

  getFilesCount(): number {
    var count = 0;
    for (let i = 0; i < this.product.files.length; i++) {
      var pf = this.product.files[i];
      if ((pf.enabled && pf.reviewed && !pf.newUploaded) ||
        !pf.enabled && !pf.reviewed && !pf.newUploaded) {
        count++;
      }
    }
    return count;
  }

 
  get TotalSize() {
    var totalSize = 0;
    for (let i = 0; i < this.product.files.length; i++) {
      var pf = this.product.files[i];
      if ((pf.enabled && pf.reviewed && !pf.newUploaded) ||
        !pf.enabled && !pf.reviewed && !pf.newUploaded) {
        totalSize += pf.size;
      }
    }
    return this.formatFileSize(totalSize);
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

  refresh() {
    this.reportService.getReportByProductUserVersion(this.product.id, this.visitorId, this.version).subscribe((data: any) => {
      this.report = data;
    })
  }

  buyNow() {
    if (!this.storageService.isLoggedIn()) {
      // If user is not logged in, redirect to login page
      this.router.navigate(['/login']);
      return;
    }

    this.cartService.addToCart(this.product.id).subscribe(
      (data) => {
        this.router.navigate(['cart']);
      },
      (err) => {
        console.log(err);
        // Error, show an error message to the user
        // this.toastr.error('Đã có lỗi xảy ra, vui lòng thử lại sau.')
        alert('Đã có lỗi xảy ra, vui lòng thử lại sau.');
      }
    );
  }

  searchByCategory(categoryId: number){
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['category/'+categoryId]);
    })
  }
}

