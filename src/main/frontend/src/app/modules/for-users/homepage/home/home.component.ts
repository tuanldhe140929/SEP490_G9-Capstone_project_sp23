import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/DTOS/Product';
import { ProductDtoService } from 'src/app/services/product-dto.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  constructor(){}


  ngOnInit(): void {
    
  }

  
}
