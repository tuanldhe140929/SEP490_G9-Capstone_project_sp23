import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { Subject } from 'rxjs';
import { Account } from 'src/app/DTOS/Account';
import { Category } from 'src/app/DTOS/Category';
import { ForAdminService } from 'src/app/services/for-admin.service';
import { AddCategoryComponent } from './add-category/add-category.component';
import { UpdateCategoryComponent } from './update-category/update-category.component';

export interface UserData {
  id: string;
  name: string;
  progress: string;
  fruit: string;
}

/** Constants used to fill up our data base. */
const FRUITS: string[] = [
  'blueberry',
  'lychee',
  'kiwi',
  'mango',
  'peach',
  'lime',
  'pomegranate',
  'pineapple',
];
const NAMES: string[] = [
  'Maia',
  'Asher',
  'Olivia',
  'Atticus',
  'Amelia',
  'Jack',
  'Charlotte',
  'Theodore',
  'Isla',
  'Oliver',
  'Isabella',
  'Jasper',
  'Cora',
  'Levi',
  'Violet',
  'Arthur',
  'Mia',
  'Thomas',
  'Elizabeth',
];

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

  constructor(private forAdminService: ForAdminService, private dialog: MatDialog) {
    // Create 100 users
    const users = Array.from({length: 100}, (_, k) => createNewUser(k + 1));

    
    this.forAdminService.getAllCategories().subscribe(response => {
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


  // getAllCategories(){
  //   this.forAdminService.getAllCategories().subscribe(
  //     response => {
  //       this.categoryList = response;
  //       this.dataSource = new MatTableDataSource(this.categoryList);
  //       for(let i=0;i<this.categoryList.length;i++){
  //         this.nameList.push(this.categoryList[i].name);
  //       }
  //     }
  //   )
  // }

  openAddDialog(getNameList: string[]) {
    const dialogRef = this.dialog.open(AddCategoryComponent, {
      data: {
        nameList: getNameList
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(),200)
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
      setTimeout(() => this.refresh(),200)
    });
  }

  refresh() {
    this.forAdminService.getAllCategories().subscribe((data: Category[]) => {
      this.dataSource.data = data;
      console.log(data)
    })
  }
}



/** Builds and returns a new User. */
function createNewUser(id: number): UserData {
  const name =
    NAMES[Math.round(Math.random() * (NAMES.length - 1))] +
    ' ' +
    NAMES[Math.round(Math.random() * (NAMES.length - 1))].charAt(0) +
    '.';

  return {
    id: id.toString(),
    name: name,
    progress: Math.round(Math.random() * 100).toString(),
    fruit: FRUITS[Math.round(Math.random() * (FRUITS.length - 1))],
  };
}