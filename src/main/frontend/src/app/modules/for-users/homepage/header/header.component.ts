import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { Observable, startWith, map } from 'rxjs';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { Product } from 'src/app/DTOS/Product';
import { ProductDtoService } from 'src/app/services/product-dto.service';
import { NavigationExtras, Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{
  myControl = new FormControl('');
  allProducts: Product[] = [];
  allProductsName: string[] = [];
  options: string[] = [];
  filteredOptions: Observable<string[]>;
  removeMark = require("vietnamese-tonemarkless");
  keyword: string;

  constructor(
    private productDtoService: ProductDtoService,
    private formBuilder: FormBuilder,
    private router: Router){}

  ngOnInit(){
    this.getAllProducts();
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value|| '')),
    );
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase().trim().replace(/\s+/g,' ');
    return this.options.filter(option => option.toLowerCase().includes(filterValue))
  }

  onSearch(){
    this.router.navigate(['/shopping', this.keyword]);
    // window.location.href = "http://localhost:4200/shopping/"+this.keyword;
    
  }

  onOptionSelected(event: MatAutocompleteSelectedEvent){
    this.router.navigate(['/shopping', event]);
  }

  getAllProducts(){
    this.productDtoService.getAllProducts().subscribe(
      data => {
        this.allProducts = data;
        for(let i=0;i<this.allProducts.length;i++){
          this.options.push(this.allProducts[i].name);
        }
      }
    )
  }
}
