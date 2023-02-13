import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Preview } from '../../../DTOS/Preview';
import { Product } from '../../../DTOS/Product';
import { Tag } from '../../../DTOS/Tag';
import { User } from '../../../DTOS/User';
import { CommonService } from '../../../services/common.service';
import { StorageService } from '../../../services/storage.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  owner: User = new User;
  visitor: User = new User;
  product: Product = new Product;
  constructor(private activatedRoute: ActivatedRoute,
    private commonService: CommonService,
    private router: Router,
    private storageService: StorageService) {
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
          this.getProduct();
        },
        error => {
          this.router.navigate(['error']);
        }
      );
    }
  }

  getVisitor() {
    this.commonService.getCurrentLogedInUser().subscribe(
      data => {
        this.visitor = data;
        this.checkIfPurchased();
      },
      error => {
        console.log(error);
      })
  }

  getProduct() {
    var productName = this.activatedRoute.snapshot.paramMap.get('productName')?.toString();
    if (productName) {
      this.commonService.getProductByNameAndUserId(productName, this.owner.id).subscribe(
        data => {
          this.product = data;
          console.log(this.product);
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

  onChooseImage(preview: Preview): void {

  }




  formatFileSize(fileSize: number) {
    if (fileSize < 1000000) {
      return fileSize / 1000 + 'kb';
    } else {
      return (fileSize / 1000000).toFixed(3) + 'Mb';
    }
  }
}
