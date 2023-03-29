import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { Router } from '@angular/router';
import { Account } from 'src/app/dtos/Account';
import { Category } from 'src/app/dtos/Category';
import { Product } from 'src/app/dtos/Product';
import { Report } from 'src/app/dtos/Report';
import { User } from 'src/app/dtos/User';
import { ViolationType } from 'src/app/dtos/ViolationType';
import { ReportProductComponent } from 'src/app/modules/guest-views/product-details/report-product/report-product.component';
import { AccountService } from 'src/app/services/account.service';
import { ProductService } from 'src/app/services/product.service';
import { ReportService } from 'src/app/services/report.service';
import { UserService } from 'src/app/services/user.service';
import { ReportedProductDetailsComponent } from './reported-product-details/reported-product-details.component';

interface ReportEntity{
  productId: number
  productName: string
  productVersion: string
  userId: number
  userName: string
  reportedDate: string
  reportedDescription: string
  reportedViolationType: ViolationType
}

@Component({
  selector: 'app-report-list',
  templateUrl: './report-list.component.html',
  styleUrls: ['./report-list.component.css']
})
export class ReportListComponent {
  displayedColumns: string[] = ['Mã sản phẩm','Tên sản phẩm','Chi tiết'];
  dataSource: MatTableDataSource<Product>;

  reportList: ReportEntity[] = [];
  reportEntities: ReportEntity[] =[];
  nameList: string[] = [];
  mergedData: any[] = [];
  users: User[];
  accounts: Account[];
  products: Product[];
  reports: Report[];
  selectedOption: string = "PENDING";  

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private reportService: ReportService, 
    private userService: UserService,
    private accountService: AccountService, 
    private productService: ProductService, 
    private dialog: MatDialog, 
    private router: Router) {
      this.productService.getProductsByReportStatus("PENDING").subscribe(products => {
        this.products = products;
        this.dataSource = new MatTableDataSource(products);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      })

    
    // this.reportService.getByStatus("PENDING").subscribe (reports => {
    //   this.reports = reports;
    //   console.log(reports);
    //   this.userService.getAllUsers().subscribe( users => {
    //     this.users = users;
    //     this.productService.getAllProductsLatestVers().subscribe (products => {
    //       this.products = products;
    //       this.reportEntities = this.reports.map(report => {
    //         const product = this.products.find(p => {p.id === report.product.id; console.log(p.id+" "+report.product.id)});
    //         const user = this.users.find(u => {u.id === report.user.id; console.log(u.id+" "+report.user.id)});
    //         return{
    //           productId: report.product.id,
    //           productName: report.product.name,
    //           productVersion: report.product.activeVersion,
    //           userId: report.user.id,
    //           userName: report.user.username,
    //           reportedDate: report.created_date,
    //           reportedDescription: report.description,
    //           reportedViolationType: report.violation_type
    //         } as ReportEntity;
            
    //       })
    //       console.log(this.reportEntities);
    //       this.dataSource = new MatTableDataSource(this.reportEntities);
    //     this.reportList = this.reportEntities;
    //     this.dataSource.paginator = this.paginator;
    //     this.dataSource.sort = this.sort;
    //     })
        
    //   });
      
    // });
    
    
    

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


  refresh(status: string) {
    this.productService.getProductsByReportStatus(status).subscribe(products => {
      this.products = products;
      this.dataSource = new MatTableDataSource(products);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    })
  }

  onChangeStatus(event: any){
    const status = (event.target as HTMLSelectElement).value;
    this.refresh(status);
  }

  openDetails(productId: string){
    const dialogRef = this.dialog.open(ReportedProductDetailsComponent, {
      data: {
        productId: productId,
        status: this.selectedOption
      },
      height: "90%",
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(this.selectedOption),400)
    });
  }
}
