import { AfterViewInit, Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Account } from 'src/app/DTOS/Account';
import { ForAdminService } from 'src/app/services/for-admin.service';
import { AddStaffComponent } from './add-staff/add-staff.component';
import { Observable, Subject, Subscription} from 'rxjs'
import { UpdateStaffStatusComponent } from './update-staff-status/update-staff-status.component';
import {MatPaginator} from '@angular/material/paginator'
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { DataTableDirective } from 'angular-datatables';


@Component({
  selector: 'app-staffs',
  templateUrl: './staffs.component.html',
  styleUrls: ['./staffs.component.css']
})
export class StaffsComponent implements OnInit{

  subscription: Subscription = new Subscription;
  constructor(private forAdminService: ForAdminService, private dialog: MatDialog){

  }

  staffList: Account[] = [];
  ngOnInit(): void {
    this.getAllStaffs();
    this.subscription = this.forAdminService.refresh$.subscribe(() => {
      this.getAllStaffs();
    })
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    console.log('Unsubscibed');
  }

  isDtInitialized: boolean = false;
  dtElement: DataTableDirective;

  getAllStaffs(){
    this.forAdminService.getAllStaffs().subscribe(
      response => {
        this.staffList = response;
        $(function(){
          $('#staffsTable').DataTable();
        });
      }
    )
  }

  openAddDialog() {
    const dialogRef = this.dialog.open(AddStaffComponent, {
      height:'52.1%',
      width:'60%'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

  openUpdateDialog(getId: number, getEmail: string, getEnabled: boolean){
    const dialogRef = this.dialog.open(UpdateStaffStatusComponent, {
      data: {
        id: getId,
        email: getEmail,
        enabled: getEnabled
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

}


// import { HttpErrorResponse } from '@angular/common/http';
// import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
// import { MatDialog } from '@angular/material/dialog';
// import {MatPaginator} from '@angular/material/paginator';
// import {MatSort} from '@angular/material/sort';
// import {MatTableDataSource} from '@angular/material/table';
// import { Subscription } from 'rxjs';
// import { Account } from 'src/app/DTOS/Account';
// import { ForAdminService } from 'src/app/services/for-admin.service';
// import { AddStaffComponent } from './add-staff/add-staff.component';
// import { UpdateStaffStatusComponent } from './update-staff-status/update-staff-status.component';

// export interface staffData {
//   email: string;
//   accountCreatedDate: Date;
//   enabled: boolean;
// }

// /** Constants used to fill up our data base. */
// const FRUITS: string[] = [
//   'blueberry',
//   'lychee',
//   'kiwi',
//   'mango',
//   'peach',
//   'lime',
//   'pomegranate',
//   'pineapple',
// ];
// const NAMES: string[] = [
//   'Maia',
//   'Asher',
//   'Olivia',
//   'Atticus',
//   'Amelia',
//   'Jack',
//   'Charlotte',
//   'Theodore',
//   'Isla',
//   'Oliver',
//   'Isabella',
//   'Jasper',
//   'Cora',
//   'Levi',
//   'Violet',
//   'Arthur',
//   'Mia',
//   'Thomas',
//   'Elizabeth',
// ];

// /**
//  * @title Data table with sorting, pagination, and filtering.
//  */
// @Component({
//  selector: 'app-staffs',
// templateUrl: './staffs.component.html',
// styleUrls: ['./staffs.component.css']
// })
// export class StaffsComponent implements AfterViewInit, OnInit {
//   displayedColumns: string[] = ['Email', 'Ngày tạo', 'Trạng thái'];
//   dataSource: MatTableDataSource<Account>;

//   subscription: Subscription
//   staffList: Account[];
//   @ViewChild(MatPaginator) paginator: MatPaginator;
//   @ViewChild(MatSort) sort: MatSort;
//   private dataArray: any;

//   constructor(private forAdminService: ForAdminService, private dialog: MatDialog) {

//     this.dataSource = new MatTableDataSource(this.staffList);
//   }

//    ngOnInit(): void {
//     this.subscription.add(this.forAdminService.getAllStaffs()
//     .subscribe((res) => {
//       console.log(res);
//       this.dataArray = res;
//       this.dataSource = new MatTableDataSource<Account>(this.dataArray);
//       this.dataSource.paginator = this.paginator;
//       this.dataSource.sort = this.sort;
//     },
//     (err: HttpErrorResponse) => {
//       console.log(err);
//     }))
//    }
//    ngOnDestroy(): void {
//      this.subscription.unsubscribe();
//      console.log('Unsubscibed');
//    }

//    getAllStaffs(){
//      this.forAdminService.getAllStaffs().subscribe(
//        response => {
//          this.staffList = response;
//          $(function(){
//            $('#staffsTable').DataTable({
//              responsive: true
//            });
//          });
//        }
//      )
//    }

//    openAddDialog() {
//      const dialogRef = this.dialog.open(AddStaffComponent, {
//        height:'52.1%',
//        width:'60%'
//      });

//      dialogRef.afterClosed().subscribe(result => {
//        console.log(`Dialog result: ${result}`);
//      });
//    }

//    openUpdateDialog(getId: number, getEmail: string, getEnabled: boolean){
//      const dialogRef = this.dialog.open(UpdateStaffStatusComponent, {
//        data: {
//          id: getId,
//          email: getEmail,
//          enabled: getEnabled
//        }
//      });
//      dialogRef.afterClosed().subscribe(result => {
//        console.log(`Dialog result: ${result}`);
//      });
//    }
//   ngAfterViewInit() {
//     this.dataSource.paginator = this.paginator;
//     this.dataSource.sort = this.sort;
//   }

//   applyFilter(event: Event) {
//     const filterValue = (event.target as HTMLInputElement).value;
//     this.dataSource.filter = filterValue.trim().toLowerCase();

//     if (this.dataSource.paginator) {
//       this.dataSource.paginator.firstPage();
//     }
//   }
// }

