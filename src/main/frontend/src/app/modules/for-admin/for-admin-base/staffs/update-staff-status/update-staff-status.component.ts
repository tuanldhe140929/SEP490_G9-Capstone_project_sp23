import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { ForAdminService } from 'src/app/services/for-admin.service';

@Component({
  selector: 'app-update-staff-status',
  templateUrl: './update-staff-status.component.html',
  styleUrls: ['./update-staff-status.component.css']
})
export class UpdateStaffStatusComponent implements OnInit{
  constructor(@Inject(MAT_DIALOG_DATA) public data: {id: number, email: string, enabled: boolean}, private forAdminService: ForAdminService, private toastr: ToastrService){}

  formerStatus: boolean = this.data.enabled;

  ngOnInit(): void {
    
  }

  onUpdateStatus(){
    this.forAdminService.updateStaffStatus(this.data.id).subscribe(
      data => {
        console.log(data);
        if(this.formerStatus){
          this.toastr.success("Tắt tài khoản thành công!");
        }else{
          this.toastr.success("Bật tài khoản thành công!");
        }
      }
    )
  }
}
