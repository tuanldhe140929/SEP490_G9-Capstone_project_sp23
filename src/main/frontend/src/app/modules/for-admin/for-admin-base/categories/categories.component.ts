import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { Subject } from 'rxjs';
import { Account } from 'src/app/DTOS/Account';
import { Category } from 'src/app/DTOS/Category';
import { CategoryService } from 'src/app/services/category.service';
import { AddCategoryComponent } from './add-category/add-category.component';
import { UpdateCategoryComponent } from './update-category/update-category.component';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements AfterViewInit{
  displayedColumns: string[] = ['ID', 'Tên phân loại','Hành động'];
  dataSource: MatTableDataSource<Category>;

  categoryList: Category[] = [];
  nameList: string[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private categoryService: CategoryService, private dialog: MatDialog) {
    this.categoryService.getAllCategories().subscribe(response => {
      this.dataSource = new MatTableDataSource(response);
      this.categoryList = response;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      for(let i=0;i<this.categoryList.length;i++){
        this.nameList.push(this.categoryList[i].name);
      }
      console.log(this.nameList.length)
    })

    // Assign the data to the data source for the table to render
    // this.dataSource = new MatTableDataSource([{'id':1,'name':'AAA'},{'id':2,'name':'BBB'}]);
  }


  ngAfterViewInit(): void {
    
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openAddDialog(getNameList: string[]) {
    const dialogRef = this.dialog.open(AddCategoryComponent, {
      data: {
        nameList: getNameList
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(),400)
    });
  }

  openUpdateDialog(getNameList: string[], getId: number, getOldName: string) {
    const dialogRef = this.dialog.open(UpdateCategoryComponent, {
      data: {
        nameList: getNameList,
        id: getId,
        oldName: getOldName
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(),400)
    });
  }

  refresh() {
    this.categoryService.getAllCategories().subscribe((data: Category[]) => {
      this.dataSource.data = data;
      this.nameList = [];
      for (let i = 0; i < data.length; i++) {
        this.nameList.push(data[i].name);
      }
      console.log(data);
    })
  }
}
