import { ProductService } from 'src/app/services/product.service';
import { Component, OnInit } from '@angular/core';
import { data } from 'jquery';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  constructor(
    private productService: ProductService
  ){}


  ngOnInit(): void {
    
  }
  getProduct(){
    this.productService.getAllProductForHome().subscribe(
      data => {
        this.productService = data;
      }
    )
  }
  
}
