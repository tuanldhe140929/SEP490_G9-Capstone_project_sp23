import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Account } from 'src/app/DTOS/Account';
import { ForAdminService } from 'src/app/services/for-admin.service';
import { AddStaffComponent } from './add-staff/add-staff.component';
import { Observable, Subject} from 'rxjs'
import { UpdateStaffStatusComponent } from './update-staff-status/update-staff-status.component';

@Component({
  selector: 'app-staffs',
  templateUrl: './staffs.component.html',
  styleUrls: ['./staffs.component.css']
})
export class StaffsComponent implements OnInit{

  constructor(private forAdminService: ForAdminService, private dialog: MatDialog){}

  staffList: Account[] = [];
  ngOnInit(): void {
    this.getAllStaffs();

  }

  getAllStaffs(){
    this.forAdminService.getAllStaffs().subscribe(
      response => {
        this.staffList = response;
        $(function(){
          $('#staffsTable').DataTable({
            responsive: true
          });
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
