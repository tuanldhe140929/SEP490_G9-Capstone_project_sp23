import { Product } from '../../../dtos/Product';
import { ProductService } from 'src/app/services/product.service';
import { Component, OnInit } from '@angular/core';
import { data } from 'jquery';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  productList: Product[] = [];
  itemsPerPage: number = 9;
  p: number = 1;
  constructor(
    private productService: ProductService,
    private router:Router
  ){}


  ngOnInit(): void {
    this.getProduct();
  }

  public getCoverImage(product: Product): string {
    if (product != null && product.id != -1 && product.coverImage != null) {

      return 'http://localhost:9000/public/serveMedia/image?source=' + product.coverImage.replace(/\\/g, '/');
    }
    else {
      return "https://lyon.palmaresdudroit.fr/images/joomlart/demo/default.jpg";
    }
  }

  redirectProductDetails(product:Product){
	this.router.navigate(['/products', product.id]);
  }
  getProduct(){
    this.productService.getAllProductForHome().subscribe(
      data => {
        console.log(data);
        this.productList = data;
        for (let i = 0; i < this.productList.length; i++){
          this.productList[i].price = Number.parseFloat(this.productList[i].price.toFixed(1));
}
      }
    )
  }
  
  searchByCategory(categoryId: number){
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['category/'+categoryId]);
    })
  }

  searchBySeller(sellerId: number){
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['collection/'+sellerId]);
    })
  }
}
