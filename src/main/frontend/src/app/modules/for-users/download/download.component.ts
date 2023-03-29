import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { Product } from '../../../dtos/Product';
import { ProductFile } from '../../../dtos/ProductFile';
import { ProductService } from '../../../services/product.service';

@Component({
  selector: 'app-download',
  templateUrl: './download.component.html',
  styleUrls: ['./download.component.css']
})
export class DownloadComponent implements OnInit {

  product: Product;

  constructor(private activatedRoute: ActivatedRoute,
    private productService: ProductService,
private router:Router  ) {

  }

  ngOnInit(): void {
    var productIdAndName = this.activatedRoute.snapshot.paramMap.get('productId');
    console.log(productIdAndName);
    if (productIdAndName) {
      var productId = Number.parseInt(productIdAndName.split('-')[0]);
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
}


