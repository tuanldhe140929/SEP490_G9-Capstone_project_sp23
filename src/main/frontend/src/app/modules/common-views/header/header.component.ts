import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { map, Observable, startWith } from 'rxjs';
import { Product } from 'src/app/dtos/Product';
import { ProductService } from 'src/app/services/product.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';
import { MatAutocompleteModule, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { AccountService } from 'src/app/services/account.service';
import { SellerService } from 'src/app/services/seller.service';
import { Seller } from 'src/app/dtos/Seller';
import { User } from '../../../dtos/User';
import { CategoryService } from 'src/app/services/category.service';
import { Category } from 'src/app/dtos/Category';
import { TagService } from 'src/app/services/tag.service';
import { Tag } from 'src/app/dtos/Tag';
import { Role } from 'src/app/dtos/Role';

const removeMark = require("vietnamese-tonemarkless");

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  loginStatus: boolean;
  username: string;
  user: User = new User;
  productList: Product[] = [];
  sellerList: Seller[] = [];
  categoryList: Category[] = [];
  tagList: Tag[] = [];
  nameList: string[] = [];
  sellerNameList: string[] = [];
  filteredOptions: Observable<string[]>;
  myControl = new FormControl('');
  chosenOption: string = "PRODUCTS";
  keyword: string = "";

  constructor(
    private storageService: StorageService,
    private accountService: AccountService,
    private userService: UserService,
    private router: Router,
    private productService: ProductService,
    private sellerService: SellerService,
    private categoryService: CategoryService,
    private tagService: TagService) {

  }

  isSeller(): boolean {

    const sellerRoleId = 4;

    if (this.user.id != -1) {
      for (let i = 0; i < this.user.roles.length; i++) {
        if (this.user.roles[i].id == sellerRoleId)
          return true;
      }
      return false;
    } else {
      return false;
    }
  }

  ngOnInit(): void {
    this.getAllCategories();
    this.getAllTags();
    this.productService.getProductsForSearching("",0,[],0,1000).subscribe(
      data => {
        this.productList = data;
        for (let i = 0; i <= this.productList.length; i++) {
          if(this.productList[i]!=null)
          this.nameList.push(this.productList[i].name);
        }
      }
    )
    this.sellerService.getSellersForSearching("").subscribe(
      data => {
        this.sellerList = data;
        for(let i = 0;i <= this.sellerList.length; i++){
          if(this.sellerList[i]!=null)
          this.sellerNameList.push(this.sellerList[i].username);
        }
      }
    )
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
    if (this.storageService.isLoggedIn()) {
      this.loginStatus = true;
      this.userService.getCurrentUserInfo().subscribe(
        data => {
          this.user = data;
        }
      )
    } else {
      this.loginStatus = false;
    }
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase().trim().replace(/\s+/g, ' ');
    if(this.chosenOption == "PRODUCTS"){
      return this.nameList.filter(option => option.toLowerCase().includes(filterValue));
    }else{
      return this.sellerNameList.filter(option => option.toLowerCase().includes(filterValue));
    }
  }

  onLogout() {
    this.accountService.logout().subscribe(
      data => {
        console.log('Logged out');
        this.router.navigate(['login']);
      }
    )
  }

  toSearchResult() {
    if(this.chosenOption == "PRODUCTS"){
      this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
        this.router.navigate(['/result', this.keyword]);
      })
    }else{
      this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
        this.router.navigate(['/sellers', this.keyword])
      })
    }
  }

  onOptionSelected(event: MatAutocompleteSelectedEvent) {
    const selectedOption = event.option.value;
    if(this.chosenOption == "PRODUCTS"){
      this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
        this.router.navigate(['/result', selectedOption])
      })
    }else{
      this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
        this.router.navigate(['/sellers', selectedOption])
      })
    }
    
  }

  redirectSellerPage() {
    this.router.navigate(['collection/' + this.user.id]);
  }

  onChangeSearch(event: any){
    this.chosenOption = (event.target as HTMLSelectElement).value;
  }

  getAllCategories(){
    this.categoryService.getAllCategories().subscribe(
      data => {
        this.categoryList = data;
      }
    )
  }

  getAllTags(){
    this.tagService.getAllTags().subscribe(
      data => {
        this.tagList = data;
      }
    )
  }

  searchByCategory(categoryid: number){
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['category/'+categoryid]);
    })
  }

  searchByTag(tagid: number){
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['tag/'+tagid]);
    })
  }

  viewPayouts() {
    this.router.navigate(['payout']);
  }
  viewPurchased() {
    this.router.navigate(['purchased']);
  }

  // isSeller(): boolean {
  //   const sellerRole: Role = { id: 4, name: 'ROLE_SELLER' };
  //   return this.user.roles.some(role => role.id === sellerRole.id);
  // }
}
