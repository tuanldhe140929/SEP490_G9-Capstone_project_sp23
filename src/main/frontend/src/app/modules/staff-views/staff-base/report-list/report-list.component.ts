import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { Account } from 'src/app/DTOS/Account';
import { Category } from 'src/app/DTOS/Category';
import { Product } from 'src/app/DTOS/Product';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';


@Component({
  selector: 'app-report-list',
  templateUrl: './report-list.component.html',
  styleUrls: ['./report-list.component.css']
})
export class ReportListComponent {
  displayedColumns: string[] = ['Mã sản phẩm', 'Phiên bản','Chi tiết','Các báo cáo'];
  dataSource: MatTableDataSource<Category>;

  productList: Product[] = [];
  nameList: string[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private productService: ProductService, private dialog: MatDialog, private router: Router) {
    this.productService.getProductsByReportStatus("PENDING").subscribe(response => {
      this.dataSource = new MatTableDataSource(response);
      this.productList = response;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      for(let i=0;i<this.productList.length;i++){
        this.nameList.push(this.productList[i].name);
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

  // openAddDialog(getNameList: string[]) {
  //   const dialogRef = this.dialog.open(AddCategoryComponent, {
  //     data: {
  //       nameList: getNameList
  //     }
  //   });
  //   dialogRef.afterClosed().subscribe(result => {
  //     console.log(`Dialog result: ${result}`);
  //     setTimeout(() => this.refresh(),400)
  //   });
  // }

  // openUpdateDialog(getNameList: string[], getId: number, getOldName: string) {
  //   const dialogRef = this.dialog.open(UpdateCategoryComponent, {
  //     data: {
  //       nameList: getNameList,
  //       id: getId,
  //       oldName: getOldName
  //     }
  //   });
  //   dialogRef.afterClosed().subscribe(result => {
  //     console.log(`Dialog result: ${result}`);
  //     setTimeout(() => this.refresh(),400)
  //   });
  // }

  openDetails(productId: number){
    const url = this.router.createUrlTree(["/products/"+productId]);
    window.open(url.toString(), '_blank');
  }

  refresh() {
    this.productService.getProductsByReportStatus("PENDING").subscribe((data: Product[]) => {
      this.dataSource.data = data;
      this.nameList = [];
      for (let i = 0; i < data.length; i++) {
        this.nameList.push(data[i].name);
      }
      console.log(data);
    })
  }
}
