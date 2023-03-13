import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { map, Observable, startWith } from 'rxjs';
import { Product } from 'src/app/DTOS/Product';
import { User } from 'src/app/DTOS/User';
import { AuthService } from 'src/app/services/auth.service';
import { ManageAccountInfoService } from 'src/app/services/manage-account-info.service';
import { ProductService } from 'src/app/services/product.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';
import { MatAutocompleteModule, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { Role } from '../../../DTOS/Role';

const removeMark = require("vietnamese-tonemarkless");

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{
  
  loginStatus: boolean;
  username: string;
  user: User = new User;
  productList: Product[] = [];
  nameList: string[] = [];
  filteredOptions: Observable<string[]>;
  myControl = new FormControl('');
  keyword: string;

  constructor(
    private storageService: StorageService, 
    private authService: AuthService, 
    private manageAccountInfoService: ManageAccountInfoService,
    private router: Router,
    private productService: ProductService){

  }

  isSeller(): boolean {

    const sellerRoleId = 4;

    if (this.user.id != -1) {
      console.log(this.user);
      for (let i = 0; i < this.user.roles.length; i++) {
        if (this.user.roles[i].id == sellerRoleId)
          return true;
      }
        return false;
        return false;
    } else {
      return false;
    }
  }
  
  ngOnInit(): void {
    this.productService.getAllProducts().subscribe(
      data => {
        this.productList = data;
        for(let i=0;i<=this.productList.length;i++){
          this.nameList.push(this.productList[i].name);
        }
      }
    )
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
    if(this.storageService.isLoggedIn()){
      this.loginStatus = true;
      this.manageAccountInfoService.getCurrentUserInfo().subscribe(
        data => {
          this.user = data;
        }
      )
    }else{
      this.loginStatus = false;
    }
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase().trim().replace(/\s+/g,' ');
    return this.nameList.filter(option => option.toLowerCase().includes(filterValue));
  }

  onLogout(){
    this.authService.logout().subscribe(
      data => {
        console.log('Logged out');
        this.router.navigate(['login']);
      }
    )
  }

  toSearchResult(){
    this.router.navigate(['/result',this.keyword])
  }

  onOptionSelected(event: MatAutocompleteSelectedEvent){
    const selectedOption = event.option.value;
    this.router.navigate(['/result', selectedOption]);
  }

  redirectSellerPage() {
    this.router.navigate(['collection/' + this.user.id]);
  }
}
