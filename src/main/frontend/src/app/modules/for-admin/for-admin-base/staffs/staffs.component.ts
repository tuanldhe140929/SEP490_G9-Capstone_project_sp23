// import { AfterViewInit, Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
// import { MatDialog } from '@angular/material/dialog';
// import { Account } from 'src/app/DTOS/Account';
// import { ForAdminService } from 'src/app/services/for-admin.service';
// import { AddStaffComponent } from './add-staff/add-staff.component';
// import { Observable, Subject, Subscription} from 'rxjs'
// import { UpdateStaffStatusComponent } from './update-staff-status/update-staff-status.component';
// import {MatPaginator} from '@angular/material/paginator'
// import { MatSort } from '@angular/material/sort';
// import { MatTableDataSource } from '@angular/material/table';
// import { DataTableDirective } from 'angular-datatables';
// import { ToastrService } from 'ngx-toastr';


// @Component({
//   selector: 'app-staffs',
//   templateUrl: './staffs.component.html',
//   styleUrls: ['./staffs.component.css']
// })
// export class StaffsComponent implements OnInit{

//   datatable = $('#staffsTable').DataTable();
//   dtOptions: DataTables.Settings = {}; 

//   subscription: Subscription = new Subscription;
//   constructor(private forAdminService: ForAdminService, private dialog: MatDialog, private toastr: ToastrService){

//   }

//   staffList: Account[] = [];
//   emailList: string[] = [];
//   ngOnInit(): void {
//     this.getAllStaffs();
//     this.dtOptions = {
//       pagingType: 'full_numbers',
//       searching: true
//     }
//     this.subscription = this.forAdminService.refresh$.subscribe(() => {
//       this.getAllStaffs();
//     })
//   }

//   ngOnDestroy(): void {
//     this.subscription.unsubscribe();
//     console.log('Unsubscibed');
//   }

//   isDtInitialized: boolean = false;
//   dtElement: DataTableDirective;

//   dataSource: any;

//   dtTrigger:Subject<any> = new Subject();

//   getAllStaffs(){
//     this.forAdminService.getAllStaffs().subscribe(
//       response => {
//         this.staffList = response;
//         this.dtTrigger.next(null);
//         this.dataSource = new MatTableDataSource(this.staffList);
//         for(let i = 0;i< this.staffList.length; i++){
//           this.emailList.push(this.staffList[i].email);
//         }
//         $(function(){
//           $('#staffsTable').DataTable();
//         });
//       }
//     )
//   }

//   openAddDialog(getEmailList: string[]) {
//     const dialogRef = this.dialog.open(AddStaffComponent, {
//       height:'52.1%',
//       width:'60%',
//       data: {
//         emailList: getEmailList
//       }
//     });

//     dialogRef.afterClosed().subscribe(result => {
//       console.log(`Dialog result: ${result}`);
//       this.refresh()
//     });
//   }

//   refresh() {
//     this.forAdminService.getAllStaffs().subscribe((data: any) => {
//       this.dataSource.data = data;
//     })
//   }

//   openUpdateDialog(getId: number, getEmail: string, getEnabled: boolean){
//     const dialogRef = this.dialog.open(UpdateStaffStatusComponent, {
//       data: {
//         id: getId,
//         email: getEmail,
//         enabled: getEnabled
//       }
//     });
//     dialogRef.afterClosed().subscribe(result => {
//       console.log(`Dialog result: ${result}`);
//       this.datatable.destroy();
//       $('#staffsTable').DataTable();
//     });
//   }

//   displayedColumns: string[] = ['Email','Ngày tạo','Trạng thái'];
// }


import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Account } from 'src/app/DTOS/Account';
import { ForAdminService } from 'src/app/services/for-admin.service';
import { AddStaffComponent } from './add-staff/add-staff.component';
import { UpdateStaffStatusComponent } from './update-staff-status/update-staff-status.component';

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
  selector: 'app-staffs',
  templateUrl: './staffs.component.html',
  styleUrls: ['./staffs.component.css']
})
export class StaffsComponent implements AfterViewInit {
  displayedColumns: string[] = ['Email', 'Ngày tạo', 'Trạng thái', 'Hành động'];
  dataSource: MatTableDataSource<Account>;

  staffList: Account[] = [];
  emailList: string[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private forAdminService: ForAdminService, private dialog: MatDialog) {
    // Create 100 users
    const users = Array.from({ length: 100 }, (_, k) => createNewUser(k + 1));

    // Assign the data to the data source for the table to render
    this.forAdminService.getAllStaffs().subscribe(
      response => {
        this.dataSource = new MatTableDataSource(response);
        this.staffList = response;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        for (let i = 0; i < this.staffList.length; i++) {
          this.emailList.push(this.staffList[i].email);
        }
        console.log(this.emailList.length)
      }
    )


  }

  ngAfterViewInit() {

  }



  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openAddDialog(getEmailList: string[]) {
    const dialogRef = this.dialog.open(AddStaffComponent, {
      height: '52.1%',
      width: '60%',
      data: {
        emailList: getEmailList,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(),200)
    });
  }



  openUpdateDialog(getId: number, getEmail: string, getEnabled: boolean) {
    const dialogRef = this.dialog.open(UpdateStaffStatusComponent, {
      data: {
        id: getId,
        email: getEmail,
        enabled: getEnabled
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(),200)
    });
  }

  refresh() {
    this.forAdminService.getAllStaffs().subscribe((data: any) => {
      this.dataSource.data = data;
      console.log(data);
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