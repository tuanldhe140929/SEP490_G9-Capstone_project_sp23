import { OnInit, ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Product } from '../../../dtos/Product';
import { ProductFile } from '../../../dtos/ProductFile';
import { ProductFileService } from '../../../services/product-file.service';
import { ProductService } from '../../../services/product.service';

@Component({
  selector: 'app-download',
  templateUrl: './download.component.html',
  styleUrls: ['./download.component.css']
})
export class DownloadComponent implements OnInit {
  @ViewChild('infoModal', { static: false }) private infoModal: any;
  info = "";
  product: Product;
  token: string = "";
  constructor(private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private productFileService: ProductFileService,
    private router: Router,
    private modalService: NgbModal  ) {

  }
  openInfoModal() {
    this.modalService.open(this.infoModal, { centered: true });
  }
  ngOnInit(): void {
    var productIdAndName = this.activatedRoute.snapshot.paramMap.get('productId');
    var tokenParam = this.activatedRoute.snapshot.queryParamMap.get('token');
    if (tokenParam) {
      this.token = tokenParam;
    }
    console.log(productIdAndName);
    if (productIdAndName) {
      var productId = Number.parseInt(productIdAndName.split('-')[0]);
      if (productId) {
        this.getProduct(productId);
      }
    }
  }
  getProduct(productId: number) {
    const getProductReq = this.productService.getProductByIdForDownload(productId);
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
    if (file.size < 1024 * 1024) {
      return (file.size / 1024).toFixed(3) + 'KB';
    } else {
      return (file.size / (1024 * 1024)).toFixed(3) + 'MB';
    }
  }

  redirectProductPage() {
    this.router.navigate(['products/' + this.product.id]);
  }

  download() {
    this.productFileService.download(this.product.id, this.token).subscribe(
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
        //window.open(url); // open the URL in a new tab to initiate the download
      },
      error => {
        this.info = "Không thể tải";
        this.openInfoModal();
        console.log(error);
      }
    )
  }
  dismissError() {
    this.info = "";
    this.modalService.dismissAll();
  }
}


