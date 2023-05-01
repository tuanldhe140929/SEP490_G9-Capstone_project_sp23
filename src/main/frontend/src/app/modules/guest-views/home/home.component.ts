import { Product } from '../../../dtos/Product';
import { ProductService } from 'src/app/services/product.service';
import { Component, Input, OnInit } from '@angular/core';
import { data } from 'jquery';
import { Router } from '@angular/router';
import { SlideInterface } from './types/slide.interface';
import { ProductDetailsDTO } from 'src/app/dtos/ProductDetailsDTO';
import { Preview } from 'src/app/dtos/Preview';
interface carouselImage {
  imageSrc: string;
  imageAlt: string;
}
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  @Input() slides: SlideInterface[] = [];
  productList: Product[] = [];
  lastestProductList: Product[] = [];
  itemsPerPage: number = 9;
  p: number = 1;
  totalLength: any;
  page: number = 1;
  showpost: any = [];
  currentIndex: number = 0;
  timeoutId?: number;
  productDetails: ProductDetailsDTO;
  randomProductList: Product[] = [];
  product : Product;
  productsWithPreviewPictures: Product[] = [];
  filteredProducts: Product[] = [];
  constructor(
    private productService: ProductService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getProduct();
    this.getLatestProduct();
  }

  public getCoverImage(product: Product): string {
    if (product != null && product.id != -1 && product.coverImage != null) {

      return 'http://localhost:9000/public/serveMedia/image?source=' + product.coverImage.replace(/\\/g, '/');
    }
    else {
      return "https://lyon.palmaresdudroit.fr/images/joomlart/demo/default.jpg";
    }
  }

  public getPreviewPicture(product: Product): string{
    if(product != null && product.id != -1 && product.previewPictures != null){
      return 'http://localhost:9000/public/serveMedia/image?source='+product.previewPictures[0].source.replace(/\\/g, '/');
    }else {
      return "https://lyon.palmaresdudroit.fr/images/joomlart/demo/default.jpg";
    }
  }

  redirectProductDetails(product: Product) {
    this.router.navigate(['/products', product.id]);
  }

  getProduct() {
    this.productService.getAllProductForHome().subscribe(
      data => {
        console.log(data);
        this.productList = data;
        for (let i = 0; i < this.productList.length; i++) {
          this.productList[i].price = Number.parseFloat(this.productList[i].price.toFixed(1));
        }
        this.getProductsWithPreviewPictures();
      }
    )
  }
  getLatestProduct() {
    this.productService.getLastestUpdatedProductForHomePage().subscribe(
      data => {
        console.log(data);
        this.lastestProductList = data.slice(0, 4);
        for (let i = 0; i < this.lastestProductList.length; i++) {
          this.lastestProductList[i].price = Number.parseFloat(this.lastestProductList[i].price.toFixed(1));
        }
      }
    )
  }
  
  ProductPreviewPicture(product : Product){
    if(product.previewPictures.length>0){
      return true
    }else{
      return false
    }
  }
  
getProductsWithPreviewPictures(): Product[] {
  // Populate the productsWithPreviewPictures array with products that have preview pictures
  for (const product of this.productList) {
    if (this.ProductPreviewPicture(product)==true) {
      this.productsWithPreviewPictures.push(product);
    }
  }

  // Shuffle the array randomly
  for (let i = this.productsWithPreviewPictures.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [this.productsWithPreviewPictures[i], this.productsWithPreviewPictures[j]] = [this.productsWithPreviewPictures[j], this.productsWithPreviewPictures[i]];
  }

  // Filter the first 4 products in the shuffled array that have preview pictures
  this.filteredProducts = this.productsWithPreviewPictures.filter(product => product.previewPictures.length > 0).slice(0, 4);
  return this.filteredProducts;
}


  ngOnDestroy() {
    window.clearTimeout(this.timeoutId);
  }
  resetTimer() {
    if (this.timeoutId) {
      window.clearTimeout(this.timeoutId);
    }
    this.timeoutId = window.setTimeout(() => this.goToNext(), 3000);
  }

  goToPrevious(): void {
    const isFirstSlide = this.currentIndex === 0;
    const newIndex = isFirstSlide
      ? this.slides.length - 1
      : this.currentIndex - 1;

    this.resetTimer();
    this.currentIndex = newIndex;
  }

  goToNext(): void {
    const isLastSlide = this.currentIndex === this.slides.length - 1;
    const newIndex = isLastSlide ? 0 : this.currentIndex + 1;

    this.resetTimer();
    this.currentIndex = newIndex;
  }

  goToSlide(slideIndex: number): void {
    this.resetTimer();
    this.currentIndex = slideIndex;
  }

  getCurrentSlideUrl() {
    return `url('${this.slides[this.currentIndex].url}')`;
  }

  searchByCategory(categoryId: number) {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate(['category/' + categoryId]);
    })
  }

  searchBySeller(sellerId: number) {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate(['collection/' + sellerId]);
    })
  }

  //Image slider//
  @Input() images: carouselImage[] = []
  @Input() indicators = true;
  @Input() controls = true;
  selectedIndex = 0;
  //set index for image dot slider
  selectImage(index: number): void {
    this.selectedIndex = index
  }
}
