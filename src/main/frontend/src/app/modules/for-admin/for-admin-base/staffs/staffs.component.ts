
import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Account } from 'src/app/DTOS/Account';
import { ForAdminService } from 'src/app/services/for-admin.service';
import { AddStaffComponent } from './add-staff/add-staff.component';
import { UpdateStaffStatusComponent } from './update-staff-status/update-staff-status.component';


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
      setTimeout(() => this.refresh(),400)
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
      setTimeout(() => this.refresh(),400)
    });
  }

  refresh() {
    this.forAdminService.getAllStaffs().subscribe((data: any) => {
      this.dataSource.data = data;
      this.emailList = [];
      for (let i = 0; i < data.length; i++) {
        this.emailList.push(data[i].email);
      }
      console.log(data);
    })
  }
}

