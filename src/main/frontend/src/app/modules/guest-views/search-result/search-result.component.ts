import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from 'src/app/dtos/Category';
import { Product } from 'src/app/dtos/Product';
import { Tag } from 'src/app/dtos/Tag';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { TagService } from 'src/app/services/tag.service';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit{

  productList: Product[] = [];
  resultList: Product[] = [];
  categoryList: Category[] = [];
  tagList: Tag[] = [];
  filterResult: Product[] = [];
  keyword: any;
  minprice: number = 0;
  maxprice: number = 1000;
  chosenCategory: number = 0;
  chosenTags:number[] = [];
  p:number = 1;
  itemsPerPage: number = 9;
  totalResult: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private productService: ProductService,
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
    this.productService.getFilteredProducts(this.keyword,0,[],0,1000).subscribe(
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

  getCoverImage(product: Product): string{
    console.log(product.coverImage==null);
    if(product.coverImage!=null){
      return 'http://localhost:9000/public/serveMedia/image?source=' + product.coverImage.replace(/\\/g, '/');
    }else{
      return 'assets/images/noimage.png'
    }
    
  }
  
  openDetails(id: number){
    this.router.navigate(['/products',id]);
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

  checkValidMinMax(): boolean{
    if(this.minprice<0||this.maxprice<0||this.minprice>1000||this.maxprice>1000){
      return false;
    }else{
      return true;
    }
  }

  refresh(){
    console.log(this.checkedTags);
    this.productService.getFilteredProducts(this.keyword,this.chosenCategory, this.checkedTags,this.minprice,this.maxprice).subscribe(
      data => {
        this.resultList = data;
      }
    )
  }

  checkedTags: number[] = [];
  updateCheckedValues(event: any){
    const value = event.target.value;
    if(event.target.checked){
      this.checkedTags.push(value);
    }else{
      this.checkedTags = this.checkedTags.filter(v => v !== value);
    }
  }
}
