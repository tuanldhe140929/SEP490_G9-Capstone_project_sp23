import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from 'src/app/dtos/Category';
import { Product } from 'src/app/dtos/Product';
import { Tag } from 'src/app/dtos/Tag';
import { CategoryService } from 'src/app/services/category.service';
import { ProductDtoService } from 'src/app/services/product-dto.service';
import { TagService } from 'src/app/services/tag.service';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit{


  categoryList: Category[] = [];
  tagList: Tag[] = [];
  productList: Product[] = [];
  searchResult: Product[] = [];
  keyword: string;

  constructor(
    private categoryService: CategoryService, 
    private tagService: TagService,
    private productDtoService: ProductDtoService,
    private activatedRoute: ActivatedRoute,
    private router: Router){}

  ngOnInit(): void {
    this.getAllTags();
    this.getAllCategories();
    this.activatedRoute.params.subscribe(
      params => {
        this.keyword = params['keyword'].trim().toLowerCase().replace(/\s+/g,' ');
      }
    );
    this.getProductsByKeyword();

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


  getProductsByKeyword(){
    this.productDtoService.getAllProducts().subscribe(
      data => {
        this.productList = data;
        for(let i=0;i<this.productList.length;i++){
          if(this.productList[i].name.toLowerCase().trim().includes(this.keyword.toLowerCase().trim())){
            this.searchResult.push(this.productList[i]);
          }
        }
        console.log(this.searchResult.length)
      }
    )
  }

  getCoverImage(product: Product): string{
    return 'http://localhost:9000/public/serveMedia/image?source=' + product.coverImage.replace(/\\/g, '/');
  }
  
  openDetails(id: number){
    this.router.navigate(['/products',id]);
  }

}
