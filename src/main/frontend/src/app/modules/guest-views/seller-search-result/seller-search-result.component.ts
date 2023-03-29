import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from 'src/app/dtos/Category';
import { Product } from 'src/app/dtos/Product';
import { Seller } from 'src/app/dtos/Seller';
import { Tag } from 'src/app/dtos/Tag';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { SellerService } from 'src/app/services/seller.service';
import { TagService } from 'src/app/services/tag.service';


@Component({
  selector: 'app-seller-search-result',
  templateUrl: './seller-search-result.component.html',
  styleUrls: ['./seller-search-result.component.css']
})
export class SellerSearchResultComponent implements OnInit{

  resultList: Seller[] = [];
  categoryList: Category[] = [];
  tagList: Tag[] = [];
  filterResult: Product[] = [];
  keyword: any;
  minprice: number = 0;
  maxprice: number = 10000000;
  chosenCategory: number = 0;
  chosenTags:number[] = [];
  p:number = 1;
  itemsPerPage: number = 9;
  totalResult: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private sellerService: SellerService,
    private categoryService: CategoryService,
    private tagService: TagService,
    private router: Router){}

  ngOnInit(): void {
    this.getSearchResult();
    this.getAllCategories();
    this.getAllTags();
  }

  getSearchResult(){
    this.keyword = this.activatedRoute.snapshot.paramMap.get('keyword')?.trim().toLowerCase().replace(/\s+/g,' ');;
    // this.productService.getAllProducts().subscribe(
    //   data => {
    //     this.productList = data;
    //     for(let i=0;i<this.productList.length;i++){
    //       if(this.productList[i].name.trim().toLowerCase().includes(this.keyword)){
    //         this.resultList.push(this.productList[i]);
    //       }
    //     }
    //   }
    // )
    this.sellerService.getSellersForSearching(this.keyword).subscribe(
      data => {
        this.resultList = data;
        this.totalResult = this.resultList.length;
      }
    )
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

  getCoverImage(seller: Seller): string{
    console.log(seller.avatar==null);
    if(seller.avatar!=null){
      return 'http://localhost:9000/public/serveMedia/image?source=' + seller.avatar;
    }else{
      return 'assets/images/noimage.png'
    }
    
  }
  
  openCollection(id: number){
    this.router.navigate(['/collection',id]);
  }


  onChangeCategory(event: any){
    console.log(this.chosenCategory)
  }

  compareMinMax(): boolean{
    if(this.minprice<=this.maxprice&&this.minprice!=null&&this.maxprice!=null){
      return true;
    }else{
      return false;
    }
  }

  refresh(){
    this.sellerService.getSellersForSearching(this.keyword).subscribe(
      data => {
        this.resultList = data;
      }
    )
  }
}
