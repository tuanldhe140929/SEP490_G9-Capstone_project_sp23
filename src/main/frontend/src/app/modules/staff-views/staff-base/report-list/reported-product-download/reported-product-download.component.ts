import { Inject, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Product } from 'src/app/dtos/Product';
import { ProductFile } from 'src/app/dtos/ProductFile';
import { ProductFileService } from 'src/app/services/product-file.service';
import { ProductService } from 'src/app/services/product.service';


@Component({
  selector: 'app-reported-product-download',
  templateUrl: './reported-product-download.component.html',
  styleUrls: ['./reported-product-download.component.css']
})
export class ReportedProductDownloadComponent implements OnInit{
  product: Product;
  token: string = "";
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {productId: number, productName:string, version: string}, 
    private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private productFileService: ProductFileService,
    private toastr: ToastrService,
    private dialogRef: MatDialogRef<ReportedProductDownloadComponent>,
    private router: Router) {

  }

  ngOnInit(): void {
    var productIdAndName = this.data.productId;
    var tokenParam = this.activatedRoute.snapshot.queryParamMap.get('token');
    if (tokenParam) {
      this.token = tokenParam;
    }
    console.log(productIdAndName);
    if (productIdAndName) {
      var productId = productIdAndName;
      if (productId) {
        this.getProduct(productId);
      }
    }
  }
  getProduct(productId: number) {
    const getProductReq = this.productService.getProductById(productId);
    getProductReq.subscribe(
      (data) => {
        this.product = data;
        console.log(this.product);
      },
      (error) => {
        console.log(error);
      });
  }

  getFileSize(file: ProductFile) {
    if (file.size < 1000000) {
      return file.size / 1000 + 'kb';
    } else {
      return (file.size / 1000000).toFixed(3) + 'Mb';
    }
  }

  redirectProductPage() {
    this.router.navigate(['products/' + this.product.id]);
  }

  download() {
    this.productService.getProductByIdForStaff(this.data.productId, this.data.version).subscribe(
      result => {
        this.productFileService.downloadForStaff(this.data.productId, this.data.version).subscribe(
          response => {
            console.log(response);
    
            const blob = new Blob([response], { type: 'application/zip' }); // create a Blob object from the response
            const url = window.URL.createObjectURL(blob); // create a URL object from the Blob
    
            const link = document.createElement('a');
            link.href = url;
            link.download = this.product.name+".zip"; // set the desired file name here
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
          },
          error => {
            console.log(error);
          }
        )
      },
      error => {
        this.toastr.error("Đã có lỗi xảy ra");
        this.dialogRef.close({data: "error"});
      }
    )
    
  }
}
