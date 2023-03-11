import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from 'src/app/DTOS/Category';
import { Product } from 'src/app/DTOS/Product';
import { Tag } from 'src/app/DTOS/Tag';
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
  maxprice: number = 10000000;
  chosenCategory: number = 0;
  chosenTags:number[] = [];

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
    this.productService.getFilteredProducts(this.keyword,0,0,10000000).subscribe(
      data => {
        this.resultList = data;
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
    return 'http://localhost:9000/public/serveMedia/image?source=' + product.coverImage.replace(/\\/g, '/');
  }
  
  openDetails(id: number){
    this.router.navigate(['/products',id]);
  }


  onChangeCategory(event: any){
    console.log(this.chosenCategory)
  }

  refresh(){
    this.productService.getFilteredProducts(this.keyword,this.chosenCategory,this.minprice,this.maxprice).subscribe(
      data => {
        this.resultList = data;
      }
    )
  }
}
